package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/*
 * 		DataManager: where all the Items are held. Ideally
 * 			only the ManagerManager will call methods of this class
 */

public class DataManager {
	
	private HashMap<String, Item> itemMap = new HashMap<String, Item>();
	
	private ArrayList<Material> materials;
	private ArrayList<CraftBook> craftBooks;
	
	private ArrayList<ArrayList<Craftable>> skillTree;
	private ArrayList<Craftable> action;
	private ArrayList<Craftable> crystal;
	private ArrayList<Craftable> food;
	private ArrayList<Craftable> machine;
	private ArrayList<Craftable> medicine;
	private ArrayList<Craftable> tool;
	
	private boolean debug;
	
	private final String DEFAULT_CRAFTBOOK = "Book of Expertise";
	//private static CraftBook loadedCraftBook;
	
	public DataManager(boolean debug)
	{
		this.debug = debug;
		//itemMap = new HashMap<String, Item>();
		skillTree = new ArrayList<ArrayList<Craftable>>();
		
		init();
		loadPrices();
	}
	
	private void init()
	{
		materials = new ArrayList<Material>();
		
		craftBooks = new ArrayList<CraftBook>();
		
		ItemLoader loader = new ItemLoader(itemMap, materials, debug);
		loader.loadCraftBook(craftBooks);
		Craftable.setCraftBook((CraftBook) itemMap.get(DEFAULT_CRAFTBOOK));
		//loadedCraftBook = (CraftBook) itemMap.get(DEFAULT_CRAFTBOOK);
		
		//init craftable lists
		action = new ArrayList<Craftable>();
		crystal = new ArrayList<Craftable>();
		food = new ArrayList<Craftable>();
		machine = new ArrayList<Craftable>();
		medicine = new ArrayList<Craftable>();
		tool = new ArrayList<Craftable>();
		
		/*
		 *	add Craftable lists to skillTree, which is pretty
		 *	much just a structure that holds all the Craftables
		 */
		
		skillTree.add(action);
		skillTree.add(crystal);
		skillTree.add(food);
		skillTree.add(machine);
		skillTree.add(medicine);
		skillTree.add(tool);			
		
		//load craftable recipes
		loader.loadAction(action);
		loader.loadCrystal(crystal);
		loader.loadFood(food);
		loader.loadMachine(machine);
		loader.loadMedicine(medicine);
		loader.loadTool(tool);
	}
	
	//loads previously saved prices/worth of each Item
	public void loadPrices()
	{
		try
		{
			Scanner input = new Scanner(new File("prices.txt"));

			while (input.hasNext())
			{
				String temp = input.next().replace('_', ' ');
				
				if (itemMap.containsKey(temp))
				{
					itemMap.get(temp).setWorth(input.nextInt());
					itemMap.get(temp).setTimeUpdated(input.nextLong());
				}
				else
				{
					input.nextInt();	//advances past unfound item
					input.nextLong();	//		'	'	
				}
			}
			
			input.close();
			
			/*for (Material m : materials)
			{
				m.updateCost();
			}*/
			
			for (ArrayList<Craftable> s : skillTree)
			{
				//System.out.println("ping");
				for (Craftable c : s)
				{
					c.updateCost();
				}
			}
		}
		catch(FileNotFoundException e)
		{
			
		}
	}
	
	//saves the prices/worth for use in a future program launch
	public void savePrices()
	{
		//System.out.println("saveme");

		Set<String> tempSet = itemMap.keySet();
		ArrayList<String> tempList = new ArrayList<String>();
		
		for (String s : tempSet)
		{
			String altered = s.replace(" ", "_");
			tempList.add(altered+" "+itemMap.get(s).getWorth()+" "+itemMap.get(s).getTimeUpdated());
		}
		
		Path path = Paths.get("prices.txt");
		
		try
		{
			Files.write(path, tempList, StandardCharsets.UTF_8);
		}
		catch(IOException e)
		{
			
		}
	}
	
	/*public static double getCostPerWorkload()
	{
		return loadedCraftBook.getWorthPerWorkload();
	}*/
	
	public static void setCraftBook(CraftBook cb)
	{
		Craftable.setCraftBook(cb);
		//loadedCraftBook = cb;
	}
	
	
	public Item getItem(String name)
	{
		return itemMap.get(name);
	}
	
	/**
	 * Updates the cost of all craftable items.
	 * Currently only used by ManagerManager when changing/updating
	 * craft books.
	 */
	public void refreshItems()
	{
		for (ArrayList<Craftable> al : skillTree)
		{
			for (Craftable c : al)
			{
				c.updateCost();
			}
		}
	}
	
	public void setItemWorth(String itemName, long worth)
	{
		Item i = itemMap.get(itemName);
		i.setWorth(worth);
		
		//changing craft book price changes all the profit ratios
		if (i.getType() == ItemType.CRAFTBOOK)
		{
			refreshItems();
		}
	}
	
	public boolean itemExists(String itemName)
	{
		return itemMap.containsKey(itemName);
	}
	
	public Iterator<CraftBook> getCraftBookListIterator()
	{
		return craftBooks.iterator();
	}
	
	public Iterator<Material> getMaterialListIterator()
	{
		return materials.iterator();
	}
	
	public ArrayList<Iterator<Craftable>> getSkillTreeIterators()
	{
		ArrayList<Iterator<Craftable>> iteratorList = new ArrayList<Iterator<Craftable>>();
		for (ArrayList<Craftable> al : skillTree)
		{
			iteratorList.add(al.iterator());
		}
		return iteratorList;
	}
}
