package com.sunladder.view.picsudoku;

/**
 * Created by Sun Yaozong on 2018/5/29.
 */

public class PicPanelBean implements IPicPanelBean {

    private int width;
    private int height;
    private String imgUrl;
    private String thumbnail;

    public PicPanelBean(int width, int height, String imgUrl, String thumbnail) {
        this.width = width;
        this.height = height;
        this.imgUrl = imgUrl;
        this.thumbnail = thumbnail;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isLanscape() {
        return width > height;
    }

    @Override
    public String imgUrl() {
        return this.imgUrl;
    }

    @Override
    public String thumbnail() {
        return this.thumbnail;
    }
}
