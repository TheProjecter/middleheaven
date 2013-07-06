package org.middleheaven.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import org.middleheaven.collections.TransformedCollection;
import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.util.function.Mapper;

/**
 * 
 * @param <E>
 * @param <V>
 */
public class QueueGraphPath<E, V> implements GraphPath<E, V> {

	
	private final Deque<Edge<V,E>> queue = new LinkedList<Edge<V,E>>();
	private final Graph<E, V> graph;
	
	
	public QueueGraphPath (Graph<E,V> graph){
		this.graph = graph;
	}
	
	@Override
	public V getStartVertex() {
		if (queue.isEmpty()){
			return null;
		}
		
		return queue.getFirst().getSourceVertex().getObject();
	}

	@Override
	public V getEndVertex() {
		return queue.getLast().getSourceVertex().getObject();
	}

	@Override
	public Collection<E> getEdges() {
		return Collections.unmodifiableCollection(TransformedCollection.transform(queue, new Mapper<E, Edge<V,E>>(){

			@Override
			public E apply(Edge<V, E> obj) {
				return obj.getObject();
			}

		}));
	}

	@Override
	public void visit(GraphPathVisitor<E, V> visitor) {
		
		visitor.beginVisitGraph(graph);
		
		if (!this.queue.isEmpty()){
			for (Edge<V,E> edge : this.queue){
				
				visitor.beginVisitVertex(edge.getSourceVertex().getObject());
				
				visitor.visitEdge(edge.getObject());
				
				visitor.endVisitVertex(edge.getSourceVertex().getObject());
				
			}
			
			V v = this.queue.getLast().getTargetVertex().getObject();
			
			visitor.beginVisitVertex(v);
			
			visitor.endVisitVertex(v);
			
		}

		visitor.endVisitGraph(graph);
	}
	
	/**
	 * Add the next edge in the path.
	 * @param e the next edge
	 */
	public void addFirstEdge(Edge<V,E> e){
		if (e != null){
			queue.addFirst(e);
		}
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public Graph<E, V> getGraph() {
		return graph;
	}

}
