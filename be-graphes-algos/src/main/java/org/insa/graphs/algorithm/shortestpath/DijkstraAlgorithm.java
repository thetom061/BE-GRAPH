package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
       
        
        // TODO:
        // tas binaire pour les couts
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        
        //les labels crées pour tout les sommets
        ArrayList<Label> labels = new ArrayList<Label>();
        
        final int nbNodes = data.getGraph().size();
        
        //initialisation
        for(Node node : data.getGraph().getNodes()) {
        	Label label = new Label(node);
        	labels.add(label);
        }
        
        // On met le cout du label pour le sommet d'origine à 0
        Label originLabel = labels.get(data.getOrigin().getId());
        originLabel.setCost(0);
        
        heap.insert(originLabel);
        
        Label destinationLabel = labels.get(data.getDestination().getId());
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        //tant qu'il existe des sommets non marques
        while(!destinationLabel.getMarque() && !heap.isEmpty()){
        	Label label=heap.deleteMin();
        	System.out.println(label.getNode().getId());
        	label.setMarque(true);
        	List <Arc> successeurs=label.getNode().getSuccessors();
        	
        	for (Arc successeur: successeurs) {
        		if (!data.isAllowed(successeur)) {
                    continue;
                }
        		Label y=labels.get(successeur.getDestination().getId());
        		System.out.println(y.getNode().getId());
        		
        		if (!y.getMarque()) {
        			float previousCost=y.getCost();
        			float cout=successeur.getLength()+label.getCost();
        			if (cout<y.getCost()) {
        			//	labels.get(successeur.getDestination().getId()).setCost(cout);
        				y.setCost(cout);        				
        				y.setPreviousArc(successeur);
        				y.setPreviousLabel(label);
        				predecessorArcs[y.getPreviousArc().getDestination().getId()] = y.getPreviousArc();
        				//si il est deja dans le tas on l'enlève
        				}	
        			if(previousCost!=Float.MAX_VALUE) {
        					System.out.println("insert+remove");
        					heap.remove(y);
        					heap.insert(y);	
        					
        			}else {
        				System.out.println("only insert");
        				heap.insert(y);        				
        			}	
        		}
        	}
        }
        //si le chemin n'est pas possible
        if (destinationLabel.getCost()==Float.MAX_VALUE) {
        	solution= new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        }else {
        	/*
        	notifyDestinationReached(data.getDestination());
        	ArrayList<Arc> arcs = new ArrayList<Arc>();
        	Label label = destinationLabel;
        	while(label.hasPreviousArc()){
        		arcs.add(label.getPreviousArc());
        		label = label.getPreviousLabel();
        	}
        	
        	Path path=new Path(data.getGraph(),arcs);
        	solution=new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL,path);
        }
        */
        	 // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
        }   
        return solution;
    }

}
