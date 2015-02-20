import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 
 * A panel that will visually represent the data contained in a graph object
 * 
 * @author nunnalcs
 *
 */
public class MapPanel extends JPanel {

	private static final float ZOOM_SPEED = 1.05f;
	private static final float ZOOM_MAX = 30000.0f;
	private static final float ZOOM_MIN = 0.1f;

	private static final float SELECTED_CITY_SIZE = 3.0f;
	private static final float CITY_SIZE = 2.5f;
	private static final float CONNECTION_CITY_SIZE = 3.5f;

	private static final float CONNECTION_WIDTH = .3f;
	private static final float PATH_CONNECTION_WIDTH = .3f;

	private static final Color SELECTED_CITY_COLOR = Color.RED;
	private static final Color CITY_COLOR = Color.BLACK;
	private static final Color CONNECTION_COLOR = Color.GRAY;
	private static final long serialVersionUID = 1L;

	private ArrayList<Color> connectionColors;

	private Graph<City, Connection, String> map;
	private City selectedCity = null;
	private HashMap<Integer, ArrayList<City>> clickMap = new HashMap<Integer, ArrayList<City>>();
	private JButton addCityButton;

	
	private int partitionWidth;
	private int partitionCount = 200;
	private final int selectionMaxRadius = 40;
	private int biggestStringWidth = 0;

	// The bounds on the map
	private int xMin, xMax, yMin, yMax;

	// Used to pan and zoom
	private double zoom = 1.0;
	private double centerX = 0.0;
	private double centerY = 0.0;
	
	// A list of all the paths currently being drawn by the panel
	private ArrayList<Path> pathsToDraw;

	// Used for the name mask
	private double xDrawPoint = 0;
	private double yDrawPoint = 0;
	
	// Used for the scale in the corner of the map
	private Double scaleUnit = 150.0;
	private Double lineMultiplier = 150.0;

	public MapPanel(Graph<City, Connection, String> map, ApplicationWindow app) {
		super();
		
		this.map = map;
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		this.connectionColors = new ArrayList<Color>();

		findBounds();
		updateClickMap();
		initColors();

		this.addCityButton = new JButton();
		this.addCityButton.setBounds(0, 0, 0, 0);
		this.addCityButton.setText("Add City");
		this.addCityButton.addActionListener(new ActionListener() {

			@Override
		public void actionPerformed(ActionEvent e) {
				if (selectedCity != null) {
					app.addToList(selectedCity.getName());
					// System.out.println("You want to add: " +
					// selectedCity.getName());
				}
				selectedCity = null;
				repaint();
			}
		});
		this.add(addCityButton);

		// Add mouse function
		MouseHandler aHandler = new MouseHandler();
		this.addMouseListener(aHandler);
		this.addMouseWheelListener(aHandler);
		this.addMouseMotionListener(aHandler);

		this.pathsToDraw = new ArrayList<Path>();

		// Get the map is a position where we can see it

		centerX = -xMin;
		centerY = -yMin;

		this.repaint();
	}

	/**
	 * Will add the path to the mapPanel and then draw it on
	 * 
	 * @param pathToAdd
	 *            Path object to be drawn
	 */
	public void addPath(Path pathToAdd) {
		this.pathsToDraw.add(pathToAdd);
		this.repaint();
	}

	public ArrayList<Path> getPaths() {
		return this.pathsToDraw;
	}

	/**
	 * Clears all the current paths
	 */
	public void clearPaths() {
		this.pathsToDraw.clear();
		this.repaint();
	}

	public class MouseHandler implements MouseListener, MouseWheelListener,
			MouseMotionListener {

		Point lastMousePoint = new Point(0, 0);

		@Override
		public void mouseClicked(MouseEvent e) {

			Point clickPoint = e.getPoint();

			// Pan and zoom the click point
			clickPoint.x -= centerX;
			clickPoint.y -= centerY;
			clickPoint.x /= zoom;
			clickPoint.y /= zoom;
			
			int partitionNumber = Math.floorDiv((int) clickPoint.x - xMin,
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
			for (City city : citiesToSearch) {
				int xDistSquared = (int) Math.pow(city.getXCoord()
						- clickPoint.x, 2);
				int yDistSquared = (int) Math.pow(city.getYCoord()
						- clickPoint.y, 2);
				int distance = (int) Math.sqrt(xDistSquared + yDistSquared);
				if (distance < closestCityDist) {
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
			if (arg0.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

				int mouseDirection = 0;
				if (arg0.getPreciseWheelRotation() > 0)
					mouseDirection = 1;
				else
					mouseDirection = -1;

				if (mouseDirection > 0 && zoom >= ZOOM_MIN) {
					zoom /= mouseDirection * ZOOM_SPEED;
					centerX = (mouseDirection * (1 / ZOOM_SPEED)
							* (centerX - arg0.getX()) + arg0.getX());
					centerY = (mouseDirection * (1 / ZOOM_SPEED)
							* (centerY - arg0.getY()) + arg0.getY());
				} else if (zoom <= ZOOM_MAX) {
					zoom *= Math.abs(mouseDirection * ZOOM_SPEED);
					centerX = (Math.abs(mouseDirection) * ZOOM_SPEED
							* (centerX - arg0.getX()) + arg0.getX());
					centerY = (Math.abs(mouseDirection) * ZOOM_SPEED
							* (centerY - arg0.getY()) + arg0.getY());
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

		BufferedImage nameCollisionImage = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D nameMask = (Graphics2D) nameCollisionImage.getGraphics();
		nameMask.setColor(Color.WHITE);

		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Translate for pan
		g2d.translate(centerX, centerY);
		nameMask.translate(centerX, centerY);
		xDrawPoint = centerX;
		yDrawPoint = centerY;

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
			nameMask.translate(translateX, translateY);
			xDrawPoint += translateX;
			yDrawPoint += translateY;

			// Draw city names:
			java.awt.FontMetrics fontMetric = g2d.getFontMetrics();
			int stringWidth = fontMetric.stringWidth(city.getName());
			if (stringWidth > biggestStringWidth)
				biggestStringWidth = stringWidth;
			int stringHeight = 15;

			Point[] checkPoints = new Point[4];
			checkPoints[0] = new Point((int) xDrawPoint, (int) yDrawPoint);
			checkPoints[1] = new Point((int) xDrawPoint + stringWidth,
					(int) yDrawPoint);
			checkPoints[2] = new Point((int) xDrawPoint, (int) yDrawPoint
					+ stringHeight);
			checkPoints[3] = new Point((int) xDrawPoint + stringWidth,
					(int) yDrawPoint + stringHeight);

			boolean isBlocked = false;
			for (Point p : checkPoints) {
				if (p.x > 0 && p.x < nameCollisionImage.getWidth()) {
					if (p.y > 0 && p.y < nameCollisionImage.getHeight()) {
						int greyColor = nameCollisionImage.getRGB((int) p.x,
								(int) p.y);
						if (greyColor > -10) {
							isBlocked = true;
						}
					}
				}
			}

			if (!isBlocked
					|| (selectedCity != null && selectedCity.equals(city))) {
				g2d.drawString(city.getName(), (int) (-stringWidth / 2),
						(int) (CITY_SIZE * zoom + 10));
				nameMask.fillRect(0, 0, biggestStringWidth, stringHeight);
			}

			// Draw city
			Ellipse2D.Double circle = new Ellipse2D.Double();
			circle.height = CITY_SIZE * zoom;
			circle.width = CITY_SIZE * zoom;
			g2d.setColor(CITY_COLOR);
			double centerLinks = CITY_SIZE * zoom / 2;
			g2d.translate(-centerLinks, -centerLinks);
			g2d.fill(circle);
			g2d.translate(centerLinks, centerLinks);

			xDrawPoint -= translateX;
			yDrawPoint -= translateY;
			g2d.translate(-translateX, -translateY);
			nameMask.translate(-translateX, -translateY);
		}

		drawPaths(g2d);
		drawSelectedCity(g2d);
		drawScale(g2d);

		// Translate for pan
		g2d.translate(-centerX, -centerY);
		nameMask.translate(-centerX, -centerY);

		// draw scale
		drawScale(g2d);
		
		// Draw border
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setColor(new Color(160, 160, 160));
		g2d.setStroke(new BasicStroke(2));
		g2d.drawRect(1, 1, this.getWidth() - 18, this.getHeight() - 41);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawRect(3, 3, this.getWidth() - 22, this.getHeight() - 45);
		
//		 g2d.drawImage(nameCollisionImage, null, 0, 0);
	}

	private void drawPaths(Graphics2D g2d) {
		int colorNumber = 0;
		for (Path path : pathsToDraw) {
			if (connectionColors.size() > 0) {
				g2d.setColor(connectionColors.get(colorNumber));
				colorNumber++;
				colorNumber = colorNumber % connectionColors.size();
			} else
				g2d.setColor(CONNECTION_COLOR);

			ArrayList<Point> linkPoints = new ArrayList<Point>();
			for (City city : path.getCities()) {
				linkPoints.add(new Point((int) (city.getXCoord() * zoom),
						(int) (city.getYCoord() * zoom)));
				double translateX = city.getXCoord() * zoom;
				double translateY = city.getYCoord() * zoom;
				g2d.translate(translateX, translateY);

				Ellipse2D.Double circle = new Ellipse2D.Double();
				circle.height = CONNECTION_CITY_SIZE * zoom;
				circle.width = CONNECTION_CITY_SIZE * zoom;
				double centerLinks = CONNECTION_CITY_SIZE * zoom / 2;
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

	/**
	 * @param g2d
	 *            the graphics object on which to draw
	 */
	private void drawSelectedCity(Graphics2D g2d) {
		if (selectedCity != null) {
			g2d.setColor(SELECTED_CITY_COLOR);
			double translateX = selectedCity.getXCoord() * zoom;
			double translateY = selectedCity.getYCoord() * zoom;
			g2d.translate(translateX, translateY);

			Ellipse2D.Double circle = new Ellipse2D.Double();
			circle.height = SELECTED_CITY_SIZE * zoom;
			circle.width = SELECTED_CITY_SIZE * zoom;
			double centerLinks = SELECTED_CITY_SIZE * zoom / 2;
			g2d.translate(-centerLinks, -centerLinks);
			g2d.fill(circle);
			g2d.translate(centerLinks, centerLinks);

			g2d.translate(-translateX, -translateY);
			g2d.setColor(CITY_COLOR);

			Point buttonPoint = new Point((int) translateX, (int) translateY);
			buttonPoint.x += centerX - 40;
			buttonPoint.y += centerY + centerLinks + 20;
			addCityButton.setBounds(buttonPoint.x, buttonPoint.y, 80, 20);
		} else {
			addCityButton.setBounds(0, 0, 0, 0);
		}
	}

	private void drawScale(Graphics2D g2d)
	{
		int margin = 10;
		g2d.setColor(new Color(50, 50, 50));
		g2d.translate(margin, this.getHeight() - margin - 38);
		
		int lineMin = 100;
		int lineMax = 200;
		Double lineWidth = ((zoom) * lineMultiplier);
		if (lineWidth < lineMin)
		{
			scaleUnit *= 2;
			lineMultiplier *= 2;
		}
		if (lineWidth > lineMax)
		{
			scaleUnit /= 2;
			lineMultiplier /= 2;
		}
		String str = (int)Math.floor(scaleUnit) + " Miles";
		g2d.drawString(str, 3, -5);
		g2d.drawLine((int)Math.floor(lineWidth), 0, 0, 0);
		g2d.drawLine(0, 0, 0, -5);
		g2d.drawLine((int)Math.floor(lineWidth), 0, (int)Math.floor(lineWidth), -5);

		g2d.translate(-margin, -(this.getHeight() - margin - 38));
	}
	
	private void findBounds() {
		xMin = Integer.MAX_VALUE;
		xMax = Integer.MIN_VALUE;
		yMin = Integer.MAX_VALUE;
		yMax = Integer.MIN_VALUE;

		for (City city : map.getElements()) {
			if (city.getXCoord() > xMax)
				xMax = (int) city.getXCoord();
			else if (city.getXCoord() < xMin)
				xMin = (int) city.getXCoord();
			if (city.getYCoord() > yMax)
				yMax = (int) city.getYCoord();
			else if (city.getYCoord() < yMin)
				yMin = (int) city.getYCoord();
		}

	}

	private void updateClickMap() {
		clickMap.clear();

		int mapWidth = xMax - xMin;
		
		// Prime the clickMap hashMap with emtpy arrayLists;
		partitionWidth = mapWidth / partitionCount;
		
		if (partitionWidth == 0)
		{
			partitionCount = 1;
			partitionWidth = mapWidth / partitionCount;
		}
		
		for (int i = 0; i < partitionCount; i++)
			clickMap.put(i, new ArrayList<City>());

		for (City city : map.getElements()) {
			int partitionNumber = Math.floorDiv((int) city.getXCoord() - xMin,
					partitionWidth);
			if (partitionNumber >= partitionCount)
				partitionNumber = partitionCount - 1;
			clickMap.get(partitionNumber).add(city);
		}
	}

	private void initColors() {
		if (connectionColors == null)
			connectionColors = new ArrayList<Color>();
		connectionColors.add(Color.BLUE);
		connectionColors.add(Color.GREEN);
		connectionColors.add(Color.RED);
		connectionColors.add(Color.YELLOW);
		connectionColors.add(Color.CYAN);
		connectionColors.add(Color.PINK);

	}

	public void centerMap()
	{
		int margin = 40;
		
		double mapWidth = Math.abs(xMax - xMin);
		double mapHeight = Math.abs(yMax - yMin);
		
		double xZoom = (this.getWidth() - margin * 2) / mapWidth;
		double yZoom = (this.getHeight() - margin * 2) / mapHeight;
		
		zoom = (xZoom < yZoom ? xZoom : yZoom);
		
		double offSetX = (this.getWidth() - (mapWidth * zoom)) / 2;
		double offSetY = (this.getHeight() - (mapHeight * zoom)) / 2;
		
		centerX = -xMin * zoom + offSetX;
		centerY = -yMin * zoom + offSetY - 20;
	}
}
