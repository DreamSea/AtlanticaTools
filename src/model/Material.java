package model;

/*
 * 		Material: Currently placeholder for things that are not craftable...
 */

public class Material extends Item {
	
	public Material(String name) {
		super(name, Item.TYPE_MATERIAL);
	}

	
	// This converts the material into a craftable
	public Craftable convert(int workload, int numCrafted, int numComponents)
	{
		Craftable toReturn = new Craftable(name, workload, numCrafted, numComponents);
		for (Craftable c : craftsInto)
		{
			toReturn.craftsInto.add(c);
		}
		//toReturn.craftsInto = craftsInto.;
		return toReturn;
	}

}
