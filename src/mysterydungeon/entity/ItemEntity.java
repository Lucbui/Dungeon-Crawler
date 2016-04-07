/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;
import mysterydungeon.item.Item;

/**
 *
 * @author Justis
 */
public class ItemEntity implements Entity<Item>
{
    private final Item item;
    private final Node currentNode;
    
    public ItemEntity(Item item, Dungeon dungeon)
    {
        this.item = item;
        currentNode = Entity.generateRandomLocation(dungeon);
    }
    
    public ItemEntity(Item item, Node node)
    {
        this.item = item;
        currentNode = node;
    }
    
    @Override
    public Item getContained()
    {
        return item;
    }
    
    @Override
    public int getX()
    {
        return currentNode.getX();
    }
    
    @Override
    public int getY()
    {
        return currentNode.getY();
    }   
    
    @Override
    public BufferedImage getImage()
    {
        return item.getImage();
    }
}
