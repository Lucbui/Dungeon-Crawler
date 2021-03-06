/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.SpeciesEntity;

/**
 * Represents a light-generating item.
 * This type of item is for increasing the fog of war a certain amount.
 * @author jlamanna
 */
public class LightItem implements Item
{

    /**
     * The item "Torch", which expands the FOW by 50%.
     */
    public static final LightItem TORCH = new LightItem("Torch", 1.5);

    /**
     * The item "Flashlight", which doubles the FOW.
     */
    public static final LightItem FLASHLIGHT = new LightItem("Flashlight", 2);
    
    private final double size;
    private final String name;
    
    /**
     * Creates a light item.
     * @param name The name of the item.
     * @param size The new radius of light is equal to 100 times this parameter.
     */
    public LightItem(String name, double size)
    {
        this.size = size;
        this.name = name;
    }
    
    @Override
    public boolean useItem(SpeciesEntity user)
    {
        MysteryDungeon.updateLog(String.format("%s was able to see farther.", user.getName()));
        Dungeon thisDungeon = user.getDungeon();
        thisDungeon.setShadow(thisDungeon.generateShadow((int)(100*size), (int)(25 * size)));
        thisDungeon.setDiscovered(user.getTileX(), user.getTileY());
        return REMOVE;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public BufferedImage getImage()
    {
        try
        {
            return ImageIO.read(new File("Sprites/item_" + name.replaceAll(" ", "_") + ".png"));
        }
        catch (IOException ex)
        {
            return new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    @Override
    public String getDescription()
    {
        return String.format("Increases view size by %.1fx", size);
    }
    
    @Override
    public int getType()
    {
        return Item.LIGHT;
    }
}
