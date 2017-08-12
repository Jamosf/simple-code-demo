/*
 * 线索二叉树的基本实现 
 */
template<typename Type>
class ThreadNode
{
friend class ThreadTree;
friend class TnreadInorderIterator;
private:
	int leftThread,rightThread;
	ThreadNode<Type> *leftChild,*rightChild;
	Type data;
public:
	ThreadNode(const Type item):data(item),leftChild(null),rightChild(null),leftThread(0),rightThread(0){}
};

template<typename Type>
class ThreadTree
{
friend class TnreadInorderIterator;
private:
	ThreadNode<Type> *root;
public:......
};

template<typename Type>
class ThreadInorderIterator
{
private:
	ThreadTree<Type> &T;
	ThreadNode<Type> *current;
public:
	ThreadInorderIterator(ThreadTree<Type> &Tree):T(Tree){current = T.root;}
	ThreadNode<Type> *First();
	ThreadNode<Type> *Last();
	ThreadNode<Type> *Next();
	ThreadNode<Type> *prior();
};