package model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import manager.Main;

public class Item {

	/*
	 * 	For type casting from item map
	 */
	static final byte TYPE_MATERIAL = 0;
	static final byte TYPE_CRAFTABLE = 1;
	static final byte TYPE_CRAFTBOOK = 2;

	protected String name;
	
	
	
	/*
	 * 	For crafting purposes, 2^31 should be enough
	 * 	Init: from .txt during first load
	 * 	Changes: only through gui setting.
	 */
	private long worth; //how much it can be sold for
	private long timeUpdated; //when worth was updated
	public static DateFormat df = DateFormat.getDateInstance();
	private static long bootTime = System.currentTimeMillis(); //when the program started
	
	/*
	 * temp: using long because of how FormattedTextField returns value (long)
	 * 
	 * 	Changes: probably every gui setting of something
	 * in craft tree ancestors
	 */
	private long cost; //cost to buy all of the components
	private byte type;
	
	//TODO: private craftsInto
	ArrayList<Craftable> craftsInto;
	
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
		craftsInto = new ArrayList<Craftable>();
		timeUpdated = bootTime;
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

	public long getWorth()
	{
		return worth;
	}
	
	public long getCost()
	{
		return cost;
	}
	
	public byte getType()
	{
		return type;
	}
	
	public long getTimeUpdated()
	{
		return timeUpdated;
	}
	
	
	/**
	 * 
	 * @return seconds since the worth of this item has been updated
	 */
	public long getTimeSinceUpdate()
	{
		return Math.max((bootTime - timeUpdated)/1000, 0);
	}
	
	public double getDaysSinceUpdate()
	{
		return getTimeSinceUpdate()/86400.0;
	}
	
	
	/*
	 * Following methods can only be used by package members (mainly DataManager)
	 */
	void setWorth(long newWorth)
	{
		if (worth > 0)
		{
			worth = newWorth;
			timeUpdated = System.currentTimeMillis();
		}
	}
	
	void setCost(long newCost)
	{
		cost = newCost;
	}
	
	void setTimeUpdated(long l)
	{
		timeUpdated = l;
	}
	
	void updateCost()
	{
		/*
		 * Craftable overrides, but for non-craftables, cost becomes
		 * the price to buy it off the market
		 */
		cost = worth;
		
		
		for (Craftable c : craftsInto)
		{
			//System.out.println(s);
			//System.out.println(Main.dm == null);
			c.updateCost();
		}
	}
	
	public int getCraftsIntoLength()
	{
		return craftsInto.size();
	}
	
	public Craftable getCraftsInto(int i)
	{
		return craftsInto.get(i);
	}
}
