//
// Created by fengshangren on 16/10/20.
//
/*
 * 题目描述为：合并一个递增有序的单链表和一个递减有序的单链表为一个递增的单链表
 * 要求时间复杂度为o(n+m)
 */

//先将其中递减的单链表逆序
void reverse(node *L){
    node *q = L,*p = nullptr;
    while(q != nullptr){
        node *temp = q;
        q = q->next;
        temp->next = p;
        p = temp;
    }
    L = p;
}

//归并算法，两个单链表，链表一般有两个指针，一个是本身，另一个是动态的指向链表的指针temp
node *merge(node *a,node *b){
    node *s,*r;   //合并之后的链表，一个是链表本身s，一个是动态的r
    node *p = a, *q = b;
    while(p != nullptr || q != nullptr){
        if(p->data <= q->data){
            if(p == a){
                s = p;
                r = p;
            }else{
                r->next = p;
            }
            p = p->next;
        }else{
            if(q == b){
                s = q;
                r = q;
            }else{
                r->next = q;
            }
            q = q->next;
        }
        r = r->next;
    }
    if(p == nullptr && q != nullptr){
        r->next = q;
    }
    if(p != nullptr && q == nullptr){
        r->next = p;
    }
    return s;
}

//最终执行
node *func(node *a,node *b){
    reverse(b);
    node * result = merge(a,b);
    return result;
}

