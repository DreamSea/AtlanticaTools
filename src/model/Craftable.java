package model;

import java.util.HashMap;

import manager.Main;

public class Craftable extends Item
{
	private Item[] craftedFromItems;
	private int[] craftedFromNumbers;
	
	public static Craftable lastCraftable;
	private static int slotNumber;
	
	private int numCrafted;
	private int workload;
	
	private double worthPerWorkload;
	private double profitRatio;
	
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
		
		slotNumber = 0;
		lastCraftable = this;
		
		
		worthPerWorkload = (getWorth()*0.99 - getCost())/(workload/numCrafted);
		profitRatio = worthPerWorkload - DataManager.getCostPerWorkload();
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
	
	void remapRecipe(HashMap<String, Item> itemMap)
	{
		for (int i = 0; i < craftedFromItems.length; i++)
		{
			//System.out.println(craftedFromItems[i].name);
			craftedFromItems[i] = itemMap.get(craftedFromItems[i].getName());
		}
	}
	
	void updateCost()
	{
		long tempCost = 0;
		for (int i = 0; i < craftedFromItems.length; i++)
		{
			tempCost += craftedFromItems[i].getWorth() * craftedFromNumbers[i];
		}
		tempCost /= numCrafted;
		
		worthPerWorkload = (getWorth()*0.99 - tempCost)/(workload/numCrafted);
		profitRatio = worthPerWorkload - DataManager.getCostPerWorkload();
		
		setCost(tempCost);
		
		/*
		 * TODO may lead to repeated updates on same item
		 * Use boolean hasChecked set before each update?
		 */
		for (Craftable c : craftsInto)
		{
			c.updateCost();
		}
	}
	
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
		return profitRatio;
	}
	
}
