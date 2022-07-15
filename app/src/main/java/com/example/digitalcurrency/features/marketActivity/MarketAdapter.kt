package com.example.digitalcurrency.features.marketActivity


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digitalcurrency.R
import com.example.digitalcurrency.apiManager.BASE_URL_IMAGE
import com.example.digitalcurrency.apiManager.model.CoinsInfo
import com.example.digitalcurrency.databinding.ItemRecyclerMarketBinding

class MarketAdapter(
    private val data: ArrayList<CoinsInfo.Data>,
    private val recyclerCallBack: RecyclerCallBack
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @SuppressLint("SetTextI18n")
        fun bindViews(data: CoinsInfo.Data) {
            binding.txtCoinName.text = data.coinInfo!!.fullName
            binding.txtCoinPrice.text = data.dISPLAY!!.uSD!!.pRICE

            val txtNumberMarketCap = data.rAW!!.uSD!!.mKTCAP !!/ 1000000000
            val indexDot = txtNumberMarketCap.toString().indexOf('.')
            binding.txtMarketCap.text =
                "$" + txtNumberMarketCap.toString().substring(0, indexDot + 3) + " Billion"

            val taghir = data.dISPLAY!!.uSD!!.cHANGEPCT24HOUR!!.toDouble()
            if (taghir > 0) {
                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorGain
                    )
                )
            } else if (taghir < 0) {
                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorLoss
                    )
                )
            }
            binding.txtTaghir.text = data.dISPLAY.uSD!!.cHANGEPCT24HOUR + "%"

            Glide.with(itemView).load(BASE_URL_IMAGE + data.coinInfo.imageUrl).into(binding.imgItem)

            itemView.setOnClickListener {
                recyclerCallBack.onCoinItemClicked(data)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        binding =
            ItemRecyclerMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MarketViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RecyclerCallBack {
        fun onCoinItemClicked(data: CoinsInfo.Data)
    }
}