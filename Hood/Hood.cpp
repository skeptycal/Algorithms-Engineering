#include <iostream>
#include <cstdio>
#include <vector>
#include <cmath>
#include <algorithm>

struct point {
    int x,y;
    
    point() {}
    point(int xc, int yc)
    {
        x = xc;
        y = yc;
    }

    // sort by lowest x coordinate, if x are equal,
    // sort by lowest y coordinate.
    bool operator <(const point &other) const
    {
       if (x == other.x) return y < other.y;
       return x < other.x;
    }
};

// Returns the distance between two points
double distance(const point &a, const point &b) 
{
    return sqrt(pow(a.x - b.x,2) + pow(a.y - b.y,2));
}

// Returns a positive value of ccw turn, or negative for cw turn.
// If the points lie on a straight line, returns zero.
double cross(const point &o, const point &a, const point &b)
{
    return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
} 

int main()
{
    int C;
    std::cin >> C; 
    std::vector<point> shots;

    // read input
    for(int c = 0; c < C; ++c)
    {
        int x, y;
        std::cin >> x >> y;        
        shots.push_back(point(x,y));
    }

    // sort list of points
    std::sort(shots.begin(), shots.end());
    std::vector<point> hull(2*C);
    int k = 0;
    
    // if 3 points form a clockwise turn, this point is a part of the hull 
    // if they form a counter-clockwise turn, or are collinear, we try a
    // different point
    
    // find lower hull
    for(int i = 0; i < C; ++i)
    {
        // we have added 2 or more points, and not counter-clockwise, go back
        while(k >= 2 && cross(hull[k-2], hull[k-1], shots[i]) <= 0)
            k--;

        // add point to hull
        hull[k++] = shots[i];
    }

    // find upper hull
    int j = k+1;
    for(int i = C-2; i >= 0; --i)
    {
        // we have added 2 or more points, and not counter-clockwise, go back
        while(k >= j && cross(hull[k-2], hull[k-1], shots[i]) <= 0)
           k--;
        
        // add point to hull
        hull[k++] = shots[i];
    }

    // remove points that are not in hull
    hull.resize(k); 

    // highest distance
    double highest;

    // for every point in hull, compute distance to
    // other points on hull,
    for(int i = 0; i < k; ++i)
    {
        for(int j = i+1; j < k; ++j)
        {
            //compute distance
            double res = distance(hull[i], hull[j]);
            
            // do we have a longer distance?
            if(res > highest)
                highest = res;
        }
    }

    printf("%f", highest);
    return 0;
}

