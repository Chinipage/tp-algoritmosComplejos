package xqtr.util;

public class Support {
	
	public static void setTimeout(int delay, Runnable runnable) {
		
	    new Thread(() -> {
	        try {
	            Thread.sleep(delay);
	            runnable.run();
	        } catch (Exception e) {
	            System.err.println(e);
	        }
	    }).start();
	}
}
