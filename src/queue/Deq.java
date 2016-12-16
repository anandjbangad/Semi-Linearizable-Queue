package queue;
import java.util.concurrent.*;
public class Deq extends Thread {
	 
	   ConcurrentLinkedQueue<String> queue;
	   Deq(ConcurrentLinkedQueue<String> queue){
	      this.queue = queue;
	   }
	   public void run() {
	      String str;
	     System.out.println("");
	      //for (int x = 0; x < 10; x++) {
	         while ((str= queue.poll()) != null) {
	            System.out.print(" "+ str+ " ");
	         }
	         
	      //}
	   }
	}