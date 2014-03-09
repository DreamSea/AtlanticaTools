package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import types.Craftable;
import types.Item;
import types.Material;

public class DataManager {
	
	public HashMap<String, Item> itemMap;
	
	public ArrayList<Material> materials;
	
	public ArrayList<ArrayList<Craftable>> skillTree;
	public ArrayList<Craftable> food;
	
	private static ArrayList<Craftable> currentList;
	
	private double costPerWorkload;	//temporary, replace with craft book item later
	
	public DataManager()
	{
		itemMap = new HashMap<String, Item>();
		skillTree = new ArrayList<ArrayList<Craftable>>();
		
		init();
		loadPrices();
	}
	
	private void init()
	{
		initMaterial();
		initFood();
		costPerWorkload = 1.0;
	}
	
	private void initMaterial()
	{
		materials = new ArrayList<Material>();
		addMaterial("Salt");
		addMaterial("Pork");
		addMaterial("Wheat");
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
	}
	
	// Lazy Material Adding
	private void addMaterial(String material)
	{
		itemMap.put(material, new Material(material));
		materials.add((Material) itemMap.get(material));
	}
	
	// Lazy Craftable Adding
	private void addCraftable(String craftable, int workload, int numCrafted, int numComponents)
	{
		itemMap.put(craftable, new Craftable(craftable, workload, numCrafted, numComponents));
		currentList.add((Craftable) itemMap.get(craftable));
	}
	
	private void addRecipe(String name, int number)
	{
		Item temp = itemMap.get(name);
		temp.craftsInto.add(Craftable.lastCraftable);
		Craftable.lastCraftable.addRecipe(itemMap.get(name), number);
		
	}
	
	private void loadPrices()
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
				}
				else
				{
					input.nextInt();
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
	
	Item getItem(String name)
	{
		return itemMap.get(name);
	}
}
