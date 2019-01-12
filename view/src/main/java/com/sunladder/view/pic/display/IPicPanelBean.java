package com.sunladder.view.pic.display;

/**
 * Created by syzhugh on 2018/5/29.
 */

public interface IPicPanelBean {

    int getWidth();

    int getHeight();

    boolean isLanscape();

    String imgUrl();

    String thumbnail();
}
