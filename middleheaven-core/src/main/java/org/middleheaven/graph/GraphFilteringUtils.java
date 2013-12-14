package org.middleheaven.graph;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.function.Predicate;

/**
 * 
 * 
 */
public final class GraphFilteringUtils {

	
	private GraphFilteringUtils (){}
	
	/**
	 * Fills a new graph by coping another graph and Predicate to eliminate Edges and Vertices.
	 * @param <E>
	 * @param <V>
	 * @param sourceGraph
	 * @param verticesClassifier
	 * @param edgeClassifier
	 * @return
	 */
	public static <E, V, G extends Graph<E,V> > G  filter(G sourceGraph, Predicate<V> verticesClassifier , Predicate<E> edgeClassifier ) {
		
		G targetGraph = Introspector.of(sourceGraph).introspectClass().newInstance();
		
		for ( Edge<V,E> edge : sourceGraph.getEdges()){

			Vertex<V,E> s = edge.getSourceVertex();
			Vertex<V,E> t = edge.getTargetVertex();
			
			if ((verticesClassifier.apply(s.getObject()) || verticesClassifier.apply(t.getObject())) && edgeClassifier.apply(edge.getObject())){
				targetGraph.addEdge(edge.getObject(), s.getObject(), t.getObject(), edge.getCost());
			}
		}
		
		return targetGraph;
	}
	
	public static <E, V, G extends Graph<E,V> > G  filter(G sourceGraph, Predicate<V> verticesClassifier ) {
		return filter(sourceGraph, verticesClassifier, new Predicate<E>(){

			@Override
			public Boolean apply(E obj) {
				return Boolean.TRUE;
			}});
	}
		
	
	
}
