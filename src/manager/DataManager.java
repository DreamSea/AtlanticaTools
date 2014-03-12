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

import types.Craftable;
import types.Item;
import types.Material;

public class DataManager {
	
	public HashMap<String, Item> itemMap;
	
	public ArrayList<Material> materials;
	
	public ArrayList<ArrayList<Craftable>> skillTree;
	public ArrayList<Craftable> crystal;
	public ArrayList<Craftable> food;
	
	private static ArrayList<Craftable> currentList;
	
	public static double costPerWorkload = 1.0;	//TODO temporary, replace with craft book item later
	
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
		initCrystal();
		initFood();
	}
	
	private void initMaterial()
	{
		materials = new ArrayList<Material>();
		addMaterial("Phoenix Crystal");
		addMaterial("Giant Crystal");
		addMaterial("Redemption Crystal");
		addMaterial("Dragon Crystal");
		addMaterial("Ashen Crystal");
		addMaterial("Salt");
		addMaterial("Pork");
		addMaterial("Wheat");
		addMaterial("Barley");
		addMaterial("Salt");
		addMaterial("Sesame Oil");
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
		
		addCraftable("Barley Bibimbap", 7000, 10, 3);
			addRecipe("Barley", 60);
			addRecipe("Salt", 20);
			addRecipe("Sesame Oil", 5);
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
		
		temp.craftsInto.add(Craftable.lastCraftable);
		
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
	
	public void savePrices()
	{
		System.out.println("saveme");

		Set<String> tempSet = itemMap.keySet();
		ArrayList<String> tempList = new ArrayList<String>();
		
		for (String s : tempSet)
		{
			String altered = s.replace(" ", "_");
			tempList.add(altered+" "+itemMap.get(s).worth);
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
}
