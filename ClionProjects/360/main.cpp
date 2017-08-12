#include <iostream>
#include <cmath>
using namespace std;

int func(long &m,long &n,int &count){
    int sum = 0;
    sum = m / pow(n,count);
    m = m - sum*pow(n,count);
    return sum;
}

int main()
{
    long  M,N;
    while(cin>>M>>N){
        if(N >= 2 && N <= 16){

            int a[60];
            int i = 0;
            int count = 0;
            char output;
            long total = M;
            while(total/N){
                count++;
                total /= N;
            }
            while(M){
                a[i] = func(M,N,count);
                if(a[i] >= 10){
                    output = a[i] + 55;
                    cout<<output;
                }
                else{
                    cout<<a[i];
                }

                if(M == 0 && count){
                    for(int j = 0; j < count; j++){
                        cout<<"0";
                    }
                }
                i++;
                count--;
            }
        }
        cout<<endl;

    }

    return 0;
}