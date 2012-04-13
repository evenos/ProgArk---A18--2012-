package no.progark.a18.towerdefence;

import java.util.Map;
import java.util.TreeMap;

/**
 * A general scene used to represent the background and base context container.
 */
public abstract class Scene extends State{
	private Scene nextScene;
	private Map<String, Layer> layers;
	
	/**
	 * a new empty scene with no {@linkplain Scene#nextScene} set.
	 */
	public Scene() {
		layers = new TreeMap<String, Layer>();
	}
	
	/**
	 * 
	 * @param nextScene the scene to precede this on completion
	 */
	public Scene(Scene nextScene){
		this();
		this.nextScene = nextScene;
	}
	
	/**
	 * 
	 * @return the scene to precede this on completion
	 */
	public Scene getNextScene() {
		return nextScene;
	}
	
	/**
	 * 
	 * @param nextScene the scene to precede this on completion
	 */
	public void setNextScene(Scene nextScene) {
		this.nextScene = nextScene;
	}
	
	/**
	 * 
	 * @param layer the layer
	 * @param name the name identifying the layer
	 */
	public void addlayer(Layer layer, String name){
		layers.put(name, layer);
		registerUpdateHandler(layer);
	}
	
	/**
	 * 
	 * @param layer
	 * @param content
	 */
	public void addContent(String layer, Content content){
		layers.get(layer).addContent(content);
	}
}
