import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Sleeping {

	static int mem[][][];
	static int energy[];
	static int N, M, R;
	
	public static int solve()
	{
		for(int n = 0; n < N; ++n) 
		{
			for(int m = 0; m < M + 1; ++m) 
			{
				for(int r = 0; r < R + 1; ++r) 
				{
					// reachable
					if(mem[n][m][r] != -1) 
					{
						if(r < R && m < M) 
						{
							// ny energi for den nye 'staten'
							int new_energy = mem[n][m][r] + (r+1) * energy[n];
							
							// velger å sove i dette minuttet.
							// sjekker om energien vi nå får er
							// bedre enn den energien som muligens
							// allerede har blitt satt for denne 'staten'
							if(mem[n+1][m+1][r+1] < new_energy) 
								mem[n+1][m+1][r+1] = new_energy;
						}
						
						// sover ikke, sjekker om energi i den 'staten'
						// vi er i nå er bedre enn verdien som finnes fra 
						// før i den 'staten' der man velger å ikke sove 
						// i dette minuttet.
						if(mem[n][m][r] >= mem[n+1][m][0])
							mem[n+1][m][0] = mem[n][m][r];
					}
				}
			}
		}
		
		// sjekker alle verdier for 'r' og henter ut beste verdi
		int best = -1;
		for(int s = 0; s < R + 1; ++s) {
			if(best < mem[N][M][s])
				best = mem[N][M][s];
		}
		return best;	
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int test_cases;
		test_cases = Integer.parseInt(br.readLine());

		for(int i = 0; i < test_cases; ++i)
		{
			String info[] = br.readLine().split(" ");
			N = Integer.parseInt(info[0]);
			M = Integer.parseInt(info[1]);
			R = Integer.parseInt(info[2]);

			mem = new int[N+1][M+1][R+1];
			energy = new int[N];
			
			info = br.readLine().split(" ");
			for(int j = 0; j < N; ++j)
				energy[j] = Integer.parseInt(info[j]);

			for(int n = 0; n < N + 1; ++n) 
			{
				for(int m = 0; m < M + 1; ++m) 
				{
					for(int r = 0; r < R + 1; ++r) 
					{
						mem[n][m][r] = -1;
					}
				}
			}

            // total minutes that of sleep we can get
            // there are N/R+1 groups, +1 because we
            // have to be awake the following minute.
            // for each group we sleep R minutes
            // there are then N%(r+1) remaining minutes to sleep
			int sleep_in_groups = N / (R + 1) * R;
			int rest_sleep = N % (R + 1);
			
            if(sleep_in_groups + rest_sleep < M) {
				System.out.println("impossible");
			} else {
				mem[0][0][0] = 0;
				int solution = solve();
				System.out.println(solution);
			}
		}
	}
}
