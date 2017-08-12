#include <iostream>
using namespace std;

int main() {
    int x = 1;
    x = x++;
    
    std::cout << x << std::endl;
    return 0;
}