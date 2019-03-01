package com.sample.characterviewer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Icon : Parcelable {
    @SerializedName("Width")
    @Expose
    var width: String? = null
    @SerializedName("URL")
    @Expose
    var url: String? = null
    @SerializedName("Height")
    @Expose
    var height: String? = null
}
