import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Fish {

	public static void main(String[] args) throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				System.in));

		// Number of towns
		int N = Integer.parseInt(buffer.readLine());

		// 1-indexed array of towns
		town[] towns = new town[N + 1];
		long totalFish = 0;

		// Read 'town' input data
		for (int i = 1; i <= N; i++) {
			String[] info = buffer.readLine().split(" ");
			int townDistance = Integer.parseInt(info[0]);
			int townFish = Integer.parseInt(info[1]);
			towns[i] = new town(townDistance, townFish);
			totalFish += townFish;
			if (i - 1 >= 1) {
				towns[i - 1].setNext(towns[i]);
			}
		}

        // The highest number of fish possible to have
        // at all cities is equal to the total available
        // fish divided by the number of cities.
		long upperBound = totalFish / N;
		long lowerBound = 1;
		long Y = (lowerBound + upperBound) / 2;
		long oldY = -1;
		long fishEstimate = -1;

        // As long as our scope of solutions is not empty
        // try to distribute fish
		while (Y != oldY) {
			for (int i = 1; i < towns.length; i++) {
                // Given a specific Y value, determine how
                // much fish a town has to recieve / can send
                // to fulfill this requirement
				towns[i].findRequiredFish(Y);
			}

			for (int i = 1; i < towns.length - 1; i++) {
				if (towns[i].reqFish < 0) {
					long surplus = towns[i].surplusFish();
					// if surplus > 0, add number of fish that will arrive
					if (surplus > 0) {
						towns[i].next.reqFish -= surplus;
					}
					// subtract amount of fish sent from origin-town
					towns[i].reqFish += surplus;

				} else if (towns[i].reqFish > 0) {
					// first town to check that is starved
					int checkedTown = i;
					// required fish for all starved towns
					int requiredFish = 0;
					// go through towns til we find a town that can supply
					// the required fish so far, inc when new starved town.
					// if a town that can satisfy is found, break
					while (checkedTown + 1 < towns.length) {
						requiredFish += towns[checkedTown].missingFish();
						checkedTown++;

						if (towns[checkedTown].reqFish <= requiredFish) {
							towns[checkedTown].reqFish += requiredFish;
							i = checkedTown - 1;
							break;
						}
					}
					// checkedTown exceeded all towns without finding a supplier
					if (checkedTown == towns.length) {
						towns[N].reqFish = 1;
						break;
					}
					i = checkedTown - 1;
				}
			}

			oldY = Y;

			// if reqFish < 0, the correct answer is to the right
            // Y is larger -> each city has at least Y fish, but
            // Y can possibly be larger.
			if (towns[N].reqFish < 0) {
				lowerBound = Y + 1;
				if (Y > fishEstimate) {
					fishEstimate = Y;
				}
			}
			// the correct answer is to the left. Y is smaller.
            // the last town has less fish than the distribution value.
			else if (towns[N].reqFish > 0) {
				upperBound = Y - 1;
			}
			// answer has been found. An equal distribution was possible.
			else {
				System.out.println(Y);
				System.exit(0);
			}

			// calculate our new Y
			Y = (lowerBound + upperBound) / 2;
		}

		// if no correct answer has been found, print out the closest estimate
		System.out.println(fishEstimate);
	}

	private static class town {
		public int distance;
		public long fish;
		public long reqFish;
		public int loss;
		public town next;

		public town(int distance, long fish) {
			this.distance = distance;
			this.fish = fish;
			this.next = null;
			this.loss = -1;
			this.reqFish = -1;
		}

        // Update the location of the next town
        // and calculate how much fish is lost
        // during transfer
		public void setNext(town next) {
			this.next = next;
			this.loss = next.distance - this.distance;
		}

        // The amount of fish this city has given a
        // specific Y value.
        // negative -> surplus, positive -> needs more
		public void findRequiredFish(long Y) {
			this.reqFish = Y - this.fish;
		}

        // How much fish the next town can recieve
        // when loss is included
		public long surplusFish() {
			return Math.abs(this.reqFish) - this.loss;
		}

        // The number of fish this town has to recieve
        // when loss is included
		public long missingFish() {
			return this.reqFish + this.loss;
		}
	}
}