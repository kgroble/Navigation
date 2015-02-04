import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class MyPanel extends JComponent{
	private JButton enter;
	private JTextArea from,to;
	private JLabel toLabel, fromLabel;
	
	public MyPanel(){
		super();

		from=new JTextArea();
		from.setBounds(140, 10, 100, 20);
		to= new JTextArea();
		to.setBounds(250,10,100,20);
		enter= new JButton();
		enter.setBounds(10, 5, 100, 30);
		enter.setText("ENTER");
		this.add(enter);
		this.add(from);
		this.add(to);
		//add basic buttons and txt boxes
		
	}
}
