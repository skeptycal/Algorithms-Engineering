#include <iostream>
#include <climits>
#include <vector>
#include <queue>
#include <cstring>

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

// try to exhaust connection for all available paths between s and t
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
    int N;
    std::cin >> N;

    // for each test case
    for(int n = 0; n < N; ++n)
    {
        int M, F;
        std::cin >> M >> F;
        std::vector<std::vector<int> >  consultants(F, std::vector<int>());


        for(int f = 0; f < F; ++f)
        {
            int num;
            std::cin >> num;
            for(int i = 0; i < num; ++i) {
                int c;
                std::cin >> c;
                consultants[f].push_back(c);
            }   

        }

        // is it possible to visit all companies in 1 to M days
        // the first solution will be the least number of days
        // if we dont find a solution in this interval, impossible
        bool possible = false;
        for(int m = 1; m < M+1; ++m)
        {
            std::vector<std::vector<int> > graph(M+F+2, std::vector<int>(M+F+2, 0));
            
            // super-source
            int S = M+F;
            
            // super-sink
            int SS = M+F+1;

            for(int i = 0; i < F; ++i)
            {
                // connect super-source to every consultant
                graph[S][i] = m;

                // connect every company to super-sink
                for(int j = 0; j < M; ++j)
                    graph[j+F][SS] = 1;

                // connect every consultant to its respective company
                for(int j = 0; j < consultants[i].size(); ++j)
                {
                    int &comp = consultants[i][j];
                    graph[i][comp+F] = 1;
                }
            }

            // if the flow to super-source is equal to the number
            // of companies to visit, we have a solution
            int res = FF(graph, S, SS);
            if(res == M)
            {
                possible = true;
                std::cout << m << std::endl;
                break;
            }
        }

        if (!possible)
            std::cout << "impossible" << std::endl;
    }
}
