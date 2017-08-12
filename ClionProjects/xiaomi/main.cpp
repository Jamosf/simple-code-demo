#include <iostream>
#include <string>
using namespace std;

int main() {
    string ss;
    getline(cin,ss);
    string str[100];
    int count = 0;
    int first = 0,second;
    int i = 0;
    while(true){
        if(ss[i] == ' '){
            second = i;
            str[count] = ss.substr(first,second-first);
            first = second + 1;
            count++;
        }
        if(i == ss.length()){
            second = i;
            str[count] = ss.substr(first,second - first);
            break;
        }
        i++;
    }
    for(int j = count; j >= 0; j--){
        cout<<str[j]<<" ";
    }
    return 0;
}