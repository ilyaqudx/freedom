
public class IntTreeMap {

	private Node root;
	private int  size;
	public static final boolean BLACK = true , RED = false;
	
	@SuppressWarnings("unused")
	public int add(Integer key,int value)
	{
		if(key == null)throw new NullPointerException();
		
		Node t = root;
		Node p = null;
		int cmp= 0;
		while(t != null){
			p   = t;
			cmp = key.compareTo(t.key);
			if(cmp < 0)
				t = t.left;
			else if(cmp > 0)
				t = t.right;
			return t.value;
		}
		
		Node x = new Node(key, value, p);
		if(p == null)
			root = x;
		else if(cmp < 0)
			p.left = x;
		else
			p.right = x;
		
		fixAfterInserted(x);
		size++;
		return 0;
	}
	
	public static void main(String[] args) {
		
		IntTreeMap map = new IntTreeMap();
		map.add(20, 20);
		map.add(10, 10);
		map.add(26, 26);
		
	}
	
	private void fixAfterInserted(Node x)
	{
		setColor(x, RED);
		while(x != null && x != root && colorOf(parentOf(x)) ==  RED){
			//判断父节点是否是左节点
			if(parentOf(x) == leftOf(gradeOf(x)))
			{
				Node r = rightOf(gradeOf(x));
				//叔父节点也为红
				if(colorOf(r) == RED){
					//设置父节点为黑
					setColor(parentOf(x), BLACK);
					//设置叔父为黑
					setColor(r, BLACK);
					//设置祖父为红
					setColor(gradeOf(x), RED);
					//当前节点为祖父节点
					x = gradeOf(x);	
				}else{
					//叔父节点为NULL或者为黑
					
					//判断x是否和父节点在一个方向,即都是左节点
					if(x == rightOf(parentOf(x)))
					{
						//如果是右节点,则先要将X旋转到和父节点到一个方向,所以以父节点为中心,向左旋转
						x = parentOf(x);
						rotateLeft(x);
					}
					//将父节点设为黑
					setColor(parentOf(x), BLACK);
					//将祖父节点设为红
					setColor(gradeOf(x), RED);
					//再以x节点为中心,进行右旋转
					rotateRight(x);
				}
			}else{
				//父节点为右节点
				
				//获取叔父节点
				Node r = leftOf(gradeOf(x));
				//如果叔父节点也是红
				if(colorOf(r) == RED){
					//设置父为黑
					setColor(parentOf(x), BLACK);
					//设置叔父为黑
					setColor(r, BLACK);
					//设置祖父为红
					setColor(gradeOf(x), RED);
					//设置当前X为祖父
					x = gradeOf(x);
				}
				else{//叔父节点为NULL或者为黑
					
					//判断X节点是否和父节点为同一方向节点
					if(x == leftOf(parentOf(x))){
						//不在同一方向上,通过旋转将两节点旋转到一个方向上
						x = parentOf(x);
						rotateRight(x);
					}
					
					//设置父为黑
					setColor(parentOf(x), BLACK);
					//设置祖父为红
					setColor(gradeOf(x), RED);
					//将X左旋
					rotateLeft(x);
				}
			}
		}
		//最后,ROOT始终为黑
		setColor(root, BLACK);
	}
	
	
	/**
	 * 以X节点为中心,进行向左旋转
	 * 
	 * 先声明几个节点
	 * 		:X节点的右子节点为R
	 * 		:X节点的父节点为    Xp
	 * 		:R节点的左子节点为Rl
	 * 
	 * 
	 * 1-X节点的父节点为R,R的左子节点为X
	 * 2-X节点的右子节点为RL,RL的父节点为X
	 * 3-R的父节点为XP,R为XP的子节点,至于是左子节点还是右子节点,和之前X的位置保持一致
	 * */
	private void rotateLeft(Node x) 
	{
		if(null != x){
			Node r  = x.right;
			Node xp = x.parent;
			Node rl = r.left;
			boolean left = leftOf(xp) == x;
			//重新设置x的父节点为r  
			x.parent = r;
			//重新设置r的左子节点为x
			r.left   = x;
			//重新设置x节点的右子节点为rl
			x.right  = rl;
			//重新设置rl的父节点为x
			if(rl != null)
				rl.parent= x;
			//重新设置r的父节点为xp
			r.parent = xp;
			//重新设置rp的左子节点为r
			if(xp == null)
				root = r;
			else if(left)
				xp.left  = r;
			else 
				xp.right = r;
		}
	}

	/**
	 * 以X为中心向右旋转
	 * 
	 * 先声明几个节点
	 * 		:X的左子节点为L
	 * 		:L的右子节点为Lr
	 * 		:X的父节点为Lp
	 * 
	 * 1-X节点的父节点为L,L的右子节点为X
	 * 2-X节点的左子节点为Lr,Lr的父节点为X
	 * 3-L的父节点为Xp,L为Xp的子节点,至于是左子节点还是右子节点,和之前X节点保持一致
	 * */
	private void rotateRight(Node x) 
	{
		if(x != null){
			//获取X的左子节点
			Node l  = x.left;
			//获取l的右子节点
			Node lr = l.right;
			//获取x的父节点
			Node xp = x.parent;
			//x是否为
			boolean right = rightOf(xp) == x;
			
			//更新X的父节点为L
			x.parent = l;
			//更新l的右子节点为X
			l.right  = x;
			//更新X节点的左子节点为lr
			x.left   = lr;
			//更新Lr节点的父节点为x
			if(lr != null)
				lr.parent = x;
			//更新L的父节点为Xp
			l.parent = xp;
			//更新Xp的子节点为L
			if(xp == null)//说明X为根节点,则设置l为根节点
				root = l;
			else if(right)//如果之前X为右子节点,则更新XP的右子节点为L
				xp.right = l;
			else
				xp.left  = l;
		}
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}


	private void setColor(Node n,boolean color){
		if(n != null)
			n.color = color;
	}
	
	private Node rightOf(Node n){
		return n == null ? null : n.left;
	}
	
	private boolean colorOf(Node n){
		return n == null ? BLACK : n.color;
	}
	
	private Node parentOf(Node n){
		return n == null ? null : n.parent;
	}
	
	private Node leftOf(Node parent){
		return null == parent ? null : parent.left;
	}
	
	private Node gradeOf(Node n){
		return parentOf(parentOf(n));
	}
	
	class Node{
		public Integer key;
		public int     value;
		public Node    left;
		public Node    right;
		public Node    parent;
		public boolean color = BLACK;
		public Node(Integer key, int value, Node parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
	}
}
