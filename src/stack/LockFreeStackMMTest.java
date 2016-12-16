package stack;
import java.util.EmptyStackException;
import java.util.concurrent.ThreadLocalRandom;


public class LockFreeStackMMTest extends Thread {
	 LockFreeStackMemoryManagement<Integer> instance = new LockFreeStackMemoryManagement<Integer>();
	//int operation;
	 public void run(){
		
		 
		 try {
			instance.push(ThreadLocalRandom.current().nextInt(0,6));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		 try {
			instance.pop();
		} catch (EmptyException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

}
}
