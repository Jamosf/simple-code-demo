#include<iostream>
#include<string>
#include"woman.h"
using namespace std;

class oldwoman:public woman 
{
public:
    oldwoman(int age,string strName,float height);
	void showname();
	int m_iSoncount;
private:
protected:
};