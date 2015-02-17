import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OpeningWindow extends JFrame {

	private static final int HEIGHT = 400, WIDTH = 400;

	public OpeningWindow() {
		this.setMinimumSize(new Dimension(HEIGHT, WIDTH));
		this.setMaximumSize(new Dimension(HEIGHT, WIDTH));

		// this.setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		JButton ireland = new JButton();
		panel.add(ireland);
		ireland.setBounds(0, 0, HEIGHT / 2, WIDTH / 2);
		ireland.setText("ireland");
		ireland.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Graph<City, Connection, String> map = new Graph<City, Connection, String>();
				new Setup(map, "ireland");
				AStar a = new AStar(map);
				new ApplicationWindow(map, a);
				setVisible(false);
			}
		});

		JButton texas = new JButton();
		panel.add(texas);
		texas.setBounds(WIDTH / 2, 0, HEIGHT / 2, WIDTH / 2);
		texas.setText("texas");
		texas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Graph<City, Connection, String> map = new Graph<City, Connection, String>();
				new Setup(map, "texas");
				AStar a = new AStar(map);
				new ApplicationWindow(map, a);
				setVisible(false);
			}
		});

		panel.setBounds(0, 0, HEIGHT, WIDTH);
		this.add(panel);

		setVisible(true);
	}
}
