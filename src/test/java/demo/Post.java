package demo;


import java.io.File;

/**
 * Created by Михаил on 20.07.2017.
 */

public class Post {
    private String postID;
    private String userID;
    private String commentID;
    private String token;

    private String text;

    private String imageLoad;
    private String imageDownload;
    private String imagesFolder;

    public Post(String token, String imagesFolder, String ImageLoad){
        this.token = token;
        File currentDirFile = new File("");
        this.imagesFolder = currentDirFile.getAbsolutePath() + imagesFolder;
        this.imageLoad = this.imagesFolder + ImageLoad;
    }

    public String getPostID(){
        return postID;
    }
    public String getUserID() {
        return userID;
    }
    public String getCommentID() {
        return commentID;
    }
    public String getText() {
        return text;
    }
    public String getImageLoad() {
        return imageLoad;
    }
    public String getImageDownload() {
        return imageDownload;
    }
    public String getImagesFolder() {
        return imagesFolder;
    }
    public String getToken() {
        return token;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setImageLoad(String imageLoad) {
        this.imageLoad = imagesFolder + imageLoad;
    }
    public void setImageDownload(String imageDownload) {
        this.imageDownload = imagesFolder + imageDownload;
    }
    public void setImagesFolder(String imagesFolder) {
        File currentDirFile = new File("");
        this.imagesFolder = currentDirFile.getAbsolutePath() + imagesFolder;
    }

    public String randomText(){
        String symbols = "qwerty1234567890";
        StringBuilder randString = new StringBuilder();
        int count = (int)(Math.random()*20);
        for(int i=0;i<count;i++)
            randString.append(symbols.charAt((int)(Math.random()*symbols.length())));
        text = randString.toString();
        return randString.toString();
    }
}
