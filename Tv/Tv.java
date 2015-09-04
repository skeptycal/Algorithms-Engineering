import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Tv {
	private static HashMap<Integer,ArrayList<tvShow>> shows;
	private static int maxPoints[];
	private static int lastEndMinute;

	public static void main(String[] args) throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

		// Number of test-cases
		int N = Integer.parseInt(buffer.readLine());

		for(int i = 0; i < N; i++) {
			// read numer of shows
			int numTvShows = Integer.parseInt(buffer.readLine());
			shows = new HashMap<Integer,ArrayList<tvShow>>();

			lastEndMinute = 0;
			for(int j = 0; j < numTvShows; j++) {
				String showInfo[] = buffer.readLine().split(" ");
				int s = Integer.parseInt(showInfo[0]);
				int d = Integer.parseInt(showInfo[1]);
				int p = Integer.parseInt(showInfo[2]);
				tvShow newShow = new tvShow(s,d,p);

                // if hashmap contains list for shows at this minute
                // add show to list
				if(shows.containsKey(newShow.k)) {
					shows.get(newShow.k).add(newShow);
				} else {
                    // create new list in hashmap and add show to list
					shows.put(newShow.k, new ArrayList<tvShow>());
					shows.get(newShow.k).add(newShow);
				}

				// the highest end value for any show
				if(newShow.k > lastEndMinute)
					lastEndMinute = newShow.k;
			}

			// 1-indexed array containing max points from 0 to <index> minutes
			maxPoints = new int[lastEndMinute+1];
			// initialize max points from 0 to 0 -> 0
			maxPoints[0] = 0;
			// initialize max points from 1 to lastStartMinute+1 -> -1 (not yet calculated)
			for(int j = 1; j < lastEndMinute + 1; j++) {
				maxPoints[j] = -1;
			}

			points();

			System.out.println(maxPoints[lastEndMinute]);
		}
	}


	private static void points() {
		maxPoints[lastEndMinute] = points(lastEndMinute);
	}

	private static int points(int idx) {
		if(maxPoints[idx] != -1) {
			return maxPoints[idx];
		}

        // if max points up to minute idx - 1 has not yet
        // been solved, solve it first.
		if(maxPoints[idx-1] == -1) {
			maxPoints[idx-1] = points(idx-1);
		}

		ArrayList<tvShow> list = shows.get(idx);
		if(list == null) {
            // there are no shows ending at this minute
			return maxPoints[idx-1];
		}

        // check all shows that end in minute idx
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		for(int i = 0; i < list.size(); i++) {
			int start = list.get(i).start;
            // if max points up to the start minute of the current show
            // has not been solved, solve it.
			if(maxPoints[start] == -1) {
				maxPoints[start] = points(start);
			}
            // store candidate for max points for minute idx
			candidates.add(maxPoints[start] + list.get(i).points);
		}

        // check all candidates and find the best one
		int maxCandidate = 0;
		for(int i = 0; i < candidates.size(); i++) {
			if(candidates.get(i) > maxCandidate) {
				maxCandidate = candidates.get(i);
			}
		}

        // max points for minute idx is the higher value between
        // max points for minute idx-1 OR to watch a show that ends
        // in minute idx + the points recieved before watching that show.
		return Math.max(maxPoints[idx-1],maxCandidate);
	}

	private static class tvShow {
		private int start;
		private int duration;
		private int k;
		private int points;

		public tvShow(int s, int d, int p) {
			this.start = s;
			this.duration = d;
			this.points = p;
			this.k = s+d;
		}
	}
}
