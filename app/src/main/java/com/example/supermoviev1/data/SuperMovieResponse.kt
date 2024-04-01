package com.example.supermoviev1.data

import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.Duration


class SuperMovieResponse(
    @SerializedName("Search") val search: List<Supermovie>,
    @SerializedName("totalResults") val totalResults: String,
    @SerializedName("Response") val response: String,
)

class Supermovie(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Plot") val synopsis: String,
    @SerializedName("Runtime") val log: String,
    @SerializedName("Director") val director: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("Country") val country: String,


    ) {

}


class Image (
    @SerializedName("url") val url:String,
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
//HOLA
