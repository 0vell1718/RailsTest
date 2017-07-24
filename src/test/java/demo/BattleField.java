package demo;

import webdriver.BaseEntity;

import java.util.ArrayList;

/**
 * Created by Михаил on 24.07.2017.
 */
public class BattleField extends BaseEntity {
    private Integer four_deck = 1;
    private Integer three_deck = 2;
    private Integer double_deck = 3;
    private Integer one_deck = 4;

    private ArrayList<Point> missPoints = new ArrayList<>();
    private ArrayList<Point> hitPoints = new ArrayList<>();
    private ArrayList<Point> ship = new ArrayList<>();
    private boolean isShipHit = false;


    public Point setNextHitPoint(Point lastHitPoint){
        isShipHit = true;
        addDiagonalPointsToMiss(lastHitPoint);
        


        return new Point(0,0);
    }

    private void addDiagonalPointsToMiss(Point centerPoint){
        Integer x = centerPoint.getX();
        Integer y = centerPoint.getY();

        for(int xx = x - 1; xx <= x + 1; xx += 2){
            for(int yy = y - 1; yy <= y + 1; yy +=2 ){
                if(xx < 0 || xx > 9 || yy < 0 || yy > 9){
                    continue;
                } else {
                    Point p = new Point(xx, yy, "miss");
                    if(!missPoints.contains(p))
                        missPoints.add(p);
                }
            }
        }
    }

    public Point setNextEmptyPoint(Point lastMissPoint){

        return new Point(0,0);
    }


    public void addMissPoint(Point missPoint){
        missPoints.add(missPoint);
    }

    public void addHitPoint(Point hitPoint){
        hitPoints.add(hitPoint);
    }

    @Override
    protected String formatLogMsg(String message) {
        return null;
    }
}
