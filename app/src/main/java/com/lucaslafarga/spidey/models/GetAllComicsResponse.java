package com.lucaslafarga.spidey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllComicsResponse {

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
    public GetAllComicsData data;
    @SerializedName("etag")
    @Expose
    public String etag;

}