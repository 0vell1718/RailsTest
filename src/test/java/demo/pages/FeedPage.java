package demo.pages;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Label;

/**
 * Created by Михаил on 19.07.2017.
 */

public class FeedPage extends BaseForm{

    private Label lblMyPage = new Label(By.xpath("//span[contains(text(), 'Моя Страница')]"), "My Page");

    public FeedPage(){
        super(By.id("main_feed"),"Feed Page");
    }

    public void navigateMyPage(){
        lblMyPage.click();
    }

}
