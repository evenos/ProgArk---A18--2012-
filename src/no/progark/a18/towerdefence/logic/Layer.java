package no.progark.a18.towerdefence.logic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;

public abstract class Layer implements IUpdateHandler, Iterable<Content>{
	private List<Content> contents = new LinkedList<Content>();
	
	/**
	 * Add content to this layer
	 * @param content to be added
	 */
	public void addContent(Content content){
		contents.add(content);
	}
	
	/**
	 * Remove content from this layer
	 * @param content to be removed
	 */
	public void removeContent(Content content){
		contents.remove(content);
	}
	
	/**
	 * Clear all content from this layer
	 */
	public void clearContent(){
		contents.clear();
	}
	
	public Iterator<Content> iterator() {
		return contents.iterator();
	}

}
