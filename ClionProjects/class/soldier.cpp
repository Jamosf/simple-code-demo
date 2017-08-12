#include<iostream>
#include"soldier.h"
using namespace std;

void famer::work()
{
	cout<<"i am working,don't disturb me!"<<endl;
}

famer::famer(int age)
{
    m_iAge = age;
	cout<<"My age is "<<m_iAge<<endl;
}

void famer::sow()
{
	cout<<"i am sowing"<<endl;
}