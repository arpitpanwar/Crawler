package edu.upenn.cis455.xpathengine.model;

import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * A model class containing list of elements and visited elements
 * The class is used when matching the xpath within the document
 * @author cis455
 *
 */


public class ElementListModel {
	
	private ArrayList<Element> elementList;
	private ArrayList<Element> visitedList;
	private Node node;
	
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	
	public ArrayList<Element> getElementList() {
		return elementList;
	}
	public void setElementList(ArrayList<Element> elementList) {
		this.elementList = elementList;
	}
	public ArrayList<Element> getVisitedList() {
		return visitedList;
	}
	public void setVisitedList(ArrayList<Element> visitedList) {
		this.visitedList = visitedList;
	}
	
	
	

}
