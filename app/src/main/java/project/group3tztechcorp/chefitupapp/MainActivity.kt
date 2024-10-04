package project.group3tztechcorp.chefitupapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private final var SPLASH_SCREEN : Long = 5000

    //Variables
    lateinit var topAnim : Animation
    lateinit var bottomAnim : Animation
    lateinit var icon : ImageView
    lateinit var title : TextView
    lateinit var slogan : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        //decalrations
        icon = findViewById(R.id.myIcon)
        title = findViewById(R.id.myTitle)
        slogan = findViewById(R.id.mySlogan)

        //Animate image and text
        icon.animation = topAnim
        title.animation = bottomAnim
        slogan.animation = bottomAnim

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                startActivity(Intent(this@MainActivity, Login::class.java))
            }
        },SPLASH_SCREEN)

    }
}