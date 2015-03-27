package com.lvable.mysensorbox;

/**
 * Created by Jiaqi Ning on 24/3/2015.
 */
public class IconData {

    private int titleStringId;
    private int imgID;
    private int bgColor;
    private int titleBarColor;

    public IconData(int title, int imgID, int bgColor, int titleBarColor) {
        this.titleStringId = title;
        this.imgID = imgID;
        this.bgColor = bgColor;
        this.titleBarColor = titleBarColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public int getTitleBarColor() {
        return titleBarColor;
    }

    public int getTitleStringId() {
        return titleStringId;
    }

    public int getImgID() {
        return imgID;
    }



}
