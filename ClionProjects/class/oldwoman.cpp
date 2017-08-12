#include<iostream>
#include<string>
#include"oldwoman.h"
using namespace std;


oldwoman::oldwoman(int age,string strName,float height):woman(age,strName,height)
{
	cout<<"我在打扫卫生"<<endl;
}


void oldwoman::showname()
{
	cout<<"告诉你我的名字..."<<endl;
}