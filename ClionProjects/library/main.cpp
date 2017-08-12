/**
 * 问题描述：将数组A拆分为两个数组B和C，要求B存储大于等于0的数，C存储小于等于0的数
 */
/*
template<typename Type>
class Node{
public:
    Type data;
    Node *next;
    void insert(Node<Type>*,Type);
};

template<typename Type>
void Node<Type>::insert(Node<Type> *L, Type x) {
    Node *c = L->next,*p = L;
    while(c != nullptr && c->data >= x){
        p = c;
        c = c->next;
    }
    Node *q = new Node;
    q->data = x;
    q->next = c;
    p->next = q;
}

class Node{
public:
    int data;
    Node * next;
    void insert(Node*,int,int);
};

void Node::insert(Node *L, int x, int k) {
    Node *c = new Node(),*temp,*pre;
    c->data = x;
    if(L == nullptr){
        L = c;
    }else{
        temp = L->next;
        pre = L;
    }
    while(temp != nullptr && temp->data != k){
        temp = temp->next;
        pre = pre->next;
    }
    if(temp == nullptr){
        pre->next = c;
    }else{
        pre->next = c;
        c->next = temp;
    }
}

class node{
public:
    int element;
    node *link;
    void insert(node*,int);
};

void node::insert(node * p, int e) {
    node *c = new node();
    node *pre,temp;
    c->element = e;
    while(p->link){
        if(p->link->element > e){
            c->link = p->link;
            p->link = c;
            return;
        }
        p = p->link;
    }
    p->link = c;
    c->link = nullptr;
    return;
}*/

class Lnode{
public:
    int data;
    Lnode *next,*pred;
    int freq;
    Lnode *Locate(Lnode *,int);
};

Lnode *Lnode::Locate(Lnode *L, int x) {
    Lnode * temp = L->next;
    Lnode * search;
    if(L->data == x){
        L->freq ++;
        return L;
    }
    //用于把x找出来
    while(temp != L){
        if(x == temp->data){
            temp->freq++;
            break;
        }else{
            temp = temp->next;
        }
    }
    if(temp == L)return nullptr;
    search = temp->pred;
    while(search != L){
        if(search->freq < temp->freq){

        }
    }

}

























