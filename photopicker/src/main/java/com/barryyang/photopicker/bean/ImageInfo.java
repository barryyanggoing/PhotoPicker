package com.barryyang.photopicker.bean;


/**
 * @author：Administrator on 2017/11/28 16:33
 * @description:
 * @version:
 */
public class ImageInfo {

    private String photoPath;
    private String photoName;
    private boolean isSelected = false;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
