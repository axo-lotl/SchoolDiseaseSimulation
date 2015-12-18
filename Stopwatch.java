package mainpackage;

public class Stopwatch {
	
	long start;
	
	public Stopwatch() {
		start = System.currentTimeMillis();
	}
	
	public void reset() {
		start = System.currentTimeMillis();
	}
	
	public long getTimeElapsed() {
		return System.currentTimeMillis() - start;
	}
}
