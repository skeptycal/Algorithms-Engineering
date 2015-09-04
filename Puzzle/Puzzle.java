import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Puzzle
{	
	final static String alternatives[] = { "DR", "LDR", "LD", "DRU", "LDRU", "LDU", "RU", "LRU", "LU" };
	final static String GOAL_STATE = "123456789";
	
	private static class state
	{
		String tiles;
		int blank, depth;
		
		public state(String t, int b, int d)
		{
			this.tiles = t;
			this.blank = b;
			this.depth = d;
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int N = Integer.parseInt(br.readLine());
		
		state goal_state = new state(GOAL_STATE, 8,0);
		HashMap<String, Integer> states_map = new HashMap<String, Integer>();
		exploreTiles(goal_state, states_map);
		
		for(int i = 0; i < N; ++i)
		{
			br.readLine();

			StringBuilder sb = new StringBuilder();
			for(int x = 0; x < 3; ++x) {
				String line = br.readLine();
				for(int y = 0; y < 3; ++y) {
					if(line.charAt(y) == '#') {
						sb.append(9);
					} else {
						sb.append(line.charAt(y));
					}
				}
			}
			
			Integer result = states_map.get(sb.toString());
			if(result != null) {
				System.out.println(result);
			} else {
				System.out.println("impossible");
			}
		}
	}
	
	public static void exploreTiles(state parent, HashMap<String, Integer> states_map)
	{
		states_map.put(parent.tiles, parent.depth);
		
		if(parent.depth >= 39)
			return;
		
		String alt = alternatives[parent.blank];
		for(int i = 0; i < alt.length(); ++i)
		{
			int blank = parent.blank;
			StringBuilder sb = new StringBuilder(parent.tiles);
			
			if(alt.charAt(i) == 'L') {
				sb.setCharAt(blank,parent.tiles.charAt(blank-1));
				sb.setCharAt(blank-1,parent.tiles.charAt(blank));
				blank -= 1;
			} 
			
			else if(alt.charAt(i) == 'D') {
				sb.setCharAt(blank,parent.tiles.charAt(blank+3));
				sb.setCharAt(blank+3,parent.tiles.charAt(blank));
				blank += 3;
			} 
			
			else if(alt.charAt(i) == 'R') {
				sb.setCharAt(blank,parent.tiles.charAt(blank+1));
				sb.setCharAt(blank+1,parent.tiles.charAt(blank));
				blank += 1;
			} 
			
			else {
				sb.setCharAt(blank,parent.tiles.charAt(blank-3));
				sb.setCharAt(blank-3,parent.tiles.charAt(blank));
				blank -= 3;
			} 
			
			String tiles = sb.toString();
			state s = new state(tiles, blank, parent.depth + 1);
			
			if(!states_map.containsKey(s.tiles)) {
				exploreTiles(s, states_map);
			} else if(states_map.get(s.tiles) > parent.depth + 1) {
				exploreTiles(s, states_map);
			}
		}
	}
}