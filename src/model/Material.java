package model;

/*
 * 		Material: Currently placeholder for things that are not craftable...
 */

public class Material extends Item {
	
	public Material(String name) {
		super(name);
	}
	
	public ItemType getType()
	{
		return ItemType.MATERIAL;
	}
	
}
