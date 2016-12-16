package EleminationArray;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

import stack.EmptyException;
//import stack.LockFreeStack;
import stack.LockFreeStackMemoryManagement;

public class EliminationBackOffStack<T> extends LockFreeStackMemoryManagement<T> {
	public static int count_pop;
	public static int count_push;

	private static class RangePolicy {
		int maxRange;
		int currentRange = 1;

		RangePolicy(int maxRange) {
			this.maxRange = maxRange;
		}

		public void recordEliminationSuccess() {
			if (currentRange < maxRange)
				currentRange++;
		}

		public void recordEliminationTimeout() {
			if (currentRange > 1)
				currentRange--;
		}

		public int getRange() {
			return currentRange;
		}
	}

	// private boolean blocking = false;
	private EliminationArray<T> eliminationArray;
	private static ThreadLocal<RangePolicy> policy;

	public EliminationBackOffStack(final int exchangerCapacity, int exchangerWaitDuration) {
		super();
		// this.blocking = blocking;
		eliminationArray = new EliminationArray<T>(exchangerCapacity, exchangerWaitDuration);
		policy = new ThreadLocal<RangePolicy>() {
			protected synchronized RangePolicy initialValue() {
				return new RangePolicy(exchangerCapacity);
			}
		};
	}
	public static ThreadLocal<Integer> freesize = new ThreadLocal<Integer>() {
		protected synchronized Integer initialValue() {
			return new Integer(0);
		}
	};

	public void push(T value) {
		RangePolicy rangePolicy = policy.get();
		Node node = new Node(value);

		while (true) {
			if (this.tryPush(node)) {
				count_push = count_push + 1;
				// System.out.println("push " +node.value);
				return;
			}

			else {
				try {
					T otherValue = eliminationArray.visit(value, rangePolicy.getRange());
					if (otherValue == null) {
						rangePolicy.recordEliminationSuccess();
						count_push = count_push + 1;
						return;
					}
				} catch (TimeoutException e) {
					rangePolicy.recordEliminationTimeout();
				}
			}
		}
	}

	public T pop() throws EmptyException {
		RangePolicy rangePolicy = policy.get();

		while (true) {
			Node returnNode = tryPop();
			if (returnNode != null) {
				// System.out.println("pop" + returnNode.value);
				if (freesize.get() <= 10){
					free(returnNode);
					freesize.set(freesize.get()+1);
				}
				count_pop = count_pop + 1;
				return returnNode.value;
			} else {
				try {
					T otherValue = eliminationArray.visit(null, rangePolicy.getRange());
					if (otherValue != null) {
						rangePolicy.recordEliminationSuccess();
						count_pop = count_pop +1;
						return otherValue;
					}
					// else if(!blocking){
					// return null;
					// }
				} catch (TimeoutException e) {
					// System.out.println("dfdsf");
					// EleminationBackOffStackTest.count_pop =
					// EleminationBackOffStackTest.count_pop - 1;
					rangePolicy.recordEliminationTimeout();
				}

				// if(!blocking){
				// return null;
				// }
			}
		}
	}
}

class EliminationArray<T> {
	private int duration = 10;
	private Random random;
	private LockFreeExchanger<T>[] exchanger;

	@SuppressWarnings("unchecked")
	EliminationArray(int capacity, int duration) {
		this.duration = duration;
		exchanger = (LockFreeExchanger<T>[]) new LockFreeExchanger[capacity];
		for (int i = 0; i < capacity; i++) {
			exchanger[i] = new LockFreeExchanger<T>();
		}
		random = new Random();
	}

	public T visit(T value, int range) throws TimeoutException {
		int slot = random.nextInt(range);
		return (exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS));
	}
}

class LockFreeExchanger<T> {
	static final int EMPTY = 0, WAITING = 1, BUSY = 2;
	AtomicStampedReference<T> slot = new AtomicStampedReference<T>(null, 0);
	public T exchange(T myItem, long timeout, TimeUnit unit) throws TimeoutException {
		long nanos = unit.toNanos(timeout);
		long timeBound = System.nanoTime() + nanos;
		int[] stampHolder = { EMPTY };

		while (true) {
			if (System.nanoTime() > timeBound)
				throw new TimeoutException();
			T yrItem = slot.get(stampHolder);
			int stamp = stampHolder[0];
			switch (stamp) {
			case EMPTY:
				if (slot.compareAndSet(yrItem, myItem, EMPTY, WAITING)) {
					while (System.nanoTime() > timeBound) {
						yrItem = slot.get(stampHolder);
						if (stampHolder[0] == BUSY) {
							slot.set(null, EMPTY);
							return yrItem;
						}
					}
					if (slot.compareAndSet(myItem, null, WAITING, EMPTY))
						throw new TimeoutException();
					else {
						yrItem = slot.get(stampHolder);
						slot.set(null, EMPTY);
						return yrItem;
					}
				}
				break;

			case WAITING:
				if (slot.compareAndSet(yrItem, myItem, WAITING, BUSY))
					return yrItem;
				break;

			case BUSY:
				break;

			default:
			}
		}
	}
}