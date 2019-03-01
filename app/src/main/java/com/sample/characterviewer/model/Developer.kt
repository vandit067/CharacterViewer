package com.sample.characterviewer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Developer {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null

}
