package paint;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.io.*;


public class Painter extends JFrame implements ActionListener {
	protected	Container c = getContentPane();
	protected	String menuBar[]={"File(F)","Edit(E)","View(V)","Help(H)"};
	protected	String menuItem[][]={
		{"New File(N)|78","Open(O)|79","Save(S)|83","Save As(A)","Exit(X)|88"},
		{"Undo(U)|90","Redo(R)|89","Cut(T)|87","Copy(C)|68","Paste(P)|85"},
		{"ToolKit(T)|84","Color(C)|76","Status(S)","Property(M)"},
	};
	protected JMenuItem jMenuItem[][]=new JMenuItem[4][5];
	protected	JMenu jMenu[];
	protected	JCheckBoxMenuItem jCheckBoxMenuItem[] = new JCheckBoxMenuItem[4];
	protected	String ButtonName[]={"STRAIGHTLINE","RECTANGLE","ECLIPSE","ROUNDRECT","CURVE","","POLYGON","PENCIL","ERASER","TEXT","SELECT"};
	protected JToggleButton jToggleButton[];
    protected ButtonGroup buttonGroup;
	protected	JPanel jPanel[]=new JPanel[5];
	protected	JLabel jLabel[]=new JLabel[1];
	protected	String toolname[]={"img/linefu.png","img/rectangle.png","img/circle.png","img/r.png","img/curve.png","","img/polygon.png","img/pen.png","img/eraser.png","img/T.png","img/dotted_rect.png"};
	protected	Icon tool[] = new ImageIcon[11];
	protected	int i,j,show_x,show_y,drawMethod = 7,draw_panel_width=750, draw_panel_height=750;
	protected Paint color_border,color_inside;
	
	// Main components within the main frame
	protected WelcomePage welcome;
	protected SetPanel setPanel;
	protected DrawPanel drawPanel;
	protected UnderDrawPanel underDrawPanel;
	protected ColorPalette ColorPalette;
	protected JMenuBar bar;
	protected Stroke stroke;
	protected Shape shape;
	protected String isFilled;
	
	public Painter(){
	
		// Set the UI theme
		try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // Configure theming colors. 
            // {@see http://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html}
            
            UIManager.put("nimbusBase", Color.BLACK);
            UIManager.put("control", Color.DARK_GRAY);
            UIManager.put("text", Color.WHITE);
            UIManager.put("nimbusLightBackground", Color.DARK_GRAY);
            UIManager.put("nimbusFocus", new Color(255, 255, 255, 0));
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("nimbusSelectionBackground", Color.LIGHT_GRAY);
            UIManager.put("controlLHighlight", Color.DARK_GRAY);
            UIManager.put("TabbedPane.foreground", Color.BLACK);
            UIManager.put("ToolBar.opaque", false);
            UIManager.put("ToolBarSeparator.textForeground", new Color(110, 110, 110));
            UIManager.put("ToolTip.background", Color.DARK_GRAY);
            UIManager.put("ToolTip.foreground", Color.BLACK);

        } catch (Exception e) {}
		
		
		// Initialize by welcome page
		welcome = new WelcomePage(this);
		this.setContentPane(welcome);

		// Menu bar components
		bar = new JMenuBar();
		jMenu=new JMenu[menuBar.length];
		for(i=0;i<menuBar.length;i++){
			jMenu[i] = new JMenu(menuBar[i]);
			jMenu[i].setMnemonic(menuBar[i].split("\\(")[1].charAt(0));
			bar.add(jMenu[i]);
		}
		
		for(i=0;i<menuItem.length;i++){
			for(j=0;j<menuItem[i].length;j++){
				if(i==0 && j==4 || i==1 && j==2) jMenu[i].addSeparator();
				if(i!=2){
					jMenuItem[i][j] = new JMenuItem(menuItem[i][j].split("\\|")[0]);
					if(menuItem[i][j].split("\\|").length!=1)
						jMenuItem[i][j].setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(menuItem[i][j].split("\\|")[1]), ActionEvent.CTRL_MASK) );
					jMenuItem[i][j].addActionListener(this);
					jMenuItem[i][j].setMnemonic(menuItem[i][j].split("\\(")[1].charAt(0));

					jMenu[i].add(jMenuItem[i][j]);
				}
				else{
					jCheckBoxMenuItem[j] = new JCheckBoxMenuItem(menuItem[i][j].split("\\|")[0]);
					if(menuItem[i][j].split("\\|").length!=1)
						jCheckBoxMenuItem[j].setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(menuItem[i][j].split("\\|")[1]), ActionEvent.CTRL_MASK) );
					jCheckBoxMenuItem[j].addActionListener(this);
					jCheckBoxMenuItem[j].setMnemonic(menuItem[i][j].split("\\(")[1].charAt(0));
					jCheckBoxMenuItem[j].setSelected( true );
					jMenu[i].add(jCheckBoxMenuItem[j]);
				}
			}
		}
		
		
		// container c contain the main components
		c.setLayout( new BorderLayout() );
		
		for(i=0;i<5;i++)
			jPanel[i]=new JPanel();
			
		jLabel[0]=new JLabel("Coordination(x, y)");
		
		
		// add the tools to tool bar panel
		buttonGroup = new ButtonGroup();
		JToolBar jToolBar = new JToolBar("Toolbar", JToolBar.VERTICAL);
		jToggleButton = new JToggleButton[ButtonName.length];
		for(i=0;i < ButtonName.length;i++){
			tool[i] = new ImageIcon(toolname[i]);
			jToggleButton[i] = new JToggleButton(tool[i]);
			jToggleButton[i].addActionListener( this );
			jToggleButton[i].setFocusable( false );
			buttonGroup.add(jToggleButton[i]);
		}
		jToolBar.add(jToggleButton[7]);
		jToolBar.add(jToggleButton[8]);
		jToolBar.add(jToggleButton[0]);
		jToolBar.add(jToggleButton[4]);
		jToolBar.add(jToggleButton[1]);
		jToolBar.add(jToggleButton[3]);
		jToolBar.add(jToggleButton[2]);
		jToolBar.add(jToggleButton[6]);
		jToolBar.add(jToggleButton[9]);
		jToolBar.add(jToggleButton[10]);
		jToggleButton[7].setSelected(true);
		jToolBar.setLayout(new GridLayout(6, 2, 2, 2));
		jPanel[2].add(jToolBar);
		jPanel[2].setLayout(new BoxLayout(jPanel[2], BoxLayout.Y_AXIS));
		jToolBar.setFloatable(false);
		
		// add color picker panel
		ColorPalette=new ColorPalette(this);
		jPanel[3].setLayout(new FlowLayout(FlowLayout.LEFT));
		jPanel[3].add(ColorPalette);
		drawPanel=new DrawPanel(this);
		underDrawPanel=new UnderDrawPanel(this);
		underDrawPanel.setLayout(null);
		underDrawPanel.add(drawPanel);
		drawPanel.setBounds(new Rectangle(2, 2, draw_panel_width, draw_panel_height));
		
		setPanel=new SetPanel(this);
		jPanel[2].add(setPanel);
		
		// Parent panel jPanel[0] containing all the functional panels like background panel, drawing panel etc
		jPanel[0].setLayout( new BorderLayout());
		jPanel[0].add(underDrawPanel,BorderLayout.CENTER);// back ground panel to maintain the actual canvas
		jPanel[0].add(jPanel[2],BorderLayout.WEST);// Tool bar panel on the west
		jPanel[0].add(jPanel[3],BorderLayout.SOUTH); // Color palette panel on button
//		jPanel[0].add(jPanel[4],BorderLayout.EAST); // Setting Panel on right
		
		jLabel[0].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		underDrawPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		underDrawPanel.setBackground(Color.gray);
		jPanel[3].setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(172,168,153)));
		
		c.add(jPanel[0],BorderLayout.CENTER);
		c.add(jLabel[0],BorderLayout.SOUTH);
		
		setSize(draw_panel_width,draw_panel_height);
		setTitle("SketchMe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		show();
		
	}
	public void save(){
		FileDialog fileDialog = new FileDialog(new Frame() , "Save", FileDialog.SAVE );
		fileDialog.show();
		if(fileDialog.getFile()==null) return;
		drawPanel.filename = fileDialog.getDirectory() + fileDialog.getFile();
	}
	
	public void actionPerformed( ActionEvent e ){
		for(i=0;i<ButtonName.length;i++){
			if(e.getSource()==jToggleButton[i]){
				drawMethod=i;
				drawPanel.clear();
				drawPanel.repaint();
   				jMenuItem[1][2].setEnabled(false);
   				jMenuItem[1][3].setEnabled(false);
			}
		}
		
		if(e.getSource()==jMenuItem[1][0]){
			drawPanel.undo();
		}
		else if(e.getSource()==jMenuItem[1][1]){
			drawPanel.redo();
		}
		else if(e.getSource()==jMenuItem[1][2]){
			drawPanel.cut();
		}
		else if(e.getSource()==jMenuItem[1][3]){
			drawPanel.copy();
		}
		else if(e.getSource()==jMenuItem[1][4]){
			drawPanel.paste();
		}
		
		// menu item new file
		else if(e.getSource()==jMenuItem[0][0]){
			underDrawPanel.remove(drawPanel);
			drawPanel=null;
			drawPanel=new DrawPanel(this);
			underDrawPanel.add(drawPanel);
			drawPanel.setBounds(new Rectangle(2, 2, draw_panel_width, draw_panel_height));
			underDrawPanel.ctrl_area.setLocation(draw_panel_width+3,draw_panel_height+3);
			underDrawPanel.ctrl_area2.setLocation(draw_panel_width+3,draw_panel_height/2+3);
			underDrawPanel.ctrl_area3.setLocation(draw_panel_width/2+3,draw_panel_height+3);
			repaint();
		}
		else if(e.getSource()==jMenuItem[0][1]){
			FileDialog fileDialog = new FileDialog( new Frame() , "SELECT IMAGE", FileDialog.LOAD );
			fileDialog.show();
			if(fileDialog.getFile()==null) return;
			
			underDrawPanel.removeAll();
			drawPanel=null;
			drawPanel=new DrawPanel(this);
			underDrawPanel.add(drawPanel);
			drawPanel.setBounds(new Rectangle(2, 2, draw_panel_width, draw_panel_height));
			
			drawPanel.openfile(fileDialog.getDirectory()+fileDialog.getFile());
		}
		else if(e.getSource()==jMenuItem[0][2]){
			if(drawPanel.filename==null){
				save();
			}
			else{
				try{
					int dotpos = drawPanel.filename.lastIndexOf('.');
					ImageIO.write(drawPanel.bufImg, drawPanel.filename.substring(dotpos + 1), new File(drawPanel.filename));
				}
				catch(IOException even) {
					JOptionPane.showMessageDialog(null, even.toString(),"CAN'T SAVE IMAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(e.getSource()==jMenuItem[0][3]){
			save();
			try{
				int dotpos = drawPanel.filename.lastIndexOf('.');
				ImageIO.write(drawPanel.bufImg, drawPanel.filename.substring(dotpos + 1), new File(drawPanel.filename));
			}
			catch(IOException even) {
				JOptionPane.showMessageDialog(null, even.toString(),"CAN'T SAVE IMAGE", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource()==jMenuItem[0][4]){
			System.exit(0);
		}
		for(i=0;i<2;i++){
			if(jCheckBoxMenuItem[i].isSelected())
				jPanel[i+2].setVisible( true );
           	else
           		jPanel[i+2].setVisible( false );
       	}
       	if(jCheckBoxMenuItem[3].isSelected()){
       		setPanel.setVisible( true );
       		jPanel[4].setVisible( true );
       	}
       	else{
       		setPanel.setVisible( false );
       		jPanel[4].setVisible( false );
       	}
		if(jCheckBoxMenuItem[2].isSelected())
			jLabel[0].setVisible( true );
       	else
       		jLabel[0].setVisible( false );
	}
	

}

