#include<iostream>  
#include<cstdio>  
using namespace std;  
  
int ops[21];  
const char sym[3] = {'+' , '-' , ' '};  
int result , num;  
  
void dfs(int layer, int currentResult, int lastOp, int lastSum)  
{  
    lastSum *= (layer > 9) ? 100 : 10;  
    lastSum += layer;  
    if(layer == 9)  
    {  
        currentResult += (lastOp) ? (-1 * lastSum) : lastSum;  
        if(currentResult == result)  
        {  
            ++num;  
            printf("1");  
            for(int i = 2 ; i <= 9 ; ++i)  
            {  
                if(sym[ops[i-1]] != ' ')  
                    printf(" %c ", sym[ops[i-1]]);  
                printf("%d", i);  
            }  
            printf(" = %d\n" , result);  
        }  
        return;  
    }  
    ops[layer] = 2;  
    dfs(layer + 1 , currentResult , lastOp , lastSum);   //Continue  
    currentResult += (lastOp)? (-1 * lastSum) : lastSum;  
    ops[layer] = 0;  
    dfs(layer + 1 , currentResult , 0 , 0);  //Plus  
    ops[layer] = 1;  
    dfs(layer + 1 , currentResult , 1 , 0);  //Minus  
}  
  
int main(void)  
{  
    while(scanf("%d", &result) != EOF)  
    {  
        num = 0;  
        dfs(1 , 0 , 0 , 0);  
        printf("%d\n" , num);  
    }  
    return 0;  
}  