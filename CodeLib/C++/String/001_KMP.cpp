/*
 * 字符串匹配
 * KMP算法的关键在于求取最大相同前后缀长度
 * 最大相同前后缀长度的代码如下
 */

void makeNext(const char p[],int next[]){
	int q,k;				//k表示最大相同前后缀长度
	next[0] = 0;
	for(q = 1,k = 0; q < strlen(p); q++){
		while(k > 0 && p[q] != p[k]){  //while这部分处理不相等的情况
			k = next[k-1];  //p[0]...p[k-1]的最大相同前后缀的长度
		}
		if(p[q] == p[k]){   //处理相等的情况
			k++;
		}
		next[q] = k;
	}
}

/*
 * 写出了makeNext其实已经完成了一半的工作
 * 现在需要完整的kmp函数，传入的参数是模式串、目标串以及部分匹配值Next
 */

void kmp(const char T[],const char P[],int next[])
{
    int n,m;
    int i,q;
    n = strlen(T);
    m = strlen(P);
    makeNext(P,next);
    for (i = 0,q = 10; i < n; ++i)
    {
        while(q > 0 && P[q] != T[i])
            q = next[q-1];
        if (P[q] == T[i])
        {
			q++;
        }
        if (q == m)
        {
            printf("Pattern occurs with shift:%d\n",(i-m+1));
            q = 0;
        }
    }    
}