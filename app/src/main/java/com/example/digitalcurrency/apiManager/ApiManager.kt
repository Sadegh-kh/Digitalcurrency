package com.example.digitalcurrency.apiManager

import com.example.digitalcurrency.apiManager.model.ChartCoin
import com.example.digitalcurrency.apiManager.model.CoinsInfo
import com.example.digitalcurrency.apiManager.model.NewsInfo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager {
    private val apiService: ApiService


    init {
        val okHttpClient = OkHttpClient.Builder().addInterceptor {
            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()
            newRequest.addHeader("Authorization", API_KEY)
            return@addInterceptor it.proceed(newRequest.build())
        }.build()

        val retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        apiService = retrofit.create(ApiService::class.java)

    }

    fun getNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {
        apiService.getTopNews().enqueue(object : Callback<NewsInfo> {
            override fun onResponse(call: Call<NewsInfo>, response: Response<NewsInfo>) {
                val data = response.body()!!
                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()
                data.data.forEach {
                    dataToSend.add(Pair(it.title, it.url))
                }

                apiCallback.onSuccess(dataToSend)


            }

            override fun onFailure(call: Call<NewsInfo>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }
        })
    }

    fun getCoinsList(apiCallback: ApiCallback<List<CoinsInfo.Data>>) {
        apiService.getCoin().enqueue(object : Callback<CoinsInfo> {
            override fun onResponse(
                call: Call<CoinsInfo>,
                response: Response<CoinsInfo>
            ) {
                val data = response.body()!!
                apiCallback.onSuccess(data.data as List<CoinsInfo.Data>)

            }

            override fun onFailure(call: Call<CoinsInfo>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }
        })
    }

    fun getChartData(
        period: String,
        fsym: String,
        apiCallback: ApiCallback<Pair<List<ChartCoin.Data.Data>, ChartCoin.Data.Data>>
    ) {
        var histoPeriod = ""
        var limit = 30
        var aggregate = 1

        when (period) {
            HOUR -> {
                histoPeriod = HISTO_HOUR

                //60 minute=1 hour
                limit = 60

                //update per 12 minute
                aggregate = 12
            }
            HOURS24 -> {
                histoPeriod = HISTO_HOUR

                limit = 24


            }
            WEEK -> {
                histoPeriod = HISTO_DAY
                aggregate = 6
            }
            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30
            }
            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }
            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }
            ALL -> {
                histoPeriod = HISTO_DAY
                limit = 2000
                aggregate = 30
            }

        }

        apiService.getChartData(histoPeriod, fsym, aggregate, limit)
            .enqueue(object : Callback<ChartCoin> {
                override fun onResponse(call: Call<ChartCoin>, response: Response<ChartCoin>) {
                    val dataFull = response.body()!!
                    val data = dataFull.data.data
                    val dataBaseLine = dataFull.data.data.maxByOrNull {
                        it.close
                    }
                    val returnData = Pair(data, dataBaseLine!!)
                    apiCallback.onSuccess(returnData)
                }

                override fun onFailure(call: Call<ChartCoin>, t: Throwable) {
                    apiCallback.onError(t.message!!)
                }
            })
    }

    interface ApiCallback<T> {

        fun onSuccess(data: T)
        fun onError(errorMessage: String)
    }
}