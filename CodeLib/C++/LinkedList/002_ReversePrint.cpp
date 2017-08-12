/*
 * 逆向输出不带头结点的单向链表中数据域的递归算法
 * 链表的数据类型名称为list，结点的数据域和指针域的名称分别为data和next
 */
/*
void print(list * node){
    if(node->next != null){
        print(node->next);
        printf("%d",node->data);
    }
}
*/
/*
 * 逆向输出带头结点单链表数据域的非递归方法
 * 运用头插法输入即可
 * 这个算法针对每个字符，连续的输入对应连续的输出，还是比较可以的。
 */
#include <iostream>
using namespace std;

template <typename Type>
class node{
public:
    Type data;
    node *next;
    node *create();
    void print(node *);
};

template <typename Type>
node<Type> *node<Type>::create(){
    Type ch;
    node *head,*p;
    ch = getchar();
    head = nullptr;
    while(ch != '\n'){
        p = (node*)malloc(sizeof(node));
        p->data = ch;
        p->next = head;
        head = p;
        ch = getchar();
    }
    return head;
}

template <typename Type>
void node<Type>::print(node *head) {
    node *p = head;
    while(p != nullptr){
        cout<<p->data;
        p = p->next;
    }
    cout<<endl;
}

int main(){
    node<char> *head;
    node<char> *p = new node<char>();
    cout<<"input the num:"<<endl;
    head = p->create();
    p->print(head);
    delete p;
    return 0;
}

