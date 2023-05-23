import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class GraphAlgorithms {

	PriorityQueue<PathState> pq;  // Used for Dijkstra's shortest path algorithm
	Queue<Vertex> queue;  // use for BFS
	Stack<Vertex> stack;  // used for DFS and for Topological Sort
	Set<String> visited;  // names of the visited vertices

	public GraphAlgorithms() {
		pq = new PriorityQueue<PathState>();
	}

	public String findShortestPath(Graph graph, String startVertexName, String endVertexName) {
		if (startVertexName == null || endVertexName == null) {
			return "path not found";
		}
		if (graph.getVertex(startVertexName) == null || graph.getVertex(endVertexName) == null) {
			return "path not found";
		}
		visited = new TreeSet<String>();
		PathState state = new PathState(graph.getVertex(startVertexName), 0, startVertexName);
		pq.add(state);
		while(!pq.isEmpty()) {
			state = pq.remove();
				visited.add(state.vertex.getName());
				if (state.vertex.getName().equals(endVertexName)) {
					String result = "Shortest Path " + state.pathToThisVertex + "\nTotal Weight: " + state.totalPathWt;
					return result.trim();
				}
				int currCost = state.totalPathWt;
				String currPath = state.pathToThisVertex + " ";
				Map<String,Integer> map = state.vertex.getAdjacentVerticesWeighted();
				for (Map.Entry<String, Integer> entry : map.entrySet()) {
					if (!visited.contains(entry.getKey())) {
						int nextCost = currCost + entry.getValue();
						String nextPath = currPath + entry.getKey();
						PathState nextState = new PathState(graph.getVertex(entry.getKey()), nextCost, nextPath);
						pq.add(nextState);
					}
				}
			}
		return "path not found";
	}

	public String breadthFirstTraversal(Graph graph, String startVertexName) {
		String result = "";
		queue = new ArrayDeque();
		visited = new TreeSet<String>();
		if (graph.getVertex(startVertexName) == null || startVertexName == null) {
			return "path not found";
		}
		queue.add(graph.getVertex(startVertexName));
		while (!queue.isEmpty()) {
			Vertex currentVertex = queue.remove();
			if (!visited.contains(currentVertex.getName())){
				result += currentVertex.getName() + " ";
				visited.add(currentVertex.getName());
				for (String vertexName : currentVertex.getAdjacentVertices()) {
					if (!visited.contains(vertexName)) {
						queue.add(graph.getVertex(vertexName));
					}
				}
			}
		}
		return result.trim();
	}

	public String depthFirstTraversal(Graph graph, String startVertexName) {
		if (graph.getVertex(startVertexName) == null || startVertexName == null) {
			return "path not found";
		}
		String result = "";
		stack = new Stack<Vertex>();
		visited = new TreeSet<String>();
		Vertex startVertex = graph.getVertex(startVertexName);
		stack.push(startVertex);
		result += startVertex.getName() +  " ";
		visited.add(startVertexName);
		while (!stack.isEmpty()) {
			boolean wasVisited = false;
			Vertex currentVertex = stack.peek();
			for (String vertex : currentVertex.getAdjacentVertices()) {
				if (! visited.contains(vertex)) {
					stack.push(graph.getVertex(vertex));
					visited.add(vertex);
					result += vertex + " ";
					wasVisited = true;
					break;
				}		
			}
			if (wasVisited == false) {
				stack.pop();
			}
		}	
		return result.trim();
	}

	public String topologicalSort(Graph graph) {
		String result = "";
		if (graph == null) {
			return "path not found";
		}
		stack = new Stack<Vertex>();
		int n = graph.vertexCount();
		visited = new TreeSet<String>();
		for (int i = 0; i < n; i++) {
			for (Vertex vertex : graph.getVertices()) {
				boolean hasUnvisitedSuccessor = false;
				for (String neighbor : vertex.getAdjacentVertices()) {
					if (!visited.contains(neighbor)) {
						hasUnvisitedSuccessor = true;
					}
				}
				if (!visited.contains(vertex.getName()) && !hasUnvisitedSuccessor) {
					visited.add(vertex.getName());
					stack.push(vertex);
					break;
				}
			}
		}
		while (!stack.isEmpty()) {
			result += stack.pop().getName() + " ";
		}
		return result.trim();
	}

	public static void main(String[] args) throws IOException {
		Graph graph = new Graph("graphData3.csv");
		GraphAlgorithms graphAlgorithms = new GraphAlgorithms();

		System.out.println(graphAlgorithms.findShortestPath( graph, "underpants", "LAX"));
		System.out.println(graphAlgorithms.findShortestPath( graph, "CVG", "DEN"));
		System.out.println(graphAlgorithms.findShortestPath( graph, "ATL", "DEN"));
		System.out.println(graphAlgorithms.breadthFirstTraversal(graph, "CVG"));
		System.out.println(graphAlgorithms.depthFirstTraversal(graph, "CVG"));
		System.out.println(graphAlgorithms.topologicalSort(graph));

	}

	public class PathState implements Comparable {
		public Vertex vertex;
		public int totalPathWt;
		public String pathToThisVertex;

		public PathState(Vertex v, int wt, String path) {
			vertex = v;
			totalPathWt = wt;
			pathToThisVertex = path;
		}

		@Override
		public int compareTo(Object other) {
			if (this.totalPathWt < ((PathState) other).totalPathWt)
				return -1;
			else if (this.totalPathWt > ((PathState) other).totalPathWt)
				return 1;
			else
				return 0;

		}
	}
}
