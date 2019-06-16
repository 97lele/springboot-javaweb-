package com.gdut.xg.shop.util.LRU;

import lombok.Data;

import java.util.List;

@Data
public class Node<K,G> {

	private K key;
	private List<G> data;
	private Node<K,G> next;
	public Node(K k,List<G> d) {
		this.data=d;
		this.key=k;
	}
	

	
	
}
