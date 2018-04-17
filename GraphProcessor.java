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
					
				}
			}
	      	 shortestPathPrecomputation();
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
    	// if graph has < 2 words
    	if(vertices.size() < 2 || word1 == null || word2 == null) 
    		return new ArrayList<String>();
    	word1 = word1.toUpperCase().trim();
        word2 = word2.toUpperCase().trim();
    	if(word1.equals(word2)
    			|| word1.equals("") || word2.equals("") ) // if the two words are the same
    		return null;
        int location1 = -1; // stores location of word1 and 2 in the array vertices
        int location2 = -1; 
        int counter = 0; // tracks location of words 
        for(Vertex<String> v: vertices) {
        	if(v.getVal().equals(word1))
        		location1 = counter;
        	if(v.getVal().equals(word2))
        		location2 = counter;
        	counter++;
        }
        // if either word isn't in the graph
        if(location1 == -1 || location2 == -1)
        	return null;
        // returns the shortest path from word1 to word2
        return paths.get(location2).get(location1);
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
    	// add new cell to column to create a new row
    	for(int i = 0; i < vertices.size(); i++) {
			ArrayList<String> aa = new ArrayList<String>();
			for(int j = 0; j < vertices.size(); j++)
			paths.get(i).add(j, aa);
    	}
    	// i iterates through list, counter finds elements to compute a path for
    	 
    	int counter;
		int i = counter = 0;
		
		
		//finds shortest path, builds shortest path from i to j
    	for(ArrayList<ArrayList<String>> v: paths) {
    		dijkstra(vertices.get(i));
    		for(int j = counter; j < paths.size(); j++) {
    			ArrayList<String> finalPath = buildPath(vertices.get(i), vertices.get(j));
    				v.set(j, finalPath);
    				paths.get(j).set(i, finalPath);
    		}
    		i++;
    		counter++;
    		// resets vertex values
    		for(Vertex<String> ver: vertices)
        		ver.setDefault(); 
    	}
    }
    
    private void dijkstra(Vertex<String> start) {
    	start.setWeight(0); // sets start weight at 0
    	PriorityQueue<Vertex<String>> pq = new PriorityQueue<Vertex<String>>(new VertexComparator());
    	pq.add(start); // adds first vertex to PQ
    	while(!pq.isEmpty()) { // runs through whole pq 
    		Vertex<String> min = pq.poll(); // removes highest element in pq
    		min.setVisited(true); // marks element as visited
    		for(String str: graph.getNeighbors(min.getVal())) { 
    			Vertex<String> neighbor = null;
    			
    			//finds the vertices to each neighbor
    			for(Vertex<String> v: vertices) {
    				if(v.getVal().equals(str)) {
    					neighbor = v;
    					break;
    				}
    			}
    			// iterates through each unvisited neighbor
    			if(!neighbor.isVisited()) {
    				// checks if the weight can be reduced
    				if(neighbor.getWeight() > min.getWeight() + 1) {
    					
    					//updates vaules for neighbor and puts in priority queue
    					neighbor.setWeight(min.getWeight() + 1);
    					neighbor.setPred(min);
    					pq.add(neighbor);
    				}
    			}
    		}
    	}
    }
private ArrayList<String> buildPath(Vertex<String> start, Vertex<String> end) {
	Stack<String> stack = new Stack<String>();
	Vertex<String> current = end;
	ArrayList<String> array = new ArrayList<String>();
	// adds vertices to start of list
	while(current != start && current != null) {
		stack.add(current.getVal());
		current = current.getPred();
		}
	stack.add(start.getVal());
	// if path does not exist or start = end
	if(stack.size() == 1 || current == null || current != start)
		return new ArrayList<String>();
	while(!stack.isEmpty()) {
		array.add(stack.pop());
	}
	return array;
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
}
