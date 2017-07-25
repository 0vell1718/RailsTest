package demo.pages;

import demo.BattleField;
import demo.Point;
import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Label;

import java.util.Objects;

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
        String status = getGameStatus();
        if (!Objects.equals(status, gameStartYou) && !Objects.equals(status, gameStartRival) &&
                !Objects.equals(status, moveOff) && !Objects.equals(status, moveOn)) {
            startGameWait();
        }
    }

    public void playGame(){
        while (endGameStatus()){

            if(myStep()){                                                   //проверяем наш ли ход
                shot(point);                                                //первый выстрел будет в точку [0,0], далее по ситуации

                point.setStatus(getPointStatus(point));                     //обновляем статус точки
                String status = point.getStatus();                          //берем ее новый статус для определения дальнейших действий

                if(status.contains("hit")){                                 //если новый статус попадание -
                    logger.info("Попадание x:" + point.getX() + " y:" + point.getY());
                    battleField.addPossibleHitPointsLists(point);           //формируем списки возможных точек
                    point = battleField.getNextPossibleHitPoint(point);     //берем точку из списков для добивания корабля

                }else if(status.contains("miss")){                          //если новый статус мимо -
                    logger.info("Мимо x:" + point.getX() + " y:" + point.getY());
                    point = battleField.getNextEmptyPoint(point);           //ищем новую точку для выстрела
                }
            }else if(rivalStep()){                                          //если ход соперника - повторяем цикл
                continue;
            }
        }
    }

    private boolean endGameStatus(){
        switch (getGameStatus()) {
            case rivalLeave:
                logger.fatal(rivalLeave);
                return false;
            case gameWin:
                logger.info(gameWin);
                return false;
            case gameLose:
                logger.fatal(gameLose);
                return false;
            case serverError:
                logger.fatal(serverError);
                return false;
            case gameError:
                logger.fatal(gameError);
                return false;
            default:
                return true;
        }
    }

    private boolean myStep(){
        return getGameStatus().contains(gameStartYou) || getGameStatus().contains(moveOn);
    }

    private boolean rivalStep(){
        return getGameStatus().contains(gameStartRival) || getGameStatus().contains(moveOff);
    }

    private String getPointStatus(Point point){
        String status = new Label(By.xpath(String.format(pointStatusLocator, point.getY(), point.getX())), "Point")
                .getAttribute("class");
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
                "Point y:" + point.getY() + ", x:" + point.getX() + " shot!").click();
    }

    //избавиться от sleep!!!
    private String getGameStatus() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Label(By.xpath("//div[@class='notification-message']/parent::div"), "Label Game Status").getNotification();
    }
}
