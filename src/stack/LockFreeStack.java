package stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> {
AtomicReference<Node> top = new AtomicReference<Node>(null);
static final int MIN_DELAY = 0;
static final int MAX_DELAY = 2;
Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);;

	protected boolean tryPush(Node node){
		Node oldTop = top.get();
		node.next = oldTop;
		//System.out.println(node.value);
		return(top.compareAndSet(oldTop, node));
		}
	public void push(T value) throws InterruptedException{
		//System.out.println(value);
		Node node = new Node(value);
		while (true) {
		if (tryPush(node)) {
			System.out.println("push "+ node.value);
			//System.out.println(top.get());
		return;
		} else {
			
		backoff.backoff();
		}
		}
		}
	public class Node {
		public T value;
		public Node next;
		public Node(T value) {
		this.value = value;
		this.next = null;
		}
		}
	protected Node tryPop() throws EmptyException {
		
		Node oldTop = top.get();
		//System.out.println(top.get());
		if (oldTop == null) {
			//System.out.println("sadsaddsa");
		throw new EmptyException();
		}
		Node newTop = oldTop.next;
		if (top.compareAndSet(oldTop, newTop)) {
		return oldTop;
		} 
		else {
		return null;
		}
		}
	    public T pop() throws EmptyException, InterruptedException {
	    	while (true) {
	    	Node returnNode = tryPop();
	    	//System.out.println("dskadskasd");
	    	//System.out.println(returnNode.value);
	    	if (returnNode != null) {
	    	//System.out.println("dfkldfskdfskldfs");
	    	System.out.println("pop " + returnNode.value);
	    	return returnNode.value;
	    	} 
	    	else {
	    	backoff.backoff();
	    	}
	    	}
			}
}
