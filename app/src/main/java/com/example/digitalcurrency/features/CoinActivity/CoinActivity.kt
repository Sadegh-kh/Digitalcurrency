package com.example.digitalcurrency.features.CoinActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.digitalcurrency.R
import com.example.digitalcurrency.apiManager.*
import com.example.digitalcurrency.apiManager.model.ChartCoin
import com.example.digitalcurrency.apiManager.model.CoinAboutItem
import com.example.digitalcurrency.apiManager.model.CoinsInfo
import com.example.digitalcurrency.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    private val apiManager = ApiManager()
    lateinit var coinItemAbout: CoinAboutItem
    lateinit var coinInfo: CoinsInfo.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getParcelableExtra<CoinAboutItem>("aboutCoin") == null) {
            coinItemAbout = CoinAboutItem()
        } else {
            coinItemAbout = intent.getParcelableExtra("aboutCoin")!!
        }
        coinInfo = intent.getParcelableExtra("dataCoin")!!
        initUi()


    }

    private fun openWebsite(uri: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun initUi() {
        initChartUi()
        initStatisticsUi()
        initAboutUi()
    }

    private fun initAboutUi() {
        binding.aboutMain.txtAboutWebsite.text = coinItemAbout.coinWebsite
        binding.aboutMain.txtAboutGithub.text = coinItemAbout.coinGithub
        binding.aboutMain.txtAboutTwitter.text = "@" + coinItemAbout.coinTwitter
        binding.aboutMain.txtAboutReddit.text = coinItemAbout.coinReddit

        binding.aboutMain.txtAboutCoinMore.text = coinItemAbout.coinDesc
        clickOnAbout(coinItemAbout)
    }

    private fun clickOnAbout(aboutCoin: CoinAboutItem?) {
        binding.aboutMain.txtAboutWebsite.setOnClickListener {
            openWebsite(aboutCoin!!.coinWebsite)
        }
        binding.aboutMain.txtAboutGithub.setOnClickListener {
            openWebsite(aboutCoin!!.coinGithub)
        }
        binding.aboutMain.txtAboutReddit.setOnClickListener {
            openWebsite(aboutCoin!!.coinReddit)
        }
        binding.aboutMain.txtAboutTwitter.setOnClickListener {
            val baseUrl = "https://twitter.com/"
            openWebsite(baseUrl + aboutCoin!!.coinTwitter)
        }
    }

    private fun initStatisticsUi() {
        initDataToolbar(coinInfo)
        initDataStatistics(coinInfo)

    }

    private fun initDataToolbar(dataFromMarket: CoinsInfo.Data?) {
        binding.layoutToolbar.titleToolbar.title = dataFromMarket!!.coinInfo!!.fullName
        Glide.with(this).load(BASE_URL_IMAGE + dataFromMarket.dISPLAY!!.uSD!!.iMAGEURL)
            .into(binding.layoutToolbar.imgToolbar)

        binding.layoutToolbar.btnBackToMarket.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initDataStatistics(dataFromMarket: CoinsInfo.Data?) {
        binding.statisticsMain.txtStatisticsPriceOpen.text =
            dataFromMarket!!.dISPLAY!!.uSD!!.oPENDAY
        binding.statisticsMain.txtStatisticsPriceTodayhight.text =
            dataFromMarket.dISPLAY!!.uSD!!.hIGHDAY
        binding.statisticsMain.txtStatisticsPriceTodayLow.text =
            dataFromMarket.dISPLAY.uSD!!.lOWDAY
        binding.statisticsMain.txtStatisticsPriceChangeToday.text =
            dataFromMarket.dISPLAY.uSD.cHANGEDAY

        binding.statisticsMain.txtStatisticsVolume.text = dataFromMarket.dISPLAY.uSD.vOLUME24HOUR
        binding.statisticsMain.txtStatisticsTotalVolume.text =
            dataFromMarket.dISPLAY.uSD.tOTALVOLUME24H
        binding.statisticsMain.txtStatisticsMaketCap.text = dataFromMarket.dISPLAY.uSD.mKTCAP
        binding.statisticsMain.txtStatisticsSupply.text = dataFromMarket.dISPLAY.uSD.sUPPLY

    }

    private fun initChartUi() {

        initInfoChart()

        var period = HOUR
        requestForShowChart(period)
        binding.chartMain.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radio_12h -> {
                    period = HOUR
                }
                R.id.radio_1day -> {
                    period = HOURS24
                }
                R.id.radio_1week -> {
                    period = WEEK
                }
                R.id.radio_1month -> {
                    period = MONTH
                }
                R.id.radio_3month -> {
                    period = MONTH3
                }
                R.id.radio_1year -> {
                    period = YEAR
                }
                R.id.radio_all -> {
                    period = ALL
                }
            }

            requestForShowChart(period)
        }

        binding.chartMain.sparkView.setScrubListener {
            if (it == null) {
                binding.chartMain.txtChartPrice.text = coinInfo.dISPLAY?.uSD?.pRICE
            } else {
                binding.chartMain.txtChartPrice.text =
                    "$ " + (it as ChartCoin.Data.Data).close.toString()
            }
        }


    }

    private fun initInfoChart() {
        binding.chartMain.txtChartPrice.text = coinInfo.dISPLAY?.uSD?.pRICE
        binding.chartMain.txtChartChange1.text = " " + coinInfo.dISPLAY?.uSD?.cHANGE24HOUR
        binding.chartMain.txtChartChange2.text = coinInfo.dISPLAY?.uSD?.cHANGEPCT24HOUR + "%"

        val taghir = coinInfo.rAW?.uSD?.cHANGEPCT24HOUR!!
        if (taghir > 0) {
            binding.chartMain.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )
            binding.chartMain.txtChartUpdown.text = "▲"
            binding.chartMain.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.chartMain.sparkView.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorGain
            )

        } else if (taghir < 0) {
            binding.chartMain.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )
            binding.chartMain.txtChartUpdown.text = "▼"
            binding.chartMain.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )
            binding.chartMain.sparkView.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorLoss
            )

        } else {
            binding.chartMain.txtChartUpdown.text = "="
        }


    }

    private fun requestForShowChart(period: String) {
        apiManager.getChartData(
            period,
            coinInfo.coinInfo?.name!!,
            object : ApiManager.ApiCallback<Pair<List<ChartCoin.Data.Data>, ChartCoin.Data.Data>> {
                override fun onSuccess(data: Pair<List<ChartCoin.Data.Data>, ChartCoin.Data.Data>) {
                    val chartAdapter = ChartAdapter(data.first, data.second.open.toFloat())
                    binding.chartMain.sparkView.adapter = chartAdapter

                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(this@CoinActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


}