enum Color {
	RED, BLACK;
}

class RBTNode {
	int key, value;
	Color color;
	RBTNode left, right, p;
	boolean leaf;
	
	public RBTNode() {
		key = -1;
		value = -1;
		left = null;
		right = null;
		p = null;
		leaf = true;
		color = Color.BLACK;
	}

	public RBTNode(int key, int value, Color color) {
		this.key = key;
		this.value = value;
		this.color = color;
		left = new RBTNode();
		right = new RBTNode();
		p = new RBTNode();
		leaf = false;
	}
}

class BSTNode {
	int key, value;
	BSTNode left, right;
	public BSTNode() {
		key = -1;
		value = -1;
		left = null;
		right = null;
	}

	public BSTNode(int key, int value) {
		this.key = key;
		this.value = value;
		left = null;
		right = null;
	}
}

class BinarySearchTree {
	BSTNode root;

	public BinarySearchTree() {
		root = null;
	}

	public void insert(int key, int value) {
		BSTNode x = root, y = null;
		BSTNode z = new BSTNode(key, value);
		if (root == null) {
			root = z;
			return;
		}
		while (x != null) {
			if (x.key == key) {
				x.value = value;
				return;
			}
			y = x;
			if (key < x.key)
				x = x.left;
			else
				x = x.right;
		}
		if (key > y.key)
			y.right = z;
		else
			y.left = z;
	}

	public void transplant(BSTNode p, BSTNode u, BSTNode v) {
		if (p == null) {
			root = v;
			return;
		} else if (p.left == u)
			p.left = v;
		else
			p.right = v;
	}

	public void delete(int key) {
		BSTNode x = root, y = null;
		while (x != null) {
			if (x.key == key)
				break;
			y = x;
			if (key < x.key)
				x = x.left;
			else
				x = x.right;
		}
		if (x == null)
			return;
		if (x.left == null)
			transplant(y, x, x.right);
		else if (x.right == null)
			transplant(y, x, x.right);
		else {
			BSTNode u = x.right, v = null;
			while (u.left != null) {
				v = u;
				u = u.left;
			}
			u.left = x.left;
			if (u != x.right) {
				v.left = u.right;
				u.right = x.right;
			}
			transplant(y, x, u);
		}
	}	
}

public class RedBlackTree {
	RBTNode root;

	public RedBlackTree() {
		root = new RBTNode();
	}

	public void leftRotate(RBTNode x) {
		RBTNode p = x.p, y = x.right;
		assert(!y.leaf);
		x.right = y.left;
		y.left.p = x;
		y.left = x;
		x.p = y;
		y.p = p;
		if (p.leaf)
			root = y;
		else if (p.left == x)
			p.left = y;
		else
			p.right = y;
	}

	public void rightRotate(RBTNode x) {
		RBTNode p = x.p, y = x.left;
		assert(!y.leaf);
		x.left = y.right;
		y.right.p = x;
		y.right = x;
		x.p = y;
		y.p = p;
		if (p.leaf)
			root = y;
		else if (p.left == x)
			p.left = y;
		else
			p.right = y;
	}

	public void insert(int key, int value) {
		RBTNode z = new RBTNode(key, value, Color.RED);
		if (root.leaf) {
			root = z;
			z.color = Color.BLACK;
			return;
		}
		RBTNode x = root, y = null;
		while (!x.leaf) {
			if (key == x.key) {
				x.value = value;
				return;
			}
			y = x;
			if (key < x.key)
				x = x.left;
			else
				x = x.right;
		}
		z.p = y;
		if (key > y.key)
			y.right = z;
		else
			y.left = z;
		insertFixUp(z);
	}

	public void insertFixUp(RBTNode z) {
		RBTNode y = null;
		while (z.p.color == Color.RED) {
			if (z.p == z.p.p.left) {
				y = z.p.p.right;
				if (y.color == Color.RED) {
					z.p.color = Color.BLACK;
					y.color = Color.BLACK;
					z.p.p.color = Color.BLACK;
					z = z.p.p;
				} else {
					if (z == z.p.right) {
						z = z.p;
						leftRotate(z);
					}
					z.p.color = Color.BLACK;
					z.p.p.color = Color.RED;
					rightRotate(z.p.p);
				}
			} else {
				y = z.p.p.left;	
				if (y.color == Color.RED) {
					z.p.color = Color.BLACK;
					z.p.p.color = Color.RED;
					y.color = Color.BLACK;
					z = z.p.p;
				} else {
					if (z == z.p.left) {
						z = z.p;
						rightRotate(z);
					}
					z.p.color = Color.BLACK;
					z.p.p.color = Color.RED;
					leftRotate(z.p.p);
				}
			}
		}
		root.color = Color.BLACK;
	}

	public void transplant(RBTNode u, RBTNode v) {
		if (u.p.leaf)
			root = v;
		else if (u.p.left == u)
			u.p.left = v;
		else
			u.p.right = v;
		v.p = u.p;
	}

	public RBTNode minimum(RBTNode x) {
		assert(!x.leaf);
		while (!x.left.leaf)
			x = x.left;
		return x;
	}

	public void delete(int key) {
		RBTNode z = root;

		//find the deleted node z
		while (!z.leaf) {
			if (key == z.key)
				break;
			if (key < z.key)
				z = z.left;
			else
				z = z.right;
		}

		//not found
		if (z.leaf)
			return;

		//y denotes the deleted node z or the successor of z, and x denotes the successor of y
		RBTNode y = z, x = null;

		//the color of the deleted node
		Color c = y.color;
		
		//the deleted node has no left child, replace z with z.right
		if (z.left.leaf) {
			x = z.right;
			transplant(z, z.right);
		}

		//the deleted node has no right child, replace z with z.left
		else if (z.right.leaf) {
			x = z.left;
			transplant(z, z.left);
		
		//the deleted node has two children, find the successor of the deleted node z, which is y
		} else {
			y = minimum(z.right);
			x = y.right;
			c = y.color;
			if (y != z.right) {
				transplant(y, y.right);
				y.right = z.right;
				z.right.p = y;
			}
			y.left = z.left;
			z.left.p = y;
			transplant(z, y);
			y.color = z.color;
		}

		//if the color of the deleted or successor node is RED, properties of RBT remain unchanged, just return
		if (c == Color.BLACK)
			deleteFixUp(x);
	}

	public void deleteFixUp(RBTNode x) {
		//the color of x is red-black or black-black, based on the color of x, if it is red, the loop breaks, and change it to black
		//if x is the root, the loop breaks as well
		while (x != root && x.color == Color.BLACK) {
			if (x == x.p.left) {
				//w denotes the sibling of x
				RBTNode w = x.p.right;

				//the color of w is red, swap the color of w and x.p, change this case to the following cases with left rotation
				if (w.color == Color.RED) {
					w.color = Color.BLACK;
					x.p.color = Color.RED;
					leftRotate(x.p);
					w = x.p.right;
				}

				//w has two black children (null child is impossilbe, since the black height of x is at least 2)
				if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
					w.color = Color.RED;
					x = x.p;
				} else { 
					//the right child of w is black, swap the color of w.left and w, change this case to the following case with right rotation.
					if (w.right.color == Color.BLACK) {
						w.left.color = Color.BLACK;
						w.color = Color.RED;
						rightRotate(w);
						w = x.p.right;
					}

					//the final case, the right child of w is red, swap the color of x.p and w, change w.right to black, left rotate x.p and the loop breaks
					w.color = x.p.color;
					x.p.color = Color.BLACK;
					w.right.color = Color.BLACK;
					leftRotate(x.p);
					x = root;
				}
			} else {
				//just repalce right with left.
				RBTNode w = x.p.left;
				if (w.color == Color.RED) {
					w.color = Color.BLACK;
					x.p.color = Color.RED;
					rightRotate(x.p);
					w = x.p.left;
				}
				if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
					w.color = Color.RED;
					x = x.p;
				} else { 
					if (w.left.color == Color.BLACK) {
						w.right.color = Color.BLACK;
						w.color = Color.RED;
						leftRotate(w);
						w = x.p.left;
					}
					w.color = x.p.color;
					x.p.color = Color.BLACK;
					w.left.color = Color.BLACK;
					rightRotate(x.p);
					x = root;
				}
			}
		}
		x.color = Color.BLACK;
	}
}
