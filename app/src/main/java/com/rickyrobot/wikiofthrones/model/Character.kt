package com.rickyrobot.wikiofthrones.model

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)
