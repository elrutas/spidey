package com.lucaslafarga.spidey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComicImage {
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("extension")
    @Expose
    public String extension;

}
