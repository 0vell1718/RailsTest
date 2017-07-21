package demo.tests;

import demo.Post;
import demo.VKAPIUtils;
import demo.pages.*;
import webdriver.BaseTest;

public class VKTest extends BaseTest {

    public void runTest() {
        logStep("Open Main Page & Login");
        MainPage mainPage = new MainPage();
        mainPage.login(email, password);

        logStep("Navigate MyPage");
        FeedPage feedPage = new FeedPage();
        feedPage.navigateMyPage();

        logStep("Open My Page");
        MyPage myPage = new MyPage();

        logStep("Initialized Post & VKAPIUtils");
        Post post = new Post(token, imagesFolder, loadImage);
        VKAPIUtils vkAPIUtils = new VKAPIUtils();

        logStep("Create new post");
        myPage.setUserID(post);
        vkAPIUtils.addNewPost(post);                //work!
        myPage.checkedNewPost(post);                //not work in chrome!

        logStep("Changed post");
        vkAPIUtils.changedPost(post);               //work!
        myPage.checkedChangedPost(post);            //not work in chrome!

        logStep("Add comment");
        vkAPIUtils.addPostComment(post);            //work!
        myPage.checkedComment(post);                //not work in chrome!

        logStep("Like post");
        myPage.likePost(post);                      //work!
        vkAPIUtils.checkedLikePost(post);           //work!

        logStep("Delete post");
        vkAPIUtils.deletePost(post);                //work!
        myPage.assertDeletedPost(post);             //work!
    }
}
