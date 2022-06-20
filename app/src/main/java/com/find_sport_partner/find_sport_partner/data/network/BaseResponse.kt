package com.find_sport_partner.find_sport_partner.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    val items: T?
) {
    @JsonClass(generateAdapter = true)
    class Meta(
        val pagination: Pagination
    ) {
        @JsonClass(generateAdapter = true)
        class Pagination(
            val total: Int,
            val count: Int,
            val each_page: Int,
            val curr_page: Int,
            val total_pages: Int,
            val links: Links?
        ){
            @JsonClass(generateAdapter = true)
            class Links(
                val prev: String?,
                val next: String?
            )
        }
    }

    @JsonClass(generateAdapter = true)
    data class Status(
        val status: String,
        val code: String,
        val message: String
    ) {


    }
}

