import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tank {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// read number of cities and roads from testcase
		int num_cities, num_roads;
		String[] input1 = br.readLine().split(" ");
		num_cities = Integer.parseInt(input1[0]);
		num_roads = Integer.parseInt(input1[1]);
		
		// create array to store the price for fuel per unit at city idx
		// get price per city and store in array
		int[] fuel_cost = new int[num_cities];
		String[] fuel_info = br.readLine().split(" ");
		for(int n = 0; n < num_cities; ++n) {
			fuel_cost[n] = Integer.parseInt(fuel_info[n]);
		}
		
	 	// temp storage of connections
		int[][] connections = new int[num_roads][3];
		for(int m = 0; m < num_roads; ++m) {
			String[] con_info = br.readLine().split(" ");
			connections[m][0] = Integer.parseInt(con_info[0]);
			connections[m][1] = Integer.parseInt(con_info[1]);
			connections[m][2] = Integer.parseInt(con_info[2]);
		}
		
		// max fuel capacity, source node, destination node
		int max_fuel_cap, src, dest;
		String[] car_info = br.readLine().split(" ");
		max_fuel_cap = Integer.parseInt(car_info[0]);
		src = Integer.parseInt(car_info[1]);
		dest = Integer.parseInt(car_info[2]);
		
		// create a vector to store a city container for each city
		// and link together node 'x' fuel state 'y' with fuel state 'y+1'
		ArrayList<city_container> cities = new ArrayList<city_container>();
		for(int n = 0; n < num_cities; ++n) {
			cities.add(new city_container(n, max_fuel_cap));
			
			// add edge to increment fuel without leaving our node
			for(int offset = 0; offset < max_fuel_cap; ++offset) {
				road r = new road(cities.get(n).fuel_state.get(offset), cities.get(n).fuel_state.get(offset+1), fuel_cost[n]);
				cities.get(n).fuel_state.get(offset).addNeighbour(r);
			}
		}
		
		// we now know our max fuel capacity
		// go through the roads we stored earlier
		// and create the proper connections
		// in our city structure
		for(int c = 0; c < num_roads; ++c) {
			
			// create a more informative name
			int from = connections[c][0];
			int to = connections[c][1];
			int cost = connections[c][2];
						
			// cost of fuel exceeds maximum fuel cap, invalid road
			if(cost > max_fuel_cap) continue;
			
			for(int offset = cost; offset <= max_fuel_cap; ++offset) {
				// create a new road between these two cities
				// and add it to our adjacency lists
				// we already have enough fuel to get there, so cost is 0
				road r = new road(cities.get(from).fuel_state.get(offset), cities.get(to).fuel_state.get(offset-cost), 0);
				cities.get(from).fuel_state.get(offset).addNeighbour(r);
				
				road rr = new road(cities.get(to).fuel_state.get(offset), cities.get(from).fuel_state.get(offset-cost), 0);
				cities.get(to).fuel_state.get(offset).addNeighbour(rr);
			}
		} 
		
		// Run dijkstra on the graph 'cities',
		// starting from node src in fuel state 0
		// with a max fuel cap of max_fuel_cap
		dijkstra dj = new dijkstra(cities, src, 0, max_fuel_cap);
		int result = cities.get(dest).fuel_state.get(0).price_at;
		if(result == Integer.MAX_VALUE)
			System.out.println("impossible");
		else
			System.out.println(result);
		
	}
	
	private static class dijkstra {
		Index_Min_PQ<city> pq;
		ArrayList<city_container> cities;
		
		public dijkstra(ArrayList<city_container> cc, int src, int state, int max_fuel_cap) {
			this.cities = cc;
			
			cities.get(src).fuel_state.get(state).price_at = 0;
			
			pq = new Index_Min_PQ<city>(cities.size() * (max_fuel_cap+1));
			
			pq.enqueue(cities.get(src).fuel_state.get(state).id, cities.get(src).fuel_state.get(state));

            // maximum of V queued cities
            // dequeue takes log V time (binary heap implementation)
            while(!pq.isEmpty())
				relax(pq.dequeue());
		}
		
		private void relax(city c) {
			for(int i = 0; i < c.neighbours.size(); ++i) {
				if(c.price_at + c.neighbours.get(i).cost < c.neighbours.get(i).to.price_at) {
					c.neighbours.get(i).to.price_at = c.price_at + c.neighbours.get(i).cost;
					
                    // change or queue takes log V time, and happens at most 1 time per edge
					if(pq.containsKey(c.neighbours.get(i).to.id)) {
						pq.change(c.neighbours.get(i).to.id, c.neighbours.get(i).to);
					} else {
						pq.enqueue(c.neighbours.get(i).to.id, c.neighbours.get(i).to);							
					}
				}
			}
		}
	}
	
	// this struct represents an edge or road between two cities
	private static class road {
		city from;
		city to;
		int cost;
		
		public road(city a, city b, int c) {
			from = a;
			to = b;
			cost = c;
		}
	}
	
	// this struct is used to represent a city with a given fuel state, for a node
	private static class city implements Comparable<city> {
		int id;
		int price_at;
		
		ArrayList<road> neighbours = new ArrayList<road>();
		
		public city(int id) {
			this.id = id;
			this.price_at = Integer.MAX_VALUE;
		}
		
		public void addNeighbour(road r) {
			neighbours.add(r);
		}
		
		public int compareTo(city other) {
			return (this.price_at - other.price_at);
		}		
	}
	
	// this struct represents all the different fuel states for a given node
	private static class city_container {
		ArrayList<city> fuel_state = new ArrayList<city>();
		
		public city_container(int id, int max_cap) {
			for(int i = 0; i < max_cap + 1; ++i) {
				int capacity = max_cap + 1;
				int offset = i;
				int city_id = offset + (id * capacity);
				
				fuel_state.add(new city(city_id));
			}
		}
	}
	
	// Indexed PQ for use with dijkstra shortest path algorithm
	private static class Index_Min_PQ<E extends Comparable<E> > {
		private int[] pq;
		private int[] qp;
		private E[] keys;
		private int N = 0;
		
		public Index_Min_PQ(int max)
		{
			pq = new int[max + 1];
			qp = new int[max + 1];
			keys = (E[]) new Comparable[max + 1];
		}
		
		public void enqueue(int i, E e)
		{
			pq[++N] = i;
			qp[i] = N;
			keys[i] = e;
			swim(N);
		}
		
		public E dequeue()
		{
			E ret = keys[pq[1]];
	 		pq[1] = pq[N];
	 		qp[pq[1]] = 1;
			N--;
			sink(1);
			return ret;
		}
		
		public boolean containsKey(int i) {
			return keys[i] != null;
		}
		
		public void change(int i, E e)
		{
			keys[i] = e;
			swim(qp[i]);
			sink(qp[i]);
		}

		public int size()
		{
			return N;
		}
		
		public boolean isEmpty()
		{
			return size() == 0;
		}
		
		public void swim(int idx)
		{
			for(;idx/2 >= 1 && less(idx, idx/2); idx/=2)
			{
					exch(idx,idx/2);
			}
		}
		
		public void sink(int idx)
		{
			while(idx*2 <= N)
			{
				int j = idx*2;
				if(j < N && less(j+1, j)) j++;
				if(!less(j, idx)) break;
				exch(j,idx);
				idx = j;
			}
		}
		
		private boolean less(int a, int b)
		{
			return keys[pq[a]].compareTo(keys[pq[b]]) < 0;
		}
		
		private void exch(int a, int b)
		{
			int temp;
			temp = pq[a];
			pq[a] = pq[b];
			pq[b] = temp;
			qp[pq[a]] = a;
			qp[pq[b]] = b;
		}
	}
}
