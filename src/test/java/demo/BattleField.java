package demo;

import webdriver.BaseEntity;

import java.util.ArrayList;
/**
 * Created by Михаил on 24.07.2017.
 */
public class BattleField extends BaseEntity {
    private Integer fourDeck = 1;
    private Integer threeDeck = 2;
    private Integer doubleDeck = 3;
    private Integer oneDeck = 4;

    private ArrayList<Point> missPoints = new ArrayList<>();
    private ArrayList<Point> hitPoints = new ArrayList<>();

    private ArrayList<Point> ship = new ArrayList<>();
    private ArrayList<Point> verticalPossibleHitPoints = new ArrayList<>();
    private ArrayList<Point> horizontalPossibleHitPoints = new ArrayList<>();


    public void addPossibleHitPointsLists(Point lastHitPoint){
        hitPoints.add(lastHitPoint);
        ship.add(lastHitPoint);

        addDiagonalPointsToMissList(lastHitPoint);
        addPossibleHitPointsToLists(lastHitPoint);
        deleteUnnecessaryHitPointsFromLists(lastHitPoint);
    }

    public Point getNextPossibleHitPoint(){
        if(verticalPossibleHitPoints.isEmpty() && horizontalPossibleHitPoints.isEmpty()){
            destroyShip();
            return getNextEmptyPoint();
        }else if(verticalPossibleHitPoints.isEmpty() && !horizontalPossibleHitPoints.isEmpty()){
            return horizontalPossibleHitPoints.get(0);
        }else if(!verticalPossibleHitPoints.isEmpty() && horizontalPossibleHitPoints.isEmpty()){
            return verticalPossibleHitPoints.get(0);
        }else {
            return verticalPossibleHitPoints.get(0);
        }
    }

    public Point getNextEmptyPoint() {
        Point p = new Point(0, 0, "miss");
        if (fourDeck > 0 || threeDeck > 0 || doubleDeck > 0) {

        }else if(oneDeck > 0){
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    p = new Point(x, y, "miss");
                    if (!missPoints.contains(p)){
                        p.setStatus("hit");
                        if (!hitPoints.contains(p)) {
                            p.setStatus("empty");
                        }
                    }
                }
            }
        }
        return p;
    }


    private void addDiagonalPointsToMissList(Point centerPoint){
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

    private void addPossibleHitPointsToLists(Point centerPoint){
        //vertical points
        Point p1 = new Point(centerPoint.getX(), centerPoint.getY() - 1);
        Point p2 = new Point(centerPoint.getX(), centerPoint.getY() + 1);
        addPossibleHitPointsToList(verticalPossibleHitPoints, p1, p2);

        //horizontal points
        Point p3 = new Point(centerPoint.getX() - 1, centerPoint.getY());
        Point p4 = new Point(centerPoint.getX() + 1, centerPoint.getY());
        addPossibleHitPointsToList(horizontalPossibleHitPoints, p3, p4);
    }

    private void addPossibleHitPointsToList(ArrayList<Point> pointList, Point ... points){
        for(Point p : points){
            p.setStatus("miss");
            if(!pointList.contains(p) && !missPoints.contains(p)){
                p.setStatus("hit");
                if(!hitPoints.contains(p)){
                    if(p.getX() < 0 || p.getX() > 9 || p.getY() < 0 || p.getY() > 9){
                        continue;
                    }else{
                        p.setStatus("empty");
                        pointList.add(p);
                    }
                }
            }
        }
    }

    private void deleteUnnecessaryHitPointsFromLists(Point lastHitPoint){
        if(verticalPossibleHitPoints.contains(lastHitPoint)){
            for(Point p : horizontalPossibleHitPoints){
                if(!missPoints.contains(p)){
                    missPoints.add(p);
                }
            }
            horizontalPossibleHitPoints.clear();
        }else if(horizontalPossibleHitPoints.contains(lastHitPoint)) {
            for (Point p : verticalPossibleHitPoints) {
                if(!missPoints.contains(p)){
                    missPoints.add(p);
                }
            }
            verticalPossibleHitPoints.clear();
        }
    }

    private void destroyShip(){
        Integer size = ship.size();
        String s = "";

        switch(size) {
            case 1:
                s = "oneDeck";
                oneDeck--;
                break;
            case 2:
                s = "twoDeck";
                doubleDeck--;
                break;
            case 3:
                s = "threeDeck";
                threeDeck--;
                break;
            case 4:
                s = "fourDeck";
                fourDeck--;
                break;
        }

        logger.info(" !!!! Destroyed " + s + " ship !!!! ");
        ship.clear();
    }

    public void addPointToMissList(Point missPoint){
        missPoints.add(missPoint);
    }

    @Override
    protected String formatLogMsg(String message) {
        return null;
    }
}
