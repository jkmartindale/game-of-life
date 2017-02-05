//Simple container that is aware of whether it is dead or alive and can tell the future
public class Cell
{
	public boolean alive;
	public boolean survives;

	public Cell()
	{
		alive = false;
	}

	public String toString() { return "" + alive; } //Boolean to string trick :3
}