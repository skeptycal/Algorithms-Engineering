#include <algorithm>
#include <set>
#include <string>
#include <iostream>
#include <vector>
#include <sstream>
#include <cmath>

bool is_prime(const int &num);
void main_scramble(const std::string &s, const int &max_word_len, const int &len, std::set<std::string> &my_set);
void permute(std::string s, std::set<int> &my_set);

bool is_prime(const int &num)
{
    if(num <= 1) return false;
    if(num == 2) return true;
    int num_sqrt = std::sqrt(num);
    for(int i = 2; i <= num_sqrt; ++i)
    {
        if(i != num && num%i == 0)
            return false;
    }
    return true;
} 

void main_scramble(const std::string &s, const int &max_word_len, const int &len, std::set<std::string> &my_set)
{
    // create array of size 'len' - the size of our sub-sequence
    // the values in this array represent the index of the character
    // that we wish to use.
    // if chars[0] = 5, that means that the first character in our sub-sequence
    // is gonna be the character located at position 5 in the original sequence
    int chars[len];

    // The initial word is gonna be the first 'len' letters in the sequence
    for(int i = 0; i < len; ++i)
        chars[i] = i;

    while(true)
    {
        // create our sequence as a string
        std::string sub_seq = "";
        for(int i = 0; i < len; ++i)
            sub_seq += s[chars[i]];
        my_set.insert(sub_seq);
        
        // if the first character in our sub-sequence
        // has exhausted all unique characters, stop
        // total number of 'len' digit windows will be max_word_len - len
        if(chars[0] == max_word_len - len)
            break;

        // offset by 1 because words of length 1 have their first 
        // character in position 0 and so on. We want to exhaust
        // the right-most character first, then move left.
        int pos = len-1;

        // find the right-most character that has yet to be exhausted
        while(chars[pos] == max_word_len - len + pos)
            pos--;
        
        // increment the position in the word for this character
        chars[pos]++;

        // also increment the position for the following characters
        for(int i = pos + 1; i < len; ++i)
            chars[i] = chars[i-1]+1;
    }
}

void permute(std::string s, std::set<int> &my_set)
{
    std::sort(s.begin(), s.end());
    do  {
        my_set.insert(std::atoi(s.c_str()));
    } while(std::next_permutation(s.begin(), s.end()));
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
        std::getline(std::cin, line);
        std::set<std::string> scrambled;
        std::set<int> perms;
        std::string &s = line;

        int length_of_word = s.size();

        // scramble input to find all digits of length 1 up to length_of_word
        for(int i = 1; i < length_of_word + 1; ++i)
            main_scramble(s, length_of_word, i, scrambled);
    
        int primes = 0;
        // for all scrambled digits, create all permutations of that word
        while(!scrambled.empty())
        {
            permute(*scrambled.begin(), perms);
            scrambled.erase(scrambled.begin());
        }

        // for each permutation, check if this is a prime
        while(!perms.empty())
        {
            int c_num = *perms.begin();
            if(is_prime(c_num))
                primes++;
            //std::cout << c_num << std::endl;
            perms.erase(perms.begin());
        }

        std::cout << primes << std::endl;
    }
    return 0;
}
