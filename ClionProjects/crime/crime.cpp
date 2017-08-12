#include <iostream>
using namespace std;

int slove(int n,int t,int c,int array[]){
    int sum = 0;
    int count = 0;
    for(int i = 0; i < c; i++){
        sum += array[i];
    }
    if(sum <= t){
        count++;
    }
    for(int j = 0; j < n-c; j++){
        sum += array[j+c]-array[j];
        if(sum <= t){
            count++;
        }
    }

    return count;
}

int main() {

    int N,T,C;
    while(cin>>N>>T>>C){
        int *array = new int[N];
        for(int j = 0; j < N; j++){
            cin>>array[j];
        }
        cout<<slove(N,T,C,array)<<endl;
        delete []array;
    }
    return 0;
}