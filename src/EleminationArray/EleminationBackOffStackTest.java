package EleminationArray;

import java.util.concurrent.ThreadLocalRandom;
import EleminationArray.EliminationBackOffStack;
import stack.Backoff;
import stack.EmptyException;
import EleminationArray.TestElemination;


public class EleminationBackOffStackTest extends Thread {
	EliminationBackOffStack<Integer> instance = new EliminationBackOffStack<Integer>(TestElemination.size_e,TestElemination.time_out);
	static final int MIN_DELAY = 1;
	static final int MAX_DELAY = 3;
	Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);
	private long elapsed;
	public static volatile int descide;
	
	public void run(){
		long start = System.currentTimeMillis();
		for(int i=0; i<TestElemination.operations; i++){
		descide = ThreadLocalRandom.current().nextInt(0,2);
		//if(descide ==0){
		instance.push(ThreadLocalRandom.current().nextInt(0,100));
		
		//count_push = count_push + 1;

		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(0,TestElemination.delay));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
		//}
		//if(descide ==1){
			
		try {
			instance.pop();
		} catch (EmptyException e2) {
			// TODO Auto-generated catch block
			//e2.printStackTrace();
		}
		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(0,TestElemination.delay));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	//count_pop = count_pop + 1;

		//}
	}
		long end = System.currentTimeMillis();
		elapsed = end - start;
	}
	public long getElapsedTime() {
		return elapsed;
	}
}
