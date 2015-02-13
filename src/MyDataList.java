import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;


public class MyDataList<E extends Comparable<? super E>> extends ArrayList<E> implements ListModel<E>{
	
	private static final long serialVersionUID = 1L;

	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public E getElementAt(int index) {
		// TODO Auto-generated method stub
		return get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

}
