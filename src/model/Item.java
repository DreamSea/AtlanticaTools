package model;

import java.text.DateFormat;
import java.util.ArrayList;

public class Item {

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
	//private long cost; //cost to buy all of the components
	
	//TODO: private craftsInto
	ArrayList<Craftable> craftsInto;
	
	/**
	 * 
	 * @param name : Item name
	 * @param type : 0 = Material, 1 = Craftable
	 */
	public Item (String name)
	{
		this.name = name;
		worth = 1;
		//cost = 1;
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
	
	public double getWorthWithInterest()
	{
		return getWorth()*0.99;
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
			
			//updates cost of all the craftables this item crafts into
			for (Craftable c : craftsInto)
			{
				//System.out.println(s);
				//System.out.println(Main.dm == null);
				c.updateCost();
			}
		}
	}
	
	
	void setTimeUpdated(long l)
	{
		timeUpdated = l;
	}
	
	// package private, used by ItemLoader.java
	void addCraftsInto(Craftable c)
	{
		craftsInto.add(c);
	}
	
	public int getCraftsIntoLength()
	{
		return craftsInto.size();
	}
	
	public Craftable getCraftsInto(int i)
	{
		return craftsInto.get(i);
	}
	
	/**
	 * Used for typecasting. All subclasses should override.
	 * @return ItemType of this class
	 * @throws Exception 
	 */
	public ItemType getType()
	{
		try {
			throw new Exception("One of the subclasses didn't override this.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ItemType.UNDECLARED;
	}
}
