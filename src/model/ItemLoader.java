package model;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemLoader {

	private HashMap<String, Item> itemMap;
	private ArrayList<Material> materials; 
	private ArrayList<Craftable> currentList;
	
	
	public ItemLoader(HashMap<String, Item> hm, ArrayList<Material> al)
	{
		itemMap = hm;
		materials = al;
	}
	
	//No modifier for methods - only meant to be called by DataManager really
	void loadCraftBook(ArrayList<CraftBook> cb)
	{	
		if (cb.isEmpty()) //TODO: these checks shouldn't be needed
		{
			cb.add(new CraftBook("Small Crafting Secrets", 100));
			cb.add(new CraftBook("Normal Crafting Secrets", 1000));
			cb.add(new CraftBook("Artisan Crafting Secrets", 10000));
			cb.add(new CraftBook("Book of Craftsmanship", 100000));
			cb.add(new CraftBook("Book of Expertise", 1000000));
			cb.add(new CraftBook("Book of Mastery", 5000000));
			cb.add(new CraftBook("Golden Book of Craftsmanship", 50000000));
		
			for (CraftBook book : cb)
			{
				itemMap.put(book.getName(), book);
			}
		}
	}
	
	void loadAction(ArrayList<Craftable> action)
	{
		if (action.isEmpty())
		{
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
	}
	
	void loadCrystal(ArrayList<Craftable> crystal)
	{
		if (crystal.isEmpty())
		{
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
	}
	
	void loadFood(ArrayList<Craftable> food)
	{
		if (food.isEmpty())
		{
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
	}
	
	void loadMachine(ArrayList<Craftable> machine)
	{
		if (machine.isEmpty())
		{
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
	}
	
	void loadMedicine(ArrayList<Craftable> medicine)
	{
		if (medicine.isEmpty())
		{
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
	}
	
	void loadTool(ArrayList<Craftable> tool)
	{
		if (tool.isEmpty())
		{
			currentList = tool;
		
			addCraftable("Small Bolt", 3500, 20, 3);
				addRecipe("Platinum Ingot", 1);
				addRecipe("Silver Ingot", 30);
				addRecipe("Coral", 100);
		}
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
		//itemMap should only contain key if previously added as material
		if (itemMap.containsKey(craftable))
		{
			//TODO: just in case for now, but this check shouldn't be needed
			//once other areas of code are cleaned up
			if (itemMap.get(craftable).getType() == 0)
			{
				Material toConvert = (Material) itemMap.get(craftable);
				
				materials.remove(toConvert);
				
				Craftable converted = toConvert.convert(workload, numCrafted, numComponents);
				
				itemMap.remove(craftable);
				itemMap.put(craftable,  converted);
				
				//resets all the components from the previous material into new craftable
				for (Craftable c : converted.craftsInto)
				{
					c.remapRecipe(itemMap);
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
		temp.craftsInto.add(Craftable.lastCraftable);
		Craftable.lastCraftable.addRecipe(itemMap.get(name), number);
	}
}
