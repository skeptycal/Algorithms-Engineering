
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Mayan {

	private static int[] glyphs;
	private static int[] carvings;

	private static String lineGlyphs;
	private static String lineCarvings;

	public static void main(String[] args) throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				System.in));

		String[] info = buffer.readLine().split(" ");
		int g = Integer.parseInt(info[0]);
		int S = Integer.parseInt(info[1]);

		glyphs = new int['z' + 1];
		carvings = new int['z' + 1];

        // original glyphs
		lineGlyphs = buffer.readLine();

        // count occurrences of characters for
        // our given window size
		for (int i = 0; i < g; i++) {
			char c = lineGlyphs.charAt(i);
			glyphs[c]++;
		}

        // sequence to inspect
		lineCarvings = buffer.readLine();
        
        // create our first sub-sequence and
        // count occurences of characters
		for (int i = 0; i < g; i++) {
			char c = lineCarvings.charAt(i);
			carvings[c]++;
		}

		int totalAppearances = 0;
        
        // check our first sub-sequence
		if (checkGlyph()) {
			totalAppearances++;
		}

        // Create and check all sub-sequences
		for (int i = 1; i < lineCarvings.length() - g + 1; i++) {

            // create the next sub-sequence by removing the
            // first element of our window, and append the
            // element right of the last element in our window.
            // abcdefgh -> window: abcd -> new window: bcde
            
			char prevChar = lineCarvings.charAt(i - 1);
			carvings[prevChar]--;
            
			char nextChar = lineCarvings.charAt(i + g - 1);
            carvings[nextChar]++;
			
            // check our new sub-sequence
            if (checkGlyph()) {
				totalAppearances++;
			}

		}
		System.out.println(totalAppearances);
	}

    
    // check if the amount of occurences in our original glyphs
    // and the sub-sequence to inspect is equal.
    // aka is it a permutation?
	public static boolean checkGlyph() {
		for (int i = 0; i < lineGlyphs.length(); i++) {

			if (glyphs[lineGlyphs.charAt(i)] != carvings[lineGlyphs.charAt(i)]) {
				return false;
			}
		}
		return true;
	}
}