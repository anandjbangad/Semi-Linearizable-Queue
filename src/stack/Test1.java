package stack;


public class Test1 {
	public static int THREADS =6;
	public static int count =0;
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException{
		
	//	LockFreeTest[] stack_threads = new LockFreeTest[THREADS];
		LockFreeStackMMTest[] stack_threads1 = new LockFreeStackMMTest[THREADS];
		for(int i=0;i<THREADS;i++){
			stack_threads1[i] = new LockFreeStackMMTest();
		}
		for(int i=0; i<THREADS;i++){
			stack_threads1[i].start();
		}
		
	}
}