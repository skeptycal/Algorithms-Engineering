#include <iostream>
#include <math.h>

using namespace std;

int main(int argc, const char * argv[]) {
    int testCases;
    cin >> testCases;
    for(int i = 0; i < testCases; i++) {
        double aX,aY,bX,bY,cX,cY;
        cin >> aX >> aY;
        cin >> bX >> bY;
        cin >> cX >> cY;
        
        // heron's formula -> |(ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) / 2|
        double areal = fabs(((aX*(bY-cY)+bX*(cY-aY)+cX*(aY-bY))/2));
        
        printf("%.3f\n",areal);
    }
    return 0;
}
