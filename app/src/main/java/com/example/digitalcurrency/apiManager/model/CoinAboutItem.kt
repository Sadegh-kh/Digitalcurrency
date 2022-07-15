package com.example.digitalcurrency.apiManager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoinAboutItem (
    var coinWebsite:String?="no-data",
    var coinGithub:String?="no-data",
    var coinTwitter:String?="no-data",
    var coinDesc:String?="no-data",
    var coinReddit:String?="no-data"
        ):Parcelable