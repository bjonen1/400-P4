import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This class adds additional functionality to the graph as a whole.
 * 
 * Contains an instance variable, {@link #graph}, which stores information for all the vertices and edges.
 * @see #populateGraph(String)
 *  - loads a dictionary of words as vertices in the graph.
 *  - finds possible edges between all pairs of vertices and adds these edges in the graph.
 *  - returns number of vertices added as Integer.
 *  - every call to this method will add to the existing graph.
 *  - this method needs to be invoked first for other methods on shortest path computation to work.
 * @see #shortestPathPrecomputation()
 *  - applies a shortest path algorithm to precompute data structures (that store shortest path data)
 *  - the shortest path data structures are used later to 
 *    to quickly find the shortest path and distance between two vertices.
 *  - this method is called after any call to populateGraph.
 *  - It is not called again unless new graph information is added via populateGraph().
 * @see #getShortestPath(String, String)
 *  - returns a list of vertices that constitute the shortest path between two given vertices, 
 *    computed using the precomputed data structures computed as part of {@link #shortestPathPrecomputation()}.
 *  - {@link #shortestPathPrecomputation()} must have been invoked once before invoking this method.
 * @see #getShortestDistance(String, String)
 *  - returns distance (number of edges) as an Integer for the shortest path between two given vertices
 *  - this is computed using the precomputed data structures computed as part of {@link #shortestPathPrecomputation()}.
 *  - {@link #shortestPathPrecomputation()} must have been invoked once before invoking this method.
 *  
 * @author sapan (sapan@cs.wisc.edu)
 * 
 */
public class GraphProcessor {

    /**
     * Graph which stores the dictionary words and their associated connections
     */
	private ArrayList<ArrayList<ArrayList<String>>> paths;
    private GraphADT<String> graph;
    private ArrayList<Vertex<String>> vertices;
    private int numVertices;
    /**
     * Constructor for this class. Initializes instances variables to set the starting state of the object
     */
    public GraphProcessor() {
        this.graph = new Graph<>();
        vertices = new ArrayList<>();
        this.paths = new ArrayList<ArrayList<ArrayList<String>>>();
        numVertices = 0;
    }
        
    /**
     * Builds a graph from the words in a file. Populate an internal graph, by adding words from the dictionary as vertices
     * and finding and adding the corresponding connections (edges) between 
     * existing words.
     * 
     * Reads a word from the file and adds it as a vertex to a graph.
     * Repeat for all words.
     * 
     * For all possible pairs of vertices, finds if the pair of vertices is adjacent {@link WordProcessor#isAdjacent(String, String)}
     * If a pair is adjacent, adds an undirected and unweighted edge between the pair of vertices in the graph.
     * 
     * @param filepath file path to the dictionary
     * @return Integer the number of vertices (words) added
     */
    public Integer populateGraph(String filepath) {
       int count = 0;
       try {
    	   Stream<String> stream = WordProcessor.getWordStream(filepath);
    	   List<String> listOfLines = stream.collect(Collectors.toList());
    	   
    		for(String word: listOfLines) {
				String newString = graph.addVertex(word);
				if(newString != null) {
					Vertex<String> v = new Vertex<String>(word);
					vertices.add(v);
					count++;
					numVertices++;
					for(String vertices: graph.getAllVertices()) {
						if(vertices.equals(word))
							continue;
						if(WordProcessor.isAdjacent(word, vertices)) {
							graph.addEdge(word, vertices);
						}
					}
					paths.add(new ArrayList<ArrayList<String>>());
					for(ArrayList<ArrayList<String>> col: paths) {
						col.add(new ArrayList<String>());
					}
					shortestPathPrecomputation();
				}
			}
		} catch (IOException e) {
			count = -1; 
		}
    	
    	
    	return count;
    
    }

    
    /**
     * Gets the list of words that create the shortest path between word1 and word2
     * 
     * Example: Given a dictionary,
     *             cat
     *             rat
     *             hat
     *             neat
     *             wheat
     *             kit
     *  shortest path between cat and wheat is the following list of words:
     *     [cat, hat, heat, wheat]
     * 
     * @param word1 first word
     * @param word2 second word
     * @return List<String> list of the words
     */
    public List<String> getShortestPath(String word1, String word2) {
    	  int loc1 = -1;
          int loc2 = -2;
          int counter = 0;
          for(Vertex<String> v: vertices) {
          	if(v.getVal().equals(word1))
          		loc1 = counter;
          	if(v.getVal().equals(word2))
          		loc2 = counter;
          }
      	return paths.get(loc2).get(loc1);
      }
    
    /**
     * Gets the distance of the shortest path between word1 and word2
     * 
     * Example: Given a dictionary,
     *             cat
     *             rat
     *             hat
     *             neat
     *             wheat
     *             kit
     *  distance of the shortest path between cat and wheat, [cat, hat, heat, wheat]
     *   = 3 (the number of edges in the shortest path)
     * 
     * @param word1 first word
     * @param word2 second word
     * @return Integer distance
     */
    public Integer getShortestDistance(String word1, String word2) {
    	 return getShortestPath(word1, word2).size() - 1;
    }
    
    /**
     * Computes shortest paths and distances between all possible pairs of vertices.
     * This method is called after every set of updates in the graph to recompute the path information.
     * Any shortest path algorithm can be used (Djikstra's or Floyd-Warshall recommended).
     */
    public void shortestPathPrecomputation() {
    	Vertex<String> newVertex = vertices.get(numVertices - 1);
    	int i = 0;
    	for(Vertex<String> v: vertices) {
    		if(v.getVal().equals(newVertex.getVal()))
    			continue;
    		ArrayList<String> shortestPath = dijkstra(v, newVertex);
    		paths.get(numVertices - 1).add(i, shortestPath);
    		paths.get(i).add(numVertices - 1, shortestPath);
    		i++;
    	}
    }
    
    private ArrayList<String> dijkstra(Vertex<String> start, Vertex<String> end) {
    	start.setWeight(0);
    	PriorityQueue<Vertex<String>> pq = new PriorityQueue<Vertex<String>>(new VertexComparator());
    	pq.add(start);
    	while(!pq.isEmpty()) {
    		Vertex<String> min = pq.poll();
    		min.setVisited(true);
    		for(String str: graph.getNeighbors(min.getVal())) {
    			Vertex<String> neighbor = null;
    			for(Vertex<String> v: vertices) {
    				if(v.getVal().equals(str)) {
    					neighbor = v;
    				}
    			}
    			if(!neighbor.isVisited()) {
    				if(neighbor.getWeight() > min.getWeight() + 1) {
    					neighbor.setWeight(min.getWeight() + 1);
    					neighbor.setPred(min);
    					pq.add(neighbor);
    				}
    			}
    		}
    	}
    	LinkedList<String> p = new LinkedList<String>();
    	Vertex<String> current = end;
    	while(current != null) {
    		p.addFirst(current.getVal());
    		current.setDefault();
    		current = current.getPred();
    		}
    	String[] arr = (String[]) p.toArray();
    	return new ArrayList<String>(Arrays.asList(arr));
    }
}
    class Vertex<T>{
    	public Vertex(T val) {
    		visited = false;
    		weight = Integer.MAX_VALUE;
    		pred = null;
    		this.val = val;
    	}
    	
    	public T getVal() {
			return val;
		}
    	public boolean isVisited() {
			return visited;
		}
		public void setVisited(boolean visited) {
			this.visited = visited;
		}
		public int getWeight() {
			return weight;
		}
		public void setWeight(int weight) {
			this.weight = weight;
		}
		public Vertex<String> getPred() {
			return pred;
		}
		public void setPred(Vertex<String> pred) {
			this.pred = pred;
		}
		public void setDefault() {
			visited = false;
    		weight = Integer.MAX_VALUE;
    		pred = null;
		}
		private boolean visited;
		private int weight;
    	private Vertex<String> pred;
    	private T val;
	}
    
    class VertexComparator implements Comparator<Vertex<String>> {
		
    	@Override
		public int compare(Vertex<String> o1, Vertex<String> o2) {
			Integer comp1 = o1.getWeight();
			Integer comp2 = o2.getWeight();
			if(comp1.equals(comp2)) {
				return o1.getVal().compareTo(o2.getVal());
			}
			else
				return comp1.compareTo(comp2);
		}
}
