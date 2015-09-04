import java.io.*;

public class Mega {

      public static void main(String[] args) throws Exception {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            int LEN = Integer.parseInt(buffer.readLine());
            String[] nums = buffer.readLine().split(" ");

            int MAX_N = 100000;
          
            // Keep track of frequencies of each number
            long[] A = new long[MAX_N];
          
            // Keep track of the number of pairs that are less than some number
            // given an interval.
            // number of triplets for a number 'n' is the number of pairs from 0-'n-1'
            long[] B = new long[MAX_N];
            int[] input = new int[MAX_N];

            for(int i = 0; i < nums.length; i++) {
                  // read sequence of numbers
                  input[i] = Integer.parseInt(nums[i]);
            }

            long ans = 0;
          
            // start at the end of the sequence
            for(int i = LEN - 1; i >= 0; --i) {
                  int num = input[i];
                
                  // add the number of pairs below num that is a valid pair
                  ans += query(B, num-1);
                
                  // num can be paired with the amount of numbers listed in
                  // our frequency tree in the interval 0->num-1
                  increment(B, num, query(A, num - 1));
                
                  // increase the frequency of the number
                  increment(A, num, 1);
            }
            System.out.println(ans);
      }

      public static void increment(long[] tree, int at, long what) {
            for(int i = at; i < tree.length; i |= (i+1)) {
                  tree[i] += what;
            }
      }

      public static long query(long[] tree, int at) {
            long res = 0;
            for (int i = at; i >= 0; i = (i & (i + 1)) - 1) {
                  res += tree[i];
            }
            return res;
      }
}
