package com.sample.characterviewer.api


import com.sample.characterviewer.model.CharactersData

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET(".")
    fun getCharactersData(@Query("q") characterType: String, @Query("format") format: String): Single<CharactersData>
}
