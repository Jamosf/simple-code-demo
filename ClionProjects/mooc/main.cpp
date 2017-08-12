#include <iostream>
using namespace std;

int main(){
    int a[100];
    int sum = 0;
    int cc = 0;
//    cin >> a[cc];
    while(cin){
        do{
            cin >> a[cc];
//        cout << a[cc];
            cc++;
        } while(cin.get() != '\n');
        if(a[0] == 1){
            sum += (a[2]-a[1]+1)*(a[3]);
        }else{
            sum += a[1]*a[2];
        }
        cout << sum<< endl;
        cc = 0;
    }

    return 0;
}