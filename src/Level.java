import java.awt.Graphics;

//Builds a level; Path is the Level.txt; ts is the TileSet/Pictures

public class Level
{
	private TileSet ts; //Used TileSet
	private int sizeX, sizeY;
	private int[][] tileMap;

	public Level(String path, TileSet ts)
	{
		this.ts = ts;
		String file = Utils.loadFileAsString(path); //Level data
		String[] tokens = file.split("\\s");
		sizeX = Utils.tryParseInt(tokens[0]);
		sizeY = Utils.tryParseInt(tokens[1]);
		tileMap = new int[sizeX][sizeY];//Level Data for each field
		int i = 2; //skip the x/y size
		
		for (int y = 0; y < sizeY; y++)
		{
			for (int x = 0; x < sizeX; x++)
			{
				tileMap[x][y] = Utils.tryParseInt(tokens[i++]);
			}
		}
	}

	public void renderMap(Graphics g)
	{
		for (int tileY = 0; tileY < sizeY; tileY++)
		{
			for (int tileX = 0; tileX < sizeX; tileX++)
			{
				ts.renderTile(g, tileMap[tileX][tileY], tileX * TileSet.TILEWIDTH, tileY * TileSet.TILEHEIGHT);
			}
		}
	}
}
