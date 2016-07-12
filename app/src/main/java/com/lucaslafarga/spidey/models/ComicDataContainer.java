package com.lucaslafarga.spidey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ComicDataContainer {

    @SerializedName("offset")
    @Expose
    public String offset;
    @SerializedName("limit")
    @Expose
    public String limit;
    @SerializedName("total")
    @Expose
    public String total;
    @SerializedName("count")
    @Expose
    public String count;
    @SerializedName("results")
    @Expose
    public List<Comic> comicList = new ArrayList<>();

}
