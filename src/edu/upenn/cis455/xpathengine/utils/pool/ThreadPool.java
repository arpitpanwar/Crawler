package edu.upenn.cis455.xpathengine.utils.pool;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import edu.upenn.cis455.crawler.Crawler;
import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.HTMLAccessor;
import edu.upenn.cis455.storage.accessor.RobotsAccessor;
import edu.upenn.cis455.storage.accessor.VisitedListAccessor;
import edu.upenn.cis455.storage.accessor.XMLAccessor;
import edu.upenn.cis455.storage.entity.VisitedListEntity;
import edu.upenn.cis455.xpathengine.utils.BlockingQueue;
import edu.upenn.cis455.xpathengine.utils.Constants;


/**
 * Threadpool for handling requests
 * @author cis455
 *
 */

public class ThreadPool {
	static final Logger LOG = Logger.getLogger(ThreadPool.class);

	private BlockingQueue<Object> queue;
	private int threadCount;
	private Worker[] workers;
	private Thread[] workerThreads;
	private volatile boolean shutdown = false;
	private static final Object KILL_ME = new Object();
	private boolean shutdownRequested = false;
	private AtomicLong totalParsedSize; 
	private VisitedListAccessor accessor;
	private XMLAccessor xmlAccessor;
	private HTMLAccessor htmlAccessor;
	private RobotsAccessor robAccessor;
	private long maxSize;
	private Lock lock,incrementLock;
	private AtomicLong totalPagesRead;
	
	public ThreadPool(int count,VisitedListAccessor accessor,XMLAccessor xmlAccessor,HTMLAccessor htmlAccessor,long size,RobotsAccessor robAccessor) throws IllegalArgumentException{
		
		if(isValidCount(count))
			this.threadCount = count;
		else
			throw new IllegalArgumentException("Invalid Thread Count");
		
		workers = new Worker[count];
		workerThreads = new Thread[count];
		queue = new BlockingQueue<Object>();
		totalParsedSize = new AtomicLong(0);
		this.accessor = accessor;
		this.xmlAccessor = xmlAccessor;
		this.htmlAccessor = htmlAccessor;
		this.maxSize = size;
		this.robAccessor = robAccessor;
		this.lock = new ReentrantLock();
		this.incrementLock = new ReentrantLock();
		this.totalPagesRead = new AtomicLong(0);
		
		startThreads();
	}
	
	public boolean execute(Callable<Object> task){
		
		return this.queue.offer(task);
		
	}
	
	private void incrementSize(long size){
		
			incrementLock.lock();
			totalParsedSize.addAndGet(size);
			incrementLock.unlock();
		
	}
	
	public long getTotalParsedSize(){
		return this.totalParsedSize.longValue();
	}
	
	private void startThreads(){
		
		synchronized (this) {
		
			for(int i=0;i<threadCount;i++){
				
				this.workers[i] = new Worker();
				this.workerThreads[i] = new Thread(workers[i]);
				this.workerThreads[i].start();
			}
		}
	}
	
	public synchronized boolean shutDown(){
		
		this.shutdown = true;
		this.queue.add(KILL_ME);
			try{
				for(Worker w: workers){
					
					w.thread.interrupt();
					
				}
				
				queue.clear();
				
				for(Thread workerThread : workerThreads){
					
					workerThread.interrupt();
					
				}
				
				return true;
				
			}catch(Exception e){
				LOG.debug(Thread.currentThread()+": Worker Thread Shutdown Failed");
				return false;
				
			}
		
	}
	
		
	private boolean isValidCount(int count){
		
		if(count<=Constants.MAX_THREAD_COUNT && count>0)
			return true;
		else
			return false;
		
	} 
	
	public Thread[] getWorkerThreads(){
		return workerThreads;
	}
	
	public Worker[] getWorkers(){
		return workers;
	}
	
	public boolean isShutdownRequested() {
		return shutdownRequested;
	}

	public void setShutdownRequested(boolean shutdownRequested) {
		this.shutdownRequested = shutdownRequested;
	}
	
	public synchronized void isShutdownRequired(){
		lock.lock();
		boolean required=true;
		Thread[] threads = getWorkerThreads();
		loop:for(Thread t: threads){
			if(t!=Thread.currentThread()){
				if(t.getState()!=Thread.State.WAITING){
					required=false;
					break loop;
				}
			}
		}
		
		if(required){
			if(this.queue.getSize()!=0){
				required=false;
			}
		}
		
		if(required){
			System.out.println("No more links present. Shutting down.");
			
			shutdownRequested=true;
			
		}
		lock.unlock();
		
	}
	public long getTotalPagesRead() {
		return totalPagesRead.longValue();
	}

	public void setTotalPagesRead() {
		this.totalPagesRead.incrementAndGet();		
	}
/**
 * Worker class which executes the task
 * @author cis455
 *
 */
	 public class Worker implements Runnable{

		private final Thread thread;
		private String status = "Waiting"; 		
		
		public String getStatus(){
			return status;
		}
		
		private void setStatus(String s){
			this.status = s;
		}
		
		private Worker(){
			this.thread = Thread.currentThread();
		}
		
		@Override
		public void run() {
			try{
			 while(!shutdown){
					try{
						    Object r = queue.take();
							if(r == KILL_ME)
								return;
							else
								if(r instanceof Callable)
								{	
									if(r instanceof Crawler)
									{
										@SuppressWarnings("unchecked")
										Object  o = ((Callable<Object>) r).call();
										if(o==null){
											System.out.println("Null");
										}
										if(o instanceof HashMap<?, ?>)
										{	
											addLinkToProcessed(((Crawler) r).getCurrentURL());
											HashMap<String,Object> retVal = (HashMap<String,Object>)o;
											processAndAddToQueue(retVal);
										}		
									}
								}
								else
									LOG.error("Invalid Object received "+r.getClass()+".Only Runnable expected");
						   						
					}catch(InterruptedException interrupt){
						if(thread.isInterrupted())
							return;
					    LOG.debug("Interrupted exception in: "+thread.getName());
					}				
				}
			}catch(Exception intr){
							
				this.thread.interrupt();
				
			}finally{
				isShutdownRequired();
				this.thread.interrupt();
			}
		}
		
		private void addLinkToProcessed(String link){
			VisitedListEntity entity = new VisitedListEntity();
			entity.setPageUrl(link);
			entity.setVisitedTime(new Date().getTime());
			accessor.putEntity(entity);
			setTotalPagesRead();
		}
		
		private void processAndAddToQueue(HashMap<String,Object> retVal){
			
			List<String> linkLists = (List<String>)retVal.get(Constants.LINK_INFO_LINKS);
			long value = (Long)retVal.get(Constants.LINK_INFO_SIZE);
			
			if(value>0){
				incrementSize(value);
			}
			
			if(linkLists!=null){
				for(String link:linkLists){
					VisitedListEntity ent = accessor.fetchEntityFromPrimaryKey(link);
					if(ent==null){
						try{
							Crawler crawl = new Crawler(link, xmlAccessor, htmlAccessor, maxSize,robAccessor);
							execute(crawl);
						}catch(MalformedURLException mfue){
							LOG.debug("Malformed URL: "+mfue.getMessage());
						}
					}
				}
			}
			isShutdownRequired();
						
		}
		
	}

}
