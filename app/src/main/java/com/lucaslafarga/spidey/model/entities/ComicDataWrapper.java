package com.lucaslafarga.spidey.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComicDataWrapper {

    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("copyright")
    @Expose
    public String copyright;
    @SerializedName("attributionText")
    @Expose
    public String attributionText;
    @SerializedName("attributionHTML")
    @Expose
    public String attributionHTML;
    @SerializedName("data")
    @Expose
    public ComicDataContainer data;
    @SerializedName("etag")
    @Expose
    public String etag;

}