#include <iostream>
#include <cmath>
#include <queue>
#include <vector>
#include <cstring>
#include <climits>

struct vec2;
struct floe;

// struct to store position of floe
struct vec2
{
    int x, y;
    vec2() {}
    vec2(int xc, int yc)
    {
        x = xc;
        y = yc;
    }
};

// a floe has a position, number of penguins
// here, and how many penguins can leave
struct floe
{
    vec2 pos;
    int at, cap;
    floe() : pos(-1, -1), at(0), cap(0) {}
    floe(int x, int y, int a, int c)
    {
        at = a;
        cap = c;
        pos.x = x;
        pos.y = y;
    }
};

// find distance between two floes
double distance(const vec2 &a, const vec2 &b)
{
    return sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2));
}

// returns true if there is a path from s to v
bool bfs(const std::vector<std::vector<int> > &graph, int s, int t, int parent[])
{
    int N = graph.size();
    bool marked[N];
    std::memset(marked, 0, sizeof(marked));
    std::queue<int> q;
    q.push(s);
    marked[s] = true;
    parent[s] = -1;

    while(!q.empty())
    {
        int u = q.front();
        q.pop();

        for(int v = 0; v < N; ++v)
        {
            if(!marked[v] && graph[u][v] > 0)
            {
                q.push(v);
                parent[v] = u;
                marked[v] = true;
            }
        }
    }

    return marked[t];
}

// Try to exhaust connection for all available paths between s and t
int FF(std::vector<std::vector<int> > &graph, int s, int t)
{
    if(s == t) return 1;
    int N = graph.size();
    int max_flow = 0;
    int parent[N];

    while(bfs(graph, s, t, parent))
    {
        int cur_path_flow = INT_MAX;
        for(int n = t; n != s; n = parent[n])
        {
            int cur_node = parent[n];
            cur_path_flow = std::min(cur_path_flow, graph[cur_node][n]);
        }
        
        for(int n = t; n != s; n = parent[n])
        {
            int cur_node = parent[n];
            graph[cur_node][n] -= cur_path_flow;
            graph[n][cur_node] += cur_path_flow;
        }
        max_flow += cur_path_flow;
    }
    return max_flow;
}

int main()
{
    int T;
    std::cin >> T;

    // for each test case
    for(int t = 0; t < T; ++t)
    {
        int N, S;
        double D;
        std::cin >> N >> D;
        std::vector<floe> floes;

        // Source connecting to all floes
        S = 2*N;

        // total penguins in graph
        int total_penguins = 0;
        std::vector<int> result;

        // there are 'N' floes in this graph
        for(int n = 0; n < N; ++n)
        {
            int x, y, at, cap;
            std::cin >> x >> y >> at >> cap;
            total_penguins += at;
            floes.push_back(floe(x, y, at, cap));            
        }

        //std::cout << "total penguins:" << total_penguins << std::endl;

        // every floe is possibly a sink
        for(int sink = 0; sink < N; ++sink)
        {
            // each node has an n_in and n_out
            // n_in -> edges going into n
            // n_out -> edges coming out from n
            std::vector<std::vector<int> > graph(S+1, std::vector<int>(S+1, 0));
            

            for(int i = 0; i < N; ++i)
            {
                floe &cur_floe = floes[i];
                
                // edges from source to in vertex
                graph[S][i] = cur_floe.at;

                // edges from in vertex to out vertex
                graph[i][i+N] = cur_floe.cap;

                // for each floe, check if the distance between
                // every pair of floe is less than or equal to
                // the max distance a penguin can jump 'D' 
                for(int j = 0; j < N; ++j)
                {
                    if(i == j) continue;
                    floe &tar_floe = floes[j];
                    double dist = distance(cur_floe.pos, tar_floe.pos);
                    
                    // edges from out vertex to in vertex
                    if (dist <= D) 
                        graph[i+N][j] = cur_floe.cap;
                }
            }

            // if the total number of penguins is equal to
            // the flow we computed for this sink, then 
            // this floe is part of the solution
            int res = FF(graph, S, sink);
            if(res == total_penguins)
                result.push_back(sink);
        }


        // if no results, print -1, else
        // print the 0-based index of every
        // solution
        int num_results = result.size();
        if(num_results == 0) {
            std::cout << -1 << std::endl;
        } else {
            for(int i = 0; i < num_results; ++i)
            {
                std::cout << result[i];
                if(i < num_results - 1)
                    std::cout << " ";
            }
            std::cout << std::endl;    
        }
    }

    return 0;
}
