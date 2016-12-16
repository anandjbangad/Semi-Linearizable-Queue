package queue;

import java.util.concurrent.*;

public class Test {
	public static int THREADS = 3;
	public static int operations =10;
	public static void main(String[] args) {
		SemiLinearizable<Integer> instance = new SemiLinearizable<Integer>();
		ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	//	Enq[] enq_threads = new Enq[THREADS];
		Deq[] deq_threads = new Deq[THREADS];
		SemiLinearizableTest[] semi_threads = new SemiLinearizableTest[THREADS];
	//	enq_threads[0] = new Enq(queue);
	//	enq_threads[0].start();
		for(int i=0; i< THREADS * operations; i++){
			instance.enq(i);
		}
		for (int i = 0; i < THREADS; i++) {

			deq_threads[i] = new Deq(queue);
		}
		for (int i = 0; i < THREADS; i++) {
			// enq_threads[i].start();
			deq_threads[i].start();
		}
		// for(int i=0; i<10;i++){
		// instance.enq(i);
		// }
		System.out.println("");
		for (int i = 0; i < THREADS; i++) {
			semi_threads[i] = new SemiLinearizableTest();
		}
		for (int i = 0; i < THREADS; i++) {
			semi_threads[i].start();
		}
	}
}
