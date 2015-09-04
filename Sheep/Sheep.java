import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Sheep {
	public static void main(String[] args) throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				System.in));
		int N = Integer.parseInt(buffer.readLine());
		for (int i = 0; i < N; i++) {
			String temp[] = buffer.readLine().split(" ");
			int H = Integer.parseInt(temp[0]);
			int W = Integer.parseInt(temp[1]);
			char grid[][] = new char[W][H];

			for (int j = 0; j < H; j++) {
				String info = buffer.readLine();
				for (int k = 0; k < info.length(); k++) {
					grid[k][j] = info.charAt(k);
				}
			}

			Graph G = new Graph(W, H, grid);
			DFS dfs = new DFS(G);
			System.out.println(dfs.connectedComponents());
		
		}
		buffer.close();

	}

	private static class Graph {
		private final int Height;
		private int Width;
		private char grid[][];

		public Graph(int W, int H, char g[][]) {
			Width = W;
			Height = H;
			grid = g;
		}

		public int Width() {
			return Width;
		}

		public int Height() {
			return Height;
		}
	}

	private static class DFS {
		private boolean marked[][];
		private int count;
		private int cc;

		public DFS(Graph G) {
			count = 0;
			cc = 0;
			marked = new boolean[G.Width()][G.Height()];

			for (int i = 0; i < G.Height(); i++) {
				for (int j = 0; j < G.Width(); j++) {
					if (!marked[j][i] && G.grid[j][i] != '.') {
						dfs(G, j, i);
						cc++;
					}
				}
			}
		}

		private void dfs(Graph G, int w, int h) {
			if (G.grid[w][h] == '#') {
				count++;
				marked[w][h] = true;

				if (h - 1 >= 0) {
					if (!marked[w][h - 1]) {
						dfs(G, w, h - 1); // up
					}
				}
				if (h + 1 < G.Height()) {
					if (!marked[w][h + 1]) {
						dfs(G, w, h + 1); // down
					}
				}
				if (w - 1 >= 0) {
					if (!marked[w - 1][h]) {
						dfs(G, w - 1, h); // left
					}
				}
				if (w + 1 < G.Width()) {
					if (!marked[w + 1][h]) {
						dfs(G, w + 1, h); // right;
					}
				}
			}
		}

		public boolean marked(int w, int h) {
			return marked[w][h];
		}

		public int count() {
			return count;
		}

		public int connectedComponents() {
			return cc;
		}
	}
}
