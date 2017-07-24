package demo.pages;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;

import java.util.Random;

/**
 * Created by Михаил on 12.07.2017.
 */

public class MainPage extends BaseForm {

    private Button btnRndShips = new Button(By.xpath("//span[contains(text(), 'Случайным образом')]"), "Random Ships Button");
    private Button btnRndOpponent = new Button(By.className("battlefield-start-choose_rival-variant-link"), "Random Opponent Button");
    private Button btnFriend = new Button(By.partialLinkText("знакомый"), "Friend Button");
    private Button btnStart = new Button(By.className("battlefield-start-button"), "Start Button");

    public MainPage(){
        super(By.className("logo"),"Main Page");
    }

    public void randomOpponent(){
        btnRndOpponent.click();
    }

    public void friendOpponent(){
        btnFriend.click();
    }

    public void randomShips(){
        Random rnd = new Random();
        for(int i = 0; i < rnd.nextInt(15); i++)
            btnRndShips.click();
    }

    public void startGame(){
        btnStart.click();
    }
}
