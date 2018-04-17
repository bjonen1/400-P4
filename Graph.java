////////////////////////////////////////////////////////////////////////////
//Semester:         CS400 Spring 2018
//PROJECT:          P4 Dictionary Graph
//FILES:            Graph.java
//GraphProcessor.java
//GraphTest.java
//WordProcessor.java
//
//USER:         Steven Mulvey
//				Brandon Jonen	
//				Natalie Brooks
//				Nick Merfield
//				Michael O'Connor
//
//Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
//Bugs:             no known bugs
//
//Due:				2018 Apr 16, 2018 GraphProcessor.java 
////////////////////////////80 columns wide //////////////////////////////////
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Undirected and unweighted graph implementation
 * 
 * @param <E> type of a vertex
 * 
 * @author Natalie Brooks
 * 
 */
public class Graph<E> implements GraphADT<E> {
	
	/**
	 * Vertex class. Each vertex has a word and an ArrayList of adjacent vertices.
	 * @author nataliebrooks
	 * @param <K>
	 */
	class Vertex<K> {
		K word;
		ArrayList<Vertex<K>> adjacents;
		
		/**
		 * Vertex constructor.
		 * @param word is the information held in the vertex.
		 */
		public Vertex(K word) {
			this.word = word;
			adjacents = new ArrayList<Vertex<K>>();
		}
	}

	HashMap<E, Vertex<E>> words;
	
	/**
	 * Graph constructor takes no parameters and creates an empty HashMap.
	 */
	public Graph() {
		this.words = new HashMap<E, Vertex<E>>();
	}
	

    /**
     * {@inheritDoc}
     */
	/**
	 * Add new vertex to the graph
	 * 
	 * Valid argument conditions:
	 * 1. vertex should be non-null
	 * 2. vertex should not already exist in the graph 
	 * 
	 * @param vertex the vertex to be added
	 * @return vertex if vertex added, else return null if vertex can not be added (also if valid conditions are violated)
	 */
    @Override
    public E addVertex(E vertex) {
    		// check argument conditions
        if ( vertex == null || words.containsKey(vertex)) {
        		return null;
        } else {
        		Vertex<E> newVertex = new Vertex<E>(vertex);
        		words.put(vertex, newVertex);
        		return vertex;
        }
    }

    /**
     * {@inheritDoc}
     */
	/**
	 * Remove the vertex and associated edge associations from the graph
	 * 
	 * Valid argument conditions:
	 * 1. vertex should be non-null
	 * 2. vertex should exist in the graph 
	 *  
	 * @param vertex the vertex to be removed
	 * @return vertex if vertex removed, else return null if vertex and associated edges can not be removed (also if valid conditions are violated)
	 */
    @Override
    public E removeVertex(E vertex) {
    		// check argument conditions
        if ( vertex == null || !words.containsKey(vertex)) {
    			return null;
        } else {
        		Vertex<E> oldVertex = words.get(vertex);
        		// remove edges connecting vertex to other vertices
        		for ( Vertex<E> v : oldVertex.adjacents ) {
        			v.adjacents.remove(oldVertex);
        		}
        		// remove vertex from hashmap
        		words.remove(vertex);
        		return vertex;
        }
    }

    /**
     * {@inheritDoc}
     */
	/**
	 * Add an edge between two vertices (edge is undirected and unweighted)
	 * 
	 * Valid argument conditions:
	 * 1. both the vertices should exist in the graph
	 * 2. vertex1 should not equal vertex2
	 *  
	 * @param vertex1 the first vertex
	 * @param vertex2 the second vertex
	 * @return true if edge added, else return false if edge can not be added (also if valid conditions are violated)
	 */
    @Override
    public boolean addEdge(E vertex1, E vertex2) {
    		// check argument conditions
        if ( !words.containsKey(vertex1) || !words.containsKey(vertex2) || (vertex1.equals(vertex2))) {
    			return false;
        } else {
	        	Vertex<E> v1 = words.get(vertex1);
	        Vertex<E> v2 = words.get(vertex2);
	        // if edge already exists, it should not be added again
	        if ( v1.adjacents.contains(v2) || v2.adjacents.contains(v1) ) {
	        		return false;
	        } else {
		        v1.adjacents.add(v2);
		        v2.adjacents.add(v1);
		        return true;
	        }
        } 
    }    

    /**
     * {@inheritDoc}
     */
	/**
	 * Remove the edge between two vertices (edge is undirected and unweighted)
	 * 
	 * Valid argument conditions:
	 * 1. both the vertices should exist in the graph
	 * 2. vertex1 should not equal vertex2
	 *  
	 * @param vertex1 the first vertex
	 * @param vertex2 the second vertex
	 * @return true if edge removed, else return false if edge can not be removed (also if valid conditions are violated)
	 */
    @Override
    public boolean removeEdge(E vertex1, E vertex2) {
    		// check argument conditions
        if ( !words.containsKey(vertex1) || !words.containsKey(vertex2) || (vertex1.equals(vertex2))) {
    			return false;
        } else {
	        	Vertex<E> v1 = words.get(vertex1);
	        Vertex<E> v2 = words.get(vertex2);
	        // if edge does not already exist, it cannot be removed
	        if ( !v1.adjacents.contains(v2) || !v2.adjacents.contains(v1) ) {
	        		return false;
	        } else {
		        v1.adjacents.remove(v2);
		        v2.adjacents.remove(v1);
		        return true;
	        }
        } 
    }

    /**
     * {@inheritDoc}
     */
	/**
	 * Check whether the two vertices are adjacent
	 * 
	 * Valid argument conditions:
	 * 1. both the vertices should exist in the graph
	 * 2. vertex1 should not equal vertex2
	 *  
	 * @param vertex1 the first vertex
	 * @param vertex2 the second vertex
	 * @return true if both the vertices have an edge with each other, else return false if vertex1 and vertex2 are not connected (also if valid conditions are violated)
	 */
    @Override
    public boolean isAdjacent(E vertex1, E vertex2) {
    		// check argument conditions
        if ( !words.containsKey(vertex1) || !words.containsKey(vertex2) || (vertex1.equals(vertex2))) {
    			return false;
        } else {
	        	Vertex<E> v1 = words.get(vertex1);
	        Vertex<E> v2 = words.get(vertex2);
	        // edge exists
	        if ( v1.adjacents.contains(v2) && v2.adjacents.contains(v1) ) {
	        		return true;
	        } else {
		        return false;
	        }
        } 
    }

    /**
     * {@inheritDoc}
     */
	/**
	 * Get all the neighbor vertices of a vertex
	 * 
	 * Valid argument conditions:
	 * 1. vertex is not null
	 * 2. vertex exists
	 * 
	 * @param vertex the vertex
	 * @return an iterable for all the immediate connected neighbor vertices
	 */
    @Override
    public Iterable<E> getNeighbors(E vertex) {
    		// check argument conditions
        if ( vertex == null || !words.containsKey(vertex)) {
    			return null;
        } else {
        		Vertex<E> v = words.get(vertex);
        		ArrayList<E> neighbors = new ArrayList<E>();
        		// create ArrayList of the vertex words
        		for ( Vertex<E> n : v.adjacents ) {
        			neighbors.add(n.word);
        		}
        		return neighbors;
        }
    }

    /**
     * {@inheritDoc}
     */
	/**
	 * Get all the vertices in the graph
	 * 
	 * @return an iterable for all the vertices
	 */
    @Override
    public Iterable<E> getAllVertices() {
        return words.keySet();
    }

}
