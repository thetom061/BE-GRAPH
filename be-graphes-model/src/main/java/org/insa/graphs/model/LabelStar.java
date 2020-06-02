package org.insa.graphs.model;

public class LabelStar extends Label{
	protected float coutEstime;
	
	
	public LabelStar(Node node,float cost) {
		super(node);
		this.coutEstime=cost;
	}
	
	public void setCoutEstime(float cost){
		this.coutEstime=cost;
	}
	
	@Override
	public float getCost() {
		return coutEstime+cout;
	}
	
	public float getCoutEstime() {
		return coutEstime;
	}
}
