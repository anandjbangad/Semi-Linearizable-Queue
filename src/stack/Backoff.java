package stack;

import java.util.Random;

public class Backoff {
		final int minDelay, maxDelay;
		int limit;
		final Random random;
		public Backoff(int min, int max) {
		this.minDelay = min;
		this.maxDelay = max;
		this.limit = minDelay;
		random = new Random();
		}
		public void backoff() throws InterruptedException {
		int delay = random.nextInt(limit);
		limit = Math.min(maxDelay, 2 * limit);
		Thread.sleep(delay);
		}
		}

