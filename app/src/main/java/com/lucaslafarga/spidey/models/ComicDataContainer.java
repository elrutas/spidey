package com.lucaslafarga.spidey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ComicDataContainer {

    @SerializedName("offset")
    @Expose
    public int offset;
    @SerializedName("limit")
    @Expose
    public int limit;
    @SerializedName("total")
    @Expose
    public int total;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("results")
    @Expose
    public List<Comic> comicList = new ArrayList<>();

}
