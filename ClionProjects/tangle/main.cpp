#include<iostream>
#include<iomanip>
#include<cmath>
using namespace std;

void slove(int n,int m,int x,int y,int t,float *xy[]){
    float cc,ss,sum = 0;
    cc = 1 - pow((1 - xy[x-1][y-1]),t);
    for(int i = 0;i < n; i++){
        for(int j = 0; j < m; j++){
            sum += xy[i][j];
        }
    }
    ss = 1 - pow(1 - sum/(n*m),t);
    if(cc > ss){
        cout<<fixed<<setprecision(2)<<"cc\n"<<cc<<endl;
    }else if(cc < ss){
        cout<<fixed<<setprecision(2)<<"ss\n"<<ss<<endl;
    }else if(cc == ss){
        cout<<fixed<<setprecision(2)<<"equal\n"<<cc<<endl;
    }
}

int main(){
    int n,m,x,y,t;
    while(cin>>n>>m>>x>>y>>t){
        float **xy = new float *[n];
        for(int i = 0;i < n; i++){
            xy[i] = new float [m];
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                cin>>xy[i][j];
            }
        }
        slove(n,m,x,y,t,xy);
        for(int i = 0; i < n; i++){
            delete []xy[i];
        }
        delete []xy;
    }
}