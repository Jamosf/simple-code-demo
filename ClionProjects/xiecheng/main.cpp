#include <iostream>
#include <string>
#include <vector>
using namespace std;

int main() {
    int a[10000];
    string ss;
    cin>>ss;
    vector<int> vc;
    for(int i = 0; i < ss.length(); i++){
        if(ss[i] != ','){
            vc.push_back(ss[i]);
        }
    }
    int temp;
    int len = vc.size();
    for(int i = 0; i < len;i++){
        for(int j = i + 1; j < len; j++){
            if(vc[i] >= vc[j]){
                temp = vc[i];
                vc[i] = vc[j];
                vc[j] = temp;
            }
        }
    }
    int max = vc[len-1]-vc[0];
    cout<<max<<endl;
    return 0;
}