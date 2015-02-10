import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * 
 * A panel that will visually represent the data contained in a graph object
 * 
 * @author nunnalcs
 *
 */
public class MapPanel extends JPanel {

	static final double ZOOM_SPEED = 1.05;
	static final double CITY_SIZE = 10.0;

	private static final long serialVersionUID = 1L;
	private Graph<City, Connection, String> map;

	double zoom = 1.0;
	double xPos = 0.0;
	double yPos = 0.0;

	public MapPanel(Graph<City, Connection, String> map) {
		super();
		this.map = map;

		// Add mouse function
		MouseHandler aHandler = new MouseHandler();
		this.addMouseListener(aHandler);
		this.addMouseWheelListener(aHandler);

		// add border
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "MAP");
		border.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(border);
	}

	public String fromTo(String fromCity, String toCity) {
		// add code to make stuff change in the map to highlight route, then
		// transfer data to myPanel in a string
		return null;
	}

	public class MouseHandler implements MouseListener, MouseWheelListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.println(e.getX() + ", " + e.getY());
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
			if (arg0.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				if (arg0.getPreciseWheelRotation() > 0)
					zoom *= arg0.getPreciseWheelRotation() * ZOOM_SPEED;
				else
					zoom /= Math.abs(arg0.getPreciseWheelRotation()
							* ZOOM_SPEED);
			}
			System.out.println(zoom);
			repaint();
		}

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);

		// Translate for pan
		g2d.translate(xPos, yPos);

		ArrayList<City> cities = map.getElements();
		for (City city : cities) {
			Ellipse2D.Double circle = new Ellipse2D.Double();
			circle.height = CITY_SIZE * zoom;
			circle.width = CITY_SIZE * zoom;
			double translateX = city.getXCoord() * zoom;
			double translateY = city.getYCoord() * zoom;
			g2d.translate(translateX, translateY);

			g2d.fill(circle);
			g2d.drawString(city.getName(), 0, (int) (CITY_SIZE * 2 * zoom));
			HashMap<City, Connection> connectedCitesHashMap = map
					.getConnectedElements(city.getName());

			HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
			double centerLinks = CITY_SIZE * zoom / 2;
			g2d.translate(centerLinks, centerLinks);
			for (Map.Entry<City, Connection> entry : connectedCitesHashMap
					.entrySet()) {
				City toCity = entry.getKey();
				if (!visited.containsKey(toCity.getName())) {
					int xDiff = (int) ((toCity.getXCoord() - city.getXCoord()) * zoom);
					int yDiff = (int) ((toCity.getYCoord() - city.getYCoord()) * zoom);
					g2d.drawLine(0, 0, xDiff, yDiff);
					visited.put(toCity.getName(), true);
				}
			}
			g2d.translate(-centerLinks, -centerLinks);
			g2d.translate(-translateX, -translateY);
		}

		// Translate for pan
		g2d.translate(-xPos, -yPos);
	}

}
