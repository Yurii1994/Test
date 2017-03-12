
package com.muv.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetModel {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("url_image")
    @Expose
    private String urlImage;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

}
