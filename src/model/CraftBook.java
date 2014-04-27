package model;

/*
 * 		CraftBook: Items that can be spent to provide a set amount of workload
 * 
 */

public class CraftBook extends Item{

	public CraftBook(String name, int workload)
	{
		super(name);
		
		this.workload = workload;
	}

	private int workload;
	
	
	//returns workload book gives
	public int getWorkload()
	{
		return workload;
	}
	
	/**
	 * @return Worth of craft book divided by workload provided
	 */
	public double getWorthPerWorkload()
	{
		return getWorth()*1.0/workload;
	}
	
	public ItemType getType()
	{
		return ItemType.CRAFTBOOK;
	}
}
