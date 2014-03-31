package events;

import java.util.ArrayList;

public class ItemChangeNotifier {
	private ArrayList<ItemChangeListener> listenerList;
	
	public ItemChangeNotifier(ItemChangeListener l)
	{
		listenerList = new ArrayList<ItemChangeListener>();
		listenerList.add(l);
	}
	
	public synchronized void addListener(ItemChangeListener l)
	{
		listenerList.add(l);
	}
	
	public synchronized void removeListener(ItemChangeListener l)
	{
		listenerList.remove(l);
	}
	
	public synchronized void fireItemChangeEvent(ItemChangeEvent e)
	{
		for (ItemChangeListener l : listenerList)
		{
			l.itemChangeRecieved(e);
		}
	}
	
}
