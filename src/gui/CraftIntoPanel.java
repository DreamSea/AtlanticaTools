package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import types.Craftable;
import types.Item;
import manager.Main;

public class CraftIntoPanel extends JPanel {
	
	public CraftIntoTable craftTable;
	
	private JTable table;
	private TableListener tl;
	
	public CraftIntoPanel ()
	{
		craftTable = new CraftIntoTable();
		
		table = new JTable (craftTable);
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		tl = new TableListener();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(tl);
		
		JScrollPane jsp = new JScrollPane(table);
		jsp.setPreferredSize(new Dimension(300, 200));
		add(jsp);
	}
	
	public class CraftIntoTable extends AbstractTableModel
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
		
		/*
		 * TODO: the set false/true at beginning and end is to prevent
		 * chaos from happening when switching between items while table
		 * is defined as sorted.
		 * 
		 * All in all this entire setData() looks really ugly... but it
		 * works for now -_-
		 */
		public void setData(String s)
		{
			table.setAutoCreateRowSorter(false);
			tl.setActive(false);
			
			Item tempItem = Main.dm.itemMap.get(s);
			//System.out.println("CIP setData(): "+tempItem.craftsInto.toString());
			int length = tempItem.craftsInto.size();

			data = new Object[3][length];
			
			for (int i = 0; i < length; i++)
			{
				Craftable tempCraft = (Craftable) Main.dm.itemMap.get(tempItem.craftsInto.get(i));
				data[0][i] = tempCraft.name;
				data[1][i] = tempCraft.profitRatio;
				data[2][i] = tempCraft.getDaysSinceUpdate();
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
				Item selected = Main.dm.itemMap.get(table.getValueAt(e.getFirstIndex(), 0));
				//Main.gm.ccp.loadItem(selected);
				Main.gm.showItem(selected);
				//System.out.println(e.getSource());
			
				//System.out.println(e.getFirstIndex() +" " + e.getLastIndex());
				//System.out.println(table.getValueAt(e.getFirstIndex(), 0));
			}
		}
		
	}

}
