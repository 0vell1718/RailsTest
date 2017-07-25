package demo.tests;
import demo.pages.*;
import webdriver.BaseTest;

public class BattleShipTest extends BaseTest {

    public void runTest() {
        logStep("Open Main Page & Start Game");
        MainPage mainPage = new MainPage();
        //mainPage.randomOpponent();
        mainPage.friendOpponent();
        mainPage.randomShips();
        mainPage.startGame();

        logStep("Open Game Page");
        GamePage gamePage = new GamePage();
        gamePage.startGameWait();

        logStep("Play Game!");
        gamePage.playGame();
    }
}
