import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import java.util.Random; 
import java.util.*;
import java.util.concurrent.Callable;    
    
import java.util.concurrent.ExecutorService;    
import java.util.concurrent.Executors;    


class Main {
	private static int[] arr = new int[0];
	private static Random rand = new Random();

	private static void generateArray() {
		int length = 10_000_000;
		Main.arr = new int[length];
		for (int i = 0; i < length; i += 1) {
			Main.arr[i] = rand.nextInt(100);
		}
	}

	private static int[] getArray() {
		if (Main.arr.length == 0) {
			Main.generateArray();
		}
		int[] arr = new int[Main.arr.length];
		System.arraycopy(Main.arr, 0, arr, 0, Main.arr.length);
		return arr;
	}	

	public static void main(String[] args) {
		MyArray service = new MyArray(Main.getArray());

		long startTime = System.currentTimeMillis();

		int result = Main.process(service);

		long endTime = System.currentTimeMillis();

		System.out.println("Threaded result = " + result);
		System.out.println("Threaded calculation time: " + (endTime-startTime) + "ms (" + (endTime-startTime)/1000 + "s)"); 

		System.out.println();

		System.out.println("Simple calculation result = " + new ValidateSum(Main.getArray()).getSum());
	}

	public static int process(MyArray array) {
		MyPool pool = new MyPool(array, Runtime.getRuntime().availableProcessors());
		return pool.process();
	}
}

class ValidateSum {
	private int[] nums;
	public ValidateSum(int[] nums) {
		this.nums = nums;
	}

	public int getSum() {
		int sum = 0;
		for (int i = 0; i < this.nums.length; i += 1) {
			sum += this.nums[i];
		}

		return sum;
	}
}

class MyArray {
	private int[] arr;

	public MyArray(int[] arr) {
		this.arr = arr;
	}

	public void setElement(int idx, int num) {
		this.arr[idx] = num;
	}

	public int getElement(int idx) {
		return this.arr[idx];
	}
	
	public int getLength() {
		return this.arr.length;
	}

	public int[] getArray() {
		return this.arr;
	}
}


class MyPool {
	private MyArray myArr;
	ExecutorService executor;

	public MyPool(MyArray myArr, int nThreads) {
		this.myArr = myArr;
		System.out.println(nThreads + " threads");
		this.executor = Executors.newFixedThreadPool(nThreads);
	}

	public int process() {
		int cnt = 1;

		while (this.myArr.getLength() != 1) {

			long timerStart1 = System.currentTimeMillis();
			ArrayList<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

			int pairs = this.myArr.getLength() / 2;
			for (int i = 0; i < pairs; i++) {
	            tasks.add(Executors.callable(new MyThread(i, this.myArr)));
	        }

	        try {
	        	this.executor.invokeAll(tasks);
	        } catch (Exception e) {
	        	System.out.println(e);
	        	return -1;
	        }
	    

	        int oldLength = this.myArr.getLength();
	        this.rebuild();
        }

        return this.myArr.getElement(0);
	}

	public void rebuild() {
		boolean lengthIsEven = this.myArr.getLength() % 2 == 0;

		int half = this.myArr.getLength() / 2;
		int length = lengthIsEven ? half : half + 1;

		int[] copy = new int[length];
		System.arraycopy(this.myArr.getArray(), 0, copy, 0, length);

		this.myArr = new MyArray(copy);
	}
}

class MyThread implements Runnable {
	private int idx;
	private MyArray myArr;

	public MyThread(int idx, MyArray myArr) {
		this.idx = idx;
		this.myArr = myArr;
	}
	
	@Override
	public void run() {
		int[] data = this.getPair();
		myArr.setElement(this.idx, data[0] + data[1]);
	}

	public int getOppositeIdx() {
	 	return this.myArr.getLength() - this.idx - 1;
	 }

	public int[] getPair() {
		int[] pair = new int[]{
			this.myArr.getElement(this.idx),
			this.myArr.getElement(this.getOppositeIdx()),
		};

		return pair;
	}

}
