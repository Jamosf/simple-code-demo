#include <iostream>
#include <stdlib.h>
using namespace std;

struct node{
    int data;
    struct node *next;
};

struct node* create(struct node *head,int k){
    struct node *p1,*p2;
    p1 = (struct node*)malloc(sizeof(struct node));   //申请新的节点空间，这一步是必须的，不然新的节点无处放置
    p1->data = k;
    p1->next = NULL;
    while(p1->data > 0){
        if(head == NULL){
            head = p1;
        }else{
            p2->next = p1;
        }
        p2 = p1;
        p1 = (struct node*)malloc(sizeof(struct node));   //申请新的节点空间，这一步是必须的，不然新的节点无处放置
        cout<<"请输入一个数字";
        cin>>p1->data;
    }
    free(p1);
    p1 = NULL;
    p2->next = NULL;
    return head;
}

void show(struct node *list){
    while(list != NULL){
        cout<<list->data<<endl;
        list = list->next;
    }
}

int main() {
    int k;
    cout<<"请输入一个数字";
    cin>>k;
    struct node *head;
    head = NULL; //建一个空表
    head = create(head,k);
    show(head);
    return 0;
}