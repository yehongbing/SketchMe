package paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, ItemListener, ActionListener, ChangeListener{
	
	private Painter parentFrame;
	private static final int STRAIGHTLINE = 0;
	private static final int RECTANGLE = 1;
	private static final int ECLIPSE = 2;
	private static final int ROUNDRECT = 3;
	private static final int CURVE = 4;
	private static final int POLYGON = 6;
	
	
	public BufferedImage bufImg;
	protected BufferedImage bufImg_data[];
	protected BufferedImage bufImg_cut;
	protected ImageIcon img;
	protected JLabel jlbImg;
	
	protected int x1=-1,y1=-1,x2,y2,count,redo_lim,press,temp_x1,temp_y1,temp_x2,temp_y2,temp_x3,temp_y3,step,step_chk,step_arc,step_chk_arc,chk,first,click,cut;
	
	protected Line2D.Double line2D = new Line2D.Double();
	protected Ellipse2D.Double ellipse2D = new Ellipse2D.Double();
	protected Rectangle2D.Double rectangle2D = new Rectangle2D.Double();
	protected CubicCurve2D.Double cubicCurve2D = new CubicCurve2D.Double();
	protected RoundRectangle2D.Double roundRectangle2D = new RoundRectangle2D.Double();
	protected Polygon polygon;
	
	protected float data[]={5};
	
	protected Rectangle2D.Double rectangle2D_select = new Rectangle2D.Double();
	protected Ellipse2D.Double ellipse2D_pan = new Ellipse2D.Double();
	
	protected BasicStroke basicStroke_pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
	protected BasicStroke basicStroke_select = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,10, data, 0);
	
	protected double center_point_x;
	protected double center_point_y;
	protected double start;
	protected double end;
	
	public String filename;
	
	// Components of writing text to draw panel

	protected JTextField textField_font = new JTextField("SANS_SERIF",16), textField_word = new JTextField("JAVA",16);
	protected JDialog jDialog;
	protected int size = 100;
	protected JSpinner fontsize = new JSpinner();

	protected JCheckBox bold, italic;
	protected JButton ok, cancel;
	protected int valBold = Font.BOLD;
	protected int valItalic = Font.ITALIC;
	protected int select_x,select_y,select_w,select_h;
	
	
	public DrawPanel(Frame frame) {
		// establish the link between frame and drawing panel
		parentFrame = (Painter)frame;
		// Drawing by using the BufferedImage class
		bufImg_data = new BufferedImage[1000]; // This records all the actions done by user
		bufImg = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
		// Label as a container to maintain the canvas, which is actually a BufferedImage object
		jlbImg = new JLabel(new ImageIcon(bufImg));

		this.setLayout(null);
		this.add(jlbImg);
		jlbImg.setBounds(new Rectangle(0, 0, parentFrame.draw_panel_width, parentFrame.draw_panel_height));
		
		// initialize the edit to disabled
	 	parentFrame.jMenuItem[1][0].setEnabled(false);
		parentFrame.jMenuItem[1][1].setEnabled(false);
		parentFrame.jMenuItem[1][2].setEnabled(false);
		parentFrame.jMenuItem[1][3].setEnabled(false);
		parentFrame.jMenuItem[1][4].setEnabled(false);
		
		
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		g2d_bufImg.setPaint(Color.WHITE);
		g2d_bufImg.fill(new Rectangle2D.Double(0,0,parentFrame.draw_panel_width,parentFrame.draw_panel_height));
		
		bufImg_data[count] = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
		g2d_bufImg_data.drawImage(bufImg,0,0,this);
		
		//Preferred Text Dialog Start //
		jDialog = new JDialog(parentFrame, "Text", true);
		fontsize.setValue(new Integer(73));
		bold = new JCheckBox( "BOLD" ,true);
		italic = new JCheckBox( "ITALIC" ,true);
		ok = new JButton("Confirm");
		cancel = new JButton("Cancel");
		JPanel temp_0 = new JPanel(new GridLayout(5,1));
		JPanel temp_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel temp_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel temp_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel temp_4 = new JPanel(new FlowLayout());
		JPanel temp_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Container jDialog_c = jDialog.getContentPane();
			
    	jDialog_c.setLayout(new BoxLayout(jDialog_c, BoxLayout.Y_AXIS));
    	jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
    	jDialog.setSize(250, 200);
    	temp_5.add(new JLabel("Content: "));
		temp_5.add(textField_word);
		temp_1.add(new JLabel("Font: "));
		temp_1.add(textField_font);
		temp_2.add(new JLabel("Size:"));
		temp_2.add(fontsize);
		temp_3.add(new JLabel("S:"));
		temp_3.add(bold);
		temp_3.add(italic);
		temp_4.add(ok);
		temp_4.add(cancel);
		temp_0.add(temp_5);
		temp_0.add(temp_1);
    	temp_0.add(temp_2);
    	temp_0.add(temp_3);
    	temp_0.add(temp_4);
    	jDialog_c.add(temp_0);
    	
    	bold.addItemListener(this);
    	italic.addItemListener(this);
    	fontsize.addChangeListener(this);
    	ok.addActionListener(this);
    	cancel.addActionListener(this);
    	temp_0.setPreferredSize(new Dimension( 180 , 150 ));
    	// Preferred End //
    	
		repaint();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	// Resizing method for the background panel to use when they call mouseDragged
	public void resize(){
		bufImg = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
		jlbImg = new JLabel(new ImageIcon(bufImg));
		this.removeAll();
		this.add(jlbImg);
		jlbImg.setBounds(new Rectangle(0, 0, parentFrame.draw_panel_width, parentFrame.draw_panel_height));
		
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		g2d_bufImg.setPaint(Color.white);
		g2d_bufImg.fill(new Rectangle2D.Double(0,0,parentFrame.draw_panel_width,parentFrame.draw_panel_height));
		g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

		redo_lim = count++;
		parentFrame.jMenuItem[1][1].setEnabled(false);
		
		bufImg_data[count] = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
		g2d_bufImg_data.drawImage(bufImg,0,0,this);

		press = 0;
		if(count > 0) parentFrame.jMenuItem[1][0].setEnabled(true);
	}
	
	// Get the changed value of font size in the dialog
	@Override
	public void stateChanged(ChangeEvent e){
		size = Integer.parseInt(fontsize.getValue().toString());
		if(size <= 0) {
			fontsize.setValue(new Integer(1));
			size = 1;
		}
	}
	@Override
	public void actionPerformed( ActionEvent e ){
		jDialog.dispose();
	}
	
	// item selection for bold or/and italic
	@Override
	public void itemStateChanged(ItemEvent e){
		if ( e.getSource() == bold )
			if ( e.getStateChange() == ItemEvent.SELECTED )
				valBold = Font.BOLD;
			else
				valBold = Font.PLAIN;
		if ( e.getSource() == italic )
			if ( e.getStateChange() == ItemEvent.SELECTED )
				valItalic = Font.ITALIC;
			else
				valItalic = Font.PLAIN;
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension( parentFrame.draw_panel_width, parentFrame.draw_panel_height );
	}
	
	
	public void openfile(String filename){
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		ImageIcon icon = new ImageIcon(filename);
		g2d_bufImg.drawImage(icon.getImage(),0,0,this);
		
		count++; // Increase action
		bufImg_data[count] = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
		g2d_bufImg_data.drawImage(bufImg,0,0,this);
		
		repaint();
	}
	
	public void undo(){
		count--;		
		parentFrame.draw_panel_width = bufImg_data[count].getWidth();
		parentFrame.draw_panel_height = bufImg_data[count].getHeight();
		parentFrame.drawPanel.setSize(parentFrame.draw_panel_width, parentFrame.draw_panel_height);
		
		bufImg = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
		jlbImg = new JLabel(new ImageIcon(bufImg));
		this.removeAll();
		this.add(jlbImg);
		jlbImg.setBounds(new Rectangle(0, 0, parentFrame.draw_panel_width, parentFrame.draw_panel_height));
		
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		g2d_bufImg.setPaint(Color.white);
		g2d_bufImg.fill(new Rectangle2D.Double(0,0,parentFrame.draw_panel_width,parentFrame.draw_panel_height));
		g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

		parentFrame.underDrawPanel.ctrl_area.setLocation(parentFrame.draw_panel_width+3,parentFrame.draw_panel_height+3);
		parentFrame.underDrawPanel.ctrl_area2.setLocation(parentFrame.draw_panel_width+3,parentFrame.draw_panel_height/2+3);
		parentFrame.underDrawPanel.ctrl_area3.setLocation(parentFrame.draw_panel_width/2+3,parentFrame.draw_panel_height+3);
		
		parentFrame.underDrawPanel.x=parentFrame.draw_panel_width;
		parentFrame.underDrawPanel.y=parentFrame.draw_panel_height;
		
   		if(count <= 0)
   			parentFrame.jMenuItem[1][0].setEnabled(false);
   			parentFrame.jMenuItem[1][1].setEnabled(true);
   			cut = 3;
			repaint();
		}

	public void redo(){
		count++;
		
		parentFrame.draw_panel_width=bufImg_data[count].getWidth();
		parentFrame.draw_panel_height=bufImg_data[count].getHeight();
		parentFrame.drawPanel.setSize(parentFrame.draw_panel_width,parentFrame.draw_panel_height);

		bufImg = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
		jlbImg = new JLabel(new ImageIcon(bufImg));
		this.removeAll();
		this.add(jlbImg);
		jlbImg.setBounds(new Rectangle(0, 0, parentFrame.draw_panel_width, parentFrame.draw_panel_height));
		
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		g2d_bufImg.setPaint(Color.white);
		g2d_bufImg.fill(new Rectangle2D.Double(0,0,parentFrame.draw_panel_width,parentFrame.draw_panel_height));
		g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

		parentFrame.underDrawPanel.ctrl_area.setLocation(parentFrame.draw_panel_width+3,parentFrame.draw_panel_height+3);
		parentFrame.underDrawPanel.ctrl_area2.setLocation(parentFrame.draw_panel_width+3,parentFrame.draw_panel_height/2+3);
		parentFrame.underDrawPanel.ctrl_area3.setLocation(parentFrame.draw_panel_width/2+3,parentFrame.draw_panel_height+3);
		
		parentFrame.underDrawPanel.x=parentFrame.draw_panel_width;
		parentFrame.underDrawPanel.y=parentFrame.draw_panel_height;
		
		if(redo_lim<count)
			parentFrame.jMenuItem[1][1].setEnabled(false);
			parentFrame.jMenuItem[1][0].setEnabled(true);
			cut=3;
			repaint();
		}
	
	public void cut(){
		bufImg_cut = new BufferedImage((int)rectangle2D_select.getWidth(), (int)rectangle2D_select.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage copy = bufImg.getSubimage((int)rectangle2D_select.getX(),(int)rectangle2D_select.getY(),(int)rectangle2D_select.getWidth(),(int)rectangle2D_select.getHeight());
		Graphics2D g2d_bufImg_cut = (Graphics2D) bufImg_cut.createGraphics();
		g2d_bufImg_cut.drawImage(copy,0,0,this);
		
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		g2d_bufImg.setPaint(Color.WHITE);
		g2d_bufImg.fill(new Rectangle2D.Double((int)rectangle2D_select.getX(),(int)rectangle2D_select.getY(),(int)rectangle2D_select.getWidth(),(int)rectangle2D_select.getHeight()));
		
		redo_lim=count++;
		parentFrame.jMenuItem[1][1].setEnabled(false);

		bufImg_data[count] = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
		g2d_bufImg_data.drawImage(bufImg,0,0,this);

		press = 0;

			if(count > 0)
				parentFrame.jMenuItem[1][0].setEnabled(true);
			parentFrame.jMenuItem[1][2].setEnabled(false);
			parentFrame.jMenuItem[1][3].setEnabled(false);
		parentFrame.jMenuItem[1][4].setEnabled(true);
		cut = 3;
		repaint();
	}
	public void copy(){
		bufImg_cut = new BufferedImage((int)rectangle2D_select.getWidth(), (int)rectangle2D_select.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage copy = bufImg.getSubimage((int)rectangle2D_select.getX(),(int)rectangle2D_select.getY(),(int)rectangle2D_select.getWidth(),(int)rectangle2D_select.getHeight());
		Graphics2D g2d_bufImg_cut = (Graphics2D) bufImg_cut.createGraphics();
		g2d_bufImg_cut.drawImage(copy,0,0,this);
		parentFrame.jMenuItem[1][4].setEnabled(true);
		cut=1;
		repaint();
	}
	public void paste(){
		cut = 2;
		repaint();
	}
	public void mousePressed(MouseEvent e) {
		x1=e.getX();
		y1=e.getY();
		if(first == 0){
			polygon = new Polygon();
			polygon.addPoint(x1, y1);
			first=1;
		}
		press=1;
		chk=0;
		if(cut != 2) cut = 0;
	}

	public void mouseReleased(MouseEvent e) {
		x2=e.getX();
		y2=e.getY();
		
		if(step_chk==0)
			step=1;
		else if(step_chk==1)
			step=2;
		
		if(step_chk_arc==0)
			chk=step_arc=1;
		else if(step_chk_arc==1)
			chk=step_arc=2;
		
		if(parentFrame.drawMethod==6 && click!=1){
			polygon.addPoint(x2, y2);
			repaint();
		}
		if(parentFrame.drawMethod==10){
			if(cut!=2) cut=1;
			select_x=(int)rectangle2D_select.getX();
			select_y=(int)rectangle2D_select.getY();
			select_w=(int)rectangle2D_select.getWidth();
			select_h=(int)rectangle2D_select.getHeight();
			parentFrame.jMenuItem[1][2].setEnabled(true);
			parentFrame.jMenuItem[1][3].setEnabled(true);
		}

		if((step_chk==2 && step==2) || (step_chk_arc==2 && step_arc==2) || parentFrame.drawMethod==0 || parentFrame.drawMethod==1 || parentFrame.drawMethod==2 || parentFrame.drawMethod==3 || parentFrame.drawMethod==7 || parentFrame.drawMethod==8 || parentFrame.drawMethod==9 || cut==2){//�����Ǯ�ؐ�����������ѽ����ؐ�������r��
			toDraw();
		}
	}
	public void clear(){
		cut=select_x=select_y=select_w=select_h=step_chk_arc=step_arc=first=step_chk=step=0;
		x1=x2=y1=y2=-1;
	}
	
	public void toDraw(){
		if(x1<0 || y1<0) return;
		chk=3;
		draw(x1,y1,x2,y2);
		
		
		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
		if(cut!=2){
			if(parentFrame.color_inside!=null && parentFrame.drawMethod!=8){
				g2d_bufImg.setPaint(parentFrame.color_inside);
				g2d_bufImg.fill(parentFrame.shape);
			}
			if(parentFrame.color_border!=null && parentFrame.drawMethod!=8){
				g2d_bufImg.setPaint(parentFrame.color_border);
				g2d_bufImg.setStroke(parentFrame.stroke);
				g2d_bufImg.draw(parentFrame.shape);
			}
		}
		else{
				g2d_bufImg.drawImage(bufImg_cut,x2,y2,this);
		}
		repaint();
		clear();
		
		redo_lim=count++;
		parentFrame.jMenuItem[1][1].setEnabled(false);
		
		bufImg_data[count] = new BufferedImage(parentFrame.draw_panel_width, parentFrame.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
		g2d_bufImg_data.drawImage(bufImg,0,0,this);
	
		press=0;

		if(count>0) parentFrame.jMenuItem[1][0].setEnabled(true);
	}
	
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){
		if(click == 1){
			toDraw();
		}
		click=1;
	}
	
	public void mouseDragged(MouseEvent e){
		x2=e.getX();
		y2=e.getY();
		if(parentFrame.drawMethod==7 || parentFrame.drawMethod==8){
			draw(x1,y1,x2,y2);
			x1=e.getX();
			y1=e.getY();
		}
		if(parentFrame.drawMethod!=9)
			repaint();
	}

	public void mouseMoved(MouseEvent e) {
		parentFrame.show_x=x2=e.getX();
		parentFrame.show_y=y2=e.getY();
		
		parentFrame.jLabel[0].setText(parentFrame.show_x+","+parentFrame.show_y);
		click=0;
		if(parentFrame.drawMethod==7 || parentFrame.drawMethod==8 || cut==2)
			repaint();
	}
	
	public void draw(int input_x1,int input_y1,int input_x2,int input_y2){
		if(parentFrame.drawMethod == STRAIGHTLINE){
			parentFrame.shape=line2D;
			line2D.setLine(input_x1,input_y1,input_x2,input_y2);
		}
		else if(parentFrame.drawMethod == RECTANGLE){
			parentFrame.shape=rectangle2D;
			rectangle2D.setRect(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1-input_y2));
		}
		else if(parentFrame.drawMethod == ECLIPSE){
			parentFrame.shape=ellipse2D;
			ellipse2D.setFrame(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1-input_y2));
		}
		else if(parentFrame.drawMethod == ROUNDRECT){
			parentFrame.shape=roundRectangle2D;
			roundRectangle2D.setRoundRect(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1-input_y2),10.0f,10.0f);
		}
		else if(parentFrame.drawMethod == CURVE){
			parentFrame.shape=cubicCurve2D;
			if(step == 0){
				cubicCurve2D.setCurve(input_x1,input_y1,input_x1,input_y1,input_x2,input_y2,input_x2,input_y2);
				temp_x1=input_x1;
				temp_y1=input_y1;
				temp_x2=input_x2;
				temp_y2=input_y2;
				step_chk=0;
			}
			else if(step == 1){
				cubicCurve2D.setCurve(temp_x1,temp_y1,input_x2,input_y2,input_x2,input_y2,temp_x2,temp_y2);
				temp_x3=input_x2;
				temp_y3=input_y2;
				step_chk=1;
			}
			else if(step == 2){
				cubicCurve2D.setCurve(temp_x1,temp_y1,temp_x3,temp_y3,input_x2,input_y2,temp_x2,temp_y2);
				step_chk=2;
			}
		}
		
		else if(parentFrame.drawMethod == POLYGON){
			parentFrame.shape=polygon;
			
		}
		else if(parentFrame.drawMethod==7 || parentFrame.drawMethod==8){
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
			
			parentFrame.shape=line2D;
			line2D.setLine(input_x1,input_y1,input_x2,input_y2);
			if(parentFrame.drawMethod==7)
				g2d_bufImg.setPaint(parentFrame.color_border);
			else
				g2d_bufImg.setPaint(Color.white);
			g2d_bufImg.setStroke(parentFrame.stroke);
			g2d_bufImg.draw(parentFrame.shape);
		}
		
		else if(parentFrame.drawMethod == 9){
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		FontRenderContext frc = g2d_bufImg.getFontRenderContext();
    		jDialog.show();
    		Font f = new Font(textField_font.getText(), valBold + valItalic,size);
    		TextLayout tl = new TextLayout(textField_word.getText(), f, frc);
    		double sw = tl.getBounds().getWidth();
    		double sh = tl.getBounds().getHeight();

    		AffineTransform Tx = AffineTransform.getScaleInstance(1, 1);
    		Tx.translate(input_x2,input_y2 + sh);
    		parentFrame.shape = tl.getOutline(Tx);
		}
		else if(parentFrame.drawMethod==10){
			parentFrame.shape=rectangle2D;
			rectangle2D.setRect(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1-input_y2));
		}

	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paint(g2d);

		if(press==1 && parentFrame.drawMethod!=10 && !(x1 < 0 || y1 < 0)) {
			draw(x1,y1,x2,y2);
			if(parentFrame.drawMethod==8) return;
			if(parentFrame.color_inside!=null){
				g2d.setPaint(parentFrame.color_inside);
				g2d.fill(parentFrame.shape);
			}
			if(parentFrame.color_border!=null){
				g2d.setPaint(parentFrame.color_border);
				g2d.setStroke(parentFrame.stroke);
				g2d.draw(parentFrame.shape);
			}
		}

		if(parentFrame.drawMethod==10 && cut==0){
			g2d.setPaint(Color.black);
			g2d.setStroke(basicStroke_select);
			rectangle2D_select.setRect(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2));
			g2d.draw(rectangle2D_select);
		}
		if(cut==1){
			g2d.setPaint(Color.black);
			g2d.setStroke(basicStroke_select);
			rectangle2D_select.setRect(select_x,select_y,select_w,select_h);
			g2d.draw(rectangle2D_select);
		}
		if(cut==2){
				g2d.drawImage(bufImg_cut,x2,y2,this);
			}
		if(parentFrame.drawMethod == 7 || parentFrame.drawMethod == 8){
			g2d.setPaint(Color.black);
			g2d.setStroke(basicStroke_pen);
			ellipse2D_pan.setFrame(x2-parentFrame.setPanel.number/2,y2-parentFrame.setPanel.number/2,parentFrame.setPanel.number,parentFrame.setPanel.number);
			g2d.draw(ellipse2D_pan);
		}
	}
}
