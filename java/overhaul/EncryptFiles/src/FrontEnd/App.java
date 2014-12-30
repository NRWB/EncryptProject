package FrontEnd;

import java.awt.EventQueue;

import javax.swing.UIManager;

// https://docs.oracle.com/cd/E29587_01/PlatformServices.60x/ps_rel_discovery/src/crd_advanced_jvm_heap.html
//http://docs.oracle.com/javase/7/docs/api/javax/swing/JList.html
//http://docs.oracle.com/javase/tutorial/uiswing/concurrency/interim.html
//http://stackoverflow.com/questions/26789125/start-another-thread-after-swingworker-completes-in-java

public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					window window = new window();
					window.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}