import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MyPanel extends JComponent {
	private JButton enter;
	private JTextArea from, to;
	private JLabel toLabel, fromLabel;
	private int height;
	private final static int MARGIN = 25;

	public MyPanel(int height) {
		super();
		
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "Control Panel");
		border.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(border);

		
		this.height = height;

		from = new JTextArea();
		from.setBounds(240, this.height+MARGIN, 100, 20);
		from.setBorder(new BevelBorder(1));

		fromLabel = new JLabel();
		fromLabel.setBounds(135, this.height+MARGIN, 100, 20);
		fromLabel.setText("FROM CITY NAME:");

		to = new JTextArea();
		to.setBounds(450, this.height+MARGIN, 100, 20);
		to.setBorder(new BevelBorder(1));

		toLabel = new JLabel();
		toLabel.setBounds(350, this.height+MARGIN, 100, 20);
		toLabel.setText("     TO CITY NAME:");

		enter = new JButton();
		enter.setBounds(MARGIN, this.height+MARGIN-5, 100, 30);
		enter.setText("ENTER");

		this.add(enter);
		this.add(from);
		this.add(to);
		this.add(fromLabel);
		this.add(toLabel, BorderLayout.SOUTH);
	}
	
}
