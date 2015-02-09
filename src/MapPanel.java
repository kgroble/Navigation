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

	public MapPanel(Graph<City, Connection, String> map) {
		super();
		this.map=map;
		
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "MAP");
		border.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(border);
	}
}
