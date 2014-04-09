package model;

import java.util.HashMap;

/*
 * 		Craftable: Items that can be made by gathering the required
 * 			types and numbers of components and investing workload.
 */

public class Craftable extends Item
{
	private Item[] craftedFromItems;
	private int[] craftedFromNumbers;
	
	public static Craftable lastCraftable; 
	private static int slotNumber; //only used in constructor
	
	private int numCrafted;
	private int workload;
	
	private long cost;
	private boolean costChanged;
	
	private static CraftBook selectedCraftBook;
	private double worthPerWorkload;
	private double profitRatio;
	private boolean profitRatioChanged;
	
	/**
	 * @param name : Craftable Name
	 * @param workload : Craftable Workload
	 * @param numCrafted : Number crafted per set
	 * @param numComponents : Number of unique components this is crafted from
	 */
	public Craftable(String name, int workload, int numCrafted, int numComponents)
	{
		super(name, Item.TYPE_CRAFTABLE);
		this.workload = workload;
		this.numCrafted = numCrafted;
		craftedFromItems = new Item[numComponents];
		craftedFromNumbers = new int[numComponents];
		
		costChanged = true;
		profitRatioChanged = true;
		
		slotNumber = 0;
		lastCraftable = this;
	}
	
	/**
	 * This will need to be done all at once because slotNumber
	 * @param name
	 * @param number
	 */
	void addRecipe(Item item, int number)
	{
		craftedFromItems[slotNumber] = item;
		craftedFromNumbers[slotNumber] = number;
		slotNumber++;
	}
	
	/*public String testInfo()
	{
		Main.sb.delete(0, Main.sb.length());
		Main.sb.append(name);
		Main.sb.append(" (Workload: ");
		Main.sb.append(workload);
		Main.sb.append(", Size: ");
		Main.sb.append(numCrafted);
		Main.sb.append(")\n");
		for(int i = 0; i < craftedFromItems.length; i++)
		{
			Main.sb.append('-');
			Main.sb.append(craftedFromItems[i]);
			Main.sb.append(':');
			Main.sb.append(' ');
			Main.sb.append(craftedFromNumbers[i]);
			Main.sb.append('\n');
		}
		Main.sb.append("Total Cost: ");
		Main.sb.append(cost);
		
		return Main.sb.toString();
	}*/
	
	/**
	 * Currently called when a material is converted into craftable.
	 * This resets the recipe entry with a fresh set of items from itemMap.
	 * 
	 * Used by ItemLoader.java
	 * 
	 * @param itemMap
	 */
	void remapRecipe(HashMap<String, Item> itemMap)
	{
		for (int i = 0; i < craftedFromItems.length; i++)
		{
			//System.out.println(craftedFromItems[i].name);
			craftedFromItems[i] = itemMap.get(craftedFromItems[i].getName());
		}
	}
	
	// updateCost only called when worth of one of the components or itself changes
	void updateCost()
	{
		costChanged = true;
		profitRatioChanged = true;
	}
	
	void setWorth(long newWorth)
	{
		super.setWorth(newWorth);
		updateCost();
	}
	
	static void setCraftBook(CraftBook cb)
	{
		selectedCraftBook = cb;
	}
	
	/**
	 * 
	 * @return maximum workload from one crafting batch due to limit of
	 * 		10,000 items in a single inventory slot/stack
	 */
	public long maxWorkload()
	{
		int max = 10000/numCrafted;
		for (int i = 0; i < craftedFromNumbers.length; i++)
		{
			if (10000/craftedFromNumbers[i] < max)
			{
				max = 10000/craftedFromNumbers[i];
			}
		}
		return max*workload;
	}
	
	public int getCraftedFromLength()
	{
		return craftedFromItems.length;
	}
	
	public Item getCraftedFromItems(int i)
	{
		return craftedFromItems[i];
	}
	
	public int getCraftedFromNumbers(int i)
	{
		return craftedFromNumbers[i];
	}

	public int getWorkload()
	{
		return workload;
	}
	
	public int getCraftSize()
	{
		return numCrafted;
	}
	
	public double getProfitRatio()
	{
		/*
		 * TODO: analyse when getProfitRatio/Cost() are called
		 * vs how many times the calculation is run.
		 */
		
		if (profitRatioChanged) //only run calculation when needed
		{
			worthPerWorkload = (getWorth()*0.99 - getCost())/(workload/numCrafted);
			profitRatio = worthPerWorkload - selectedCraftBook.getWorthPerWorkload();
			profitRatioChanged = false;
		}
		return profitRatio;
	}
	
	public long getCost()
	{
		if (costChanged) //only run calculation when needed
		{
			cost = 0;
			for (int i = 0; i < craftedFromItems.length; i++)
			{
				cost += craftedFromItems[i].getWorth() * craftedFromNumbers[i];
			}
			cost /= numCrafted;
			costChanged = false;
		}
		return cost;
	}
	
	public double getCostPerWorkload()
	{
		return selectedCraftBook.getWorthPerWorkload();
	}
	
}
