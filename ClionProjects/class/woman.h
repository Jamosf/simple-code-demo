#include<iostream>
#include<string>
using namespace std;

class woman
{
public:
	woman(int age,string strName,float height);
//	~woman();
	void housework(woman p);
	void cook(woman p);	
    void show(woman p);
	int m_iAge;
	string m_strName;
	float m_fHeight;
private:
    


protected:
};