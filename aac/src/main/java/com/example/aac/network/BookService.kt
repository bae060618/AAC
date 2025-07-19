package com.example.aac.network

import com.example.aac.model.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("NLCFsase")
    fun getRecommendedBooks(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1,
        @Query("resultType") resultType: String = "xml"
    ): Call<ResponseData>

}

