#include <iostream>
#include <string>
using namespace std;

struct data
{
    string pinyin;
    int num;
};
struct data a[] = {{"yi",1},{"er",2},{"san",3},{"si",4},{"wu",5},{"liu",6},{"qi",7},{"ba",8},{"jiu",9}};

void string_num(string s){
    string str;
    bool tag = false;
    int first = 0,second = 2;

    for(int n = 0 ; n < 2; n++){

        str = s.substr(first,second);

        for(int k = 0;k < 9; k++){

            if(strcmp(a[k].pinyin.c_str(),str.c_str()) == 0){
                cout<<a[k].num;
                tag = true;
                break;
            }
        }
        if(tag){
            break;
        } else{
            second++;
        }
        tag = false;
    }
    if(tag){
        string ss = s.substr(str.length(),s.length());
        string_num(ss);
    }
}

int main() {
    string ss;
    cout<<"please type a string"<<endl;
    cin>>ss;
    string_num(ss);
    return 0;
}