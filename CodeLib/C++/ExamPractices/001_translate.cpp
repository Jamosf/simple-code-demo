#include <iostream>
#include <string>
#include <sstream>
using namespace std;

string translate(int x);
string intostring(char y);
char ten_to_thirty(int z);

int main()
{
    int i;	
	cout<<"请输入一个数字"<<endl;
	cin>>i;	
	cout<<"三十六进制的字符串为"<<translate(i)<<endl;
	system("pause");
    return 0;
}

string translate(int x)
{
	int first,second,third,forth;
	first = x/(36*36*36);
	second = (x-first*(36*36*36))/(36*36);
	third = (x-first*(36*36*36)-second*(36*36))/(36);
	forth = x-first*(36*36*36)-second*(36*36)-third*(36);
	string result = intostring(ten_to_thirty(first)) + intostring(ten_to_thirty(second)) + intostring(ten_to_thirty(third)) + intostring(ten_to_thirty(forth));
	return result; 
}

string intostring(char y)
{
    stringstream stream;
	stream<<y;
	string str = stream.str();
	return str;
}

char ten_to_thirty(int z)
{
	if(z<=9)
	{
		z = z + 48;
	}
	else
	{
		z = z + 55;
	}
	char ch = z;
	return ch;
}