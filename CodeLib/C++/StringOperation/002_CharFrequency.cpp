/*
 * 统计字符串中字符出现频度的算法
 */

void frequency(char[] s,int n, int [] freq){
	for(int i = 0; i < n; i++){
		freq[s[i]]++;                    //freq数组是以ascii为索引的int型数组
	}
}