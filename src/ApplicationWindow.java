import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.omg.CORBA.PRIVATE_MEMBER;

public class ApplicationWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 600;
	private static final int CONTROL_PANEL_WIDTH = 150;

	// Serves as the 'background' that the other two panels will be added to:
	private JPanel containerPanel;
	private MapPanel mapPanel;
	private ControlPanel controlPanel;

	private Graph<City, Connection, String> map;
	private AStar aStar;
	private ActionListener restartListen;
	private MyDataList<String> citiesList;
	private JList<String> listBox;

	public ApplicationWindow(Graph<City, Connection, String> map, AStar a) {
		this.aStar = a;
		this.citiesList = new MyDataList<String>();

		// Set up jframe
		this.setTitle("Navigation");

		// Set up container panel
		this.containerPanel = new JPanel();
		this.containerPanel.setLayout(null);
		this.containerPanel.setBackground(new Color(160, 160, 160));
		this.add(containerPanel);

		this.listBox = new JList<String>(citiesList);
		this.listBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.listBox.setBounds(ControlPanel.MARGIN,
				ControlPanel.MARGIN * 2 + 20, CONTROL_PANEL_WIDTH
						- ControlPanel.MARGIN * 2, 200);

		restartListen = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				containerPanel.remove(mapPanel);
				citiesList.clear();

				listBox.updateUI();

				// create and add mapPanel
				mapPanel = new MapPanel(map, ApplicationWindow.this);
				mapPanel.setBounds(CONTROL_PANEL_WIDTH, 0, FRAME_WIDTH
						- CONTROL_PANEL_WIDTH, FRAME_HEIGHT);
				mapPanel.setPreferredSize(new Dimension(FRAME_WIDTH
						- CONTROL_PANEL_WIDTH, FRAME_HEIGHT));
				containerPanel.add(mapPanel);

				setSize(FRAME_WIDTH, FRAME_HEIGHT);
				setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
				repaint();
			}

		};

		// Set up controlsPanel
		this.controlPanel = new ControlPanel(0, restartListen);
		this.controlPanel.setBounds(0, 0, CONTROL_PANEL_WIDTH, FRAME_HEIGHT);
		this.controlPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH,
				FRAME_HEIGHT));
		// this.controlPanel.setBackground(Color.GRAY);
		this.containerPanel.add(controlPanel);

		// Set up mapPanel
		this.mapPanel = new MapPanel(map, this);
		this.mapPanel.setBounds(CONTROL_PANEL_WIDTH, 0, FRAME_WIDTH
				- CONTROL_PANEL_WIDTH, FRAME_HEIGHT);
		this.mapPanel.setPreferredSize(new Dimension(FRAME_WIDTH
				- CONTROL_PANEL_WIDTH, FRAME_HEIGHT));
		this.map = map;
		this.containerPanel.add(this.mapPanel);

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// retrieve new height and width info
				int newWidth = getWidth();
				int newHeight = getHeight();

				mapPanel.setPreferredSize(new Dimension(newWidth
						- CONTROL_PANEL_WIDTH, newHeight));
				controlPanel.setPreferredSize(new Dimension(
						CONTROL_PANEL_WIDTH, newHeight));

				// manipulate the panels
				controlPanel.setBounds(0, 0, CONTROL_PANEL_WIDTH, newHeight);
				mapPanel.setBounds(CONTROL_PANEL_WIDTH, 0, newWidth
						- CONTROL_PANEL_WIDTH, newHeight);

				// update the frame
				repaint();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Adds cityName to the list if it is a proper city name, otherwise it does
	 * nothing
	 * 
	 * @param cityName
	 */
	public void addToList(String cityName) {
		String cityText = cityName.toLowerCase();
		ArrayList<City> cities = map.getElements();
		for (City city : cities) {
			String cityName1 = city.getName().toLowerCase();
			if (cityName1.equals(cityText)) {
				citiesList.add(city.getName());
				listBox.updateUI();
				break;
			}
		}
	}

	/**
	 * removes an element from the specified index if that index is in myList's
	 * range
	 * 
	 * @param index
	 */
	public void removeFromList(int index) {
		if (index >= 0 && index < citiesList.size()) {
			citiesList.remove(index);
			listBox.updateUI();
		}
	}

	/**
	 * @return the list of cities in the path the user has created
	 */
	public String[] getCityNames() {
		return (String[]) this.citiesList.toArray();
	}

	public class ControlPanel extends JComponent {
		private static final long serialVersionUID = 7088760637095647696L;
		private JButton add, restart, pathDistance, pathTime, clear, remove;
		private JTextArea cityName;
		private final static int MARGIN = 10;

		public ControlPanel(int height, ActionListener ab) {
			super();

			this.cityName = new JTextArea();
			this.cityName.setBounds(MARGIN, MARGIN, CONTROL_PANEL_WIDTH - 2
					* MARGIN, 20);
			this.cityName.setBorder(new BevelBorder(1));
			this.add(this.cityName);

			JScrollPane listScroller = new JScrollPane(listBox);
			listScroller.setBounds(MARGIN, MARGIN * 3 + 40, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 100);
			this.add(listScroller);

			clear = new JButton();
			clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					citiesList.clear();
					listBox.updateUI();
				}
			});
			clear.setBounds(MARGIN, MARGIN * 5 + 160, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 20);
			clear.setText("Clear List");
			this.add(clear);

			add = new JButton();
			add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addToList(cityName.getText());
				}

			});
			add.setBounds(MARGIN, MARGIN * 2 + 20, CONTROL_PANEL_WIDTH - MARGIN
					* 2, 20);
			add.setText("Add City");
			this.add(add);

			remove = new JButton();
			remove.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int cityIndex = listBox.getSelectedIndex();
					removeFromList(cityIndex);
				}
			});
			remove.setBounds(MARGIN, MARGIN * 4 + 140, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 20);
			remove.setText("Remove City");
			this.add(remove);

			pathTime = new JButton();
			pathTime.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<String> waypoints = new ArrayList<String>(
							ApplicationWindow.this.citiesList);
					Path path = ApplicationWindow.this.aStar
							.findFastestPathWithWayPoints(waypoints);
					ApplicationWindow.this.mapPanel.addPath(path);
				}
			});
			pathTime.setBounds(MARGIN, MARGIN * 7 + 200, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 20);
			pathTime.setText("Path Time");
			this.add(pathTime);

			pathDistance = new JButton();
			pathDistance.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<String> waypoints = new ArrayList<String>(
							ApplicationWindow.this.citiesList);
					Path path = ApplicationWindow.this.aStar
							.findShortestPathWithWayPoints(waypoints);
					ApplicationWindow.this.mapPanel.addPath(path);
				}
			});

			pathDistance.setBounds(MARGIN, MARGIN * 6 + 180,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 20);
			pathDistance.setText("Path Distance");
			this.add(pathDistance);

			restart = new JButton();
			restart.setBounds(MARGIN, FRAME_HEIGHT - 58 - MARGIN,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 20);
			restart.setText("RESTART");
			restart.addActionListener(ab);
			this.add(restart);

			this.setBackground(Color.GRAY);
			this.setOpaque(true);
		}

	}
}

