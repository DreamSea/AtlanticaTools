package types;

import java.text.DateFormat;
import java.util.ArrayList;

import manager.Main;

public class Item {

	/*
	 * 	For type casting from item map
	 */
	static final byte TYPE_MATERIAL = 0;
	static final byte TYPE_CRAFTABLE = 1;
	static final byte TYPE_CRAFTBOOK = 2;

	public String name;
	
	
	
	/*
	 * 	For crafting purposes, 2^31 should be enough
	 * 	Init: from .txt during first load
	 * 	Changes: only through gui setting.
	 */
	public long worth; //how much it can be sold for
	private long timeUpdated; //when worth was updated
	public static DateFormat df = DateFormat.getDateInstance();
	private static long currentTime = System.currentTimeMillis(); //when the program started
	
	/*
	 * temp: using long because of how FormattedTextField returns value (long)
	 * 
	 * 	Changes: probably every gui setting of something
	 * in craft tree ancestors
	 */
	public double cost; //cost to buy all of the components
	public byte type;
	
	public ArrayList<String> craftsInto;
	
	/**
	 * 
	 * @param name : Item name
	 * @param type : 0 = Material, 1 = Craftable
	 */
	public Item (String name, byte type)
	{
		this.name = name;
		worth = 1;
		cost = 1;
		this.type = type; 
		craftsInto = new ArrayList<String>();
		timeUpdated = currentTime;
	}
	
	public void updateCost()
	{
		cost = worth; //craftable overrides
		for (String s : craftsInto)
		{
			//System.out.println(s);
			//System.out.println(Main.dm == null);
			Main.dm.itemMap.get(s).updateCost();
		}
	}
	
	public void setWorth(long l)
	{
		worth = l;
		timeUpdated = System.currentTimeMillis();
	}
	
	public String toString()
	{
		return name;
	}
	
	//lol this duplicate ^^^
	public String getName()
	{
		return name;
	}
	
	public long getTimeUpdated()
	{
		return timeUpdated;
	}
	
	public void setTimeUpdated(long l)
	{
		timeUpdated = l;
	}
	
	
	/**
	 * 
	 * @return seconds since the worth of this item has been updated
	 */
	public long getTimeSinceUpdate()
	{
		return Math.max((currentTime - timeUpdated)/1000, 0);
	}
}
