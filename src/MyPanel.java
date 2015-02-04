import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class MyPanel extends JComponent{
	private JButton enter;
	private JTextArea from,to;
	private JLabel toLabel, fromLabel;
	private int width, height;
	private final static int MARGIN=10;
	
	public MyPanel(int width, int height){
		super();

		this.height=height;
		this.width=width;
		
		from=new JTextArea();
		from.setBounds(240, MARGIN, 100, 20);
		from.setBorder(new LineBorder(Color.BLACK));
		
		fromLabel= new JLabel();
		fromLabel.setBounds(135,MARGIN,100,20);
		fromLabel.setText("FROM CITY NAME:");
		
		to= new JTextArea();
		to.setBounds(450,MARGIN,100,20);
		to.setBorder(new LineBorder(Color.BLACK));
		
		toLabel= new JLabel();
		toLabel.setBounds(350,MARGIN,100,20);
		toLabel.setText("     TO CITY NAME:");
		
		enter= new JButton();
		enter.setBounds(MARGIN, 5, 100, 30);
		enter.setText("ENTER");
		
		this.add(enter);
		this.add(from);
		this.add(to);
		this.add(fromLabel);
		this.add(toLabel);
		
	}
}
