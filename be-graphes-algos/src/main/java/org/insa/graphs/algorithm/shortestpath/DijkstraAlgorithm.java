package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    //initialiser label qu'on override dans astar
    protected Label initialisationLabel(Node node) {
    	return new Label(node);
    }
    
    @Override
	public ShortestPathSolution doRun() {
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
        	Label label = initialisationLabel(node);
        	labels.add(label);
        }
        
        // On met le cout du label pour le sommet d'origine à 0
        Label originLabel = labels.get(data.getOrigin().getId());
        originLabel.setCost(0);
        
        heap.insert(originLabel);
        //on notifie qu'on a bien traité le node d'origine 
        notifyOriginProcessed(originLabel.getNode());
        //variable pour la vérification
        boolean test=false; 
        float coutMarque=0;
        float coutMarqueAvant;
        //retourne 0 si les vérifications passent et 1 sinon
        int verif=0;
        
        
        Label destinationLabel = labels.get(data.getDestination().getId());
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        //tant qu'il existe des sommets non marques
        while(!destinationLabel.getMarque() && !heap.isEmpty()){
        	
        	//on enlève le minimum du tas et le marque true
        	Label label=heap.deleteMin();
        	boolean previousMarque=label.getMarque();
        	label.setMarque(true);
        	
        	//test de coût croissant des labels
        	if (test==true) {
	        	coutMarqueAvant=coutMarque;
	        	coutMarque=label.getCost();
	        
	        	if (coutMarque!=0 & previousMarque==false) {
	        		if (coutMarque<coutMarqueAvant) {
	        			verif=1; 
	        		}
	        	}
        	}
        	//on notifie qu'on a marque le node associé au label
        	notifyNodeMarked(label.getNode());
        	
        	List <Arc> successeurs=label.getNode().getSuccessors();	
        	for (Arc successeur: successeurs) {
        		//pour vérifier si le mode de transport est autorisé sur l'arc
        		if (!data.isAllowed(successeur)) {
                    continue;
                }
        		//on notifie qu'on vient de passer par un node 
        		notifyNodeReached(successeur.getDestination());
        		
        		//label lié au node de destination de l'arc successeur
        		Label labelDest=labels.get(successeur.getDestination().getId());           		
        		if (!labelDest.getMarque()) {
        			float newCost=successeur.getLength()+label.getCost();
        			if (newCost<labelDest.getCost()) {
        				labelDest.setCost(newCost);        				
        				labelDest.setPreviousArc(successeur);
        				labelDest.setPreviousLabel(label);
        				//j'utilise la même méthode que dans l'algorithme de bellman-ford pour retrouver la liste d'arc final pour la solution
        				predecessorArcs[labelDest.getPreviousArc().getDestination().getId()] = labelDest.getPreviousArc();
        				//si il est déjà dans le tas on l'enleve, puis on insère dans le tas 
        				try {
    						heap.remove(labelDest);
    					}catch(ElementNotFoundException e) {}
        				heap.insert(labelDest);	
        				}	
        		}
        		//test de si le tas reste valide
        		if (test==true) {
        			if (heap.isValid()==false) {
        				System.out.println("Probleme tas");
        				verif=1;
        			}
        		}
        	}
        }
        //si le chemin n'est pas possible 
        if (destinationLabel.getCost()==Float.MAX_VALUE | destinationLabel.getCost()==0) {
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
            Path path=new Path(data.getGraph(), arcs);
            
            if (test=true) {
            	//test si le chemin est valide
            	if (path.isValid()==false) {
            		verif=1;
            	}
            	if (verif==0) {
            		System.out.println("L'algorithme vérifie bien les caractéristiques de l'algorithme de Dijkstra");
            	}else {
            		System.out.println("L'algorithme ne présente pas toutes les caractéristiques de l'algorithme de Dijkstra");
            	}
            }
            solution = new ShortestPathSolution(data, Status.OPTIMAL, path);
        }  
        
        return solution;
    }

}
