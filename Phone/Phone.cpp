#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <map>

struct node;

struct node {
    int value;
    std::map<int, node> children;
    bool is_terminating;
    node() {}
    node(int val, bool terminate)
    {
        value = val;
        is_terminating = terminate;
    }
};

node * find_child(int key, node &parent)
{
    std::map<int, node>::iterator pos = parent.children.find(key);
    if(pos == parent.children.end())
        return NULL;
    else
        return &pos->second;
}

int main()
{
    int N;
    std::string line;
    std::getline(std::cin, line);
    std::stringstream(line) >> N;
     
    // for each test case  
    for(int n = 0; n < N; ++n)
    {
        // number of phone numbers in input
        int phone_numbers;
        std::getline(std::cin, line);
        std::stringstream(line) >> phone_numbers;
    
        // create root node of tree
        node root(-1, false);
        
        // vector containing phone numbers in order sorted by length
        std::vector<std::vector<std::string> > phn(10, std::vector<std::string>());

        // put each number in the list according to its length
        for(int p = 0; p < phone_numbers; ++p)
        {
            std::getline(std::cin, line);
            int size = line.size()-1;
            phn[size].push_back(line);
        }

        // parent node for each number starts at root
        node *parent = &root;
        bool should_exit = false;
        for(int i = 0; i < 10; ++i)
        {
            for(int j = 0; j < phn[i].size(); ++j)
            {
                // new number, reset parent to root
                parent = &root;
                std::string &cur = phn[i][j];
                
                // for every digit in this number
                for(int k = 0; k < cur.size(); ++k)
                {
                    // does this path already exist?
                    node *n_ptr = find_child(cur[k], *parent);
                    
                    // path does not exist, append node to its parent
                    // and update our parent pointer to point to the
                    // newly added node
                    if(n_ptr == NULL) {
                        bool is_term = (k == cur.size()-1);
                        //std::cout << "not found" << std::endl;
                        //std::cout << "adding " << cur[k] << std::endl;
                        parent->children[cur[k]] = node(cur[k], is_term);
                        parent = find_child(cur[k], *parent);
                    } else {
                        // if this is a terminating number, list is not consistent
                        // if we dont break, move our pointer to this node, so
                        // the following digit can be tested
                        if(n_ptr->is_terminating == true)
                        {
                            std::cout << "NO " <<  std::endl;
                            should_exit = true;
                            break;
                        }
                        //std::cout << "found" << " appending char" << std::endl;
                        parent = n_ptr;
                    }
                }
                if(should_exit) break;
            }
            if(should_exit) break;
        }
        if(!should_exit) std::cout << "YES" << std::endl;
    }
    return 0;
}

