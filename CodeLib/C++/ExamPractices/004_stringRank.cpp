#include <iostream>
using namespace std;

void string_rank(char* str){
    char temp;
    for(int i =0;i < strlen(str);i++){
        for(int j = i + 1;j < strlen(str);j++){
            if(str[i] > str[j]){
                temp = str[i];
                str[i] = str[j];
                str[j] = temp;
            }else if(str[i] == str[j]){
                str[j] = '0';
            }
        }
    }
    for(int j = 0;j < strlen(str);j++){
        if(str[j]!='0'){
            cout<<str[j];
        }

    }
}

int main() {
    char ch[1000];
    cout<<"please enter a string"<<endl;
    cin>>ch;
    string_rank(ch);
    return 0;
}