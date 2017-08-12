/*
 * 树以单链表的形式存储，现将其转换成顺序存储
 */
template<typename Type>
void transform(Node *p,Type[] tree,int index){
	if(p == null) return;
	tree[index] = p->data;
	transform(p->left,tree,2*index+1);
	transform(p->right,tree,2*index+2);
}