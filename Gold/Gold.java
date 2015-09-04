import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Gold {
	public static void main(String[] args) throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				System.in));

		String temp[] = buffer.readLine().split(" ");
		int W = Integer.parseInt(temp[0]);
		int H = Integer.parseInt(temp[1]);
		char grid[][] = new char[W][H];

		int playerPos[] = new int[2];
		for (int j = 0; j < H; j++) {
			String info = buffer.readLine();
			for (int k = 0; k < info.length(); k++) {
				grid[k][j] = info.charAt(k);
				if (info.charAt(k) == 'P') {
					playerPos[0] = k;
					playerPos[1] = j;
				}
			}
		}

		buffer.close();

		Graph G = new Graph(W, H, grid);
		DFS dfs = new DFS(G, playerPos[0], playerPos[1]);
		System.out.println(dfs.collectedGold());

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
		private int gold;

		public DFS(Graph G, int startW, int startH) {
			count = 0;
			gold = 0;
			marked = new boolean[G.Width()][G.Height()];

			dfs(G, startW, startH);
		}

		private void dfs(Graph G, int w, int h) {
			boolean isGold = (G.grid[w][h] == 'G');
			if (G.grid[w][h] == '.' || isGold || G.grid[w][h] == 'P') {
				if (isGold) {
					gold++;
				}

				count++;
				marked[w][h] = true;

				if (G.grid[w][h - 1] != 'T' && G.grid[w][h + 1] != 'T'
						&& G.grid[w - 1][h] != 'T' && G.grid[w + 1][h] != 'T') {
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
		}

		public boolean marked(int w, int h) {
			return marked[w][h];
		}

		public int count() {
			return count;
		}

		public int collectedGold() {
			return gold;
		}
	}
}