import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OpeningWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6297715645298420697L;
	private static final int HEIGHT = 200, WIDTH = 400;
	private static final int FRAME_WIDTH = 16, FRAME_HEIGHT = 39;

	public OpeningWindow() {
		this.setMinimumSize(new Dimension(WIDTH + FRAME_WIDTH, HEIGHT + FRAME_HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH + FRAME_WIDTH, HEIGHT + FRAME_HEIGHT));

		// this.setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		JButton ireland = new JButton();
		panel.add(ireland);
		ireland.setBounds(0, 0, WIDTH / 2, HEIGHT);
		ireland.setText("Map of Ireland");
		ireland.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Graph<City, Connection, String> map = new Graph<City, Connection, String>();
				new Setup(map, "ireland");
				RouteFinder a = new RouteFinder(map);
				new ApplicationWindow(map, a);
				setVisible(false);
			}
		});

		JButton texas = new JButton();
		panel.add(texas);
		texas.setBounds(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
		texas.setText("Map of Texas");
		texas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Graph<City, Connection, String> map = new Graph<City, Connection, String>();
				new Setup(map, "texas");
				RouteFinder a = new RouteFinder(map);
				new ApplicationWindow(map, a);
				setVisible(false);
			}
		});

		panel.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(panel);

		setVisible(true);
	}
}
