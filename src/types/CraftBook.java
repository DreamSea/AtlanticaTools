package types;

public class CraftBook extends Item{

	public CraftBook(String name, int workload)
	{
		super(name, Item.TYPE_CRAFTBOOK);
		
		this.workload = workload;
	}

	private int workload;
	
	
	//returns workload book gives
	public int getWorkload()
	{
		return workload;
	}
	
	public double getWorthPerWorkload()
	{
		return worth*1.0/workload;
	}
}
