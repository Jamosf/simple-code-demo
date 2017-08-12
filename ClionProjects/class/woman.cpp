#include<iostream>
#include"woman.h"
using namespace std;

woman::woman(int age,string strName,float height):m_iAge(age),m_strName(strName),m_fHeight(height)
{
	cout<<"我是个女人"<<endl;
}

void woman::cook(woman p)
{
	cout<<p.m_strName<<"我会做饭"<<endl;
}

void woman::housework(woman p)
{
	cout<<p.m_strName<<"我要做家务"<<endl;
}

void woman::show(woman p)
{
	cout<<p.m_strName<<" "<<p.m_iAge<<" "<<p.m_fHeight<<endl;
}