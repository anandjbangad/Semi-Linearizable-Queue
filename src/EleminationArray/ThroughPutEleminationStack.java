package EleminationArray;





public class ThroughPutEleminationStack {
	public static int THREADS ;
	public static int operations;
	public static int delay;
	public static int time_out;
	public static int size_e;
	public static boolean time_check1 = true;
	public static volatile long throughput;
	public static void main(String[] args)throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
		THREADS = Integer.parseInt(args[0]);
		//delay = Integer.parseInt(args[1]);
		//operations = Integer.parseInt(args[2]);
		time_out = Integer.parseInt(args[1]);
		size_e = Integer.parseInt(args[2]);
		ThroughPutTest[] stack_threads = new ThroughPutTest[THREADS];
		for(int i=0;i< THREADS;i++){
			stack_threads[i] = new ThroughPutTest();
		}
		long start1 = System.currentTimeMillis();
		for(int i=0; i< THREADS; i++){
		//	start[i] = System.currentTimeMillis();
			stack_threads[i].start();
		//	complete[i] = System.currentTimeMillis();
		}
		while (System.currentTimeMillis() - start1 <= 1000);
		time_check1 = false;
		for(int i=0; i<THREADS;i++){
			stack_threads[i].join();
		}
		System.out.println("EleminationStack Number of Operations "+ ThroughPutTest.ops);
	}
	
}