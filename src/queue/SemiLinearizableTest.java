package queue;
import queue.SemiLinearizable;
public class SemiLinearizableTest extends Thread {
	SemiLinearizable<Integer> instance = new SemiLinearizable<Integer>();
	public void run(){
		if(Thread.currentThread().getId() == 10){
			//System.out.println("here");
			for(int i=0; i<TestQueue.THREADS * TestQueue.operations; i++)
				instance.enq(i);
		}
		try {
			//System.out.println("sadkdas");
			for(int i=0; i<100; i++){
			instance.deq();}
		} catch (EmptyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		}
}
