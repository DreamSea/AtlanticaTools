package model;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * 		Craftable: Items that can be made by gathering the required
 * 			types and numbers of components and investing workload.
 */

public class Craftable extends Item
{
	private Item[] craftedFromItems;
	private int[] craftedFromNumbers;
	
	private int numCrafted;
	private int workload;
	
	private long cost;
	private boolean costChanged;
	
	private static CraftBook selectedCraftBook;
	private double worthPerWorkload;
	private double profitRatio;
	private boolean profitRatioChanged;
	
	private Craftable(String name, int workload, int size, ArrayList<Item> componentList, ArrayList<Integer> componentNumbers)
	{
		super (name);
		this.workload = workload;
		numCrafted = size;
		
		int recipeSize = componentList.size();
		
		craftedFromItems = new Item[recipeSize];
		craftedFromNumbers = new int[recipeSize];
		
		for (int i = 0; i < recipeSize; i++)
		{
			craftedFromItems[i] = componentList.get(i);
			craftedFromNumbers[i] = componentNumbers.get(i);
		}	
	}
	
	static CraftBuilder builder(String name)
	{
		return new CraftBuilder(name);
	}
	
	public static class CraftBuilder
	{
		private String nameStorage;
		private int workloadStorage;
		private int sizeStorage;
		private ArrayList<Item> componentNames;
		private ArrayList<Integer> componentNumbers;
		
		
		private CraftBuilder (String name)
		{
			nameStorage = name;
			componentNames = new ArrayList<Item>();
			componentNumbers = new ArrayList<Integer>();
		}
		
		CraftBuilder setWorkload(int wl)
		{
			workloadStorage = wl;
			return this;
		}
		
		CraftBuilder setSize(int craftSize)
		{
			sizeStorage = craftSize;
			return this;
		}
		
		CraftBuilder addComponent(Item itemName, int itemNumber)
		{
			componentNames.add(itemName);
			componentNumbers.add(itemNumber);
			return this;
		}
		
		Craftable build()
		{
			return new Craftable(nameStorage, workloadStorage, 
					sizeStorage, componentNames, componentNumbers);
		}
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
	 * Don't think there's worry with items being deleted between
	 * hasNext___() and next___() calls because there are no recipe
	 * deleting methods...
	 * @return iterator that steps through all the recipes
	 */
	public RecipeIterator getRecipeIterator()
	{
		return new RecipeIterator();
	}
	
	public class RecipeIterator
	{
		private int currentItemSlot;
		private int currentNumberSlot;
		
		private RecipeIterator()
		{
			currentItemSlot = 0;
			currentNumberSlot = 0;
		}
		
		public boolean hasNextItem()
		{
			return currentItemSlot < craftedFromItems.length;
		}
		
		public boolean hasNextNumber()
		{
			return currentNumberSlot < craftedFromNumbers.length;
		}
		
		public Item nextItem()
		{
			currentItemSlot++;
			return craftedFromItems[currentItemSlot-1];
		}
		
		public int nextNumber()
		{
			currentNumberSlot++;
			return craftedFromNumbers[currentNumberSlot-1];
		}
	}

	public int getWorkload()
	{
		return workload;
	}
	
	/**
	 * 
	 * @return maximum workload from one crafting batch due to limit of
	 * 		10,000 items in a single inventory slot/stack
	 */
	public long getWorkloadMax()
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
	
	public int getCraftSize()
	{
		return numCrafted;
	}
	
	public int getCraftSizeMax()
	{
		int max = Integer.MAX_VALUE;
		for (int i = 0; i < craftedFromNumbers.length; i++)
		{
			if (10000/craftedFromNumbers[i] < max)
			{
				max = 10000/craftedFromNumbers[i];
			}
		}
		return numCrafted * max;
	}
	
	public double getProfitRatio()
	{
		/*
		 * TODO: analyse when getProfitRatio/Cost() are called
		 * vs how many times the calculation is run.
		 */
		
		if (profitRatioChanged) //only run calculation when needed
		{
			worthPerWorkload = (getWorthWithInterest() - getCost())/(workload/numCrafted);
			profitRatio = worthPerWorkload - selectedCraftBook.getWorthPerWorkload();
			profitRatioChanged = false;
		}
		return profitRatio;
	}
	
	public long getCostTotal()
	{
		if (costChanged) //only run calculation when needed
		{
			cost = 0;
			for (int i = 0; i < craftedFromItems.length; i++)
			{
				cost += craftedFromItems[i].getWorth() * craftedFromNumbers[i];
			}
			costChanged = false;
		}
		return cost;
	}
	
	public long getCost()
	{
		return getCostTotal()/numCrafted;
	}
	
	public double getWorthWithInterestTotal()
	{
		return getWorthWithInterest()*numCrafted;
	}
	
	public double getCostPerWorkload()
	{
		return selectedCraftBook.getWorthPerWorkload();
	}
	
	public double getCostOfWorkloadTotal()
	{
		return getCostPerWorkload()*workload;
	}
	
	public double getProfitTotal()
	{
		return getWorthWithInterestTotal() - getCostTotal() - getCostOfWorkloadTotal();
	}
	
	public String getSelectedCraftBook()
	{
		return selectedCraftBook.getName();
	}
	
	public ItemType getType()
	{
		return ItemType.CRAFTABLE;
	}
}
