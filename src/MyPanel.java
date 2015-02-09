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
	private int width, height;
	private final static int MARGIN = 25;

	public MyPanel(int width, int height) {
		super();
		
		MouseHandler aHandler = new MouseHandler();
		this.addMouseListener(aHandler);
		this.addMouseWheelListener(aHandler);
		
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "Control Panel");
		border.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(border);

		
		this.height = height;
		this.width = width;

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
	
	public class MouseHandler implements MouseListener, MouseWheelListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.println(e.getX()+", "+e.getY());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getScrollType()==MouseWheelEvent.WHEEL_UNIT_SCROLL){
				System.out.println("ok");
			}
			
		}
		
	}
}
