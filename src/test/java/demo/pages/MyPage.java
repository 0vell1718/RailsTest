package demo.pages;

import demo.Post;
import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Label;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Михаил on 19.07.2017.
 */

public class MyPage extends BaseForm {

    private Label userId = new Label(By.xpath("//li[@id=\"l_pr\"]/a"), "User id");
    private String likeWallPost = "//div[@id='%s']//span[contains(text(), 'Нравится')]";
    private String wallPostAuthor = "//div[@id='%s']//a[@href='/id%s']/parent::h5";
    private String wallCommentAuthor = "//div[@id='%s']//div[@class='reply_author']//a[@data-from-id='%s']";
    private String wallPostText = "//div[@id='%s']//div[@class='wall_post_text']";
    private String wallPostImage = "//div[@id='%s']//div[@class='wall_text']//a";
    private String deletedWallPost = "//div[@id='%s' and @class='dld']";


    public MyPage(){
        super(By.id("page_info_wrap"),"My Page");
    }

    public void setUserID(Post post){
        String s = userId.getAttribute("href");
        post.setUserID(s.substring(s.indexOf("id") + 2));
    }

    public void checkedNewPost(Post post){
        assertPostAuthor(post);
        assertPostText(post);
    }

    public void checkedChangedPost(Post post){
        assertPostText(post);
        assertPostImage(post);
    }

    public void checkedComment(Post post){
        assertEquals(true, new Label(By.xpath(String.format(wallCommentAuthor, post.getPostID(), post.getUserID())),
                "Assert comment author").isPresent());
    }

    public void likePost(Post post){
        new Label(By.xpath(String.format(likeWallPost, post.getPostID())), "Like post").click();
    }

    public void assertDeletedPost(Post post){
        assertEquals(false, new Label(By.xpath(String.format(deletedWallPost, post.getPostID())),
                "Assert deleted post").isPresent(5));
    }

    private void assertPostImage(Post post){
        downloadPostImage(post);

        String s1 = getMD5(post.getImageLoad());
        String s2 = getMD5(post.getImageDownload());
        assertEquals(s1, s2);

        deleteDownloadImage(post);
    }

    private void assertPostText(Post post){
        assertEquals(post.getText(),
                new Label(By.xpath(String.format(wallPostText, post.getPostID())), "Assert post text").getText());
    }

    private void assertPostAuthor(Post post){
        new Label(By.xpath(String.format(wallPostAuthor, post.getPostID(), post.getUserID())), "Assert post author").waitForIsElementPresent();
    }

    private String getMD5(String file) {
        String md5 = null;
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
            File f = new File(file);
            InputStream is = null;
            is = new FileInputStream(f);
            byte[] buffer = new byte[8192];
            int read = 0;
            while( (read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);

            md5 = bigInt.toString(16);
            is.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            logger.fatal(e.getMessage());
        }
        return md5;
    }

    private void downloadPostImage(Post post){
        Random rnd = new Random();
        post.setImageDownload(rnd.nextInt(100) + "_DownloadImage.JPG");
        try {
            String s = new Label(By.xpath(String.format(wallPostImage, post.getPostID())))
                    .getSrcFromCssValue("background-image");

            URL url = new URL(s);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(post.getImageDownload());
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
            logger.info("Image downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteDownloadImage(Post post){
        try{
            File file = new File(post.getImageDownload());
            if(file.delete()){
                logger.info(file.getName() + " is deleted!");
            }else{
                logger.warn("Delete operation is failed.");
            }
        }catch(Exception e){
            logger.warn(e.getMessage());
        }
    }
}