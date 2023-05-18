package com.clothy.clothyandroid.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OutfitResponse {
    @SerializedName("outfit")
    @Expose
    var outfit: Outfit? =null
    class Outfit: ViewModel(){
        private var searchPro= listOf<Outfit>()
        var name = ""
        private val result = MutableLiveData<List<Outfit>>()
        @SerializedName("typee")
        @Expose
        var type:String? =null
        @SerializedName("couleur")
        @Expose
        var couleur:String? =null
        @SerializedName("category")
        @Expose
        var category:String? =null
        @SerializedName("photo")
        @Expose
        var photo:String? =null
        @SerializedName("taille")
        @Expose
        var taille:String? =null
        @SerializedName("userID")
        @Expose
        var userID:String? =null
        @SerializedName("locked")
        @Expose
        var locked:Boolean? =null
        @SerializedName("_id")
        @Expose
        var idd:String? =null

        fun search(name: String){
            this.name = name
            updateResult()
        }
        private fun updateResult() {
            var list = searchPro

            //Search
            list = list.filter {
                it.type!!.contains(name, true)
            }

            result.value = list
        }
    }
}