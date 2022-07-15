package com.example.digitalcurrency.features.CoinActivity

import com.example.digitalcurrency.apiManager.model.ChartCoin
import com.robinhood.spark.SparkAdapter

class ChartAdapter (private val historicalData:List<ChartCoin.Data.Data>,
private val baseLine:Float?):SparkAdapter() {
    override fun getCount(): Int {
        return historicalData.size
    }

    override fun getItem(index: Int): ChartCoin.Data.Data {
        return historicalData[index]
    }

    override fun getY(index: Int): Float {
        return historicalData[index].close.toFloat()
    }


    //load data on chart
    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLine ?: super.getBaseLine()//load parent getBaseLine => 0
    }
}