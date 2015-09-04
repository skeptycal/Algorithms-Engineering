import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Outlet {
	public static void main(String[] args) throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				System.in));
		int N = Integer.parseInt(buffer.readLine());
		for (int i = 0; i < N; i++) {
			int numPowerStrips = Integer.parseInt(buffer.readLine());
			PowerStrip[] powerStrips = new PowerStrip[numPowerStrips];
			String[] outlets = buffer.readLine().split(" ");
			for (int j = 0; j < outlets.length; j++) {
				powerStrips[j] = new PowerStrip(Integer.parseInt(outlets[j]));
			}

			int numConnections = 0;
			for (int k = 0; k < powerStrips.length - 1; k++) {
				numConnections += --powerStrips[k].outlets;
			}
			numConnections += powerStrips[powerStrips.length - 1].outlets;
			System.out.println(numConnections);
		}
		buffer.close();
	}

	private static class PowerStrip {
		public int outlets;

		public PowerStrip(int numOutlets) {
			outlets = numOutlets;
		}

	}
}
