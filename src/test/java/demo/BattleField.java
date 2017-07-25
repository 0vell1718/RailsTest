package demo;

import webdriver.BaseEntity;

import java.util.ArrayList;
import java.util.Random;

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
        addPointToList(lastHitPoint, hitPoints);
        addPointToList(lastHitPoint, ship);

        addDiagonalPointsToMissList(lastHitPoint);
        addPossibleHitPointsToLists(lastHitPoint);
        deleteUnnecessaryHitPointsFromLists(lastHitPoint);
    }

    public Point getNextPossibleHitPoint(Point lastPoint){
        if(verticalPossibleHitPoints.isEmpty() && horizontalPossibleHitPoints.isEmpty()){
            destroyShip();
            return getNextEmptyPoint(lastPoint);
        }else if(verticalPossibleHitPoints.isEmpty() && !horizontalPossibleHitPoints.isEmpty()){
            return horizontalPossibleHitPoints.get(0);
        }else if(!verticalPossibleHitPoints.isEmpty() && horizontalPossibleHitPoints.isEmpty()){
            return verticalPossibleHitPoints.get(0);
        }else {
            return verticalPossibleHitPoints.get(0);
        }
    }

    public Point getNextEmptyPoint(Point lastPoint) {
        addPointToList(lastPoint, missPoints);
        Point p = lastPoint;
        if(!ship.isEmpty()){

            p.setStatus("empty");
            if(containsInList(p, verticalPossibleHitPoints))
                verticalPossibleHitPoints.remove(p);
            if(containsInList(p, horizontalPossibleHitPoints))
                horizontalPossibleHitPoints.remove(p);
            p.setStatus("miss");

            p = getNextPossibleHitPoint(lastPoint);

        }else if (fourDeck > 0 || threeDeck > 0 || doubleDeck > 0) {
            addPointToList(lastPoint, missPoints);
            p = randomPoint();

        }else if(oneDeck > 0){
            addPointToList(lastPoint, missPoints);
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    p = new Point(x, y, "miss");
                    if (!containsInList(p, missPoints)){
                        p.setStatus("hit");
                        if (!containsInList(p, hitPoints)) {
                            p.setStatus("empty");
                        }
                    }
                }
            }
        }
        return p;
    }

    private Point randomPoint(){
        Integer x = new Random().nextInt(10);
        Integer y = new Random().nextInt(10);
        return new Point(x, y, "empty");
    }

    private void addDiagonalPointsToMissList(Point centerPoint){
        Integer x = centerPoint.getX();
        Integer y = centerPoint.getY();

        for(int xx = x - 1; xx <= x + 1; xx += 2){
            for(int yy = y - 1; yy <= y + 1; yy +=2 ) {
                if (xx < 0 || xx > 9 || yy < 0 || yy > 9) {
                    continue;
                } else {
                    if (!containsInList(new Point(xx, yy, "miss"), missPoints)) {
                        missPoints.add(new Point(xx, yy, "miss"));
                    }
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
        for(Point p : points) {
            if (p.getX() < 0 || p.getX() > 9 || p.getY() < 0 || p.getY() > 9) {
                continue;
            } else {
                p.setStatus("miss");
                if (!containsInList(p, missPoints)) {
                    p.setStatus("hit");
                    if (!containsInList(p, hitPoints)) {
                        p.setStatus("empty");
                        if (!containsInList(p, pointList))
                            pointList.add(p);
                    }
                }
            }
        }
    }

    private Boolean containsInList(Point point, ArrayList<Point> list){
        for(Point p : list){
            if(point.getY() == p.getY() && point.getX() == p.getX() && point.getStatus() == p.getStatus()){
                return true;
            }
        }
        return false;
    }

    private void deleteUnnecessaryHitPointsFromLists(Point lastHitPoint){
        if(verticalPossibleHitPoints.contains(lastHitPoint)){
            verticalPossibleHitPoints.remove(lastHitPoint);
            for(Point p : horizontalPossibleHitPoints){
                p.setStatus("miss");
                if(!containsInList(p, missPoints)){
                    missPoints.add(p);
                }
                p.setStatus("empty");
            }
            horizontalPossibleHitPoints.clear();
        }else if(horizontalPossibleHitPoints.contains(lastHitPoint)) {
            horizontalPossibleHitPoints.remove(lastHitPoint);
            for (Point p : verticalPossibleHitPoints) {
                p.setStatus("miss");
                if(!containsInList(p, missPoints)){
                    missPoints.add(p);
                }
                p.setStatus("empty");
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
        if(!missPoints.contains(missPoint)){
            missPoints.add(missPoint);
        }
    }

    public void addPointToList(Point hitPoint, ArrayList<Point> list){
        if(!containsInList(hitPoint, list)){
            list.add(hitPoint);
        }
    }

    @Override
    protected String formatLogMsg(String message) {
        return null;
    }
}
