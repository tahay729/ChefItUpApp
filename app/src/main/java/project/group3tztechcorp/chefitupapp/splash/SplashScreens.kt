package project.group3tztechcorp.chefitupapp.splash

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import project.group3tztechcorp.chefitupapp.*
import project.group3tztechcorp.chefitupapp.recipeUI.RecipeDirections
import project.group3tztechcorp.chefitupapp.recipeUI.RecipeIngredients

class SplashScreens : AppCompatActivity() {

    lateinit var splash: LottieAnimationView
    lateinit var splash2: LottieAnimationView
    lateinit var title: TextView
    lateinit var title2: TextView
    lateinit var title3: TextView
    lateinit var backdrop: ImageView
    private var timer: Long = 10000
    private var totalTime: Long = 0
    private var timerRunning: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var countDownTimer2: CountDownTimer
    private lateinit var countDownTimer3: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_splash)

        var intent = getIntent()
        var fullName = intent.getStringExtra("fullName").toString().trim()
        var username = intent.getStringExtra("username").toString().trim()
        var date = intent.getStringExtra("savedDate").toString().trim()
        var splashNumber = intent.getIntExtra("splash", 0)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        title = findViewById(R.id.SplashTitle)
        title2 = findViewById(R.id.SplashTitle2)
        title3 = findViewById(R.id.SplashTitle3)
        splash = findViewById(R.id.SplashScreen)
        splash2 = findViewById(R.id.SplashScreen2)
        backdrop = findViewById(R.id.backgroundOfLottie)

        when (splashNumber) {
            1 -> {
                //Login Splash
                splash.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_nfzjxjbh.json")
                splash.repeatCount = 0

                title.text = "Logging Into Your Kitchen..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(4000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(4000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, UserInterface::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)

                        startActivity(intents)
                    }
                },5000)
            }
            2 -> {
                //Grocery List Splash
                splash.setAnimationFromUrl("https://assets4.lottiefiles.com/packages/lf20_us78tbqc.json")

                title.text = "Pulling List..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(5000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, GroceryListPage::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)

                        startActivity(intents)
                    }
                },6000)
            }
            3 -> {
                //Achievements Splash
                splash.setAnimationFromUrl("https://assets3.lottiefiles.com/packages/lf20_qqonnlpd.json")

                backdrop.setImageResource(R.drawable.whiteback)

                title.text = "Gathering Achievements..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(5000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, Achievements::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)

                        startActivity(intents)
                    }
                },6000)
            }
            4 -> {
                //Ingredients Splash
                splash.setAnimationFromUrl("https://assets4.lottiefiles.com/packages/lf20_p1bmwqtk.json")

                var name = intent.getStringExtra("recipe").toString().trim()
                var check = intent.getIntExtra("Check", 0)

                title.text = "Gathering Ingredients..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(5000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, RecipeIngredients::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)
                        intents.putExtra("recipe", name)
                        intents.putExtra("Check", check)

                        startActivity(intents)
                    }
                },6000)
            }
            5 -> {
                //Start Regular Recipe
                splash.setAnimationFromUrl("https://assets3.lottiefiles.com/packages/lf20_q0JOaL.json")

                var name = intent.getStringExtra("recipe").toString().trim()
                var check = intent.getIntExtra("Check", 0)

                title.text = "Its Cooking Time!"
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(5000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, RecipeDirections::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)
                        intents.putExtra("recipe", name)
                        intents.putExtra("Check", check)

                        startActivity(intents)
                    }
                },6000)
            }
            6 -> {
                //Start Challenge Recipe
                splash.setAnimationFromUrl("https://assets7.lottiefiles.com/datafiles/BNd8ZHSaX1zZ2nm/data.json")

                var name = intent.getStringExtra("recipe").toString().trim()
                var check = intent.getIntExtra("Check", 0)

                title.text = "Let The Challenge Begin!"
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(5000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, RecipeDirections::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)
                        intents.putExtra("recipe", name)
                        intents.putExtra("Check", check)

                        startActivity(intents)
                    }
                },6000)
            }
            7 -> {
                //Completing Recipe
                startTimer()
                splash.setAnimationFromUrl("https://assets8.lottiefiles.com/packages/lf20_fefIZO.json")

                var name = intent.getStringExtra("recipe").toString().trim()
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(15000)

                title.text = "Completing Recipe..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(9000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, UserInterface::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)
                        intents.putExtra("recipe", name)

                        startActivity(intents)
                    }
                },16000)
            }
            8 -> {
                //Registration Splash
                splash.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_nfzjxjbh.json")
                splash.repeatCount = 0

                title.text = "Setting Up Your New Kitchen..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(5000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, UserInterface::class.java)
                        intents.putExtra("fullName", fullName)
                        intents.putExtra("username", username)
                        intents.putExtra("savedDate", date)

                        startActivity(intents)
                    }
                },6000)
            }
            9 -> {
                //Scanner Splash
                timer = 5500
                startTimerOther()
                splash.setAnimationFromUrl("https://assets2.lottiefiles.com/private_files/lf30_4kmk2efh.json")

                splash.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(4000)

                title.text = "Scanning Receipt..."
                title.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(4000)

                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intents = Intent(this@SplashScreens, RewardsPage::class.java)
                        startActivity(intents)
                    }
                },9000)
            }
        }

    }
    fun startStop() {
        if (timerRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    fun stopTimer() {
        countDownTimer.cancel()
        timerRunning = false
    }

    fun startTimer() {
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                startTimer2()
            }
        }.start()

        timerRunning = true
    }

    fun startTimer2() {
        timer = 10000
        title2.text = "Calculating Gains..."
        title2.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(5000)
        countDownTimer2 = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
            }
        }.start()

        timerRunning = true
    }
    fun startTimer3() {
        timer = 10000
        title3.text = "Finishing Up..."
        title3.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(9000)
        countDownTimer3 = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {

            }
        }.start()

        timerRunning = true
    }

    fun startTimerOther() {
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                startTimer2Other()
            }
        }.start()

        timerRunning = true
    }

    fun startTimer2Other() {
        timer = 4000
        splash2.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_LJ6eMa.json")
        title2.text = "YOU GOT A REWARD"
        title2.animate().translationY((-2500).toFloat()).setDuration(1000).setStartDelay(4000)
        splash2.animate().translationY((1500).toFloat()).setDuration(1000).setStartDelay(4000)
        countDownTimer2 = object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
            }
        }.start()

        timerRunning = true
    }

    fun updateTimer() {
        var minutes: Int = (timer / 60000).toInt()
        var seconds: Int = (timer % 60000 / 1000).toInt()

        var timeLeft: String
        timeLeft = "" + minutes
        timeLeft += ":"
        if (seconds < 10) {
            timeLeft += "0"
        }
        timeLeft += seconds
    }
}