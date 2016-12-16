package EleminationArray;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadLocalRandom;
import EleminationArray.EliminationBackOffStack;
import stack.Backoff;
import stack.EmptyException;
import EleminationArray.TestElemination;

import EleminationArray.ThroughPutEleminationStack;
public class ThroughPutTest extends Thread {
	public static volatile int ops;
	EliminationBackOffStack<Integer> instance = new EliminationBackOffStack<Integer>(TestElemination.size_e,TestElemination.time_out);
	public void run(){
		while(ThroughPutEleminationStack.time_check1){
			instance.push(ThreadLocalRandom.current().nextInt(0,100));
			ops = ops + 1;
			//System.out.println(ops);
			try {
				instance.pop();
			} catch (EmptyException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			ops = ops + 1;
		}
	}
}
