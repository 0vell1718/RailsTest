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

    private ArrayList<Point> stepPointsOneDeckShip = new ArrayList<>();
    private ArrayList<Point> stepPointsFourDeckShip = new ArrayList<>();
    private ArrayList<Point> stepPointsThreeAndTwoDeckShip = new ArrayList<>();

    private ArrayList<Point> missPoints = new ArrayList<>();
    private ArrayList<Point> hitPoints = new ArrayList<>();

    private ArrayList<Point> ship = new ArrayList<>();
    private ArrayList<Point> verticalPossibleHitPoints = new ArrayList<>();
    private ArrayList<Point> horizontalPossibleHitPoints = new ArrayList<>();


    public void addPossibleHitPointsLists(Point lastHitPoint){
        //удаляем точку из списков для дальнейших выстрелов
        removePointFromLists(lastHitPoint, stepPointsOneDeckShip, stepPointsThreeAndTwoDeckShip, stepPointsFourDeckShip);

        addPointToList(lastHitPoint, hitPoints);
        addPointToList(lastHitPoint, ship);

        //берем диагональные от исходной точки и добавляем в список miss точек
        addDiagonalPointsToMissList(lastHitPoint);
        //берем накрест лежащие точки для следующих выстрелов(исключая miss и hit)
        addPossibleHitPointsToLists(lastHitPoint);

        //если на прошлых шагах был выбор в какую из точек стрелять(по диагонали или вертикали) и выбор был успешный
        //то из противоположной диагонали точки переходят в miss(корабль м.б. только на одной линии)
        deleteUnnecessaryHitPointsFromList(lastHitPoint, verticalPossibleHitPoints, horizontalPossibleHitPoints);
        deleteUnnecessaryHitPointsFromList(lastHitPoint, horizontalPossibleHitPoints, verticalPossibleHitPoints);
    }

    public Point getNextPossibleHitPoint(Point lastPoint){
        //попадаем сюда если попали в корабль
        //дальше точку выбиравем из окружения
        //если таких не осталось - корабль сбит, берем следующую по условию точку
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
        removePointFromLists(lastPoint, stepPointsOneDeckShip, stepPointsThreeAndTwoDeckShip, stepPointsFourDeckShip);
        Point p = lastPoint;

        //если мы на предыдущем шаге попали в корабль, а на этом нет - то идем по первому условию
        if(!ship.isEmpty()){
            p.setStatus("empty");
            if(containsInList(p, verticalPossibleHitPoints))
                verticalPossibleHitPoints.remove(p);
            if(containsInList(p, horizontalPossibleHitPoints))
                horizontalPossibleHitPoints.remove(p);
            p.setStatus("miss");
            p = getNextPossibleHitPoint(lastPoint);

        }
        //если в прошлый раз тоже было мимо - идем по этому условию
        //в самом начале будем стрелять по полю таким образом, чтобы за наименьшее число шагов
        //выбить 4х палубник - это позволит вычеркнуть для дальнейших выстрелов больше всего точек(вокруг него)
        else if (fourDeck > 0){
            p = getRandomPointFromList(stepPointsFourDeckShip);
        }
        //далее будем стрелять сеткой через 1 клетку чтобы гарантировать выбивате 2х и 3х палубников
        else if(threeDeck > 0 || doubleDeck > 0) {
            p = getRandomPointFromList(stepPointsThreeAndTwoDeckShip);
        }
        //если остались только 1х палубники то стреляем на удачу
        //при этом вычеркивая прошлые выстрелы
        else if(oneDeck > 0) {
            if (!stepPointsThreeAndTwoDeckShip.isEmpty()) {
                p = getRandomPointFromList(stepPointsThreeAndTwoDeckShip);
            } else {
                p = getRandomPointFromList(stepPointsOneDeckShip);
            }
            if (!containsInList(p, missPoints)) {
                p.setStatus("hit");
                if (!containsInList(p, hitPoints)) {
                    p.setStatus("empty");
                }
            }
        }
        return p;
    }

    public void setPointsToShot(){
        setStepPointsToFindFourDeckShip();
        setStepPointsToFindThreeAndTwoDeckShip();
        setStepPointsToFindOneDeckShip();
    }

    private void setStepPointsToFindFourDeckShip(){
       for (int xy = 0; xy < 10 ; xy++) {                       //главная диагональ
           stepPointsFourDeckShip.add(new Point(xy, xy));
       }

       for (int x = 0; x < 6 ; x++) {                           //через 1 от главной диагонали
           stepPointsFourDeckShip.add(new Point(x, x + 4));
           stepPointsFourDeckShip.add(new Point(x + 4, x));
       }

       for(int x = 0; x < 2; x++) {                             //углы
           stepPointsFourDeckShip.add(new Point(x, x + 8));
           stepPointsFourDeckShip.add(new Point(x + 8, x));
       }
    }

    private void setStepPointsToFindThreeAndTwoDeckShip(){
        stepPointsThreeAndTwoDeckShip.addAll(stepPointsFourDeckShip);
        for(int x = 0; x < 8; x++){
            stepPointsThreeAndTwoDeckShip.add(new Point(x, x + 2));
            stepPointsThreeAndTwoDeckShip.add(new Point(x + 2, x));
        }

        for(int x = 0; x < 4; x++){
            stepPointsThreeAndTwoDeckShip.add(new Point(x, x + 6));
            stepPointsThreeAndTwoDeckShip.add(new Point(x + 6, x));
        }
    }

    private void setStepPointsToFindOneDeckShip(){
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){
                stepPointsOneDeckShip.add(new Point(x, y));
            }
        }
        stepPointsOneDeckShip.removeAll(stepPointsThreeAndTwoDeckShip);
    }

    private void addDiagonalPointsToMissList(Point centerPoint){
        Integer x = centerPoint.getX();
        Integer y = centerPoint.getY();

        for(int xx = x - 1; xx <= x + 1; xx += 2){
            for(int yy = y - 1; yy <= y + 1; yy +=2 ) {
                if (xx >= 0 && xx <= 9 && yy >= 0 && yy <= 9) {
                    Point p = new Point(xx, yy, "miss");
                    if (!containsInList(p, missPoints)) {
                        missPoints.add(p);
                        removePointFromLists(p, stepPointsOneDeckShip, stepPointsThreeAndTwoDeckShip, stepPointsFourDeckShip);
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
            if (p.getX() >= 0 && p.getX() <= 9 && p.getY() >= 0 && p.getY() <= 9) {
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

    private void deleteUnnecessaryHitPointsFromList(Point lastHitPoint, ArrayList<Point> containPointList, ArrayList<Point> removeList) {
        if (containPointList.contains(lastHitPoint)) {
            containPointList.remove(lastHitPoint);
            for (Point p : removeList) {
                p.setStatus("miss");
                if (!containsInList(p, missPoints)) {
                    missPoints.add(p);
                    removePointFromLists(p, stepPointsOneDeckShip, stepPointsThreeAndTwoDeckShip, stepPointsFourDeckShip);
                }
                p.setStatus("empty");
            }
            removeList.clear();
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

    private void addPointToList(Point hitPoint, ArrayList<Point> list){
        if(!containsInList(hitPoint, list)){
            list.add(hitPoint);
        }
    }

    private void removePointFromList(Point hitPoint, ArrayList<Point> list){
        if(containsInList(hitPoint, list))
            list.remove(hitPoint);
    }

    private void removePointFromLists(Point p, ArrayList<Point> ... lists){
        for(ArrayList<Point> list : lists){
            removePointFromList(p, list);
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

    private Point getRandomPointFromList(ArrayList<Point> list){
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    protected String formatLogMsg(String message) {
        return null;
    }
}
