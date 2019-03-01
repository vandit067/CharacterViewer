package com.sample.characterviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class RelatedTopic : Parcelable {
    @SerializedName("Icon")
    @Expose
    var icon: Icon? = null
    @SerializedName("Result")
    @Expose
    var result: String? = null
    @SerializedName("Text")
    @Expose
    var text: String? = null
    @SerializedName("FirstURL")
    @Expose
    var firstURL: String? = null
}
