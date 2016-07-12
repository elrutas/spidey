package com.lucaslafarga.spidey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Comic {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("digitalId")
    @Expose
    public String digitalId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("issueNumber")
    @Expose
    public String issueNumber;
    @SerializedName("variantDescription")
    @Expose
    public String variantDescription;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("modified")
    @Expose
    public String modified;
    @SerializedName("isbn")
    @Expose
    public String isbn;
    @SerializedName("upc")
    @Expose
    public String upc;
    @SerializedName("diamondCode")
    @Expose
    public String diamondCode;
    @SerializedName("ean")
    @Expose
    public String ean;
    @SerializedName("issn")
    @Expose
    public String issn;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("pageCount")
    @Expose
    public String pageCount;
    @SerializedName("resourceURI")
    @Expose
    public String resourceURI;
    @SerializedName("thumbnail")
    @Expose
    public Thumbnail thumbnail;
    @SerializedName("images")
    @Expose
    public List<ComicImage> images = new ArrayList<>();
}
