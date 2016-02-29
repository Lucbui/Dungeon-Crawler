import javax.swing.*;
import java.awt.*;

public class DungeonView extends JFrame
{
	public static final int INITIAL_WIDTH = 800;
	public static final int INITIAL_HEIGHT = 800;
	public static final String TILES = "tiles.png";
	public static final String TILEMAP = "tilemap.png";

	private Dungeon dungeon;

	public static void main(String[] args)
	{
		DungeonView frame = new DungeonView(TILES, TILEMAP);
		frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public DungeonView(String tileFilename, String tilemapFilename)
	{
		JPanel frame = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Dungeon dungeon = new Dungeon(tileFilename, tilemapFilename);
		c = setGridBagConstraints(0, 0, 5, 1, 0.9, 1.0);
		frame.add(dungeon, c);
		add(frame);
	}

	public GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty)
	{
		return new GridBagConstraints(
			gridx, 
			gridy, 
			gridwidth, 
			gridheight, 
			weightx, 
			weighty, 
			GridBagConstraints.CENTER, 
			GridBagConstraints.BOTH, 
			new Insets(5, 5, 5, 5), 
			0, 
			0);
	}
}