package com.example.clothy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message(
    s: String,
    text: String,
    sender: String,
    idreciver: String,
    matchID: String,
    isSender: Boolean
) {
    @SerializedName("_id")
    @Expose
    var Id: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = text

    @SerializedName("from")
    @Expose
    var from: String? = sender

    @SerializedName("to")
    @Expose
    var to: String? =idreciver

    @SerializedName("matchID")
    @Expose
    var matchID: String? =matchID

    @SerializedName("isSender")
    @Expose
    var isSender: Boolean? =isSender

}