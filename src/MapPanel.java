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

	private static final long serialVersionUID = 1L;
	private Graph<City,Connection,String> map;
	
	double zoom = 1.0;
	double xPos = 0.0;
	double yPos = 0.0;

	public MapPanel(Graph<City, Connection, String> map) {
		super();
		this.map=map;
		
		//Add mouse function
		MouseHandler aHandler = new MouseHandler();
		this.addMouseListener(aHandler);
		this.addMouseWheelListener(aHandler);
		
		//add border
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "MAP");
		border.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(border);
	}
	
	public String fromTo(String fromCity, String toCity){
		//add code to make stuff change in the map to highlight route, then transfer data to myPanel in a string
		return null;
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
	
    public void paintComponent(Graphics g)
    { 
    	Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);
        
        // Translate for pan
        g2d.translate(xPos, yPos);
        
        ArrayList<City> cities = map.getElements();
        for (City city : cities)
        {
        	Ellipse2D.Double circle = new Ellipse2D.Double();
        	circle.height = 10;
        	circle.width = 10;
        	double translateX = city.getXCoord() * zoom;
        	double translateY = city.getYCoord() * zoom;
        	g2d.translate(translateX, translateY);
        	g2d.fill(circle);
        	g2d.drawString(city.getName(), 0, 20);
        	g2d.translate(-translateX, -translateY);
        }
        
        // Translate for pan
        g2d.translate(-xPos, -yPos);
    }
	
}
