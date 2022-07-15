package com.example.digitalcurrency.features.marketActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digitalcurrency.apiManager.ApiManager
import com.example.digitalcurrency.apiManager.model.CoinAboutData
import com.example.digitalcurrency.apiManager.model.CoinAboutItem
import com.example.digitalcurrency.apiManager.model.CoinsInfo

import com.example.digitalcurrency.databinding.ActivityMarketBinding
import com.example.digitalcurrency.features.CoinActivity.CoinActivity
import com.google.gson.Gson

class MarketActivity : AppCompatActivity(),MarketAdapter.RecyclerCallBack {
    lateinit var  marketAdapter:MarketAdapter
    private val apiManager=ApiManager()
    lateinit var dataNews:ArrayList<Pair<String,String>>
    lateinit var binding:ActivityMarketBinding
    lateinit var aboutMap :MutableMap<String,CoinAboutItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutWatchList.btnShowMore.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cryptocompare.com/coins/list/all/USD/1"))
            startActivity(intent)
        }

        swipeRefresh()

        //because local Json file
        getAboutDataFromAssets()

    }

    override fun onResume() {
        super.onResume()
        initUi()

    }

    private fun initUi() {
        getNewsFromApi()
        getTopCoinsFromApi()

    }

    private fun getAboutDataFromAssets() {

        val fileInString=applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val dataAboutFromJsonArray=gson.fromJson(fileInString,CoinAboutData::class.java)

        aboutMap= mutableMapOf()
        dataAboutFromJsonArray.forEach {
            aboutMap[it.currencyName!!]=CoinAboutItem(
                it.info?.web,
                it.info?.github,
                it.info?.twt,
                it.info?.desc,
                it.info?.reddit
            )
            /*for (i in 0..dataAboutFromJsonArray.size){
            aboutArray[i] =
                      }

            */
            //val btcInfo=aboutMap["BTC"]
        }
    }

    private fun getTopCoinsFromApi() {
        apiManager.getCoinsList(object :ApiManager.ApiCallback<List<CoinsInfo.Data>>{
            override fun onSuccess(data: List<CoinsInfo.Data>) {
                showDataInRecycler(data)

            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@MarketActivity, errorMessage, Toast.LENGTH_SHORT).show()
                Log.v("ErrorApi",errorMessage)
            }
        })
    }

    private fun swipeRefresh() {
        binding.swipeMarket.setOnRefreshListener {
            initUi()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeMarket.isRefreshing=false
            },1500)

        }
    }

    private fun showDataInRecycler(data: List<CoinsInfo.Data>) {
       marketAdapter=MarketAdapter(ArrayList(data),this)
        binding.layoutWatchList.recyclerMain.adapter=marketAdapter
        binding.layoutWatchList.recyclerMain.layoutManager=LinearLayoutManager(this)
    }
    override fun onCoinItemClicked(data: CoinsInfo.Data) {
        val intent=Intent(this, CoinActivity::class.java)
        intent.putExtra("aboutCoin",aboutMap[data.coinInfo?.name])
        intent.putExtra("dataCoin",data)
        startActivity(intent)
    }

    private fun getNewsFromApi() {
        apiManager.getNews(object :ApiManager.ApiCallback<ArrayList<Pair<kotlin.String,String>>>{
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews=data
                refreshNews()

            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@MarketActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun refreshNews(){

        val randomAccess=(0..49).random()
        binding.layoutNews.txtNews.text= dataNews[randomAccess].first
        binding.layoutNews.txtNews.setOnClickListener {
            refreshNews()
        }
        binding.layoutNews.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)
        }


    }


}