package queue;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import queue.SemiLinearizable;

public class ThroughPut {
	public static int THREADS;
	// public static final int operations = 1000;
	public volatile static long operation1 = 0;
	public volatile static long operation2 = 0;
	public static boolean time_check1 = true;
	public static boolean time_check2 = true;
	public static ConcurrentLinkedQueue<Integer> CLQinstance = new ConcurrentLinkedQueue<Integer>();
	public static SemiLinearizable<Integer> SLQinstance = new SemiLinearizable<Integer>();

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
			THREADS = Integer.parseInt(args[0]);
		Thread[] CLQ = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++) {
			CLQ[i] = new AddRemove();
		}
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < THREADS; i++) {
			CLQ[i].start();
			
		}
		while (System.currentTimeMillis() - start1 <= 1000);
		time_check1 = false;
		for (int i = 0; i < THREADS; i++) {
			CLQ[i].join();

		}
		System.out.println("ConcurrentLinkedQueue --> Number Of Operations are " + operation1);
		// System.out.println("");
		// System.out.println("Semi or Quasi Linearizable Queue");
		Thread[] SLQ = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++) {
			SLQ[i] = new AddRemoveSLQ();
		}
		long start2 = System.currentTimeMillis();
		for (int i = 0; i < THREADS; i++) {
			
			SLQ[i].start();
		}
		while (System.currentTimeMillis() - start2 <= 1000);
		time_check2 = false;
		for (int i = 0; i < THREADS; i++) {
			CLQ[i].join();
		}
		System.out.println("SemiLinearizableQueue --> Number Of Operations are " + operation2);
	}

	static class AddRemove extends Thread {
		public void run() {
			while (time_check1) {
				CLQinstance.add(ThreadLocalRandom.current().nextInt(0, 10));
				operation1 = operation1 + 1;
				CLQinstance.poll();
				operation1 = operation1 + 1;
			}
		}
	}

	static class AddRemoveSLQ extends Thread {
		public void run() {
			while (time_check2) {
				try {
					SLQinstance.enq(ThreadLocalRandom.current().nextInt(0, 10));
					operation2 = operation2 + 1;
					SLQinstance.deq();
					operation2 = operation2 + 1;
				} catch (EmptyException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}
	}
}
