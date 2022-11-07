package com.iggyapp.sekibunn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_tagame.*
import kotlinx.android.synthetic.main.game_layout.*
import kotlinx.android.synthetic.main.game_layout.answer_1
import kotlinx.android.synthetic.main.game_layout.answer_1_side
import kotlinx.android.synthetic.main.game_layout.batu
import kotlinx.android.synthetic.main.game_layout.maru
import kotlinx.android.synthetic.main.game_layout.number_x2
import kotlinx.android.synthetic.main.game_layout.readyMan
import kotlinx.android.synthetic.main.game_layout.startMan
import kotlinx.android.synthetic.main.game_layout.sum_answer_number
import kotlinx.android.synthetic.main.game_layout.timeer_number
import kotlinx.android.synthetic.main.game_layout.x2_number
import kotlinx.android.synthetic.main.game_layout_hard.*
import kotlinx.android.synthetic.main.game_over_layout.*
import kotlinx.android.synthetic.main.number_input.*

class TAGameActivity : AppCompatActivity() {
    var cnt = 0
    val hnd0= Handler()
    val rnb0 = object : Runnable{
        override fun run() {
            cnt = cnt + 1
            timeer_number.text=cnt.toString()
            hnd0.postDelayed(this,1000)

        }
    }
    var firstSymbol = 0
    var secondSymbol = 0
    var firstNumber = 0
    var secondNumber = 0

    var answer1 = 0
    var answer2 = 0

    //答え
    var answerFirstNumber = 0
    var answerSecondNumber = 0

    var sum_answer_n = 0

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "TAGameActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagame)
        clickEvent()

        readyMan.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            readyMan.visibility = View.GONE
            startMan.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                startMan.visibility = View.GONE
                createQuestion()
                countStart()


            }, 700)
        }, 1500)
        videoAds()
    }
    //答え合わせ
    fun checkTheAnswer(){
        var answerSymbol1 = 1
        var answerSymbol2 = 1
        if(answer_1_side.getText().toString() == "+" || answer_1_side.getText().toString() == ""){
            answerSymbol1 = 1
        }else{
            answerSymbol1 = -1
        }
        if(answer_2_side.getText().toString() == "+"){
            answerSymbol2 = 1
        }else{
            answerSymbol2 = -1
        }
        val answerA = answer_1.getText().toString().toInt() * answerSymbol1
        val answerB = answer_2.getText().toString().toInt() * answerSymbol2

        //答え合わせ
        if(answerA == answerFirstNumber && answerB == answerSecondNumber){
            correctAnswer()
        }else if(answerA == answerSecondNumber && answerB == answerFirstNumber){
            correctAnswer()
        }else{
            batu.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                clear()
                batu.visibility = View.GONE
            }, 500)
        }
    }

    //問題作成
    fun createQuestion(){
        val numnber_a = (1..4).random()
        val numnber_b = (1..9).random()
        var symbol_a = (0..1).random()
        var symbol_b = (0..1).random()

        if(symbol_a == 0){
            symbol_a = 1
        }else{
            symbol_a = -1
        }
        if(symbol_b == 0){
            symbol_b = 1
        }else{
            symbol_b = -1
        }
        answerFirstNumber = numnber_a * symbol_a
        answerSecondNumber = numnber_b * symbol_b

        val questionNumbgerA = numnber_a * symbol_a * 2
        val questionNumbgerB = answerSecondNumber

//        if(answerFirstNumber + answerSecondNumber == 0){
//            number_x2.visibility = View.GONE
//            x2_number.visibility = View.GONE
//            x1_number.visibility = View.GONE
//        }else
        if(questionNumbgerA > 0){
            number_x2.text ="$questionNumbgerA"
        }else{
            number_x2.text = questionNumbgerA.toString()
        }
        if(questionNumbgerB > 0){
            x2_number.text = "+$questionNumbgerB"
        }else{
            x2_number.text = questionNumbgerB.toString()
        }

    }

    //入力制御
    fun setNumber(number:Int){
        if(firstNumber == 0){
            if(firstSymbol == 0){
                firstSymbol = 1
            }
            answer_1.text = number.toString()
            answer1 = number
            firstNumber = 1
        }else if(firstSymbol == 1 && firstNumber == 1 && secondSymbol == 0){
            answer_1.text = ((answer1 * 10) + number).toString()
        }else if(secondSymbol == 1 && secondNumber == 0){
            answer_2.text = number.toString()
            answer2 = number
            secondNumber = 1
        }else if(secondSymbol == 1 && secondNumber == 1){
            answer_2.text = ((answer2 * 10) + number).toString()
        }
    }
    fun setSymbol(symbol:String){
        if(firstSymbol == 0){
            answer_1_side.text = symbol
            firstSymbol = 1
        }else if(firstNumber == 1 && secondSymbol == 0){
            answer_2_side.text = symbol
            secondSymbol = 1
        }
    }
    fun correctAnswer(){
        maru.visibility = View.VISIBLE
        if(sum_answer_n == 9) {
            countEnd()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            // ここに３秒後に実行したい処理
            x2_number.visibility = View.VISIBLE
//            x1.visibility = View.VISIBLE
            sum_answer_n++
            sum_answer_number.text = sum_answer_n.toString()
            clear()
            maru.visibility = View.GONE
            if(sum_answer_n == 10){
                gsme_boad.visibility = View.GONE
                game_over_layout.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    result_time.visibility = View.VISIBLE
                    result_time.text = "$cnt 秒"
                    saveTime()
                }, 1000)
                Handler(Looper.getMainLooper()).postDelayed({
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this)
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }, 300)
                SaveInt()
            }else{
                createQuestion()
            }
        }, 500)

    }

    fun countStart(){
        hnd0.post(rnb0)
    }
    fun countEnd(){
        hnd0.removeCallbacks(rnb0)
    }

    fun saveTime(){
        val pref = getSharedPreferences("my_settings", Context.MODE_PRIVATE)
        val saveInt = pref.getInt("ta_time", 9999)
        var newInt = saveInt

        if(saveInt > cnt){
            newInt = cnt
            new_record.visibility = View.VISIBLE
        }
        getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putInt("ta_time", newInt)
            apply()
        }
    }

    fun clickEvent(){
        zero.setOnClickListener {
            setNumber(0)
        }
        one.setOnClickListener {
            setNumber(1)
        }
        two.setOnClickListener {
            setNumber(2)
        }
        three.setOnClickListener {
            setNumber(3)
        }
        four.setOnClickListener {
            setNumber(4)
        }
        five.setOnClickListener {
            setNumber(5)
        }
        six.setOnClickListener {
            setNumber(6)
        }
        seven.setOnClickListener {
            setNumber(7)
        }
        eight.setOnClickListener {
            setNumber(8)
        }
        nine.setOnClickListener {
            setNumber(9)
        }
        minus.setOnClickListener {
            setSymbol("-")
        }
        plus.setOnClickListener {
            setSymbol("+")
        }
        enter.setOnClickListener {
            if(answer_2_side.getText().toString() != ""&& answer_1.getText().toString() != "" && answer_2.getText().toString() != ""){
                checkTheAnswer()
            }else{
                batu.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    clear()
                    batu.visibility = View.GONE
                }, 500)
            }
        }
        back.setOnClickListener {
            clear()
        }
        home.setOnClickListener {
            SaveInt()
            startActivity(Intent(this, TopActivity::class.java))
            overridePendingTransition(0, 0)
            finishAffinity()
        }
        home_result.setOnClickListener {
            startActivity(Intent(this, TopActivity::class.java))
            overridePendingTransition(0, 0)
            finishAffinity()
        }
    }

    fun clear(){
        firstSymbol = 0
        secondSymbol = 0
        firstNumber = 0
        secondNumber = 0
        answer1 = 0
        answer2 = 0
        answer_1.text = ""
        answer_1_side.text = ""
        answer_2.text = ""
        answer_2_side.text = ""
    }

    override fun onStop() {
        super.onStop()

    }

    fun SaveInt(){
        val pref = getSharedPreferences("my_settings", Context.MODE_PRIVATE)
        val saveInt = pref.getInt("trofy", 0)
        val newInt = saveInt + sum_answer_n
        getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putInt("trofy", newInt)
            apply()
        }
    }

    fun videoAds(){
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        adView_ta.loadAd(adRequest)
        InterstitialAd.load(this,"ca-app-pub-5047266594533407/5737678692", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }
}