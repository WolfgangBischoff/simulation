import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//Loads a picture and cuts it into tiles, saves the tiles in an Array

public class TileSet
{
	public static final int TILEWIDTH = 64, TILEHEIGHT = 64;

	private BufferedImage[] tiles;

	public TileSet(String path, int sizeX, int sizeY)
	{
		tiles = new BufferedImage[sizeX * sizeY];
		BufferedImage tileSet;
		try
		{
			tileSet = ImageIO.read(TileSet.class.getResource(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		int i = 0;
		for (int y = 0; y < sizeY; y++)
		{
			for (int x = 0; x < sizeX; x++)
			{
				tiles[i++] = tileSet.getSubimage(x * (TILEWIDTH + 3), y * (TILEHEIGHT + 3), TILEWIDTH, TILEHEIGHT);
			}
		}
	}

	//draw a Tile to a position of a canvas
	public void renderTile(Graphics g, int tileNum, int x, int y)
	{
		g.drawImage(tiles[tileNum], x, y, TILEWIDTH, TILEHEIGHT, null);
	}
}
