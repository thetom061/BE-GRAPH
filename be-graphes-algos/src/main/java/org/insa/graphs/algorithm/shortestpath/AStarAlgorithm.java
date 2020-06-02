package org.insa.graphs.algorithm.shortestpath;



import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Label;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    //pour faire les LabelStar
    @Override
    protected Label initialisationLabel(Node node) {
    	float coutEstime;
    	//longueur
    	if(getInputData().getMode()== AbstractInputData.Mode.LENGTH) {
    		coutEstime=(float) Point.distance(node.getPoint(), getInputData().getDestination().getPoint());
    		//temps
    	}else {
    		coutEstime= (float) (Point.distance(node.getPoint(), getInputData().getDestination().getPoint())/getInputData().getMaximumSpeed());
    	}
    	return new LabelStar(node,coutEstime);
    }

}
