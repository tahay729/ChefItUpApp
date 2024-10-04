package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import project.group3tztechcorp.chefitupapp.splash.SplashScreens
import java.text.SimpleDateFormat
import java.util.*

class Registration : AppCompatActivity() {

    lateinit var mFullName: TextInputLayout;
    lateinit var mFullNameText: TextInputEditText;
    lateinit var mUsername: TextInputLayout;
    lateinit var mUsernameText: TextInputEditText;
    lateinit var mEmail: TextInputLayout;
    lateinit var mEmailText: TextInputEditText;
    lateinit var mPhone: TextInputLayout;
    lateinit var mPhoneText: TextInputEditText;
    lateinit var mPassword: TextInputLayout;
    lateinit var mPasswordText: TextInputEditText;
    lateinit var mRegisterBtn: Button;
    lateinit var rootNode: FirebaseDatabase;
    lateinit var reference1: DatabaseReference;
    lateinit var reference2: DatabaseReference;
    lateinit var reference3: DatabaseReference;
    lateinit var mLoginBtn: TextView;
    lateinit var progressBar: ProgressBar;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var localDate: Calendar
    lateinit var dateFormat: SimpleDateFormat
    lateinit var date: String

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_registration)

        //Initializations
        mFullName = findViewById(R.id.editTextFullName)
        mFullNameText = findViewById(R.id.editTextFullNameText)
        mUsername = findViewById(R.id.editTextUsername)
        mUsernameText = findViewById(R.id.editTextUsernameText)
        mEmail = findViewById(R.id.editTextEmail)
        mEmailText = findViewById(R.id.editTextEmailText)
        mPhone = findViewById(R.id.editTextPhone)
        mPhoneText = findViewById(R.id.editTextPhoneText)
        mPassword = findViewById(R.id.editTextPassword)
        mPasswordText = findViewById(R.id.editTextPasswordText)
        mRegisterBtn = findViewById<Button>(R.id.button_Register)
        mLoginBtn = findViewById<TextView>(R.id.loginButton)

        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        /*
        mRegisterBtn.setOnClickListener(View.OnClickListener() {
            rootNode = FirebaseDatabase.getInstance()
            reference1 = rootNode.getReference("userRegistration")
            reference2 = rootNode.getReference("userLogin")
            reference3 = rootNode.getReference("userInformation")

            //Get all the values
            var fullName = mFullName.editText.toString().trim()
            var username = mUsername.editText.toString().trim()
            var email = mEmail.editText.toString().trim()
            var phone = mPhone.editText.toString().trim()
            var password = mPassword.editText.toString().trim()

            var registration = UserRegistration(fullName, username, email, phone, password)
            var login = UserLogin(username, email, password)

            reference1.child(username).setValue(registration)
            reference2.child(username).setValue(login)

            progressBar.visibility = View.VISIBLE

        })
        */

        mLoginBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Registration, Login::class.java))
        })

    }

    private fun validateName(): Boolean {
        var name = mFullNameText.text.toString()

        return if (name.isEmpty()) {
            mFullName.error = "Field cannot be empty"
            false
        } else {
            mFullName.error = null
            mFullName.isErrorEnabled = false
            true
        }
    }

    private fun validateUsername(): Boolean {
        var username = mUsernameText.text.toString()
        var noWhiteSpace = "\\A\\w{4,20}\\z"

        return if (username.isEmpty()) {
            mUsername.error = "Field cannot be empty"
            false
        } else if (username.length >= 15) {
            mUsername.error = "Username too long"
            false
        } else if (!username.matches(noWhiteSpace.toRegex())) {
            mUsername.error = "White spaces are not allowed"
            false
        } else {
            mUsername.error = null
            mUsername.isErrorEnabled = false
            true
        }
    }

    private fun validateEmail(): Boolean {
        var email = mEmailText.text.toString()
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return if (email.isEmpty()) {
            mEmail.error = "Field cannot be empty"
            false
        } else if (!email.matches(emailPattern.toRegex())) {
            mEmail.error = "Invalid email address"
            false
        } else {
            mEmail.error = null
            mEmail.isErrorEnabled = false
            true
        }
    }

    private fun validatePhone(): Boolean {
        var phone = mPhoneText.text.toString()

        return if (phone.isEmpty()) {
            mPhone.error = "Field cannot be empty"
            false
        } else {
            mPhone.error = null
            mPhone.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        var password = mPasswordText.text.toString()
        var passwordVal = "^(?!.* )(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,15}$"
        /*"^" +
        //"(?=.*[0-9])" +             //at least 1 digit
        //"(?=.*[a-z])" +             //at least 1 lower case letter
        //"(?=.*[A-Z])" +             //at least 1 upper case letter
        "(?!.* )" +                   //no white space
        "(?=.*[a-zA-Z])" +            //any letter
        "(?=.*[@#$%^&+=])" +          //at least 1 special character
        ".{8,}" +                     //at least 8 characters
        "$"*/

        return if (password.isEmpty()) {
            mPassword.error = "Field cannot be empty"
            false
        } else if (!password.matches(passwordVal.toRegex())) {
            mPassword.error = "Password is too weak"
            false
        } else {
            mPassword.error = null
            mPassword.isErrorEnabled = false
            true
        }
    }

    /**
     * Save data in firebase on button click
     */
    fun registerUser(view: View) {

        progressBar.visibility = View.VISIBLE

        if (!validateName() or !validateUsername() or !validateEmail() or !validatePhone() or !validatePassword()) {
            progressBar.visibility = View.INVISIBLE
            return
        }

        rootNode = FirebaseDatabase.getInstance()
        reference1 = rootNode.getReference("userRegistration")
        reference2 = rootNode.getReference("userLogin")
        reference3 = rootNode.getReference("userInformation")
        //Get all the values
        var fullName = mFullNameText.text.toString()
        var username = mUsernameText.text.toString()
        var email = mEmailText.text.toString()
        var phone = mPhoneText.text.toString()
        var password = mPasswordText.text.toString()

        //store in firebase
        var registration = UserRegistration(fullName, username, email, phone, password)
        var login = UserLogin(username, email, password)
        var information = UserInformation(username, fullName, 1, 0, 0, 0, 0)

        reference1.child(username).setValue(registration)
        reference2.child(username).setValue(login)
        reference3.child(username).setValue(information)


        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("username", username.toString())
        editor.putString("fullName", fullName.toString())

        if (sharedPreferences.getString("date", "").toString() == null) {
            localDate = Calendar.getInstance()
            dateFormat = SimpleDateFormat("MM/dd/yyyy")
            date = dateFormat.format(localDate.time)
            editor.putString("date", date)
        } else {
            date = sharedPreferences.getString("date", "").toString()
        }

        editor.commit()

        //send to next activity
        val intent = Intent(this@Registration, SplashScreens::class.java)
        intent.putExtra("fullName", fullName)
        intent.putExtra("username", username)
        intent.putExtra("email", email)
        intent.putExtra("phone", phone)
        intent.putExtra("password", password)
        intent.putExtra("savedDate", date)
        intent.putExtra("splash", 8)

        startActivity(intent)

        Toast.makeText(this@Registration, "User account has been created", Toast.LENGTH_SHORT)
            .show()
    }
}