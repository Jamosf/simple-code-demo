#include <iostream>
#include <string>
using namespace std;

void string_break(string str,int n);

int main()
{
	int M,N;
	cout<<"ÇëÊäÈëÁ½¸öÊı×Ö"<<endl;
	cin>>M>>N;
	string *ss = new string[M];
	cout<<"ÇëÊäÈë×Ö·û´®"<<endl;
	for(int k = 0; k < M;k++)
	{
        cin>>ss[k];
		string_break(ss[k],N);
	}		
	return 0;
}

void string_break(string str,int n)
{
	int len = str.length();
	int count = len/n;
    int remain = len - count*n;
	string zero = "0";
	string out;
	for(int i = 0;i < len;i+=n)
	{
		if(len - i < n)
		{
		   out = str.substr(i,len);
		   for(int j = 0;j < n-len+i; j++)
		   {
               out += zero;
		   }
		   cout<<out<<endl;
		}
		else
		{
		   cout<<str.substr(i,i+n)<<endl;	
		}
	}
}