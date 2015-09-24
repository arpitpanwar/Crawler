package edu.upenn.cis455.storage;

import java.io.File;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

import edu.upenn.cis455.xpathengine.utils.Constants;

/**
 * Class which is wrapper class for database
 * @author cis455
 *
 */
public class DBWrapper {
	
	static final Logger LOG = Logger.getLogger(DBWrapper.class);
	
	private static String envDirectory = System.getProperty("user.home")+File.separator+"crawlerdb";
	
	private static Environment myEnv;
	private static EntityStore store;
	
	public DBWrapper(String dir){
			
		envDirectory = dir;
		generateEnvironment();
		
	}
	
	private static void generateEnvironment(){
			
		try{
		
		if(!new File(envDirectory).exists()){
				new File(envDirectory).mkdirs();
		}
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
		StoreConfig config = new StoreConfig();
		
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(true);
		
		config.setAllowCreate(true);
		config.setTransactional(true);
	
		
		myEnv = new Environment(new File(envDirectory), envConfig);
		store = new EntityStore(myEnv, Constants.ENTITY_STORE_NAME, config);
		}catch(DatabaseException dbe){
			LOG.debug("Exception generating environment: "+dbe.getMessage());
		}
	}
	
	public static boolean closeEnvironment(){
		try{
			
			
			if(store!=null){
				store.close();
			}

			if(myEnv!=null){
				myEnv.cleanLog();
				myEnv.close();
				return true;
			
			}else{
				return false;
			}
			
		}catch(DatabaseException dbe){
			LOG.debug("Exception closing environment: "+dbe.getMessage());
			
		}catch(Exception e){
			LOG.debug("Unexpected error closing the environment: "+e.getMessage());
		}
		return false;
		
	}

	public static Environment getMyEnv() {
		if(myEnv == null)
			generateEnvironment();
		return myEnv;
	}

	public static EntityStore getStore() {
		if(store==null)
			generateEnvironment();
		return store;
	}
	
	
	

	
}
