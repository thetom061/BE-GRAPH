package org.insa.graphs.algorithm.test;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;

public class DijkstraPathTest extends ShortestPathTest{
	@Override
	public ShortestPathSolution makeChemin(ShortestPathData data) {
		return new DijkstraAlgorithm(data).doRun();
	}
	
}
