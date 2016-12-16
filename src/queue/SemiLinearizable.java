/*
 * LockFreeQueue.java
 *
 * Created on December 29, 2005, 2:05 PM
 *
 * The Art of Multiprocessor Programming, by Maurice Herlihy and Nir Shavit.
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 20065 Elsevier Inc. All rights reserved.
 */

package queue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

import queue.SemiLinearizable.Node;
import queue.TestQueue;

/**
 * Lock-free queue. Based on Michael and Scott
 * http://doi.acm.org/10.1145/248052.248106
 * 
 * @param T
 *            item type
 * @author Maurice Herlihy
 */
public class SemiLinearizable<T> {
	public final int tries = 5;
	public int n = 3;
	private AtomicReference<Node> head;
	private AtomicReference<Node> tail;
	private AtomicBoolean marked;
	Integer[] arr = new Integer[n];

	public SemiLinearizable() {
		Node sentinel = new Node(null);
		this.head = new AtomicReference<Node>(sentinel);
		this.tail = new AtomicReference<Node>(sentinel);
	}

	/**
	 * Append item to end of queue.
	 * 
	 * @param item
	 */
	public void enq(T value) {
		Node node = new Node(value);
		while (true) {
			Node last = tail.get();
			Node next = last.next.get();
			if (last == tail.get()) {
				if (next == null) {
					if (last.next.compareAndSet(next, node)) {
						tail.compareAndSet(last, node);
						return;
					}
				} else {
					tail.compareAndSet(last, next);
				}
			}
		}
	}

	/**
	 * Remove and return head of queue.
	 * 
	 * @return remove first item in queue
	 * @throws queue.EmptyException
	 */
	public T deq() throws EmptyException {
		// System.out.println(n);
		// System.out.println("Reached Dequeue");
		int retries = 0;
		while (true) {
			Node first = head.get();
			Node last = tail.get();
			Node copy_first = head.get();
			Node next = first.next.get();
			// System.out.println(next.value);
			if (first == head.get()) {
				if (first == last) {
					if (next == null) {
						// System.out.println("is empty");
						throw new EmptyException();
					} else {
						//System.out.println("reached here");
						tail.compareAndSet(last, next);
					}

				} else {
					// System.out.println("trying random index");
					int randomIndex = 0;
					if (retries < tries) {
						Random r = new Random();
//					    for (int i = 0; i < arr.length; i++) {
//					        arr[i] = i;
//					    }
//					    String b = Arrays.toString(arr);
//					    randomIndex = Integer.parseInt(b);
						Set<Integer> uniqueNumbers = new HashSet<>();
						while (uniqueNumbers.size()<n){
						    uniqueNumbers.add(r.nextInt(n));
						}
						int size = uniqueNumbers.size();
						
						randomIndex = ThreadLocalRandom.current().nextInt(0,n);

						retries = retries + 1;

						if (randomIndex > 0) {
							T value = ARDQ(randomIndex, next);
							if (value != null) {
								// System.out.print(" " + value + " ");
								return value;
							}
						}
						if (next != null && next.marked.get() == true) {
							if (next != last && head.compareAndSet(first, next)) {
								first = next;
								next = next.next.get();
							}
							if (next == null) {
								return null;
							}
							if (next.marked.compareAndSet(false, true)) {
								return next.value;
							}
						}

					} 
					else {
						if (first == head.get()) {
							if (first == last) {
								if (next == null) {
									throw new EmptyException();
								}
								tail.compareAndSet(last, next);
							} else {
								T value = next.value;
								if (head.compareAndSet(first, next)) {
									return value;
								}
							}
						}
					}

				}
			}
		}
	}

	
	
	
	
		public T ARDQ(int randomIndex, Node next) {
			int i = 0;
			while (i < randomIndex) {
				if (next.next.get() == null) {
					break;
				}
				next = next.next.get();
				i = i + 1;
			}
			if (next.marked.compareAndSet(false, true)) {
				return next.value;
			} else {
				return null;
			}
		}
	
		
		
	
	
	
	public class Node {
		public T value;
		public AtomicReference<Node> next;
		public AtomicBoolean marked = new AtomicBoolean();
		
		public Node(T value) {
			this.value = value;
			this.next = new AtomicReference<Node>(null);
			this.marked.set(false);
		}
	}

}
