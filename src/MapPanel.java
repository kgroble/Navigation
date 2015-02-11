import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.sun.javafx.tk.FontMetrics;

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
	static final float CONNECTION_WIDTH = 1.2f;
	static final Color CITY_COLOR = Color.BLACK;
	static final Color CONNECTION_COLOR = Color.GRAY;

	private static final long serialVersionUID = 1L;
	private Graph<City, Connection, String> map;

	double zoom = 1.0;
	double centerX = 0.0;
	double centerY = 0.0;

	public MapPanel(Graph<City, Connection, String> map) {
		super();
		this.map = map;

		// Add mouse function
		MouseHandler aHandler = new MouseHandler();
		this.addMouseListener(aHandler);
		this.addMouseWheelListener(aHandler);
		this.addMouseMotionListener(aHandler);

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

	public class MouseHandler implements MouseListener, MouseWheelListener, MouseMotionListener {

		Point lastMousePoint = new Point(0,0);
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
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
			lastMousePoint = e.getPoint();
			System.out.println(e.getPoint());
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

		@Override
		public void mouseDragged(MouseEvent e) {
			centerX += (e.getX() - lastMousePoint.getX());
			centerY += (e.getY() - lastMousePoint.getY());
			lastMousePoint = e.getPoint();
			System.out.println(e.getX());
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Translate for pan
		g2d.translate(centerX, centerY);

		ArrayList<City> cities = map.getElements();
		for (City city : cities) {
			double translateX = city.getXCoord() * zoom;
			double translateY = city.getYCoord() * zoom;
			g2d.translate(translateX, translateY);
			
			// Draw city names:
			java.awt.FontMetrics fontMetric = g2d.getFontMetrics();
			int stringWidth = fontMetric.stringWidth(city.getName());
			g2d.drawString(city.getName(), (int)(-stringWidth/2), (int) (CITY_SIZE * zoom + 10));
			HashMap<City, Connection> connectedCitesHashMap = map
					.getConnectedElements(city.getName());

			g2d.setColor(CONNECTION_COLOR);
			HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
			g2d.setStroke(new BasicStroke((float)(CONNECTION_WIDTH * zoom)));
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
			g2d.setStroke(new BasicStroke(1));
			
			// Draw city
			Ellipse2D.Double circle = new Ellipse2D.Double();
			circle.height = CITY_SIZE * zoom;
			circle.width = CITY_SIZE * zoom;
			g2d.setColor(CITY_COLOR);
			double centerLinks = CITY_SIZE * zoom / 2;
			g2d.translate(-centerLinks, -centerLinks);
			g2d.fill(circle);
			g2d.translate(centerLinks, centerLinks);
		
			g2d.translate(-translateX, -translateY);
		}

		// Translate for pan
		g2d.translate(-centerX, -centerY);
	}

}
