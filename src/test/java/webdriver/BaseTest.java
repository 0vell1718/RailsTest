package webdriver;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


/**
 * An abstract class that describes the basic test application contains
 * methods for logging and field test settings (options)
 */
public abstract class BaseTest extends BaseEntity {
    protected String email;
    protected String password;
    protected String token;
    protected String imagesFolder;
    protected String loadImage;

    /**
     * To override.
     */
    public abstract void runTest();

    @Parameters({"email", "password", "token", "imagesFolder", "loadImage"})
    @BeforeTest
    public void getParameters(String email, String password, String token, String imagesFolder, String loadImage){
        this.email = email;
        this.password = password;
        this.token = token;
        this.imagesFolder = imagesFolder;
        this.loadImage = loadImage;
    }

    /**
     * Test
     *
     * @throws Throwable Throwable
     */
    @Test
    public void xTest(){
        Class<? extends BaseTest> currentClass = this.getClass();

        try {
            logger.logTestName(currentClass.getName());
            browser.navigate(Browser.getBaseUrl());
            runTest();
            logger.logTestEnd(currentClass.getName());
        } catch (Throwable e) {

            logger.warn("");
            logger.warn(getLoc("loc.test.failed"));
            logger.warn("");
            logger.fatal(e.getMessage());
        }

    }

    /**
     * Format logging
     *
     * @param message Message
     * @return Message
     */
    protected String formatLogMsg(final String message) {
        return message;
    }

}
