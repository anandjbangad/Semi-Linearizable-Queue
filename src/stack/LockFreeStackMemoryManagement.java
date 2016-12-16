package stack;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import stack.LockFreeStack.Node;

public class LockFreeStackMemoryManagement<T> {
private AtomicStampedReference<Node> top = new AtomicStampedReference<Node>(null, 0);
static final int MIN_DELAY = 1;
static final int MAX_DELAY = 10;
Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);

ThreadLocal<Node> freeList = new ThreadLocal<Node>() {
    protected Node initialValue() { return null; };
  };
  


private Node allocate(T value) {
	    int[] stamp = new int[1];
	    Node node = freeList.get();
	    if (node == null) { // nothing to recycle
	      node = new Node(value);
	    } else {            // recycle existing node
	      freeList.set(node.next.get(stamp));
	    }
	    // initialize
	    node.value = value;
	    return node;
	  }

protected void free(Node node) {
	    Node free = freeList.get();
	    node.next = new AtomicStampedReference<Node>(free, 0);
	    freeList.set(node);
	  }
  
  


	protected boolean tryPush(Node node){
		int[] stamp = new int[1];
		Node oldTop = top.getReference();
		node.next.set(oldTop,stamp[0]);
		//System.out.println(node.value);
		return(top.compareAndSet(oldTop, node,stamp[0],stamp[0]+1)); 
		}
	public void push(T value) throws InterruptedException{
		//System.out.println(value);
		Node node = allocate(value);
		
		while (true) {
		if (tryPush(node)) {
			System.out.println("push " + node.value);
			//System.out.println(top.get());
		return;
		} else {
			
		backoff.backoff();
		}
		}
		}
	public class Node {
		public T value;
		public AtomicStampedReference<Node> next;
		public Node(T value) {
		this.value = value;
		this.next  = new AtomicStampedReference<Node>(null, 0);
		}
		}
	protected Node tryPop() throws EmptyException {
		int[] stamp = new int[1];
		int[] newstamp = new int[1];
		Node oldTop = top.get(stamp);
		//System.out.println(top.get());
		if (oldTop == null) {
			//System.out.println("sadsaddsa");
		throw new EmptyException();
		}
		Node newTop = oldTop.next.get(newstamp);
		if (top.compareAndSet(oldTop, newTop,stamp[0],newstamp[0])) {
		
			//top compared with old top if equal then change to new top
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
	    	T  p = returnNode.value;
	    	free(returnNode);
	    	//count_pop = count_pop + 1;
	    	return p;
	    	} 
	    	else {
	    	
	    	backoff.backoff();
	    	}
	    	}
			}
}
