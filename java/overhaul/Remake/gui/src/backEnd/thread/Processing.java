package backEnd.thread;

public class Processing extends Thread {
	
	public Processing(Runnable task, String processName) {
		super(task, processName);
	}
	
	public void init() {
		
	}
	

}
