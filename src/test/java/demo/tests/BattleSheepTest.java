package demo.tests;
import demo.pages.*;
import webdriver.BaseTest;

public class BattleSheepTest extends BaseTest {

    public void runTest() {
        logStep("Open Main Page & Login");
        MainPage mainPage = new MainPage();
        //mainPage.randomOpponent();
        //mainPage.randomShips();
        //mainPage.startGame();
        mainPage.nextStep();
    }
}
