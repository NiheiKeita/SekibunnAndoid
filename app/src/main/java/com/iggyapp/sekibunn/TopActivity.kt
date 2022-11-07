package com.iggyapp.sekibunn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.android.synthetic.main.activity_top.*

class TopActivity : AppCompatActivity() {
    var mode = "easy"

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "TopActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        //ボタンの処理
        unlimited_button.setOnClickListener {
            if(mode == "easy"){
                startActivity(Intent(this, UnlimitedGameActivity::class.java))
                overridePendingTransition(0, 0)
            }else{
                startActivity(Intent(this, HardUnlimitedGameActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }
        ta_button.setOnClickListener {
            if(mode == "easy"){
                startActivity(Intent(this, TAGameActivity::class.java))
                overridePendingTransition(0, 0)
            }else{
                startActivity(Intent(this, HardTAGameActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }
//        time_limit_button.setOnClickListener {
//            startActivity(Intent(this, TimeLimitGameActivity::class.java))
//            overridePendingTransition(0, 0)
//        }
//        ranking_button.setOnClickListener {
//            startActivity(Intent(this, RankingActivity::class.java))
//            overridePendingTransition(0, 0)
//        }
        easy.setOnClickListener {
            showHard()
            hard.visibility = View.VISIBLE
            easy.visibility = View.GONE
        }
        hard.setOnClickListener {
            showEasy()
            easy.visibility = View.VISIBLE
            hard.visibility = View.GONE
        }
        easy_non_active.setOnClickListener {
            showEasy()
            easy.visibility = View.VISIBLE
            hard.visibility = View.GONE
        }
        hard_non_active.setOnClickListener {
            showHard()
            hard.visibility = View.VISIBLE
            easy.visibility = View.GONE
        }

        val pref = getSharedPreferences("my_settings", Context.MODE_PRIVATE)
        val saveInt = pref.getInt("trofy", 0)
        sum_solved_text.text = "今まで解いた数： $saveInt 問"
        showEasy()


        videoAds()
    }

    fun showEasy(){
        mode = "easy"
        val pref = getSharedPreferences("my_settings", Context.MODE_PRIVATE)
        val taTimeInt = pref.getInt("ta_time", 9999)
        top_ta_text.text = "あなたのTA記録： $taTimeInt 秒"
    }
    fun showHard(){
        mode = "hard"
        val pref = getSharedPreferences("my_settings", Context.MODE_PRIVATE)
        val taHardTimeInt = pref.getInt("ta_time_hard", 9999)
        top_ta_text.text = "あなたのTA記録： $taHardTimeInt 秒"
    }

    fun videoAds(){
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

//        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d(TAG, adError.message)
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d(TAG, "Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })
//        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//            override fun onAdDismissedFullScreenContent() {
//                Log.d(TAG, "Ad was dismissed.")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                Log.d(TAG, "Ad failed to show.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                Log.d(TAG, "Ad showed fullscreen content.")
//                mInterstitialAd = null;
//            }
//        }
    }

}