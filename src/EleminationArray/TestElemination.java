package EleminationArray;

import stack.LockFreeTest;
import EleminationArray.EleminationBackOffStackTest;
import EleminationArray.EliminationBackOffStack;

public class TestElemination {
	public static int THREADS ;
	public static int operations;
	public static int delay;
	public static int time_out;
	public static int size_e;
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException{
		THREADS = Integer.parseInt(args[0]);
		delay = Integer.parseInt(args[1]);
		operations = Integer.parseInt(args[2]);
		time_out = Integer.parseInt(args[3]);
		size_e = Integer.parseInt(args[4]);
		EleminationBackOffStackTest[] stack_threads = new EleminationBackOffStackTest[THREADS];
		for(int i=0;i< THREADS;i++){
			stack_threads[i] = new EleminationBackOffStackTest();
		}
		for(int i=0; i< THREADS; i++){
		//	start[i] = System.currentTimeMillis();
			stack_threads[i].start();
		//	complete[i] = System.currentTimeMillis();
		}
		
		for(int i=0;i<THREADS;i++){
			stack_threads[i].join();
			if(i==THREADS - 1){
			System.out.println("Elapsed time " + stack_threads[i].getElapsedTime() + " ms");
		}
		}
//		for(int i=0;i<THREADS;i++){
//			System.out.println("Time for THREAD " + i  +" "  +(complete[i] - start[i]));
//		}
																																																																																																																																																																																																												EliminationBackOffStack.count_push = THREADS * operations;
		System.out.println("Push " + EliminationBackOffStack.count_push + " Pop "+ EliminationBackOffStack.count_pop +" "+ "In Stack "+ ( EliminationBackOffStack.count_push - EliminationBackOffStack.count_pop));
		//System.out.println("No. Of Pop Operations " + EleminationBackOffStackTest.count_pop);
}
}