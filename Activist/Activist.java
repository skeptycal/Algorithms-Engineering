import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.ArrayList;

public class Activist {
	public static void main(String[] args) throws Exception {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

		String info[] = buffer.readLine().split(" ");
            int N = Integer.parseInt(info[0]);
            double r = Double.parseDouble(info[1]);

            while(N != 0 && r != 0.0) {
                  Point points[] = new Point[N];

			// Les inn posisjoner
                  for(int i = 0; i < N; i++) {
                              String pointInfo[] = buffer.readLine().split(" ");
                              double x = Double.parseDouble(pointInfo[0]);
                              double y = Double.parseDouble(pointInfo[1]);
                              points[i] = new Point(x,y);
                  }

			// Oppretter sirkler som har 2 og 3 punkter i perimeteren.
			ArrayList<Line2P> lines2p = new ArrayList<Line2P>();
			ArrayList<Line3P> lines3p = new ArrayList<Line3P>();
			for(int i = 0; i < points.length; i++) {
				for(int j = i+1; j < points.length; j++) {
					if(points[i].distanceToPoint(points[j]) <= 2*r) {
						lines2p.add(new Line2P(points[i],points[j]));
					}
					for(int k = j+1; k < points.length; k++) {
						lines3p.add(new Line3P(points[i],points[j],points[k]));
					}
				}
			}

			//
			int total = 0;
			for(int i = 0; i < lines2p.size(); i++) {
				int temp = 0;
				Point center = lines2p.get(i).getCenter();

				for(int j = 0; j < points.length; j++) {
					if(points[j].distanceToPoint(center) <= r) {
						temp++;
					}
				}

				//System.out.println("center:" + center.x + "," + center.y + " points:" + temp);
				if(temp > total) {
					total = temp;
				}
			}

			for(int i = 0; i < lines3p.size();i++) {
				int temp = 0;
				double a = lines3p.get(i).a();
				double d = lines3p.get(i).d();
				double e = lines3p.get(i).e();

				double centerX = d/(2*a);
				double centerY = -e/(2*a);

				Point center = new Point(centerX,centerY);

				for(int j = 0; j < points.length; j++) {
					if(points[j].distanceToPoint(center) <= r) {
						temp++;
					}
				}

				//System.out.println("center:" + center.x + "," + center.y + " points:" + temp);
				if(temp > total) {
					total = temp;
				}
			}

			System.out.println(total);

                  // read new test case
                  info = buffer.readLine().split(" ");
                  N = Integer.parseInt(info[0]);
                  r = Double.parseDouble(info[1]);
            }
	}

	private static class Line2P {
		private Point a;
		private Point b;

		public Line2P(Point a, Point b) {
			this.a = a;
			this.b = b;
		}

		public Point getCenter() {
			return new Point((this.a.x + this.b.x)/2, (this.a.y + this.b.y)/2);
		}
	}

	private static class Line3P {
		private Point a;
		private Point b;
		private Point c;

		public Line3P(Point a, Point b, Point c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}

		public double a() {
			return this.a.x*((1*this.b.y)-(this.c.y*1)) - this.a.y*((1*this.b.x) - (this.c.x * 1)) + 1*((this.c.y*this.b.x) - (this.c.x*this.b.y));
		}

		public double d() {
			double aX = Math.pow(this.a.x,2) + Math.pow(this.a.y,2);
			double bX = Math.pow(this.b.x,2) + Math.pow(this.b.y,2);
			double cX = Math.pow(this.c.x,2) + Math.pow(this.c.y,2);
			return aX*((1*this.b.y)-(this.c.y*1)) - this.a.y*((1*bX) - (cX * 1)) + 1*((this.c.y*bX) - (cX*this.b.y));
		}

		public double e() {
			double aX = Math.pow(this.a.x,2) + Math.pow(this.a.y,2);
			double bX = Math.pow(this.b.x,2) + Math.pow(this.b.y,2);
			double cX = Math.pow(this.c.x,2) + Math.pow(this.c.y,2);
			return aX*((1*this.b.x)-(this.c.x*1)) - this.a.x*((1*bX) - (cX * 1)) + 1*((this.c.x*bX) - (cX*this.b.x));
		}
	}


	private static class Point {
		private double x;
		private double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double distanceToPoint(Point other) {
			double d = Math.sqrt(Math.pow(other.x-this.x,2) + Math.pow(other.y-this.y,2));
			return d;
		}

		@Override
		public String toString() {
			return "" + this.x + "," + this.y;
		}
	}
}
