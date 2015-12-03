package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class WelcomePage extends JPanel implements ActionListener{
	
	protected JButton createNew;
	protected JButton openCurrent;
	protected CreationDialog createDialog;
	protected Painter appFrame;
	private final static String LOGO_PATH = "img/logo.png";
	private BufferedImage image;
	protected int preferredWidth;
	protected int preferredHeight;
	
	// construction routine that pass frame as parameter
	// this allow us to link welcome page to the backbone frame
	public WelcomePage(Frame frame){
		appFrame = (Painter) frame;
		createDialog = new CreationDialog(frame);
		
		// set the location of application logo
		JPanel logoPanel = new JPanel();
		try {
		    image = ImageIO.read(new File(LOGO_PATH));
		} catch (IOException e) {
			System.out.println("Cant't read file");
		}
		JLabel picLabel = new JLabel(new ImageIcon(image));
		logoPanel.add(picLabel);
		//logoPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		
		// entry panel for either creating new file or open existing file
		JPanel entry = new JPanel();
		entry.setLayout(new GridLayout(4, 1));
		entry.setBackground(Color.WHITE);
		
		createNew = new JButton("Create New");
		createNew.setPreferredSize(new Dimension(50,50));
		openCurrent = new JButton("Open Existing");
		entry.add(createNew);
		entry.add(openCurrent);
		entry.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		
		this.setLayout(new BorderLayout());
		this.add(logoPanel, BorderLayout.CENTER);
		this.add(entry, BorderLayout.SOUTH);
		this.setBackground(Color.WHITE);
		
		createNew.addActionListener(this);
		openCurrent.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Component source = (Component) e.getSource();
		
		if(source == createNew) {
			// Pop up a dialog to prompt the user to choose the preferred size
			// the event listen to the return value of this dialog, if it is 
			// 1, then the user choose to create paint panel with specific size
			int i = createDialog.showDialog(this);
			if(i == CreationDialog.CREATE_OPTION){
				// here we have to create a new paint panel by switching the panel within the template
				appFrame.remove(this);
				appFrame.draw_panel_height = this.preferredHeight;
				appFrame.draw_panel_width = this.preferredWidth;
				appFrame.setContentPane(appFrame.c);
				appFrame.repaint();
				appFrame.pack();
				appFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
				// Set menu bar
				appFrame.setJMenuBar(appFrame.bar );
				
			}
		}
		else if(source == openCurrent) {
			FileDialog fileDialog = new FileDialog( new Frame() , "SELECT IMAGE", FileDialog.LOAD );
			fileDialog.show();
			if(fileDialog.getFile() == null) return;
			
			appFrame.underDrawPanel.removeAll();
			appFrame.drawPanel=null;
			appFrame.drawPanel = new DrawPanel(appFrame);
			appFrame.underDrawPanel.add(appFrame.drawPanel);
			appFrame.drawPanel.setBounds(new Rectangle(2, 2, appFrame.draw_panel_width, appFrame.draw_panel_height));
			appFrame.drawPanel.openfile(fileDialog.getDirectory()+fileDialog.getFile());
			
			appFrame.remove(this);
			appFrame.drawPanel.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
			appFrame.setContentPane(appFrame.c);
			appFrame.repaint();
			appFrame.pack();
			appFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
			// Set menu bar
			appFrame.setJMenuBar(appFrame.bar );
			
		}
	}
	
	
	protected class CreationDialog extends JDialog implements ActionListener{
		
		public final static int CREATE_OPTION = 1;
		public final static int CANCEL_OPTION = 0;
		public JLabel widthLabel;
		public JLabel heightLabel;
		public JLabel hint;
		public JTextField width;
		public JTextField height;
		public JButton create;
		public JButton cancel;
		int userResponse;

		
		// constructor that declare the owner of this dialog
		public CreationDialog(Frame owner) {
			super(owner, "Create A New Painting", true);
			this.setResizable(false);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

			widthLabel = new JLabel("Width");
			heightLabel = new JLabel("Height");
			
			width = new JTextField("900");
			width.setPreferredSize(new Dimension());
			height = new JTextField("800");
			height.setPreferredSize(new Dimension());
			hint = new JLabel("<html>800 X 600 Recommended</html>");
			create = new JButton("Create");
			cancel = new JButton("Cancel");
				
			// layout of dialog
			
			JPanel configBlock = new JPanel(new GridLayout(2,2));
			configBlock.add(widthLabel);
			configBlock.add(width);
			configBlock.add(heightLabel);
			configBlock.add(height);
			configBlock.setBorder(new TitledBorder(new EtchedBorder(), "Configure Canvas"));
			configBlock.setAlignmentX(Component.CENTER_ALIGNMENT);
			JPanel buttonBlock = new JPanel();
			buttonBlock.add(create);
			buttonBlock.add(cancel);
			
			JPanel dialogPanel = new JPanel();
			dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
			dialogPanel.add(configBlock);
			dialogPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			dialogPanel.add(buttonBlock);
			dialogPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			this.setPreferredSize(new Dimension(400,175));
			this.setContentPane(dialogPanel);
			this.pack();
			
			// add listeners
			create.addActionListener(this);
			cancel.addActionListener(this);
		}
		
		// show dialog to the attached components, return an integer
		public int showDialog(JPanel panel) {
			this.setLocationRelativeTo(panel);
			this.setVisible(true);
			
			return userResponse;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			// set the width and height that need to be passed out to creation procedure
			if(source == create) {
				userResponse = CREATE_OPTION;
				// Invalid input handling.
				if(width.getText().matches("[0-9]*") && height.getText().matches("[0-9]*")){
					preferredWidth = Integer.parseInt(width.getText());
					preferredHeight = Integer.parseInt(height.getText());
					this.setVisible(false);
				}
				else
					Toolkit.getDefaultToolkit().beep();
				
			}
			else if(source == cancel) {
				userResponse = CANCEL_OPTION;
				this.setVisible(false);
			}
		}
	}
}