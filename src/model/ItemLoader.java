package model;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * 		ItemLoader: helper class to load all the recipes into DataManager.
 * 			(less clutter for DataManager.java)
 */

public class ItemLoader {

	private HashMap<String, Item> itemMap;
	private ArrayList<Material> materials; 
	private ArrayList<Craftable> currentList; //used in addCraftable()
	
	
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
		
			addCraftable(Craftable.builder("Auto-Craft [I]")
					.setSize(2)
					.setWorkload(50000)
					.addComponent(asItem("Multi-Hued Crystal Shard"), 5)
					.addComponent(asItem("Platinum Ingot"), 50)
					.addComponent(asItem("Hammer [I]"), 100)
					.addComponent(asItem("Enchant Stone [I]"), 10)
					.build());
			
			addCraftable(Craftable.builder("Auto-Craft [II]")
					.setSize(4)
					.setWorkload(210000)
					.addComponent(asItem("Multi-Hued Crystal"), 1)
					.addComponent(asItem("Platinum Ingot"), 100)
					.addComponent(asItem("Hammer [II]"), 50)
					.addComponent(asItem("Enchant Stone [II]"), 10)
					.addComponent(asItem("Auto-Craft Doll MK-1"), 2)
					.build());
				
			addCraftable(Craftable.builder("Auto-Craft [III]")
					.setSize(4)
					.setWorkload(530000)
					.addComponent(asItem("Multi-Hued Jewel Shard"), 1)
					.addComponent(asItem("Ruby"), 50)
					.addComponent(asItem("Hammer [III]"), 50)
					.addComponent(asItem("Enchant Stone [III]"), 10)
					.addComponent(asItem("Auto-Craft Doll MK-2"), 3)
					.build());
				
			addCraftable(Craftable.builder("Auto-Craft [IV]")
					.setSize(4)
					.setWorkload(4500000)
					.addComponent(asItem("Multi-Hued Jewel"), 5)
					.addComponent(asItem("Ruby"), 100)
					.addComponent(asItem("Hammer [III]"), 50)
					.addComponent(asItem("Enchant Stone [IV]"), 5)
					.addComponent(asItem("Auto-Craft Doll MK-3"), 3)
					.addComponent(asItem("Sea Element"), 70)
					.build());
		}	
	}
	
	void loadCrystal(ArrayList<Craftable> crystal)
	{
		if (crystal.isEmpty())
		{
			currentList = crystal;
		
			addCraftable(Craftable.builder("Multi-Hued Crystal Shard")
					.setSize(5)
					.setWorkload(46000)
					.addComponent(asItem("Phoenix Crystal"), 1)
					.addComponent(asItem("Giant Crystal"), 1)
					.addComponent(asItem("Redemption Crystal"), 1)
					.addComponent(asItem("Dragon Crystal"), 1)
					.addComponent(asItem("Ashen Crystal"), 1)
					.build());					
		
			addCraftable(Craftable.builder("Multi-Hued Crystal")
					.setSize(5)
					.setWorkload(200000)
					.addComponent(asItem("Multi-Hued Crystal Shard"), 5)
					.addComponent(asItem("Mandragora"), 2)
					.addComponent(asItem("Platinum Ingot"), 25)
					.addComponent(asItem("Coral"), 100)
					.addComponent(asItem("Opal"), 3)
					.build());
		}
	}
	
	void loadFood(ArrayList<Craftable> food)
	{
		if (food.isEmpty())
		{
			currentList = food;
		
			addCraftable(Craftable.builder("Dumpling")
					.setSize(10)
					.setWorkload(500)
					.addComponent(asItem("Pork"), 4)
					.addComponent(asItem("Wheat"), 6)
					.build());
			
			addCraftable(Craftable.builder("Grilled Pork")
					.setSize(10)
					.setWorkload(600)
					.addComponent(asItem("Pork"), 8)
					.addComponent(asItem("Salt"), 4)
					.build());
		
			addCraftable(Craftable.builder("Grilled Shrimp")
					.setSize(10)
					.setWorkload(800)
					.addComponent(asItem("Shrimp"), 10)
					.addComponent(asItem("Salt"), 5)
					.build());
		
			addCraftable(Craftable.builder("Octopus Soup")
					.setSize(10)
					.setWorkload(960)
					.addComponent(asItem("Octopus"), 12)
					.addComponent(asItem("Salt"), 6)
					.build());
			
			addCraftable(Craftable.builder("Barley Bibimbap")
					.setSize(10)
					.setWorkload(7000)
					.addComponent(asItem("Barley"), 60)
					.addComponent(asItem("Salt"), 20)
					.addComponent(asItem("Sesame Oil"), 5)
					.build());
		}
	}
	
	void loadMachine(ArrayList<Craftable> machine)
	{
		if (machine.isEmpty())
		{
			currentList = machine;
		
			addCraftable(Craftable.builder("Auto-Craft Doll MK-1")
					.setSize(5)
					.setWorkload(1830000)
					.addComponent(asItem("Small Marionette"), 1)
					.addComponent(asItem("Mysterious Vial: Wisdom"), 1)
					.addComponent(asItem("Earth Element Shard"), 300)
					.addComponent(asItem("Normal Crafting Secrets"), 100)
					.addComponent(asItem("Polish"), 10)
					.addComponent(asItem("Pearl"), 150)
					.addComponent(asItem("Oil"), 15)
					.addComponent(asItem("Small Bolt"), 15)
					.build());
			
			addCraftable(Craftable.builder("Auto-Craft Doll MK-2")
					.setSize(5)
					.setWorkload(3500000)
					.addComponent(asItem("Small Marionette"), 1)
					.addComponent(asItem("Mysterious Vial: Wisdom"), 2)
					.addComponent(asItem("Sea Element Shard"), 200)
					.addComponent(asItem("Artisan Crafting Secrets"), 50)
					.addComponent(asItem("Polish"), 10)
					.addComponent(asItem("Platinum Ingot"), 5)
					.addComponent(asItem("Oil"), 30)
					.addComponent(asItem("Small Bolt"), 30)
					.build());
				
			addCraftable(Craftable.builder("Auto-Craft Doll MK-3")
					.setSize(5)
					.setWorkload(9800000)
					.addComponent(asItem("Small Marionette"), 1)
					.addComponent(asItem("Mysterious Vial: Wisdom"), 2)
					.addComponent(asItem("Sea Element"), 50)
					.addComponent(asItem("Book of Craftsmanship"), 20)
					.addComponent(asItem("Polish"), 10)
					.addComponent(asItem("Platinum Ingot"), 10)
					.addComponent(asItem("Oil"), 30)
					.addComponent(asItem("Small Bolt"), 30)
					.build());
			
			addCraftable(Craftable.builder("Net Trap[III]")
					.setSize(10)
					.setWorkload(3000000)
					.addComponent(asItem("Oil"), 40)
					.addComponent(asItem("Platinum Ingot"), 120)
					.addComponent(asItem("Gold Ingot"), 50)
					.addComponent(asItem("Tanzanite"), 40)
					.addComponent(asItem("Gold Thread"), 50)
					.addComponent(asItem("Big Slab of Lumber"), 50)
					.addComponent(asItem("Normal Dynamite"), 40)
					.build());
		}
	}
	
	void loadMedicine(ArrayList<Craftable> medicine)
	{
		if (medicine.isEmpty())
		{
			currentList = medicine;
		
			addCraftable(Craftable.builder("Mana Potion [III]")
					.setSize(10)
					.setWorkload(5700)
					.addComponent(asItem("Chrysanthemum"), 15)
					.addComponent(asItem("Jasmine"), 15)
					.addComponent(asItem("Cyclamen"), 10)
					.build());
				
			addCraftable(Craftable.builder("Mysterious Vial: Wisdom")
					.setSize(10)
					.setWorkload(1430000)
					.addComponent(asItem("Mandragora"), 4)
					.addComponent(asItem("Multi-Hued Crystal"), 5)
					.addComponent(asItem("Growth Vial [II]"), 50)
					.addComponent(asItem("Mana Potion [III]"), 100)
					.addComponent(asItem("Jasmine"), 400)
					.addComponent(asItem("Green Mold"), 600)
					.build());
		}
	}
	
	void loadTool(ArrayList<Craftable> tool)
	{
		if (tool.isEmpty())
		{
			currentList = tool;
		
			addCraftable(Craftable.builder("Small Bolt")
					.setSize(20)
					.setWorkload(3500)
					.addComponent(asItem("Platinum Ingot"), 1)
					.addComponent(asItem("Silver Ingot"), 30)
					.addComponent(asItem("Coral"), 100)
					.build());
			
			addCraftable(Craftable.builder("Normal Dynamite")
					.setSize(10)
					.setWorkload(140000)
					.addComponent(asItem("Pearl"), 300)
					.addComponent(asItem("Ruby"), 50)
					.addComponent(asItem("Pine"), 200)
					.addComponent(asItem("Coral"), 500)
					.addComponent(asItem("Common Gunpowder"), 50)
					.build());
		}
	}
	
	/**
	 * Helper to convert from String to Item
	 * @param name of item
	 * @return item object matching given name
	 */
	private Item asItem(String name)
	{
		if (!itemMap.containsKey(name))
		{
			addMaterial(name);
		}
		return itemMap.get(name);
	}
	
	// Lazy Material Adding
	private void addMaterial(String material)
	{
		itemMap.put(material, new Material(material));
		//System.out.println(material+" "+itemMap.get(material));
		materials.add((Material) (itemMap.get(material)));
	}
	
	private void addCraftable(Craftable c)
	{
		if (itemMap.containsKey(c.name)) //it exists as material
		{
			Material oldMaterial = (Material) itemMap.get(c.name); //hold old data
			
			itemMap.put(c.name, c); //replace data
			
			//update the items the material was used in
			for (int i = 0; i < oldMaterial.getCraftsIntoLength(); i++)
			{
				oldMaterial.getCraftsInto(i).remapRecipe(itemMap);
				c.addCraftsInto(oldMaterial.getCraftsInto(i));
			}
			
			materials.remove(oldMaterial); //clean up materials array
		}
		else //it doesnt exist as material
		{
			itemMap.put(c.name, c);
		}
		
		for (int i = 0; i < c.getCraftedFromLength(); i++)
		{
			//adds this craftable into the craftsInto
			//list of the items it is crafted from
			c.getCraftedFromItems(i).addCraftsInto(c);
		}
		
		currentList.add(c);
	}
}
