package manager;

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

import types.CraftBook;
import types.Craftable;
import types.Item;
import types.Material;

public class DataManager {
	
	private boolean hasInit = false;
	
	public HashMap<String, Item> itemMap;
	
	public ArrayList<Material> materials;
	public ArrayList<CraftBook> craftBooks;
	
	public ArrayList<ArrayList<Craftable>> skillTree;
	public ArrayList<Craftable> action;
	public ArrayList<Craftable> crystal;
	public ArrayList<Craftable> food;
	public ArrayList<Craftable> machine;
	public ArrayList<Craftable> medicine;
	public ArrayList<Craftable> tool;
	
	private static ArrayList<Craftable> currentList;
	
	public static double costPerWorkload = 1.0;	//TODO temporary, replace with craft book item later
	
	public DataManager()
	{
		itemMap = new HashMap<String, Item>();
		skillTree = new ArrayList<ArrayList<Craftable>>();
		
		//init();
		//loadPrices();
	}
	
	public void init()
	{
		if (!hasInit) //makes sure init() only runs once... since it's public and all
		{
			hasInit = true;
			
			initMaterial();
			initCraftBook();
			initAction();
			initCrystal();
			initFood();
			initMachine();
			initMedicine();
			initTool();
		}
	}
	
	private void initMaterial()
	{
		materials = new ArrayList<Material>();	
	}
	
	private void initCraftBook()
	{
		craftBooks = new ArrayList<CraftBook>();
		
		craftBooks.add(new CraftBook("Small Crafting Secrets", 100));
		craftBooks.add(new CraftBook("Normal Crafting Secrets", 1000));
		craftBooks.add(new CraftBook("Artisan Crafting Secrets", 10000));
		craftBooks.add(new CraftBook("Book of Craftsmanship", 100000));
		craftBooks.add(new CraftBook("Book of Expertise", 1000000));
		craftBooks.add(new CraftBook("Book of Mastery", 5000000));
		craftBooks.add(new CraftBook("Golden Book of Craftsmanship", 50000000));
		
		for (CraftBook cb : craftBooks)
		{
			itemMap.put(cb.getName(), cb);
		}
	}
	
	private void initAction()
	{
		action = new ArrayList<Craftable>();
		skillTree.add(action);
		currentList = action;
		
		addCraftable("Auto-Craft [I]", 50000, 2, 4);
			addRecipe("Multi-Hued Crystal Shard", 5);
			addRecipe("Platinum Ingot", 50);
			addRecipe("Hammer [I]", 100);
			addRecipe("Enchant Stone [I]", 10);
			
		addCraftable("Auto-Craft [II]", 210000, 4, 5);
			addRecipe("Multi-Hued Crystal", 1);
			addRecipe("Platinum Ingot", 100);
			addRecipe("Hammer [II]", 50);
			addRecipe("Enchant Stone[II]", 10);
			addRecipe("Auto-Craft Doll MK-1", 2);
			
	}
	
	private void initCrystal()
	{
		crystal = new ArrayList<Craftable>();
		skillTree.add(crystal);
		currentList = crystal;
		
		addCraftable("Multi-Hued Crystal Shard", 46000, 5, 5);
			addRecipe("Phoenix Crystal", 1);
			addRecipe("Giant Crystal", 1);
			addRecipe("Redemption Crystal", 1);
			addRecipe("Dragon Crystal", 1);
			addRecipe("Ashen Crystal", 1);
		
		addCraftable("Multi-Hued Crystal", 200000, 5, 5);
			addRecipe("Multi-Hued Crystal Shard", 5);
			addRecipe("Mandragora", 2);
			addRecipe("Platinum Ingot", 25);
			addRecipe("Coral", 100);
			addRecipe("Opal", 3);
	}
	
	private void initFood()
	{
		food = new ArrayList<Craftable>();
		skillTree.add(food);
		currentList = food;
		
		addCraftable("Dumpling", 500, 10, 2);
			addRecipe("Pork", 4);
			addRecipe("Wheat", 6);
			
		addCraftable("Grilled Pork", 600, 10, 2);
			addRecipe("Pork", 8);
			addRecipe("Salt", 4);
		
		addCraftable("Grilled Shrimp", 800, 10, 2);
			addRecipe("Shrimp", 10);
			addRecipe("Salt", 5);	
		
		addCraftable("Octopus Soup", 960, 10, 2);
			addRecipe("Octopus", 12);
			addRecipe("Salt", 6);
			
		addCraftable("Barley Bibimbap", 7000, 10, 3);
			addRecipe("Barley", 60);
			addRecipe("Salt", 20);
			addRecipe("Sesame Oil", 5);
	}
	
	private void initMachine()
	{
		machine = new ArrayList<Craftable>();
		skillTree.add(machine);
		currentList = machine;
		
		addCraftable("Auto-Craft Doll MK-1", 1830000, 5, 8);
			addRecipe("Small Marionette", 1);
			addRecipe("Mysterious Vial: Wisdom", 1);
			addRecipe("Earth Element Shard", 300);
			addRecipe("Normal Crafting Secrets", 100);
			addRecipe("Polish", 10);
			addRecipe("Pearl", 150);
			addRecipe("Oil", 15);
			addRecipe("Small Bolt", 15);
	}
	
	private void initMedicine()
	{
		medicine = new ArrayList<Craftable>();
		skillTree.add(medicine);
		currentList = medicine;
		
		addCraftable("Mana Potion [III]", 5700, 10, 3);
			addRecipe("Chrysanthemum", 15);
			addRecipe("Jasmine", 15);
			addRecipe("Cyclamen", 10);
		
		addCraftable("Mysterious Vial: Wisdom", 1430000, 10, 6);
			addRecipe("Mandragora", 4);
			addRecipe("Multi-Hued Crystal", 5);
			addRecipe("Growth Vial [II]", 50);
			addRecipe("Mana Potion [III]", 100);
			addRecipe("Jasmine", 400);
			addRecipe("Green Mold", 600);
	}
	
	private void initTool()
	{
		tool = new ArrayList<Craftable>();
		skillTree.add(tool);
		currentList = tool;
		
		addCraftable("Small Bolt", 3500, 20, 3);
			addRecipe("Platinum Ingot", 1);
			addRecipe("Silver Ingot", 30);
			addRecipe("Coral", 100);
	}
	
	// Lazy Material Adding
	private void addMaterial(String material)
	{
		itemMap.put(material, new Material(material));
		//System.out.println(material+" "+itemMap.get(material));
		materials.add((Material) (itemMap.get(material)));
	}
	
	/**
	 * 
	 * @param craftable Item Name
	 * @param workload Workload
	 * @param numCrafted Size
	 * @param numComponents Components
	 */
	private void addCraftable(String craftable, int workload, int numCrafted, int numComponents)
	{
		if (itemMap.containsKey(craftable))
		{
			if (itemMap.get(craftable).type == 0) //if trying to add an item previously deemed material
			{
				Material toConvert = (Material) itemMap.get(craftable);
				
				materials.remove(toConvert);
				
				Craftable converted = toConvert.convert(workload, numCrafted, numComponents);
				
				itemMap.remove(craftable);
				itemMap.put(craftable,  converted);
				
				//resets all the components from the previous material into new craftable
				for (String s : converted.craftsInto)
				{
					Item tempItem = Main.dm.itemMap.get(s);
					if (tempItem.type == 1)
					{
						//System.out.println(s);
						Craftable tempCraft = (Craftable) tempItem;
						tempCraft.remapRecipe();
					}
				}
				
				//System.out.println("duplicate: "+toConvert.name);
			}
		}
		else
		{
			itemMap.put(craftable, new Craftable(craftable, workload, numCrafted, numComponents));
		}
		
		currentList.add((Craftable) itemMap.get(craftable));
	}
	
	private void addRecipe(String name, int number)
	{
		//puts item into itemmap as a material if not already existing
		if (!itemMap.containsKey(name))
		{
			addMaterial(name);
		}
		
		Item temp = itemMap.get(name);
		temp.craftsInto.add(Craftable.lastCraftable.name);
		Craftable.lastCraftable.addRecipe(itemMap.get(name), number);
	}
	
	void loadPrices()
	{
		try
		{
			Scanner input = new Scanner(new File("prices.txt"));

			while (input.hasNext())
			{
				String temp = input.next().replace('_', ' ');
				
				if (itemMap.containsKey(temp))
				{
					itemMap.get(temp).worth = input.nextInt();
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
			tempList.add(altered+" "+itemMap.get(s).worth+" "+itemMap.get(s).getTimeUpdated());
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
	
	Item getItem(String name)
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
		if (Main.gm.currentItem != null)
		{
			Main.gm.showItem(Main.gm.currentItem);
		}
	}
}
