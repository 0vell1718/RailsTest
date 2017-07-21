package demo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.Label;

/**
 * Created by Михаил on 12.07.2017.
 */

public class MainPage extends BaseForm {

    private Label lblEmail = new Label(By.id("index_email"), "Email Label");
    private Label lblPassword = new Label(By.id("index_pass"), "Password Label");
    private Button btnLogin = new Button(By.className("index_login_button"), "Login Button");


    public MainPage(){
        super(By.id("index_login"),"Main Page");
    }

    public void login(String email, String password){
        lblEmail.sendKeys(email);
        lblPassword.sendKeys(password);
        btnLogin.click();
    }

}
