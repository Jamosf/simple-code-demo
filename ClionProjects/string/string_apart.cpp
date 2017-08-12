#include <iostream>
using namespace std;

int main(){
    double a[10];
    double temp,sum = 0,average;
    bool tag = false;
    for(int i = 0;i<10;i++){
        cin>>a[i];
        if(a[i] > 100 || a[i] < 0){
            cout<<"the score is invalid.";
            tag = true;
            break;
        }
    }
    if(!tag){
        for(int j = 0; j < 10; j++){
            for(int k = j+1; k < 10; k++){
                if(a[j] <= a[k]){
                    temp = a[j];
                    a[j] = a[k];
                    a[k] = temp;
                }
            }
        }
        for(int p = 1;p < 9;p++){
            sum += a[p];
        }
        average = sum/8;
        cout<<average;
    }

    return 0;
}