/*
 * 用二叉树来表示多叉树，以及树的度的概念
 * lLink用来表示儿子，rLink用来表示兄弟
 */
int tree::degree(Node *t){
	if(t == null) return 0;
	int count = 0,max = 0;
	t = t->lLink;
	while(t != null){
		count++;			//用来记录单个儿子的兄弟个数
		int d = degree(t);	//用来记录所有儿子结点的度数，第一个儿子结点度数由count纪录着，所以要比较count和max
		if(d > max) max = d;
		t = t->rLink;
	}
	if(count > max) return count;
	else return max;
}