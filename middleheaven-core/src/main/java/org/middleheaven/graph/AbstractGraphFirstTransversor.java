package org.middleheaven.graph;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;

public abstract class AbstractGraphFirstTransversor extends AbstractGraphTransversor {

	@Override
	public <E, V> void transverse(Graph<E, V> graph, V startVertex) {

		Collection<Vertex<V, E>> all = graph.getVertices();
		
		if (all.isEmpty()){
			return;
		}

		Vertex<V, E> start = graph.getVertex(startVertex);

		Set<Vertex<V, E>> visited = new HashSet<Vertex<V, E>>();

		visited.add(start);
		
		LinkedList<Vertex<V, E>> q = new LinkedList<Vertex<V, E>>();
		q.add(start); 

		GraphTranverseListener broadcastEvent = this.getListenerSet().broadcastEvent();
		
		while(!q.isEmpty()){
			Vertex<V, E> v = this.nextVertex(q);

			broadcastEvent.beginVertex(new VertexTraversalEvent<V, E>(v));
			
			if (!visited.contains(v)){
				for (Edge<V, E> e : v.getOutjacentEdges()) {

					
					broadcastEvent.beginEdgeTraversed(new EdgeTraversalEvent(e));
					Vertex <V, E> w = e.getTargetVertex();
					
					if (visited.add(w)) {
						q.add(w);
					}
					
					broadcastEvent.endEdgeTraversed(new EdgeTraversalEvent(e));
				}
			}
			
			broadcastEvent.endVertex(new VertexTraversalEvent<V, E>(v));
			
		}

	}

	
	protected abstract <V, E> Vertex<V, E> nextVertex(Deque<Vertex<V, E>> q);
	
}
