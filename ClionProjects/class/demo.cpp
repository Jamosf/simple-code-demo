#include<iostream>
#include<string>
#include"soldier.h"
#include"oldwoman.h"
using namespace std;


int main()
{
  famer fam(20);
  cout<<fam.m_iAge<<endl;
  fam.sow();
  fam.work();

  woman wom(20,"奶奶",158);
  

  wom.cook(wom);
  wom.housework(wom);


  oldwoman olw(30,"占么么",150);

  olw.cook(olw);
  olw.housework(olw);
  olw.showname();
  olw.m_iSoncount=4;
  cout<<"儿子的个数 "<<olw.m_iSoncount<<"这么多个"<<endl;

//  system("pause");
  return 0;
}