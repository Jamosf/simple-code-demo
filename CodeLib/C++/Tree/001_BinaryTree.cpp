/*
 * 树的C++抽象描述
 */
template <typename Type> class BinaryTree;
template <typename Type>
class BinTreeNode
{
friend class BinaryTree<Type>;
public:
	BinTreeNode():left(null),right(null){}
	BinTreeNode(Type item,BinTreeNode<Type> *left,BinTreeNode<Type> *right):data(item),leftChild(left),rightChild(right){}
	Type GetData() const{return data;}
	BinTreeNode<Type> *GetLeft() const{return leftChild}
	BinTreeNode<Type> *GetRight() const{return rightChild}
	void SetData(const Type &item){data = item;}
	void SetLeft(BinTreeNode<Type> *L){leftChild = L;}
	void SetRight(BinTreeNode<Type> *R){rightChild = R;}
	~BinTreeNode();

private:
	BinTreeNode<Type> *leftChild,*rightChild;
	Type data;
	
};

class BinaryTree
{
public:
	BinaryTree();
	~BinaryTree();
	
};


