package demo.pages;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.Label;

import java.util.Random;

/**
 * Created by Михаил on 12.07.2017.
 */

public class MainPage extends BaseForm {

    final String init = "Разместите корабли.";
    final String connect = "Подключаемся к серверу.";
    final String waitRival = "Ожидаем противника.";
    final String gameStartYou = "Игра началась, ваш ход.";
    final String gameStartRival = "Игра началась, противник ходит.";
    final String moveOn = "Ваш ход.";
    final String moveOff = "Противник ходит, ждите.";
    final String rivalLeave = "Противник покинул игру. Дальнейшая игра невозможна.";
    final String gameWin = "Игра закончена. Поздравляем, вы победили!";
    final String gameLose = "Игра закончена. Вы проиграли.";
    final String serverError = "Сервер недоступен.";
    final String gameError = "Непредвиденная ошибка. Дальнейшая игра невозможна.";
    final String cws = "Если вам понравилась игра, пожалуйста, оставьте отзыв о ней в интернет-магазине Google Chrome (это займет не более минуты).";

    private Button btnRndShips = new Button(By.xpath("//span[contains(text(), 'Случайным образом')]"), "Random Ships Button");
    private Button btnRndOpponent = new Button(By.className("battlefield-start-choose_rival-variant-link"), "Random Opponent Button");
    private Button btnStart = new Button(By.className("battlefield-start-button"), "Start Button");

    public MainPage(){
        super(By.className("logo"),"Main Page");
    }

    public void nextStep(){
        switch (getStatus()) {
            case init:
                nextStep();
                break;
            case connect:
                nextStep();
                break;
            case waitRival:
                nextStep();
                break;
            case gameStartYou:
                nextShot();
                break;
            case gameStartRival:
                nextStep();
                break;
            case moveOn:
                nextShot();
                break;
            case moveOff:
                nextStep();
                break;
            case rivalLeave:
                logger.warn(rivalLeave);
                break;
            case gameWin:
                logger.info(gameWin);
                break;
            case gameLose:
                logger.fatal(gameLose);
                break;
            case serverError:
                logger.error(serverError);
                break;
            case gameError:
                logger.error(gameError);
                break;
            case cws:
                logger.info(cws);
                break;
            default:
                break;
        }
    }

    private void nextShot(){
        
    }

    public void randomOpponent(){
        btnRndOpponent.click();
    }

    public void randomShips(){
        Random rnd = new Random();
        for(int i = 0; i < rnd.nextInt(15); i++)
            btnRndShips.click();
    }

    public void startGame(){
        btnStart.click();
    }

    private String getStatus() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Label(By.xpath("//div[@class='notification-message']/parent::div"), "Label Game Status").getNotification();
    }
}
