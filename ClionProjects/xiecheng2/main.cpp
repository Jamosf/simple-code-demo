#include <iostream>
using namespace std;

int func(int x,int n,int a[]){
    int mid,left,right;
    left = 0;
    right = n-1;
    int finally;
    while(left <= right){
        mid = (left + right) / 2;
        if (x > a[mid]) {
            left = mid + 1;
            finally = mid + 1;
        } else if (x < a[mid]) {
            right = mid - 1;
            finally = mid;
        } else if(x == a[mid]){
            return mid;
        }
    }
    return -1-finally;
}

int main() {
    int x,n;
    cin>>x>>n;
    int *a = new int[n];
    for(int i = 0; i < n; i++){
        cin>>a[i];
    }
    cout<<func(x,n,a);
    delete []a;
    return 0;
}