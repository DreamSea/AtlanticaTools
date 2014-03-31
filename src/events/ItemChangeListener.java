package events;

import java.util.EventListener;

public interface ItemChangeListener extends EventListener {
	public void itemChangeRecieved(ItemChangeEvent e);
}
