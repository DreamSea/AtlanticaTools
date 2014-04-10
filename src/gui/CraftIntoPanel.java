package gui;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import events.ItemChangeEvent;
import events.ItemChangeNotifier;
import model.Craftable;
import model.Item;

/*
 * 		CraftIntoPanel : Displays information on what craftables the
 * 			currently loaded item is used in. The real power/reason
 * 			behind craft calculator project. Package private.
 * 
 * 		Also contains gui objects that I understand the least... (tables)
 */

class CraftIntoPanel extends JPanel {
	
	CraftIntoTable craftTable; //TODO: make this private?
	
	private JTable table;
	private TableListener tl;
	private ItemChangeNotifier craftIntoNotifier;
	
	CraftIntoPanel (ItemChangeNotifier icn)
	{
		craftTable = new CraftIntoTable();
		craftIntoNotifier = icn;
		
		table = new JTable (craftTable);
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		tl = new TableListener();
		
		//no need for selecting more than one item at a time
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(tl);
		
		JScrollPane jsp = new JScrollPane(table);
		jsp.setPreferredSize(new Dimension(300, 280));
		add(jsp);
	}
	
	class CraftIntoTable extends AbstractTableModel
	{
		private String[] columnNames = {"Item", "Ratio", "Updated (days)"};
		
		private Object[][] data;

		public CraftIntoTable()
		{

		}
		
		public Class getColumnClass (int c)
		{
			return getValueAt(0, c).getClass();
		}
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			if (data != null)
				return data[0].length;
			return 0;
		}
		
		public String getColumnName(int col)
		{
			return columnNames[col];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (data.length > 0 && data[0].length > 0)
			return data[columnIndex][rowIndex];
			else return null;
		}
		
		void setData(Item i)
		{
			/*
			 * TODO: the set row sorter false/true at beginning and end is to prevent
			 * chaos from happening when switching between items while table
			 * is defined as sorted.
			 * 
			 * Listener setActive to keep listener from firing repeatedly as table
			 * is repopulated with new data
			 * 
			 * All in all this entire setData() looks really ugly... but it
			 * works for now -_-
			 */
			table.setAutoCreateRowSorter(false);
			tl.setActive(false);
			
			int length = i.getCraftsIntoLength();

			data = new Object[3][length];
			
			for (int j = 0; j < length; j++)
			{
				Craftable tempCraft = i.getCraftsInto(j);
				data[0][j] = tempCraft.getName();
				data[1][j] = tempCraft.getProfitRatio();
				data[2][j] = tempCraft.getDaysSinceUpdate();
				//System.out.println("CIP setData(): "+tempCraft.name+tempCraft.profitRatio);
			}
			repaint();
			
			table.setAutoCreateRowSorter(true);
			tl.setActive(true);
			
			//System.out.println(getTableModelListeners().toString());
		}
	}
	
	private class TableListener implements ListSelectionListener
	{
		private boolean isActive = true;
		
		public void setActive(boolean b)
		{
			isActive = b;
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
			{
				return;
			}
			if (isActive)
			{	
				craftIntoNotifier.fireItemChangeEvent(
						new ItemChangeEvent(
								e.getSource(),
								String.valueOf(table.getValueAt(e.getFirstIndex(), 0))));
			}
		}
		
	}

}
