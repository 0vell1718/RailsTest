package demo;

import webdriver.BaseEntity;

/**
 * Created by Михаил on 24.07.2017.
 */
public class Point extends BaseEntity {
    private Integer x;
    private Integer y;
    private String status;

    public Point (Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Point(Integer x, Integer y, String status){
        this(x, y);
        this.status = status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public Integer getX(){
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    protected String formatLogMsg(String message) {
        return null;
    }
}
