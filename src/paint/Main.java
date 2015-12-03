package paint;

import javax.swing.UIManager;

public class Main {
	public static void main( String args[] ){
//		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
//		catch(Exception e){e.printStackTrace();}
		Painter app = new Painter();
		app.setVisible(true);
		
		
	}

}
