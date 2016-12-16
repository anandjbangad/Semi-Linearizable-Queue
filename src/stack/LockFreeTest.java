package stack;
import java.util.EmptyStackException;
import java.util.concurrent.ThreadLocalRandom;


public class LockFreeTest extends Thread {
	 LockFreeStack<Integer> instance = new LockFreeStack<Integer>();
	//int operation;
	 public void run(){
		//operation =  ThreadLocalRandom.current().nextInt(0,2);
		//System.out.println(Thread.currentThread().getId());
//		if(Thread.currentThread().getId() %2 ==0 ){
//			try {
//				instance.push(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//			}
//		}
//		else {
//			try {
//			//System.out.println("sdakmlsdaklsadkdsaksad");
//			instance.pop();
//			} catch (EmptyException | InterruptedException e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//			}
//		}

		 
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
