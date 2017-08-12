/**
 * 问题描述：将数组A拆分为两个数组B和C，要求B存储大于等于0的数，C存储小于等于0的数
 */
int Alength;
int AToBC(){
    int i = 0,j = Alength-1;
    while(i <= j){
        while(A[i] >= 0) i++;
        while(A[j] < 0) j--;
        if(i < j) swap(A[i],A[j]);
    }
    return i;

}