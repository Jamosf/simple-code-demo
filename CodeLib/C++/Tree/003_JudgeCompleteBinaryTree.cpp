/*
 * 非递归程序，判断一棵二叉树为完全二叉树
 * 在遍历中利用完全二叉树若某结点无左子女就不应有右子女的原则进行判断
 */

template<typename Type>
int BinaryTree<Type>::Judge(BiTree bt)
{
	int tag = 0;
	BiTree p = bt;
	BiTree Q[];              //Q是队列，元素是二叉树结点指针，容量足够大
	if(p == null)return 1;
	QueueInit(Q);
	QueueIn(Q,p);			//初始化队列，根节点指针入队
	while(! QueueEmpty(Q))
	{
		p == Queue(Q);					//出队
		if(p->lChild && !tag) QueueIn(Q,p->lChild);		//左子女入队
		else if(p->lChild) return 0;
		else tag = 1;
		if(p->rChild && !tag) QueueIn(Q,p->rChild);
		else if(p->rChild) return 0;
		else tag = 1;
	}
	return 1;
}