package com.rickyrobot.wikiofthrones.network

import com.rickyrobot.wikiofthrones.model.Character
import com.rickyrobot.wikiofthrones.model.CharacterDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface GameOfThronesApi {
    @GET
    fun getCharacters(
        @Url url: String?
    ): Call<ArrayList<Character>>

    @GET("api/v2/Characters")
    fun getCharacters(): Call<ArrayList<Character>>

    @GET("api/v2/Characters/{id}")
    fun getCharacter(
        @Path("id") id: Int
    ): Call<CharacterDetail>
}