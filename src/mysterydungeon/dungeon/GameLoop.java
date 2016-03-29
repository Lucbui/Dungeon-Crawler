/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.util.ArrayList;
import mysterydungeon.Controls;
import mysterydungeon.DungeonComp;
import mysterydungeon.entity.Entity;
import mysterydungeon.move.BrawlMove;
import mysterydungeon.move.Move;

/**
 *
 * @author Justis
 */
public class GameLoop implements Runnable
{

    /**
     * The number of frames that should be used to walk one tile.
     */
    public static final int FRAMES_WALK = 6;
    
    private final DungeonComp comp;
    int moveFrame = 0;
    Move move = new BrawlMove(10);
    
    /**
     * Creates an instance of this GameLoop.
     * @param comp The DungeonComp used to paint the dungeon.
     */
    public GameLoop(DungeonComp comp)
    {
        this.comp = comp;
    }
    
    /**
     * Begins running this GameLoop, in a separate thread.
     */
    @Override
    public void run()
    {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        boolean gameRunning = true;
        while(comp.getDungeon() == null){}
        onPlayerStep(comp.getDungeon(), comp.getDungeon().getEntities().get(0));
        while(gameRunning)
        {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME); //1 if updateLength == optimal time
            updateGame(delta);
            comp.repaint();
            long sleepTime = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000;
            if(sleepTime < 10){sleepTime = 10;}
            try
            {
                Thread.sleep(sleepTime);
            }
            catch(InterruptedException ex)
            {
                gameRunning = false;
            }
        }
        System.exit(0);
    }
        
    /**
     * Updates the game using whatever game logic is programmed in.
     * @param delta A fraction representing the actual loop time, divided by the optimal time (One second).
     */
    public void updateGame(double delta)
        {
            Dungeon dungeon = comp.getDungeon();
            ArrayList<Entity> entities = dungeon.getEntities();
            Entity player = entities.get(0);
            for(Entity entity : entities)
            {
                if(entity.isMoving())
                {
                    int[] iValues = interpolate(entity, FRAMES_WALK);
                    entity.addPixel(iValues[0], iValues[1]);
                    if(entity.getDestinationNode().equals(entity.getPixelX(), entity.getPixelY()))
                    {
                        entity.setCurrentNode(entity.getDestinationNode());
                        entity.setMoving(false);
                    }
                }
                /*else if(entity.equals(player))
                {
                    if(handleControls(dungeon, entity))
                    {
                        for(Entity others : entities)
                        {
                            others.doState();
                            others.setMoving(true);
                        }
                        onPlayerStep(dungeon, player);
                    }
                }*/
            }
            if(handleControls(dungeon, player))
            {
                for(Entity others : entities)
                {
                    others.doState();
                    others.setMoving(true);
                }
                onPlayerStep(dungeon, player);
            }
        }
        
        private int[] interpolate(Entity entity, int delta)
        {
            int[] returnPoints = new int[2];
            returnPoints[0] = (entity.getDestinationNode().getX() - entity.getX()) * (24 / delta);
            returnPoints[1] = (entity.getDestinationNode().getY() - entity.getY()) * (24 / delta);
            return returnPoints;
        }
        
        private boolean handleControls(Dungeon dungeon, Entity player)
        {
            Node playerNode = player.getCurrentNode();
            Controls controls = Controls.getInstance();
            ArrayList<Entity> entities = dungeon.getEntities();
            if(controls.isFacePressed() && controls.isDirectionPressed())
            {
                if(controls.getDirection() != -1)
                {
                    player.facing = controls.getDirection();
                    controls.clearDirection();
                    return false;
                }
                else
                {
                    for(int dir = 0; dir < 8; dir++)
                    {
                        Node next = playerNode;
                        for(int range = 0; range < 4; range++)
                        {
                            if(next == null){break;}
                            next = next.getPath(dir);
                            for(Entity entity : entities)
                            {
                                if(entity.getCurrentNode().equals(next))
                                {
                                    player.facing = dir;
                                    return false;
                                }
                            }
                        }
                    }
                    return false;
                }
            }
            else if(controls.isDirectionPressed())
            {
                if(controls.getDirection() != -1 && playerNode.getPath(controls.getDirection()) != null)
                {
                    player.setDestinationNode(playerNode.getPath(controls.getDirection()));
                    player.facing = controls.getDirection();
                    player.setMoving(true);
                    controls.clearDirection();
                    return true;
                }
                else if(controls.getDirection() == -1)
                {
                    controls.clearDirection();
                    return true;
                }
                else
                {
                    //Collision. Make a bump noise in the future, I guess?
                }
            }
            else if(controls.isAttackPressed())
            {
                ArrayList<Move> knownMoves = player.getMoves();
                int attackPressed = controls.attackPressed() - 1;
                if(attackPressed < knownMoves.size())
                {
                    controls.clearAttackPressed(attackPressed);
                    Move attackUsed = knownMoves.get(attackPressed);
                    //Now, we go for the kill!
                    Entity defender = attackUsed.getDefender(comp.getDungeon(), player);
                    attackUsed.attack(comp.getDungeon(), player, defender);
                    return true;
                }
            }
            return false;
        }
        
        private void onPlayerStep(Dungeon dungeon, Entity player)
        {
            dungeon.setDiscovered(player.getDestinationNode().getY(), player.getDestinationNode().getX(), 2);
            player.addHP(1);
        }
}
