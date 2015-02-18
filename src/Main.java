import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	

	public static void main(String[] args){
		
		// Set the look and feel of the application
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
		
		new Application();
	}
}
