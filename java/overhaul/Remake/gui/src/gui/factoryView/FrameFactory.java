package gui.factoryView;

import intermediaryControl.Controller;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle.ComponentPlacement;

public class FrameFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private FrameFactory() {
		
	}
	
	public static void createMainFrame(Container host, Controller controlObj) {
		GroupLayout layout = new GroupLayout(host);
		
		final JRadioButton rdbtnEncrypt = RadioButtonFactory.createEncryptionRadioButton(controlObj);
		final JProgressBar progressBar = ProgressBarFactory.createProgressBar(controlObj);
		final JPasswordField passwordField = PasswordFieldFactory.createPasswordField(controlObj);
		final JSplitPane splitPane = SplitPaneFactory.createSplitPane(controlObj);
		
		final JLabel lblProgress = LabelFactory.createProgressLabel();
		final JLabel lblPassword = LabelFactory.createPasswordLabel();
		
		final JButton startButton = ButtonFactory.createStartButton(controlObj, passwordField);
		final JButton stopButton = ButtonFactory.createStopButton(controlObj);
		final JButton pauseButton = ButtonFactory.createPauseButton(controlObj);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblProgress)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
						.addContainerGap())
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblPassword)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
						.addComponent(rdbtnEncrypt)
						.addContainerGap())
					.addGroup(layout.createSequentialGroup()
						.addGap(211)
						.addComponent(startButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(stopButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(pauseButton)
						.addContainerGap(211, Short.MAX_VALUE))
			);
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
						.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
						.addGap(12)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(startButton)
							.addComponent(stopButton)
							.addComponent(pauseButton))
						.addGap(12)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPassword)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addComponent(rdbtnEncrypt))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(Alignment.TRAILING)
							.addComponent(lblProgress)
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap())
			);
		
		host.setLayout(layout);
	}

}
