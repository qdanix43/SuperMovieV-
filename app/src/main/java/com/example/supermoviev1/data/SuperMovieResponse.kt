package com.example.supermoviev1.data

import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class SuperMovieResponse (
    @SerializedName("response") val response:String,
    @SerializedName("results-for") val resultsFor:String,
    @SerializedName("results") val results:List<Supermovie>
) {
}

class Supermovie (
    @SerializedName("id") val id:String,
    @SerializedName("name") val name:String,
    @SerializedName("biography") val biography:Biography,
    @SerializedName("image") val image:Image
) { }



class Biography (
    @SerializedName("full-name") val realName:String,
    @SerializedName("place-of-birth") val placeOfBirth:String,
    @SerializedName("alignment") val alignment:String,
    @SerializedName("publisher") val publisher:String
) { }¡¡



class Image (
    @SerializedName("url") val url:String
) { }


class IntegerAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter?, value: Int) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader?): Int {
        if (`in` != null) {
            val value: String = `in`.nextString()
            if (value != "null") {
                return value.toInt()
            }
        }
        return 0
    }

}
