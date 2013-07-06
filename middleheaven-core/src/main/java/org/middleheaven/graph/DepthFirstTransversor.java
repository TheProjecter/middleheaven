package org.middleheaven.graph;

import java.util.Deque;

import org.middleheaven.graph.Graph.Vertex;


public class DepthFirstTransversor extends  AbstractGraphFirstTransversor {


	@Override
	protected <V, E> Vertex<V, E> nextVertex(Deque<Vertex<V, E>> q) {
		return q.removeLast(); // uses a stack
	}


	

}
