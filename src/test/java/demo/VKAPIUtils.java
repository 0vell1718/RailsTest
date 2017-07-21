package demo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import webdriver.BaseEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Михаил on 20.07.2017.
 */

public class VKAPIUtils extends BaseEntity{

    private URL request;
    private Scanner scan;
    private JSONObject jsonObject;

    public void addNewPost(Post post){
        String url = "https://api.vk.com/method/wall.post?" +
                "owner_id=" + post.getUserID() + "&" +
                "message=" + post.randomText() + "&" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");
        post.setPostID("post" + post.getUserID() + "_" + getJSONAttribute("response", "post_id"));
        browser.waitForPageToLoad();
    }

    public void changedPost(Post post){
        String imageID = uploadWallImage(post);
        String url = "https://api.vk.com/method/wall.edit?" +
                "owner_id=" + Integer.valueOf(post.getUserID()) + "&" +
                "post_id=" + Integer.valueOf(post.getPostID().substring(post.getPostID().indexOf("_") + 1))+ "&" +
                "message=" + post.randomText() + "&" +
                "attachments=" + imageID + "&" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");
        assertEquals("1", getJSONAttribute("response"));
        browser.waitForPageToLoad();
    }

    public void addPostComment(Post post){
        String url = "https://api.vk.com/method/wall.createComment?" +
                "owner_id=" + Integer.valueOf(post.getUserID()) + "&" +
                "post_id=" + Integer.valueOf(post.getPostID().substring(post.getPostID().indexOf("_") + 1))+ "&" +
                "message=" + post.getText() + "&" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");
        String s = "wpt" + post.getUserID() + "_" + getJSONAttribute("response", "cid");
        post.setCommentID(s);
        browser.waitForPageToLoad();
    }

    public void checkedLikePost(Post post){
        String url = "https://api.vk.com/method/likes.getList?" +
                "type=post" + "&" +
                "owner_id=" + Integer.valueOf(post.getUserID()) + "&" +
                "item_id=" + Integer.valueOf(post.getPostID().substring(post.getPostID().indexOf("_") + 1))+ "&" +
                "filter=likes" + "&" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");
        String users = getJSONAttribute("response", "users");

        assertEquals(true, users.contains(post.getUserID()));
        browser.waitForPageToLoad();
    }

    public void deletePost(Post post){
        String url = "https://api.vk.com/method/wall.delete?" +
                "owner_id=" + Integer.valueOf(post.getUserID()) + "&" +
                "post_id=" + Integer.valueOf(post.getPostID().substring(post.getPostID().indexOf("_") + 1))+ "&" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");
        assertEquals("1", getJSONAttribute("response"));
        browser.waitForPageToLoad();
    }

    private void sendUrl(String url, String method){
        try {
            request = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod(method);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void getJSONAnswer(){
        try {
            scan = new Scanner(request.openStream());
            String str = "";
            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();
            jsonObject = new JSONObject(str);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getJSONAttribute(String jsonObject, String get){
        getJSONAnswer();
        return this.jsonObject.getJSONObject(jsonObject).get(get).toString();
    }

    private String getJSONAttribute(String jsonObject){
        getJSONAnswer();
        return this.jsonObject.get(jsonObject).toString();
    }

    private String getWallUploadServer(Post post){
        String url = "https://api.vk.com/method/photos.getWallUploadServer?" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");
        return getJSONAttribute("response", "upload_url");
    }

    private void convertToMultipart(String uploadUrl, String imagepath) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = builder.build();

        HttpPost httpPost = new HttpPost(uploadUrl);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addPart("file", new FileBody(new File(imagepath)));

        final HttpEntity entity = entityBuilder.build();
        httpPost.setEntity(entity);

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            BufferedReader in = new BufferedReader(reader);

            scan = new Scanner(in);
            String str = "";

            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();

            jsonObject = new JSONObject(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String uploadWallImage(Post post){
        String uploadServer = getWallUploadServer(post);
        convertToMultipart(uploadServer, post.getImageLoad());

        String url = "https://api.vk.com/method/photos.saveWallPhoto?" +
                "user_id=" + post.getUserID() + "&" +
                "photo=" + jsonObject.get("photo").toString() + "&" +
                "server=" + jsonObject.getInt("server") + "&" +
                "hash=" + jsonObject.get("hash").toString() + "&" +
                "access_token=" + post.getToken();

        sendUrl(url, "POST");

        String uploadImageID = "";
        try {
            scan = new Scanner(request.openStream());

            String str = "";
            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();

            JsonParser parser = new JsonParser();
            JsonObject mainObject = null;
            mainObject = parser.parse(str).getAsJsonObject();
            JsonArray pItem = mainObject.getAsJsonArray("response");
            for (JsonElement user : pItem) {
                JsonObject userObject = user.getAsJsonObject();
                uploadImageID = userObject.get("id").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadImageID;
    }

    @Override
    protected String formatLogMsg(String message) {
        return null;
    }
}