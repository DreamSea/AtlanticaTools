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
import java.util.Scanner;
import java.util.Set;

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
	
	private static ArrayList<Craftable> currentList;
	
	//public static double costPerWorkload = 1.0;	//TODO temporary, replace with craft book item later
	
	private final String DEFAULT_CRAFTBOOK = "Book of Expertise";
	private static CraftBook loadedCraftBook;
	
	public DataManager()
	{
		//itemMap = new HashMap<String, Item>();
		skillTree = new ArrayList<ArrayList<Craftable>>();
		
		init();
		loadPrices();
	}
	
	private void init()
	{
		materials = new ArrayList<Material>();
		
		craftBooks = new ArrayList<CraftBook>();
		
		ItemLoader loader = new ItemLoader(itemMap, materials);
		loader.loadCraftBook(craftBooks);
		loadedCraftBook = (CraftBook) itemMap.get(DEFAULT_CRAFTBOOK);
		
		//init craftable lists
		action = new ArrayList<Craftable>();
		crystal = new ArrayList<Craftable>();
		food = new ArrayList<Craftable>();
		machine = new ArrayList<Craftable>();
		medicine = new ArrayList<Craftable>();
		tool = new ArrayList<Craftable>();
		
		//add craftable lists to skillTree (is this even needed?)
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
		
		//TODO: move/explain costPerWorkload init better
		//default cost at 1mill workload book
		//costPerWorkload = craftBooks.get(4).getWorthPerWorkload();
	}
	
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
			
			for (Material m : materials)
			{
				m.updateCost();
			}
			
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
	
	public static double getCostPerWorkload()
	{
		return loadedCraftBook.getWorthPerWorkload();
	}
	
	public static void setCraftBook(CraftBook cb)
	{
		loadedCraftBook = cb;
	}
	
	
	public Item getItem(String name)
	{
		return itemMap.get(name);
	}
	
	public void refreshItems()
	{
		for (ArrayList<Craftable> al : skillTree)
		{
			for (Craftable c : al)
			{
				c.updateCost();
			}
		}
		/*if (Main.gm.currentItem != null)
		{
			Main.gm.showItem(Main.gm.currentItem);
		}*/
	}
	
	public void setItemWorth(String itemName, long worth)
	{
		itemMap.get(itemName).setWorth(worth);
	}
	
	public boolean itemExists(String itemName)
	{
		return itemMap.containsKey(itemName);
	}
	
	
	/*
	 * Feels like having these ArrayList returns defeats the purpose
	 * of having them be private in the first place.
	 */
	public ArrayList<CraftBook> getCraftBookList()
	{
		return craftBooks;
	}
	
	public ArrayList<Material> getMaterialList()
	{
		return materials;
	}
	
	public ArrayList<ArrayList<Craftable>> getSkillTree()
	{
		return skillTree;
	}
}
