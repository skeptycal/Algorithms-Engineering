import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Return {
	
	static long mem[][][];
	static int costs[];
	static platform platforms[];
	
	
	// platform object
	private static class platform {
		int left, right;
		long energy;

		platform(int l, int  r, long e)
		{
			left = l;
			right = r;
			energy = e;
		}
	}
	
	// counting tree -> add
    public static void increment(int at, int what) {
        for(int i = at; i < costs.length; i |= (i+1)) {
              costs[i] += what;
        }
    }

    // counting tree -> sum
    public static int query(int at) {
        int res = 0;
        for (int i = at; i >= 0; i = (i & (i + 1)) - 1) {
              res += costs[i];
        }
        return res;
    }
    
    // cost to move from platform 'from' to platform 'to'
    public static int platform_cost(int from, int to) {
    	if(from == 0 || to == 0) return Integer.MAX_VALUE;
    	return Math.abs(query(to) - query(from));
    }
	   
    public static long checkRight(int l, int c, int r, long e, int dir)
    {
    	long result = -1;
    	if(r < platforms.length) {
    		long new_energy = e - platform_cost(c, r);
    		if(new_energy >= 0)
    			result = explorePlatform(l, r, r+1, new_energy, dir, true);
    	}
    	return result;
    }
    
    public static long checkLeft(int l, int c, int r, long e, int dir)
    {
    	long result = -1;
    	if(l > 0) {
    		long new_energy = e - platform_cost(c, l);
    		if(new_energy >= 0) 
    			result = explorePlatform(l-1, l, r, new_energy, dir, false);
    	}
    	return result;
    }
    
    public static long explorePlatform(int l, int c, int r, long e, int dir, boolean right)
    {
    	if(c < 1 || l < 0 || r > platforms.length)
    		return -1;
    	
    	if(mem[l][r][dir] != -1) {
    		return mem[l][r][dir];
    	}
    	
    	long max_energy = e + platforms[c].energy;
    	long l_energy = -1;
    	long r_energy = -1;
    	
    	if(right) 
    	{
    		r_energy = checkRight(l, c, r, max_energy, dir);
        	l_energy = checkLeft(l, c, r, max_energy, dir);
    	} else {
    		l_energy = checkLeft(l, c, r, max_energy, dir);
    		r_energy = checkRight(l, c, r, max_energy, dir);
    	}

    	max_energy = Math.max(Math.max(max_energy, l_energy), r_energy);
    	mem[l][r][dir] = max_energy;
    	return max_energy;
    }
    
	public static void main(String[] args) throws Exception 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int num_platforms, start_id;
		int start_platform = 1;
		long start_energy;

		num_platforms = Integer.parseInt(br.readLine());
		platforms = new platform[num_platforms+1];
		costs = new int[num_platforms+1];
		mem = new long[num_platforms+1][num_platforms+2][2];
		
		String info[] = br.readLine().split(" ");
		platforms[0] = new platform(-1, -1, 0);
		platforms[1] = new platform(Integer.parseInt(info[0]),Integer.parseInt(info[1]),Integer.parseInt(info[2]));
		
		for(int i = 2; i <= num_platforms; ++i)
		{
			info = br.readLine().split(" ");
			int a, b, e;
			a = Integer.parseInt(info[0]);
			b = Integer.parseInt(info[1]);
			e = Integer.parseInt(info[2]);
			platforms[i] = new platform(a, b, e);
			increment(i, platforms[i].left - platforms[i-1].right);
		}
		
		info = br.readLine().split(" ");
		start_id = Integer.parseInt(info[0]);
		start_energy = Long.parseLong(info[1]);

		for(int i = 0; i <= num_platforms; ++i)
		{
			for(int j = 0; j <= num_platforms+1; ++j)
			{
				mem[i][j][0] = -1;
				mem[i][j][1] = -1;
			}
			if(platforms[i].left <= start_id && platforms[i].right >= start_id)
				start_platform = i;
		}
		
		int	l = start_platform-1;
		int	r = start_platform+1;
		
		mem[l][r][0] = explorePlatform(l, start_platform, r, start_energy, 0, true);
		mem[l][r][1] = explorePlatform(l, start_platform, r, start_energy, 1, false);
		System.out.println(Math.max(mem[l][r][0], mem[l][r][1]));
	}
	
	
}
