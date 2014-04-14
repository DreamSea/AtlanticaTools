package gui;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class ColorRenderer extends JLabel implements TableCellRenderer {
	
	private static double profitRatioLessBad = 0;	//when profit ratio should be red
	private static double profitRatioMoreGood = 1; 	//when profit ratio should be green 
	private static double lastUpdatedMoreBad = 7;	//when last updated should be red
	
	private static Color colorGood = Color.blue;
	private static Color colorBad = Color.red;
	private static Color colorNeutral = Color.black;//prepare for eventual 'SavedConfigs' file
	
	//TODO: autoset color backgrounds
	private static Color colorGoodBackground = new Color(200, 235, 235);
	private static Color colorBadBackground = new Color(250, 215, 215);
	private static Color colorNeutralBackground = Color.white;
	
	private static NumberFormat nf = NumberFormat.getInstance();
	
	public ColorRenderer(String[] columnNames)
	{
		setHorizontalAlignment(SwingConstants.RIGHT);
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		setFont(table.getFont());
		
		//TODO: change this to a way where I don't have to put in numbers one by one
		if (column == 1)
		{
			if ((double) value < profitRatioLessBad)
			{
				setForeground(colorBad);
				setBackground(colorBadBackground);
			}
			else if ((double) value > profitRatioMoreGood)
			{
				setForeground(colorGood);
				setBackground(colorGoodBackground);
			}
			else
			{
				setForeground(colorNeutral);
				setBackground(colorNeutralBackground);
			}
		}
		else if (column == 2)
		{
			if ((double) value > lastUpdatedMoreBad)
			{
				setForeground(colorBad);
				setBackground(colorBadBackground);
			}
			else
			{
				setForeground(colorNeutral);
				setBackground(colorNeutralBackground);
			}
		}
			
		setText(nf.format(value));
		
		return this;
	}
}
