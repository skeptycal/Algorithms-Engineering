import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;

public class Amanda {

	public static void main(String[] args) throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
		System.in));
		
		String info[] = buffer.readLine().split(" ");
		int N = Integer.parseInt(info[0]);
		int M = Integer.parseInt(info[1]);

		Graph G = new Graph(N + 1); // 1-index based instead of zero
		int lounges[] = new int[G.numAirports()];
		for (int i = 0; i < G.numAirports(); i++) {
			lounges[i] = -1;
		}

		for (int i = 0; i < M; i++) {
			info = buffer.readLine().split(" ");
			Airport from = new Airport(Integer.parseInt(info[0]));
			Airport to = new Airport(Integer.parseInt(info[1]));
			int lounge = Integer.parseInt(info[2]);
			if (lounge == 1) {
				G.addRoute(from, to, lounge);
			} else if (lounge == 2) {
				lounges[from.id] = 1;
				lounges[to.id] = 1;
			} else if (lounge == 0) {
				lounges[from.id] = 0;
				lounges[to.id] = 0;
			}
		}
		buffer.close();

		DFS dfs = new DFS(G);

		int totalLounges = 0;

		int c_false = 0;
		int c_true = 0;
		for (int k = 1; k < G.numAirports(); k++) {
			for (Route r : G.adj(k)) {
				if (dfs.color(r.to.id)) {
					c_true++;
				} else {
					c_false++;
				}
				if (dfs.color(r.from.id)) {
					c_true++;
				} else {
					c_false++;
				}
			}
		}

		boolean isSmaller;
		int smaller = Math.min(c_true, c_false);
		if (smaller == c_false) {
			isSmaller = true;
		} else {
			isSmaller = false;
		}

		for (int i = 1; i < lounges.length; i++) {
			if (isSmaller) {
				if (!dfs.color(i)) {
					lounges[i] = 1;
				}
			} else {
				if (dfs.color(i)) {
					lounges[i] = 1;
				}
			}
			if (lounges[i] >= 0) {
				totalLounges += lounges[i];
			}
		}

		if (dfs.isBipartite()) {
			System.out.println(totalLounges);
		} else {
			System.out.println("impossible");
		}
	}

	private static class DFS {
		private boolean marked[];
		private boolean color[];
		private int count;
		private boolean isBipartite = true;

		public DFS(Graph G) {
			count = 0;
			marked = new boolean[G.num_airports];
			color = new boolean[G.num_airports];

			for (int i = 1; i < G.numAirports(); i++) {
				for (Route r : G.adj(i)) {
					if (!marked[r.from.id]) {
						dfs(G, r.from.id);
					}
				}
			}
		}

		private void dfs(Graph G, int r) {
			count++;
			marked[r] = true;
			for (Route w : G.adj(r)) {
				if (!marked[w.to.id]) {
					color[w.to.id] = !color[r];
					dfs(G, w.to.id);
				} else if (color[w.to.id] == color[r]) {
					isBipartite = false;
				}
			}
		}

		public boolean marked(int idx) {
			return marked[idx];
		}

		public boolean color(int idx) {
			return color[idx];
		}

		public int count() {
			return count;
		}

		public boolean isBipartite() {
			return isBipartite;
		}
	}

	private static class Graph {
		private final int num_airports;
		private int num_routes;
		private ArrayList<Route> routes[];

		public Graph(int N) {
			num_airports = N;
			routes = (ArrayList<Route>[]) new ArrayList[num_airports];
			for (int i = 1; i < num_airports; i++) {
				routes[i] = new ArrayList<Route>();
			}
		}

		public void addRoute(Airport from, Airport to, int l) {
			num_routes++;
			routes[from.id].add(new Route(from, to, l));
			routes[to.id].add(new Route(to, from, l));
		}

		public Iterable<Route> adj(int idx) {
			return routes[idx];
		}

		public int numAirports() {
			return num_airports;
		}

		public int numRoutes() {
			return num_routes;
		}
	}

	private static class Airport {
		public int id;

		public Airport(int i) {
			id = i;
		}
	}

	private static class Route {
		public Airport from;
		public Airport to;
		public int lounges;

		public Route(Airport a, Airport b, int l) {
			from = a;
			to = b;
			lounges = l;
		}
	}
}