package org.insa.graphs.model;

public class Label implements Comparable<Label>{
	protected Node sommet_courrant;
	
	protected Arc previousArc;
	
	protected boolean marque;
	
	protected float cout;
	
	protected Label previousLabel;
	
	public Label(Node node) {
		this.sommet_courrant=node;
		this.previousArc=null;
		this.marque=false;
		this.cout=Float.MAX_VALUE;
		this.previousLabel = null;
	}
	
	
	
	public Node getNode() {
		return sommet_courrant;
	}
	public void setPreviousArc(Arc arc) {
		this.previousArc=arc;
	}
	
	public float getCost(){
		return cout;
	}
	
	public void setCost(float cost) {
		this.cout=cost;
	}
	
	public boolean getMarque() {
		return marque;
	}
	
	public void setMarque(boolean bool) {
		this.marque=bool;
	}
	
	public boolean hasPreviousArc(){
		return previousArc!=null;
	}
	
	public Arc getPreviousArc() {
		return previousArc;
	}
	
	public Label getPreviousLabel() {
		return previousLabel;
	}
	
	public void setPreviousLabel(Label label) {
		this.previousLabel = label;
	}
	
	@Override
	public int compareTo(Label o) {
		return Float.compare(this.getCost(), o.getCost());
	}
}
