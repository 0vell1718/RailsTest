package demo.pages;

import demo.BattleField;
import demo.Point;
import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Label;

/**
 * Created by Михаил on 24.07.2017.
 */
public class GamePage extends BaseForm{
    final String gameStartYou = "Игра началась, ваш ход.";
    final String gameStartRival = "Игра началась, противник ходит.";
    final String moveOn = "Ваш ход.";
    final String moveOff = "Противник ходит, ждите.";
    final String rivalLeave = "Противник покинул игру. Дальнейшая игра невозможна.";
    final String gameWin = "Игра закончена. Поздравляем, вы победили!";
    final String gameLose = "Игра закончена. Вы проиграли.";
    final String serverError = "Сервер недоступен.";
    final String gameError = "Непредвиденная ошибка. Дальнейшая игра невозможна.";

    private String gameStatusLocator = "//div[@class='notification notification__%s']";
    private String pointLocator = "//div[@class='battlefield battlefield__rival']//div[@class='battlefield-cell-content' " +
            "and @data-y='%s' and @data-x='%s']";
    private String pointStatusLocator = "//div[contains(text(), 'Поле противника')]" +
            "/../div[@class='battlefield-table-placeholder']" +
            "//td[./div[@class='battlefield-cell-content' and @data-y='%s' and @data-x='%s']]";

    private Point point = new Point(0, 0, "empty");
    private BattleField battleField = new BattleField();

    public GamePage() {
        super(By.className("chat-gap"), "Game Page");
    }

    public void startGameWait(){
        if (!myStep() && !rivalStep()) {
            startGameWait();
        }
    }

    public void playGame(){
        battleField.setPointsToShot();
        while (endGameStatus()){
            if(myStep()) {                                                                  //проверяем наш ли ход
                point.setStatus(getPointStatus(point));

                if (point.getStatus().contains("empty")) {
                    shot(point);                                                            //первый выстрел будет в точку [0,0], далее по ситуации
                    point.setStatus(getPointStatus(point));                                 //обновляем статус точки
                }

                if (point.getStatus().contains("hit")) {                                    //если новый статус попадание -
                    logger.info("Hit ship! x:" + point.getX() + " y:" + point.getY());
                    battleField.addPossibleHitPointsLists(point);                           //формируем списки возможных точек
                    point = battleField.getNextPossibleHitPoint(point);                     //берем точку из списков для добивания корабля

                } else if (point.getStatus().contains("miss")) {                            //если новый статус мимо -
                    logger.info("Miss x:" + point.getX() + " y:" + point.getY());
                    point = battleField.getNextEmptyPoint(point);                           //ищем новую точку для выстрела
                }
            }
        }
    }

    private boolean endGameStatus(){
        if(getGameStatus("rival-leave")){
            logger.fatal(rivalLeave);
            return false;
        }
        if(getGameStatus("game-over-win")){
            logger.fatal(gameWin);
            return false;
        }
        if(getGameStatus("game-over-lose")){
            logger.fatal(gameLose);
            return false;
        }
        if(getGameStatus("server-error")){
            logger.fatal(serverError);
            return false;
        }
        if(getGameStatus("game-error")){
            logger.fatal(gameError);
            return false;
        }
        return true;
    }

    private boolean myStep(){
        if(getGameStatus("game-started-move-on") || getGameStatus("move-on")){
            logger.info("Move ON!");
            return true;
        } else return false;
    }

    private boolean rivalStep(){
        if(getGameStatus("game-started-move-off") || getGameStatus("move-off")){
            return true;
        } else return false;
    }

    private String getPointStatus(Point point){
        String status = new Label(By.xpath(String.format(pointStatusLocator, point.getY(), point.getX())), "Point")
                .getLocatorAttribute("class");
        String s = "";

        if(status.contains("battlefield-cell__hit")){
            s = "hit";
        } else if(status.contains("battlefield-cell__empty")){
            s = "empty";
        } else if(status.contains("battlefield-cell__miss")){
            s = "miss";
        }

        return s;
    }

    private void shot(Point point){
        new Label(By.xpath(String.format(pointLocator, point.getY(), point.getX())),
                "Point y:" + point.getY() + ", x:" + point.getX() + " is shot!").click();
    }

    private Boolean getGameStatus(String status) {
        return new Label(By.xpath(String.format(gameStatusLocator, status)), "Label Game Status:" + status).isPresent();
    }
}
