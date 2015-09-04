import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Towers {
      static int[] floors;

      public static void main(String[] args) throws Exception {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

            int N = Integer.parseInt(buffer.readLine());
          
            //as long as N is not equal to zero, there is a test set
            while(N != 0) {
                  floors = new int[N+1];

                  for(int i = 1; i < N + 1; i++) {
                        int checkedIn = Integer.parseInt(buffer.readLine());
                        increment(i,checkedIn);
                  }

                  String cmd = buffer.readLine();
                
                  // while command is not S there is still more cases
                  // for the current test
                  while(!cmd.toUpperCase().equals("S")) {
                        String[] info = cmd.split(" ");
                        String todo = info[0];
                        int a = Integer.parseInt(info[1]);
                        int b = Integer.parseInt(info[2]);
                        if(todo.equals("I")) {
                              // guests checking in
                              increment(a,b);
                        } else if(todo.equals("U")) {
                              // guests checking out
                              increment(a,-b);
                        } else if(todo.equals("T")) {
                              // check how many guests are checked in between floor a and b
                              int floor_lower = query(a-1);
                              int floor_upper = query(b);
                              int num = 0;
                              if(a == 1) {
                                    num = floor_upper;
                              } else {
                                    num = floor_upper - floor_lower;
                              }
                              StringBuilder output = new StringBuilder();
                              output.append("Det er " + num + " ");
                              if(num == 1) {
                                    output.append("gjest ");
                              } else {
                                    output.append("gjester ");
                              }
                              output.append("som bor ");
                              if(a == b) {
                                    output.append("i etasje " + a + ".");
                              } else if(a == 1 && b == N) {
                                    output.append("i hotellet.");
                              } else {
                                    output.append("mellom etasje " + a + " og etasje " + b + ".");
                              }
                              System.out.println(output.toString());
                        }
                        cmd = buffer.readLine();
                  }
                  // test finished
                  System.out.println("Slutt for i dag.");
                  N = Integer.parseInt(buffer.readLine());
            }
      }

      public static void increment(int at, int what) {
            for(int i = at; i < floors.length; i |= (i+1)) {
                  floors[i] += what;
            }
      }

      public static int query(int at) {
            int res = 0;
            for (int i = at; i >= 0; i = (i & (i + 1)) - 1) {
                  res += floors[i];
            }
            return res;
      }
}
