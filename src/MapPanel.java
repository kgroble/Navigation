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

import org.junit.experimental.max.MaxCore;

import sun.reflect.generics.tree.Tree;

import com.sun.javafx.tk.FontMetrics;

/**
 * 
 * A panel that will visually represent the data contained in a graph object
 * 
 * @author nunnalcs
 *
 */
public class MapPanel extends JPanel {

	static final float ZOOM_SPEED = 1.05f;
	static final float ZOOM_MAX = 10.0f;
	static final float ZOOM_MIN = 0.01f;
	static final float CITY_SIZE = 10.0f;
	static final float CONNECTION_WIDTH = 1.2f;
	static final float PATH_CONNECTION_WIDTH = 1.6f;
	static final Color CITY_COLOR = Color.BLACK;
	static final Color CONNECTION_COLOR = Color.GRAY;
	static final Color PATH_COLOR = Color.BLUE;

	private static final long serialVersionUID = 1L;

	private Graph<City, Connection, String> map;
	private City selectedCity = null;
	private HashMap<Integer, ArrayList<City>> clickMap = new HashMap<Integer, ArrayList<City>>();
	int partitionWidth;
	final int partitionCount = 30;
	final int selectionMaxRadius = 20;

	double zoom = 1.0;
	double centerX = 0.0;
	double centerY = 0.0;
	private ArrayList<Path> pathsToDraw;

	public MapPanel(Graph<City, Connection, String> map) {
		super();
		this.map = map;
		updateClickMap();
		
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

		this.pathsToDraw = new ArrayList<Path>();
	}

	public void addPath(Path pathToAdd) {
		this.pathsToDraw.add(pathToAdd);
		this.repaint();
	}

	public class MouseHandler implements MouseListener, MouseWheelListener,
			MouseMotionListener {

		Point lastMousePoint = new Point(0, 0);

		@Override
		public void mouseClicked(MouseEvent e) {
			
			Point clickPoint = e.getPoint();
			
			clickPoint.x -= centerX;
			clickPoint.y -= centerY;
			
			clickPoint.x /= zoom;
			clickPoint.y /= zoom;
			
			int partitionNumber = Math.floorDiv((int) clickPoint.x,
					partitionWidth);
			if (partitionNumber < 0)
				partitionNumber = 0;
			else if (partitionNumber >= partitionCount)
				partitionNumber = partitionCount - 1;
			
			ArrayList<City> citiesToSearch = new ArrayList<City>();
			
			citiesToSearch.addAll(clickMap.get(partitionNumber));
			if (partitionNumber != 0)
				citiesToSearch.addAll(clickMap.get(partitionNumber - 1));
			if (partitionNumber != partitionCount - 1)
				citiesToSearch.addAll(clickMap.get(partitionNumber + 1));
			
			int closestCityDist = selectionMaxRadius;
			City closestCity = null;
			for (City city : citiesToSearch)
			{ 
				int xDistSquared = (int) Math.pow(city.getXCoord() - clickPoint.x, 2);
				int yDistSquared = (int) Math.pow(city.getYCoord() - clickPoint.y, 2);
				int distance = (int) Math.sqrt(xDistSquared + yDistSquared);
				if (distance < closestCityDist)
				{
					closestCityDist = distance;
					closestCity = city;
				}
			}
			
			selectedCity = closestCity;
			repaint();
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
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				if (arg0.getPreciseWheelRotation() > 0) {
					zoom /= arg0.getPreciseWheelRotation() * ZOOM_SPEED;
					centerX = (arg0.getPreciseWheelRotation()
							* (1 - (ZOOM_SPEED - 1)) * (centerX - arg0.getX()) + arg0
							.getX());
					centerY = (arg0.getPreciseWheelRotation()
							* (1 - (ZOOM_SPEED - 1)) * (centerY - arg0.getY()) + arg0
							.getY());
				} else {
					zoom *= Math.abs(arg0.getPreciseWheelRotation()
							* ZOOM_SPEED);
					centerX = (Math.abs(arg0.getPreciseWheelRotation())
							* ZOOM_SPEED * (centerX - arg0.getX()) + arg0
							.getX());
					centerY = (Math.abs(arg0.getPreciseWheelRotation())
							* ZOOM_SPEED * (centerY - arg0.getY()) + arg0
							.getY());
				}
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			centerX += (e.getX() - lastMousePoint.getX());
			centerY += (e.getY() - lastMousePoint.getY());
			lastMousePoint = e.getPoint();
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
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Translate for pan
		g2d.translate(centerX, centerY);

		// Draw links
		g2d.setColor(CONNECTION_COLOR);
		g2d.setStroke(new BasicStroke((float) (CONNECTION_WIDTH * zoom)));
		ArrayList<City> cities = map.getElements();
		for (City city : cities) {
			double translateX = city.getXCoord() * zoom;
			double translateY = city.getYCoord() * zoom;
			g2d.translate(translateX, translateY);
			HashMap<City, Connection> connectedCitesHashMap = map
					.getConnectedElements(city.getName());
			for (Map.Entry<City, Connection> entry : connectedCitesHashMap
					.entrySet()) {
				City toCity = entry.getKey();
				int xDiff = (int) ((toCity.getXCoord() - city.getXCoord()) * zoom);
				int yDiff = (int) ((toCity.getYCoord() - city.getYCoord()) * zoom);
				g2d.drawLine(0, 0, xDiff, yDiff);

			}
			g2d.translate(-translateX, -translateY);
		}
		g2d.setColor(CITY_COLOR);
		g2d.setStroke(new BasicStroke(1));

		// Draw cities and names
		for (City city : cities) {
			double translateX = city.getXCoord() * zoom;
			double translateY = city.getYCoord() * zoom;
			g2d.translate(translateX, translateY);

			// Draw city names:
			java.awt.FontMetrics fontMetric = g2d.getFontMetrics();
			int stringWidth = fontMetric.stringWidth(city.getName());
			g2d.drawString(city.getName(), (int) (-stringWidth / 2),
					(int) (CITY_SIZE * zoom + 10));

			// Draw city
			Ellipse2D.Double circle = new Ellipse2D.Double();
			circle.height = CITY_SIZE * zoom;
			circle.width = CITY_SIZE * zoom;
			g2d.setColor(CITY_COLOR);
			double centerLinks = CITY_SIZE * zoom / 2;
			g2d.translate(-centerLinks, -centerLinks);
			// TODO: Probably faster to draw this seperate
			if (selectedCity != null && city.equals(selectedCity))
			{
				g2d.setColor(Color.RED);
				g2d.fill(circle);
				g2d.setColor(CITY_COLOR);
			}
			else
				g2d.fill(circle);
			g2d.translate(centerLinks, centerLinks);

			g2d.translate(-translateX, -translateY);
		}

		drawPaths(g2d);
		
		// Translate for pan
		g2d.translate(-centerX, -centerY);
	}

	private void drawPaths(Graphics2D g2d) {
		g2d.setColor(PATH_COLOR);
		for (Path path : pathsToDraw) {
			ArrayList<Point> linkPoints = new ArrayList<Point>();
			for (City city : path.getCities()) {
				linkPoints.add(new Point((int) (city.getXCoord() * zoom),
						(int) (city.getYCoord() * zoom)));
				double translateX = city.getXCoord() * zoom;
				double translateY = city.getYCoord() * zoom;
				g2d.translate(translateX, translateY);

				Ellipse2D.Double circle = new Ellipse2D.Double();
				circle.height = CITY_SIZE * zoom;
				circle.width = CITY_SIZE * zoom;
				double centerLinks = CITY_SIZE * zoom / 2;
				g2d.translate(-centerLinks, -centerLinks);
				g2d.fill(circle);
				g2d.translate(centerLinks, centerLinks);

				g2d.translate(-translateX, -translateY);
			}

			g2d.setStroke(new BasicStroke(
					(float) (PATH_CONNECTION_WIDTH * zoom)));
			for (int i = 0; i < linkPoints.size() - 1; i++) {
				g2d.drawLine(linkPoints.get(i).x, linkPoints.get(i).y,
						linkPoints.get(i + 1).x, linkPoints.get(i + 1).y);
			}
			g2d.setStroke(new BasicStroke(1));
		}
		g2d.setColor(CITY_COLOR);
	}

	private void updateClickMap() {
		clickMap.clear();

		// Find out how wide the map is
		int maxX = 0;
		for (City city : map.getElements())
			if (city.getXCoord() > maxX)
				maxX = (int) city.getXCoord();

		// Prime the clickMap hashMap with emtpy arrayLists;
		partitionWidth = maxX / partitionCount;
		for (int i = 0; i < partitionCount; i++)
			clickMap.put(i, new ArrayList<City>());

		for (City city : map.getElements()) {
			int partitionNumber = Math.floorDiv((int) city.getXCoord(),
					partitionWidth);
			if (partitionNumber >= partitionCount)
				partitionNumber = partitionCount - 1;
			clickMap.get(partitionNumber).add(city);
		}
	}
}
