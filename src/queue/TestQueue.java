package queue;

import java.util.concurrent.ConcurrentLinkedQueue;



public class TestQueue {
	public static int THREADS;
	public static int operations;
   // public static ConcurrentLinkedQueue<Integer> CLQinstance = new ConcurrentLinkedQueue<Integer>();
    public static SemiLinearizable<Integer> SLQinstance = new SemiLinearizable<Integer>();
	public static void main(String[] args)throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
		THREADS = Integer.parseInt(args[0]);
		operations = Integer.parseInt(args[1]);
		System.out.println("");
		System.out.println("Semi or Quasi Linearizable Queue");
		for(int i=0; i< THREADS * operations; i++){
			SLQinstance.enq(i);
		}
		Thread[] SLQ = new Thread[THREADS];
		for(int i=0; i< THREADS; i++){
			SLQ[i] = new AddRemoveSLQ();
		}
		for(int i=0; i<THREADS; i++){
			SLQ[i].start();
		}
		
	}



	static class AddRemoveSLQ extends Thread{
		public void run(){
			for(int i=0; i< operations; i++){
				try {
					System.out.print(" "+SLQinstance.deq()+" ");
				} catch (EmptyException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}
}
