#include <iostream>
#include <iomanip>
using namespace std;

int main() {
    int k;
    cin>>k;
    double p = 0;
    double max;
    int l1,l2,l3;
    int *a = new int[k];
    for(int i = 0; i < k; i++){
        p += a[i];
    }
    p = p/2;
    return 0;
}

* find(const DOUBLE_LINK_NODE* pDLinkNode, int data)
{
    DOUBLE_LINK_NODE* pNode = NULL;
    if(NULL == pDLinkNode)
        return NULL;

    pNode = (DOUBLE_LINK_NODE*)pDLinkNode;
    while(NULL != pNode){
        if(data == pNode->data)
            return pNode;
        pNode = pNode ->next;
    }

    return NULL;
}