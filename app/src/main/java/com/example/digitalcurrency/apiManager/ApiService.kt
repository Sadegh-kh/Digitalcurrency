package com.example.digitalcurrency.apiManager

import com.example.digitalcurrency.apiManager.model.CoinsInfo
import com.example.digitalcurrency.apiManager.model.NewsInfo
import com.example.digitalcurrency.apiManager.model.ChartCoin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrder") sortOrder: String = "popular"
    ): Call<NewsInfo>

    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getCoin(
        @Query("tsym") to_symbol: String = "USD",
        @Query("limit") limit_data: Int = 10
    ):Call<CoinsInfo>

    @Headers(API_KEY)
    @GET("v2/{period}")
    fun getChartData(
        @Path("period") period:String,
        @Query("fsym") fsym :String,
        @Query("aggregate") aggregate:Int,
        @Query("limit") limit :Int,
        @Query("tsym")tsym :String = "USD"
    ):Call<ChartCoin>

}