package paint;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SetPanel extends JPanel implements ItemListener, ChangeListener{
	private Painter parentFrame;
	protected	JPanel jPanel_set1=new JPanel();
	protected	JPanel jPanel_set2=new JPanel();
	protected	JPanel temp0, temp1,temp2;
	
	public JCheckBox jCheckBox = new JCheckBox();
	protected BufferedImage bufImg = new BufferedImage(50 ,50,BufferedImage.TYPE_3BYTE_BGR);
	protected JLabel jlbImg=new JLabel();
	float data[]={20};
	JLabel pie[]=new JLabel[3];
	public int number=5;
	JSpinner lineWidthSelect = new JSpinner();
	JRadioButton style[] = new JRadioButton[ 5 ];
	ButtonGroup styleGroup = new ButtonGroup() ,pieGroup = new ButtonGroup();
	int i;
    
	public SetPanel(Frame frame){
		parentFrame = (Painter) frame;
		this.setLayout(null);
		this.add(jPanel_set1);

		jlbImg.setIcon(new ImageIcon(bufImg));
		jPanel_set1.setLayout(new FlowLayout());
		jPanel_set1.setBounds(new Rectangle(0, 0, 100, 160));
		jPanel_set1.setBorder( new TitledBorder(null, "Setting",TitledBorder.LEFT, TitledBorder.TOP) );
		lineWidthSelect.setValue(new Integer(5));

		temp0 = new JPanel(new GridLayout(2,1));
		temp1=new JPanel(new GridLayout(2,1));
		temp2=new JPanel(new GridLayout(2,1));
//		 
//		for(i=0;i<=1;i++){
//			style[i] = new JRadioButton();
//			styleGroup.add(style[i]);
//			style[i].addActionListener(this);
//		}
//		style[0].setSelected( true );
  
		temp1.add(new JLabel("  Thickness: "));
		temp1.add(lineWidthSelect);
		
		temp2.add(new JLabel(" Dotted: "));
		temp2.add(jCheckBox);
		
		
		temp0.add(temp1);
		temp0.add(temp2);

		
		jPanel_set1.add(temp0);
		lineWidthSelect.addChangeListener( this );
		jCheckBox.addItemListener( this );
		parentFrame.stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
	}
	
//	public void pencil_add_ctrl(){
////		style[0].setSelected(true);
////		style[1].setEnabled(false);
//		jCheckBox.setSelected(false);
//		jCheckBox.setEnabled(false);
//		BasicStroke tempStroke = (BasicStroke) parentFrame.stroke;
//		parentFrame.stroke = new BasicStroke(tempStroke.getLineWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
//	}
//	
//	public void pencil_remove_ctrl(){
//		style[1].setEnabled(true);
//		jCheckBox.setEnabled(true);
//	}
	
//	public void pie_add_ctrl(){
//		pie[0].setEnabled(true);
//		pie[1].setEnabled(true);
//		pie[2].setEnabled(true);
//		style[2].setEnabled(true);
//		style[3].setEnabled(true);
//		style[4].setEnabled(true);
//	}
//	
//	public void pie_remove_ctrl(){
//		pie[0].setEnabled(false);
//		pie[1].setEnabled(false);
//		pie[2].setEnabled(false);
//		style[2].setEnabled(false);
//		style[3].setEnabled(false);
//		style[4].setEnabled(false);
//	}
//	
//	public void actionPerformed( ActionEvent e ){
//		BasicStroke tempStroke = (BasicStroke) parentFrame.stroke;
//		if ( e.getSource() == style[0] )
//			parentFrame.stroke = new BasicStroke( tempStroke.getLineWidth(), BasicStroke.CAP_ROUND, tempStroke.getLineJoin(), tempStroke.getMiterLimit(), tempStroke.getDashArray(), tempStroke.getDashPhase() );
//		else if ( e.getSource() == style[1] )
//			parentFrame.stroke = new BasicStroke( tempStroke.getLineWidth(), BasicStroke.CAP_BUTT, tempStroke.getLineJoin(), tempStroke.getMiterLimit(), tempStroke.getDashArray(), tempStroke.getDashPhase() );
//		else if ( e.getSource() == style[2] )
//			parentFrame.drawPanel.pie_shape=Arc2D.CHORD;
//		else if ( e.getSource() == style[3] )
//			parentFrame.drawPanel.pie_shape=Arc2D.OPEN;
//		else if ( e.getSource() == style[4] )
//			parentFrame.drawPanel.pie_shape=Arc2D.PIE;
//	}
	
	public void stateChanged(ChangeEvent e){
		number = Integer.parseInt(lineWidthSelect.getValue().toString());
		if(number <= 0) {
			lineWidthSelect.setValue(new Integer(1));
			number = 1;
		}
    	BasicStroke tempStroke = (BasicStroke) parentFrame.stroke;
		parentFrame.stroke = new BasicStroke( number, tempStroke.getEndCap(), tempStroke.getLineJoin(), tempStroke.getMiterLimit(), tempStroke.getDashArray(), tempStroke.getDashPhase() );
	}
	
	public void itemStateChanged( ItemEvent e ){
		BasicStroke tempStroke = (BasicStroke) parentFrame.stroke;
		if ( e.getSource() == jCheckBox ){
			if ( e.getStateChange() == ItemEvent.SELECTED )
				parentFrame.stroke = new BasicStroke( tempStroke.getLineWidth(), tempStroke.getEndCap(), tempStroke.getLineJoin(), 10, data, 0 );
			else
				parentFrame.stroke = new BasicStroke( tempStroke.getLineWidth(), tempStroke.getEndCap(), tempStroke.getLineJoin());
		}
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(100, 300);
	}
}
