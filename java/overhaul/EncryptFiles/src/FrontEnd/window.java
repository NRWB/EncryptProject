package FrontEnd;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingWorker;

import BackEnd.CryptTomb;
import BackEnd.CryptTombPassage;
import Utils.IOLib;

public class window implements WindowConsts {
	
	private boolean optRecursion;
	private int lvlRecursion;
	private List<Path> userSelectedFiles;
	private Path outputFolderPath;
	private boolean delAfterProcessed;
	
	private boolean doEncrypt; // by default doEncrypt is set to true

	private JFrame frame;
	
	private JPasswordField usersPasswordTextField;
	
	private JProgressBar prog;

	/**
	 * Making the initial window gui object will only create the base frame.
	 */
	public window() {
		createBase();
	}
	
	// http://stackoverflow.com/questions/8193801/how-to-set-specific-window-frame-size-in-java-swing
	private void createBase() {
		frame = new JFrame(APP_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(DEFAULT_WINDOW_W, DEFAULT_WINDOW_H);
		frame.setPreferredSize(new Dimension(DEFAULT_WINDOW_W, DEFAULT_WINDOW_H));
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	/**
	 * Wrapper method
	 */
	public void init() {
		initialize();
	}
	
	private void initialize() {
		initVars();
		
		// checked, done, verified. Move on;
		frame.setJMenuBar(createMenuBar());
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = createMasterGroupLayout(frame.getContentPane(), panel);
		
		// create the "tabbedPane" within the "panel" ("panel" made several lines above)
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE));
		
		// this panel holds the split pane that the ["Files To Use"] tab and the ["BASIC SETTINGS"] tab reside under
		JPanel homeTab = new JPanel();
		tabbedPane.addTab("Home", null, homeTab, null);
		
		// this pane divides / splits the ["Files To Use"] tab from the ["BASIC SETTINGS"] tab
		JSplitPane splitPane = new JSplitPane();
		GroupLayout gl_panel_1 = new GroupLayout(homeTab);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE).addGap(22)));
		
		// This panel holds the left side of the split pane, the ["Files To Use"] section
		JPanel fileListViewPanel = new JPanel();
		splitPane.setLeftComponent(fileListViewPanel);
		
		GroupLayout groupLayoutPanel = createFileViewingGroupLayout(fileListViewPanel);
		fileListViewPanel.setLayout(groupLayoutPanel);
		
		JPanel basicSettings = new JPanel();
		splitPane.setRightComponent(basicSettings);
		
		GroupLayout groupBasicSettingsLayoutPanel = createBasicSettingsLayout(basicSettings);
		basicSettings.setLayout(groupBasicSettingsLayoutPanel);
		
		splitPane.setResizeWeight(0.5d);
		
		homeTab.setLayout(gl_panel_1);
		
		JPanel advancedSettingsTab = new JPanel();
		tabbedPane.addTab("Settings", null, advancedSettingsTab, null);
		
		GroupLayout groupAdvancedSettings = createAdvancedSettingsLayout(advancedSettingsTab);
		advancedSettingsTab.setLayout(groupAdvancedSettings);
		
		JPanel runProgramTab = new JPanel();
		tabbedPane.addTab("Run", null, runProgramTab, null);
		
		GroupLayout runProgramTabLayout = createRunProgramLayout(runProgramTab);
		runProgramTab.setLayout(runProgramTabLayout);
		
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	private void initVars() {
		optRecursion = false;
		lvlRecursion = 0;
		userSelectedFiles = new ArrayList<Path>();
		doEncrypt = true;
	}
	
	// -----------------------------------------------------------------------
	// Private methods to create GUI components.
	// Meant to provide ease of access in editing individual components.
	// -----------------------------------------------------------------------
	
	/**
	 * - Could be useful to further break down the sub-components for greater detail (later on, when implementing extra features)
	 * @return
	 */
	private JMenuBar createMenuBar() {
		JMenuBar result = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		result.add(fileMenu);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		return result;
	}
	
	// =======================================================================
	
	/**
	 * +++ TAG 1
	 * - The master group layout that sits at the very back of the gui, directly against/on the frame component.
	 * @param hostingModel
	 * @return
	 */
	private GroupLayout createMasterGroupLayout(Container hostingModel, JPanel canvasPanel) {
		GroupLayout layout = new GroupLayout(hostingModel); // the master group layout for the frame
		
		prog = createProgressBar();
		JLabel progLabel = createProgressBarLabel("Progress:");
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addComponent(canvasPanel,
							Alignment.TRAILING,
							GroupLayout.DEFAULT_SIZE,
							434,
							Short.MAX_VALUE)
			.addGroup(
				layout.createSequentialGroup()
				.addGap(29)
				.addComponent(progLabel)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(prog,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
				.addGap(94)
			)
		);

		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				layout.createSequentialGroup()
				.addComponent(canvasPanel,
								GroupLayout.DEFAULT_SIZE,
								193,
								Short.MAX_VALUE)
				.addGap(12)
				.addGroup(
					layout.createParallelGroup(Alignment.LEADING)
					.addComponent(prog,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
					.addComponent(progLabel)
				)
				.addGap(11)
			)
		);
		
		return layout;
	}
	
	/**
	 * +++ TAG 1
	 * @return
	 */
	private JProgressBar createProgressBar() {
		JProgressBar result = new JProgressBar(0, 100); // sets the progress bar to be between 0 and 100 percent
		result.setValue(0); // begin at 0
		result.setStringPainted(true); // show the text representation of the current % (percentage complete)
		result.setToolTipText("<html>Monitors the progress of the<br>"
				+ "encryption or decryption process.<br>"
				+ "Uses the formula of (summation of<br>"
				+ "bytes over each selected file, or the<br>"
				+ "total bytes of a single file) divided<br>"
				+ "by (current number of bytes processed)</html>");
		return result;
	}
	
	// Implement, for when a person wants to HIDE the text that they type in the usersPassword TextField
	private JPasswordField createPasswordComponent() {
		JPasswordField jpf = new JPasswordField();
		jpf.setColumns(10);
		jpf.setEchoChar((char) 0); // TODO Chage to const value
		return jpf;
	}
	
	/**
	 * +++ TAG 1
	 * @param text
	 * @return
	 */
	private JLabel createProgressBarLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	// =======================================================================
	
	/**
	 * +++ TAG 2
	 * @param hostingModel
	 * @return
	 */
	private GroupLayout createFileViewingGroupLayout(Container hostingModel) {
		GroupLayout layout = new GroupLayout(hostingModel);
		
		JLabel filesToUseLabel = createFilesToUseTitleLabel("Files To Use");
		
		JTextPane fileListPane = createFileListViewingPane();
		
		JButton chooseFilesButton = createFileChoosingButton(fileListPane, "Choose Files"); // the button used to open and choose files for the fileListPane

		JScrollPane textPaneContainer = new JScrollPane();
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(textPaneContainer, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
							.addComponent(filesToUseLabel)
							.addComponent(chooseFilesButton))
						.addContainerGap())
			);
		layout.setVerticalGroup(
					layout.createParallelGroup(Alignment.TRAILING)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(filesToUseLabel)
						.addGap(7)
						.addComponent(textPaneContainer, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(chooseFilesButton))
			);
			
		textPaneContainer.setViewportView(fileListPane);
		
		return layout;
	}
	
	/**
	 * +++ TAG 2
	 * @param s
	 * @return
	 */
	private JLabel createFilesToUseTitleLabel(String s) {
		JLabel result = new JLabel(s);
		result.setFont(WindowConsts.createTitleFont());
		return result;
	}
	
	/**
	 * +++ TAG 2
	 * @param updateViewModel - the JTextPane used in this button's listener; the JTextPane is updated.
	 * @param text - displayed inside the button
	 * @return
	 */
	private JButton createFileChoosingButton(JTextPane updateViewModel, String text) {
		JButton result = new JButton(text);
		
		result.setToolTipText("<html>Open visual directory display to<br>"
				+ "select a pathname or pathnames<br>"
				+ "that will be encrypted or decrypted</html>");
		
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userSelectedFiles.clear();
				userSelectedFiles = IOLib.selectDirs(updateViewModel,
														result,
														optRecursion,
														lvlRecursion);
			}
		});
		
		return result;
	}
	
	/**
	 * +++ TAG 2
	 * @return
	 */
	private JTextPane createFileListViewingPane() {
		// the actual pane where files are viewed and ready to be encrypted/decrypted
		JTextPane result = new JTextPane();
		result.setEditable(false); // non-editable because it's only for display.
		return result;
	}
	
	// =======================================================================
	
	/**
	 * +++ TAG 3
	 */
	private GroupLayout createBasicSettingsLayout(Container hostingModel) {
		GroupLayout layout = new GroupLayout(hostingModel);
		
		JLabel basicSettingsLabel = createBasicSettingsLabel("Basic Settings");
		JLabel outputFolderLabel = createOutputFolderLabel("Output Folder:");
		ButtonGroup bg = new ButtonGroup();
		JRadioButton encryptRadioButton = createEncryptButton("Encrypt");
		encryptRadioButton.setSelected(true); // choice made to have this radio button set to TRUE by default
		JRadioButton decryptRadioButton = createDecryptButton("Decrypt");
		bg.add(encryptRadioButton);
		bg.add(decryptRadioButton);
		
		// should this be global, so the string represented pathname can be pulled from the JTextField?
		// answer: no, b/c when the button for the folder is created, the path is updated to the global Path object variable
		//         and the JTextField is updated from the "selectDir()" method
		JTextField outputFolderTextField = createOutputFolderTextField();
		JButton openOutputFolderButton = createOpenOutputFolderButton(outputFolderTextField, "Open");
		
		usersPasswordTextField = createPasswordComponent();
		// should this be global, so when checked, it will hide the password text field?
		// answer: as seen above in the outputFolder and it's text field updating, this will also create a listener
		//         given the password text field object, and updates internally.
		JCheckBox hidePasswordCheckBox = createHidePasswordCheckBox(usersPasswordTextField, "Hide Input?");
		
		JLabel passwordLabel = createEnterPasswordLabel("Password:");
		
		// Add these 2 radio button listeners here, because they need to change one another
		// unless a "ButtonGroup" does that for us... <investigate>
		encryptRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doEncrypt = encryptRadioButton.isSelected();
				System.out.println("Let's " + String.valueOf(doEncrypt) + " here in encrypt");
			}
		});
		decryptRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doEncrypt = encryptRadioButton.isSelected(); // decryptRadioButton.isSelected();
				System.out.println("Let's " + String.valueOf(doEncrypt) + " here in decrypt");
			}
		});
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(basicSettingsLabel)
				.addContainerGap(359, Short.MAX_VALUE)
			)
			.addGroup(
				Alignment.TRAILING, layout.createSequentialGroup()
				.addGap(28)
				.addGroup(
					layout.createParallelGroup(Alignment.LEADING)
					.addComponent(encryptRadioButton)
					.addComponent(decryptRadioButton)
					.addGroup(
						layout.createParallelGroup(Alignment.LEADING)
						.addGroup(
							layout.createSequentialGroup()
							.addComponent(outputFolderLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(openOutputFolderButton,
											GroupLayout.PREFERRED_SIZE,
											74,
											GroupLayout.PREFERRED_SIZE)
						)
						.addComponent(outputFolderTextField,
										GroupLayout.DEFAULT_SIZE,
										282,
										Short.MAX_VALUE)
					)
				)
				.addContainerGap()
			)
			.addGroup(
				layout.createSequentialGroup()
				.addGap(28)
				.addGroup(
					layout.createParallelGroup(Alignment.LEADING)
					.addGroup(
						layout.createSequentialGroup()
						.addComponent(usersPasswordTextField,
										GroupLayout.DEFAULT_SIZE,
										384,
										Short.MAX_VALUE)
						.addContainerGap()
					)
					.addGroup(
						layout.createSequentialGroup()
						.addComponent(passwordLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(hidePasswordCheckBox)
						.addGap(215)
					)
				)
			)
		);
		
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				Alignment.TRAILING, layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(basicSettingsLabel)
				.addGap(18)
				.addComponent(encryptRadioButton)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(decryptRadioButton)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(outputFolderLabel)
					.addComponent(openOutputFolderButton)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(outputFolderTextField,
								GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED,
								245,
								Short.MAX_VALUE)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(passwordLabel)
					.addComponent(hidePasswordCheckBox)
				)
				.addGap(4)
				.addComponent(usersPasswordTextField,
								GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
			)
		);
		
		return layout;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JLabel createOutputFolderLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JLabel createBasicSettingsLabel(String text) {
		JLabel result = new JLabel(text);
		result.setFont(WindowConsts.createTitleFont());
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JRadioButton createEncryptButton(String text) {
		JRadioButton result = new JRadioButton(text);
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JRadioButton createDecryptButton(String text) {
		JRadioButton result = new JRadioButton(text);
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JButton createOpenOutputFolderButton(JTextField updateViewModel, String text) {
		JButton result = new JButton(text);
		result.setToolTipText("<html>Open visual directory display to<br>"
				+ "select a pathname depicting<br>"
				+ "where encrypted or decrypted<br>"
				+ "result files should be located</html>");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outputFolderPath = IOLib.selectDir(updateViewModel, result);
			}
		});
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JCheckBox createHidePasswordCheckBox(JPasswordField pwField, String text) {
		JCheckBox result = new JCheckBox(text);
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (result.isSelected())
					pwField.setEchoChar('*');
				else
					pwField.setEchoChar((char) 0); // TODO - Make/Find Constant for this, instead of char cast of 0
			}
		});
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JLabel createEnterPasswordLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 3
	 */
	private JTextField createOutputFolderTextField() {
		JTextField result = new JTextField();
		result.setColumns(10);
		result.setToolTipText("<html>A pathname depicting where<br>"
				+ "encrypted or decrypted result<br>"
				+ "files should be located</html>");
		return result;
	}
	
	// =======================================================================
	
	/**
	 * +++ TAG 4
	 */
	private GroupLayout createAdvancedSettingsLayout(Container hostingModel) {
		GroupLayout layout = new GroupLayout(hostingModel);
		
		JLabel lblEncryptionSettings = createEncryptionSettingsLabel("Encryption Settings");
		JLabel lblDeleteOriginalFile = createDelOrigFileLabel("Delete original file afterwards?");
		JLabel lblCipherAlgorithm = createCipherAlgoLabel("Cipher Algorithm:");
		JLabel lblMessageDigestAlgorithm = createMessageDigestAlgoLabel("Message Digest Algorithm:");
		JLabel lblEncryptionKeyAlgorithm = createEncryptionKeyAlgoLabel("Encryption Key Algorithm:");
		JLabel lblInitializationVectorSize = createInitVectorSizeLabel("Initialization Vector Size:");
		JLabel lblInitializationVectorByte = createInitVectorByteArrLabel("Initialization Vector byte[]:");
		JLabel lblRecursive_1 = createRecursiveCheckBoxLabel("Recursive?");
		JLabel lblRecursiveDepth = createRecursiveDepthLvlLabel("Recursive Depth:");
		
		JCheckBox chckbxCheckForTrue = createDelFileCheckBox("Check to delete");
		JCheckBox chckbxCheckToUse = createUseRecursionCheckBox("Check to use recursion");
		
		JTextField cipherAlgoTextField = createCipherAlgoTextField();
		JTextField msgDigestAlgoTextField = createMsgDigestAlgoTextField();
		JTextField encryptionKeyAlgoTextField = createEncryptionKeyAlgoTextField();
		JTextField initVectorSizeTextField = createInitVectorSizeTextField();
		JTextField initVectorByteArrTextField = createInitVectorByteArrTextField();
		JTextField recursiveDepthLvlTextField = createRecursiveDepthLvlTextField(chckbxCheckToUse);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					layout.createParallelGroup(Alignment.LEADING)
					.addComponent(lblEncryptionSettings)
					.addGroup(
						layout.createSequentialGroup()
						.addGroup(
							layout.createParallelGroup(Alignment.LEADING)
							.addComponent(lblDeleteOriginalFile)
							.addComponent(lblCipherAlgorithm)
							.addComponent(lblMessageDigestAlgorithm)
							.addComponent(lblEncryptionKeyAlgorithm)
							.addComponent(lblInitializationVectorSize)
							.addComponent(lblInitializationVectorByte)
							.addComponent(lblRecursive_1)
							.addComponent(lblRecursiveDepth)
						)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
							layout.createParallelGroup(Alignment.LEADING)
							.addComponent(recursiveDepthLvlTextField,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE)
							.addComponent(chckbxCheckToUse)
							.addComponent(initVectorByteArrTextField,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE)
							.addComponent(initVectorSizeTextField,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE)
							.addComponent(encryptionKeyAlgoTextField,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE)
							.addComponent(msgDigestAlgoTextField,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE)
							.addComponent(cipherAlgoTextField,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE)
							.addComponent(chckbxCheckForTrue)
						)
					)
				)
				.addContainerGap(445, Short.MAX_VALUE)
			)
		);
		
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(lblEncryptionSettings)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblDeleteOriginalFile)
					.addComponent(chckbxCheckForTrue)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblCipherAlgorithm)
					.addComponent(cipherAlgoTextField,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblMessageDigestAlgorithm)
					.addComponent(msgDigestAlgoTextField,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblEncryptionKeyAlgorithm)
					.addComponent(encryptionKeyAlgoTextField,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblInitializationVectorSize)
					.addComponent(initVectorSizeTextField,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblInitializationVectorByte)
					.addComponent(initVectorByteArrTextField,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblRecursive_1)
					.addComponent(chckbxCheckToUse)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(lblRecursiveDepth)
					.addComponent(recursiveDepthLvlTextField,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
				)
				.addContainerGap(206, Short.MAX_VALUE)
			)
		);
		
		return layout;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createEncryptionSettingsLabel(String text) {
		JLabel result = new JLabel(text);
		result.setFont(WindowConsts.createTitleFont());
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createDelOrigFileLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createCipherAlgoLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createMessageDigestAlgoLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createEncryptionKeyAlgoLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createInitVectorSizeLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createInitVectorByteArrLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JCheckBox createDelFileCheckBox(String text) {
		JCheckBox result = new JCheckBox(text);
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delAfterProcessed = result.isSelected();
			}
		});
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JTextField createCipherAlgoTextField() {
		JTextField result = new JTextField();
		result.setColumns(10);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JTextField createMsgDigestAlgoTextField() {
		JTextField result = new JTextField();
		result.setColumns(10);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JTextField createEncryptionKeyAlgoTextField() {
		JTextField result = new JTextField();
		result.setColumns(10);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JTextField createInitVectorSizeTextField() {
		JTextField result = new JTextField();
		result.setColumns(10);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JTextField createInitVectorByteArrTextField() {
		JTextField result = new JTextField();
		result.setColumns(10);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JTextField createRecursiveDepthLvlTextField(JCheckBox masterComp) {
		JTextField result = new JTextField();
		masterComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (masterComp.isSelected())
					result.setEditable(true); // set the depth level input to editable
				else
					result.setEditable(false); // DO NOT set the depth level input to editable
			}
		});
		result.setColumns(10);
		result.setToolTipText("Enter numbers only!");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lvlRecursion = Integer.valueOf(result.getText());
			}
		});
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createRecursiveCheckBoxLabel(String text) {
		JLabel result = new JLabel(text);
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JLabel createRecursiveDepthLvlLabel(String text) {
		JLabel result = new JLabel(text);
		result.setToolTipText("<html>The number of sub directories<br>"
				+ "to go into/down from the initial<br>"
				+ "folder (enter \"-1\" numeric value for<br>"
				+ "infinite - all files located in a folder)</html>");
		return result;
	}
	
	/**
	 * +++ TAG 4
	 */
	private JCheckBox createUseRecursionCheckBox(String text) {
		JCheckBox result = new JCheckBox(text);
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optRecursion = result.isSelected();
			}
		});
		return result;
	}
	
	// =======================================================================
	
	/**
	 * +++ TAG 5
	 */
	private GroupLayout createRunProgramLayout(Container hostingModel) {
		GroupLayout layout = new GroupLayout(hostingModel);
		
		JButton btnStart = createStartButton("Start");
		JButton btnStop = createStopButton("Stop");
		JButton btnStopAndSave = createStopAndSaveButton("Stop and Save");
		JButton btnPauseOrResume = createPauseOrResumeButton("Pause or Resume");
		JLabel lblStartEncryptOr = createStartProgramSubTitleInPanelLabel("Start Encrypt or Decrypt Process");
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(Alignment.LEADING)
					.addGroup(
						layout.createSequentialGroup()
						.addGap(232)
						.addComponent(btnStart)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnStop)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnStopAndSave)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnPauseOrResume)
					)
					.addGroup(
						layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblStartEncryptOr)
					)
				)
				.addContainerGap(232, Short.MAX_VALUE)
			)
		);
		
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
			.addGroup(
				Alignment.TRAILING, layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(lblStartEncryptOr)
				.addPreferredGap(ComponentPlacement.RELATED,
								387,
								Short.MAX_VALUE)
				.addGroup(
					layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(btnStart)
					.addComponent(btnStop)
					.addComponent(btnStopAndSave)
					.addComponent(btnPauseOrResume)
				)
				.addGap(27)
			)
		);
		
		return layout;
	}
	
	/**
	 * +++ TAG 5
	 */
	private JButton createStartButton(String text) {
		JButton result = new JButton(text);
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prog.setValue(0); // start from zero when running through again
				createProcess(); // method that takes the data spec's and passes as parameters to a new process that will encrypt or decrpyt
				
				// TODO - after a run through, should it delete the password from the actual JPasswordField?
			}
		});
		return result;
	}
	
	/**
	 * +++ TAG 5
	 */
	private JButton createStopButton(String text) {
		JButton result = new JButton(text);
		// Not yet implemented.
		// will have to stop the progress bar, will have to also stop the actual process of encrypting or decrypting
		return result;
	}
	
	/**
	 * +++ TAG 5
	 */
	private JButton createStopAndSaveButton(String text) {
		JButton result = new JButton(text);
		//// Not yet implemented.
		return result;
	}
	
	/**
	 * +++ TAG 5
	 */
	private JButton createPauseOrResumeButton(String text) {
		JButton result = new JButton(text);
		// Not yet implemented.
		return result;
	}
	
	/**
	 * +++ TAG 5
	 */
	private JLabel createStartProgramSubTitleInPanelLabel(String text) {
		JLabel result = new JLabel();
		result.setFont(WindowConsts.createTitleFont());
		return result;
	}
	
	// =======================================================================
	
	private void createProcess() {
		
		final JProgressBarUpdater jpbu = new JProgressBarUpdater(this.userSelectedFiles);
		jpbu.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pcEvt) {
				if ("progress".equals(pcEvt.getPropertyName())) {
					prog.setValue((Integer) pcEvt.getNewValue());
				} else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
					try {
						jpbu.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		});
		jpbu.execute();
		
	}
	
	// =======================================================================
	
	private class JProgressBarUpdater extends SwingWorker<Void, Void> {
		
		private int sum;
		private List<Path> paths;
		
		public JProgressBarUpdater(List<Path> totalPaths) {
			this.paths = totalPaths;
			sum = paths.size();
		}
		
		@Override
		protected void done() {
			// after running the successful encrypt/decrypt, DELETE the List<path> and clear it from the TextPane
			userSelectedFiles.clear();
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			
			char[] chars = usersPasswordTextField.getPassword();
			byte[] bytes = new byte[chars.length * 2];
			for (int i = 0; i < chars.length; i++) {
				bytes[i * 2] = (byte) (chars[i] >> 8);
				bytes[i * 2 + 1] = (byte) chars[i];
			}
			
			// default settings
			final byte[] priv_iV = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
			Cipher c = Cipher.getInstance(CryptTombPassage.DEFAULT_CIPHER_ALGO_NAME);
			MessageDigest md = MessageDigest.getInstance(CryptTombPassage.DEFAULT_MSG_DIGEST_ALGO_NAME);
			Key sKeySpec = new SecretKeySpec(md.digest(bytes), CryptTombPassage.DEFAULT_KEY_SPEC_ALGO_NAME);
			IvParameterSpec ivps = new IvParameterSpec(priv_iV);

			CryptTomb ct = new CryptTomb(c, sKeySpec, ivps, bytes, priv_iV);
			
			int counter = 0;
			
			//TODO Note that this is DEFAULT settings;
			for (Path entry : userSelectedFiles) {
				String str = entry.toString();
				String renamed = attemptRename(str, (doEncrypt ? "-obfus" : "-deob"));
				File checker = new File(renamed);
				if (checker.exists())
					checker.delete();
				System.out.println("dbg: " + outputFolderPath.getParent().toString());
				System.out.println("dbg: " + checker.getParent());
				System.out.println();
				String pathName = outputFolderPath.toString() + Log.FILESEP + checker.getName();
				Path dest = Paths.get(pathName);
				if (doEncrypt)
					ct.encryptFile(entry, dest);
				else
					ct.decryptFile(entry, dest);
				if (delAfterProcessed)
					entry.toFile().delete();
				++counter;
				setProgress((counter * 100) / sum);
			}

			// now to zero out the users password.
			Arrays.fill(chars, '\0');
			Arrays.fill(bytes, (byte) 0);
			
			return null;
		}
	}
	
	private String attemptRename(String input, String tag) {
		String result = "";
		
		int idx = input.lastIndexOf('.');
		if (idx == -1)
			return input + tag; // no period indicating an extension, return nothing.
		
		String prefix = input.substring(0, idx);
		String post = input.substring(idx, input.length());
		
	    result = prefix + tag + post;
		
		return result;
	}
	 
}
