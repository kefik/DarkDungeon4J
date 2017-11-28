package cz.dd4j.ui.gui.utils;

public class BusyWait {

	private IWaiting waiting;

	public BusyWait(IWaiting waiting) {
		this.waiting = waiting;
	}
	
	public void busyWait() {
		// BUSY WAITING
		while (waiting.isWaiting()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
}
