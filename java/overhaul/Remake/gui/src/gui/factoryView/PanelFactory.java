package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import java.awt.Container;

public final class PanelFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private PanelFactory() {
		
	}

	public static JPanel createFileListingPanel(Controller c) {
		JPanel panel = new JPanel();
		GroupLayout layout = createFileListingGroupLayout(panel, c);
		panel.setLayout(layout);
		return panel;
	}

	public static JPanel createBasicSettingsPanel(Controller c) {
		JPanel panel = new JPanel();
		GroupLayout layout = createBasicSettingsGroupLayout(panel, c);
		panel.setLayout(layout);
		return panel;
	}

	private static GroupLayout createFileListingGroupLayout(Container host, Controller c) {
		GroupLayout layout = new GroupLayout(host);

		final JScrollPane scroller = new JScrollPane();
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		final JTextPane itemList = TextPaneFactory.createFileList(c);
		scroller.setViewportView(itemList);

		final JTextField textField = TextFieldFactory.createTextField(c);

		final JLabel lblFiles = LabelFactory.createFilesLabel();
		final JLabel lblOutputDir = LabelFactory.createOutputDirLabel();

		final JButton filesButton = ButtonFactory.createSelectFilesButton(itemList, c);
		final JButton outputButton = ButtonFactory.createSelectOutputButton(textField, c);

		layout.setHorizontalGroup(layout
			.createParallelGroup(Alignment.LEADING)
			.addGroup(layout.createSequentialGroup().addContainerGap()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
			.addComponent(scroller, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
			.addGroup(layout.createSequentialGroup()
			.addComponent(lblFiles)
			.addPreferredGap(ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
			.addComponent(filesButton))
			.addGroup(layout.createSequentialGroup()
			.addComponent(lblOutputDir)
			.addPreferredGap(ComponentPlacement.RELATED,91,Short.MAX_VALUE)
			.addComponent(outputButton))
			.addComponent(textField,GroupLayout.DEFAULT_SIZE,238, Short.MAX_VALUE))
			.addContainerGap()));
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
			.addGroup(layout.createSequentialGroup().addGap(11)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
			.addComponent(lblFiles).addComponent(filesButton))
			.addPreferredGap(ComponentPlacement.RELATED)
			.addComponent(scroller,GroupLayout.DEFAULT_SIZE, 122,Short.MAX_VALUE)
			.addGap(6).addGroup(layout.createParallelGroup(Alignment.BASELINE)
			.addComponent(lblOutputDir).addComponent(outputButton))
			.addPreferredGap(ComponentPlacement.RELATED)
			.addComponent(textField,GroupLayout.PREFERRED_SIZE,
					GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE).addGap(40)));
		return layout;
	}

	private static GroupLayout createBasicSettingsGroupLayout(Container host, Controller c) {
		GroupLayout layout = new GroupLayout(host);

		final JRadioButton rdbtnDeleteFile = RadioButtonFactory
				.createDelFilesRadioButton(c);
		final JRadioButton rdbtnSearchSubdirs = RadioButtonFactory
				.createSearchSubDirRadioButton(c);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
			.addGroup(layout.createSequentialGroup().addContainerGap()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
			.addComponent(rdbtnSearchSubdirs)
			.addComponent(rdbtnDeleteFile))
			.addContainerGap(132, Short.MAX_VALUE)));
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.TRAILING)
			.addGroup(Alignment.LEADING,layout.createSequentialGroup()
			.addContainerGap()
			.addComponent(rdbtnDeleteFile)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addComponent(rdbtnSearchSubdirs)
			.addContainerGap(264, Short.MAX_VALUE)));
		return layout;
	}

}