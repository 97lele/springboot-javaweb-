package com.gdut.xg.shop.util.LRU;


public class LRUList<K,G> {

	private int max;
	private int length;
	private Node<K,G> head;
	
	public Node<K, G> getHead() {
		return head;
	}

	public void setHead(Node<K, G> head) {
		this.head = head;
	}
	public int size() {
		return this.length;
	}

	public void print() {
		Node po=head;
		for(int g=0;g<length;g++) {
			po=po.getNext();
			System.out.println(po.getKey());

		}
	}
	
	public LRUList(int max) {
		this.max=max;
		length=0;
		head=new Node(null,null);
		
	}
	
	
	public Node<K,G> find(K t) {
		Node<K,G> p=this.head;
		while(p.getNext()!=null) {
			if(p.getNext().getKey().equals(t)) {
				break;
			};
			p=p.getNext();
		}
		return p.getNext();
	}
	
	private void check() {
		if(this.length>max) {
			Node<K,G> g=this.head.getNext();
			for(int i=0;i<length-1;i++) {
				g=g.getNext();
			}
			
			g.setNext(null);
			length--;
		}
	}
	public Boolean insert(Node<K,G> n) {
		if(head.getNext()!=null) {
			Boolean find=false;
			Node<K,G> g=this.head;
			while(g.getNext()!=null) {
				Node<K,G> temp=g.getNext();
				if(temp.getKey().equals(n.getKey())) {
					g.setNext(temp.getNext());
					temp.setNext(head.getNext());
					head.setNext(temp);
					find=true;
					break;
				}
				g=g.getNext();
			}
			if(!find) {
				n.setNext(head.getNext());
				head.setNext(n);
				length++;
			}
			
			
		}else {
			head.setNext(n);
			length++;
		}
		check();
		return head.getNext().getKey().equals(n.getKey());
	}
	
}
