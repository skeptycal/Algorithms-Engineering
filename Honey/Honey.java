import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Honey {

	private static enum Directions {
		N(1,-1,"N"),NW(0,-1,"NW"),NE(1,0,"NE"),S(-1,1,"S"),SW(-1,0,"SW"),SE(0,1,"SE");

		private int dx;
		private int dy;
		private String name;

		private Directions(int x, int y, String n) {
			this.dx = x;
			this.dy = y;
			this.name = n;
		}

		// debug print
		public String getName() {
			return name;
		}

		public int getX() {
			return this.dx;
		}

		public int getY() {
			return this.dy;
		}
	}

	private static int hexgrid[][][];
	private static int steps;
	private static int startX;
	private static int startY;
	private static int startZ;

	public static void main(String[] args) throws Exception {

		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(buffer.readLine());

		// Maximum N == 14, however we can go both north and south
        // 1-indexed step array, therefore 15
        // the zero index is used to store distance from 14,14
		hexgrid = new int[28][28][15];
		startX = 14;
		startY = 14;
		startZ = (startX+startY)*-1;

        // for each cell, set its distance from 14,14
		for(int x = -14; x < 14; x++) {
			for(int y = -14; y < 14; y++) {
                
                // distance from 14,14 to 14,14 is -1 as base-case
				if(startX+x == startX && startY+y == startY)
					hexgrid[startX+x][startY+y][0] = -1;
				else
					hexgrid[startX+x][startY+y][0] = pointToPoint(startX+x,startY+y);

                // for each cell, set the number of walks for all steps value to 0
				for(int i = 1; i < 15; i++) {
						hexgrid[startX+x][startY+y][i] = 0;
					}
			}
		}

		paths();

		for(int i = 0; i < N; i++) {
			steps = Integer.parseInt(buffer.readLine());
			System.out.println(Math.abs(hexgrid[startX][startY][steps]));
		}
	}

    // how many number of steps is required to get
    // from 14,14 to any given hex-cell
    // Distance is equal to the greatest of the absolute values of:
    //      the difference along the x-axis,
    //      the difference along the y-axis,
    //      or the difference of these two differences (arbitrary z axis).
	public static int pointToPoint(int x, int y) {
		int z = (x+y)*-1;
		int maxXY = Math.max(Math.abs(x-startX),Math.abs(y-startY));
		int dist = Math.max(Math.abs(maxXY),Math.abs(z-startZ));
		return dist;
	}

	public static void paths() {
		for(int n = 1; n < 15; n++) {
			hexgrid[startX][startY][n] = paths(startX,startY, n);
		}
	}

	public static int paths(int x, int y, int n) {
		if(hexgrid[x][y][n] != 0) {
			return hexgrid[x][y][n];
		}

        // for every adjacent cell
		for(Directions dir : Directions.values()) {
			int newX = x+dir.getX();
			int newY = y+dir.getY();
            // if distance between this adjacent cell and 14,14
            // is less than or equal to the number of available
            // steps we have, increment number of paths for current cell
			if(hexgrid[newX][newY][0] <= n-1){
				hexgrid[x][y][n] += paths(newX,newY,n-1);
			}
		}
		return hexgrid[x][y][n];
	}
}
