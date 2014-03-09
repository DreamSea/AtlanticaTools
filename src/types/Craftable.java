package types;

import manager.Main;

public class Craftable extends Item
{
	public Item[] craftedFromItems;
	public int[] craftedFromNumbers;
	
	public static Craftable lastCraftable;
	private static int slotNumber;
	
	public int numCrafted;
	public int workload;
	
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
	}
	
	/**
	 * This will need to be done all at once because slotNumber
	 * @param name
	 * @param number
	 */
	public void addRecipe(Item item, int number)
	{
		craftedFromItems[slotNumber] = item;
		craftedFromNumbers[slotNumber] = number;
		slotNumber++;
	}
	
	public String testInfo()
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
			Main.sb.append(craftedFromItems[i].name);
			Main.sb.append(':');
			Main.sb.append(' ');
			Main.sb.append(craftedFromNumbers[i]);
			Main.sb.append('\n');
		}
		Main.sb.append("Total Cost: ");
		Main.sb.append(cost);
		
		return Main.sb.toString();
	}
	
	public void updateCost()
	{
		cost = 0;
		for (int i = 0; i < craftedFromItems.length; i++)
		{
			cost += craftedFromItems[i].worth * craftedFromNumbers[i];
		}
	}

}
