package project.group3tztechcorp.chefitupapp.recipeUI

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.adapter.DirectionAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeDirectionsBinding
import project.group3tztechcorp.chefitupapp.splash.SplashScreens

private const val TAG = "MyActivity"

class RecipeDirections : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var binding: ActivityRecipeDirectionsBinding
    lateinit var name: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var user: UserInformation = UserInformation()
    private var username: String = "user"
    private var recipeLevel: String = "Easy"
    private var fullName: String = "user"
    private var completedList: ArrayList<String> = ArrayList<String>()
    private var directionsList: ArrayList<String> = ArrayList<String>()
    private var completed: Boolean = false
    private var check: Int = 0
    private var count: Int = 0
    private var timer: Long = 0
    private var totalTime: Long = 0
    private var timerRunning: Boolean = false
    private lateinit var countDownTimer: CountDownTimer

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipe_directions)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_directions)

        var intent = getIntent()
        name = intent.getStringExtra("recipe").toString().trim()
        check = intent.getIntExtra("Check", 0)
        if (check == 1) {
            this.window.decorView.setBackgroundColor(Color.RED)
        }

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        fullName = sharedPreferences.getString("fullName", "").toString()

        editor = sharedPreferences.edit()

        getAllDirections()

        binding.nextStepButton.setOnClickListener {
            binding.previousStepButton.setBackgroundColor(getColor(R.color.yellow))
            if (count == directionsList.count() - 1) {
                Toast.makeText(this, "You are at the last step", Toast.LENGTH_SHORT).show()
            } else if (count == directionsList.count() - 2) {
                count += 1
                binding.nextStepButton.setBackgroundColor(getColor(R.color.gray))
                binding.completeButton.setBackgroundColor(getColor(R.color.yellow))
                binding.directionStepNumber.text = "Step " + (count + 1).toString() + ": "
                binding.direction.text = directionsList[count]
            } else {
                count += 1
                binding.directionStepNumber.text = "Step " + (count + 1).toString() + ": "
                binding.direction.text = directionsList[count]
            }
        }

        binding.previousStepButton.setOnClickListener {
            binding.nextStepButton.setBackgroundColor(getColor(R.color.yellow))
            binding.completeButton.setBackgroundColor(getColor(R.color.gray))
            if (count == 0) {
                Toast.makeText(this, "You are at the first step", Toast.LENGTH_SHORT).show()
            } else if (count == 1) {
                count -= 1
                binding.previousStepButton.setBackgroundColor(getColor(R.color.gray))
                binding.directionStepNumber.text = "Step " + (count + 1).toString() + ": "
                binding.direction.text = directionsList[count]
            } else {
                count -= 1
                binding.directionStepNumber.text = "Step " + (count + 1).toString() + ": "
                binding.direction.text = directionsList[count]
            }
        }

        binding.completeButton.setOnClickListener {
            if (count == directionsList.count() - 1) {
                if (timer >= totalTime / 2) {
                    Toast.makeText(
                        this,
                        "You finished too fast! The timer must be at least halfway finished for you to complete",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    editor.putString("transition", (1).toString())
                    editor.commit()
                    stopTimer()
                    binding.completeButton.setBackgroundColor(getColor(R.color.yellow))
                    reference = FirebaseDatabase.getInstance().getReference("userInformation")
                    reference2 = FirebaseDatabase.getInstance().getReference("CompletedRecipes")
                    var checkUser: Query = reference.orderByChild("username").equalTo(username)

                    checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                //get data from database
                                var nameFromDB =
                                    snapshot.child(username).child("fullName").getValue()
                                var levelFromDB =
                                    snapshot.child(username).child("level").getValue()
                                var experienceFromDB =
                                    snapshot.child(username).child("experience").getValue()
                                var rewardsNumFromDB =
                                    snapshot.child(username).child("rewards").getValue()
                                var recipiesNumFromDB =
                                    snapshot.child(username).child("recipesCompleted")
                                        .getValue()
                                var achivementsNumFromDB =
                                    snapshot.child(username).child("achievementsCompleted")
                                        .getValue()

                                user = UserInformation(
                                    username,
                                    nameFromDB.toString(),
                                    levelFromDB.toString().toInt(),
                                    experienceFromDB.toString().toInt(),
                                    rewardsNumFromDB.toString().toInt(),
                                    recipiesNumFromDB.toString().toInt(),
                                    achivementsNumFromDB.toString().toInt()
                                )

                                if (check == 0) {
                                    user.increaseExp(recipeLevel)
                                } else if (check == 1) {
                                    user.increaseExpChallenge(recipeLevel)
                                }
                                user.checkLevel()

                                reference.child(username).child("level").setValue(user.level)
                                reference.child(username).child("experience")
                                    .setValue(user.experience)

                                var checks: Query =
                                    reference2.orderByChild("username").equalTo(username)
                                checks.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            var checkRecipe = false
                                            for (recipe in snapshot.child(username)
                                                .child("completedList").children) {
                                                if (recipe.getValue().toString().equals(name)) {
                                                    Log.i(TAG, "not adding recipe")
                                                    checkRecipe = true
                                                    break
                                                } else {
                                                    Log.i(TAG, "adding recipe")
                                                    checkRecipe = false
                                                }
                                            }
                                            if (!checkRecipe) {
                                                user.addCompletedRecipes()
                                            } else {

                                            }
                                            reference.child(username).child("recipesCompleted")
                                                .setValue(user.recipesCompleted)
                                        } else {
                                            user.addCompletedRecipes()
                                            reference.child(username).child("recipesCompleted")
                                                .setValue(user.recipesCompleted)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })

                    var check: Query = reference2.orderByChild("username").equalTo(username)

                    check.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.child(username)
                                    .child("completedList").children.forEach {
                                        completedList.add(it.getValue().toString())
                                    }
                                for (item in completedList) {
                                    if (item.equals(name)) {
                                        completed = true
                                        break
                                    } else {
                                        completed = false
                                    }
                                }
                                if (!completed) {
                                    completedList.add(name)
                                } else {

                                }
                                var completedRecipe =
                                    UserCompletedRecipe(username, completedList)
                                reference2.child(username).child("completedList")
                                    .setValue(completedRecipe.completedList)
                            } else {
                                completedList.add(name)
                                var completedRecipe =
                                    UserCompletedRecipe(username, completedList)
                                reference2.child(username).setValue(completedRecipe)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                    var intent: Intent =
                        Intent(this@RecipeDirections, SplashScreens::class.java)
                    intent.putExtra("recipe", name)
                    intent.putExtra("username", username)
                    intent.putExtra("fullName", fullName)
                    intent.putExtra("splash", 7)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(
                    this,
                    "You must be at the last step to complete",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun getAllDirections() {
        var recipe = name
        count = 0
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe: Query = reference.orderByChild("Name").equalTo(recipe)

        checkRecipe.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    recipeLevel = snapshot.child(recipe).child("Level").getValue().toString()
                    var nameFromDB = snapshot.child(recipe).child("Name").getValue()
                    var categoryFromDB = snapshot.child(recipe).child("Category").getValue()
                    var levelFromDB = snapshot.child(recipe).child("Level").getValue()
                    var imageURLFromDB = snapshot.child(recipe).child("Image").getValue()
                    var cookTimeFromDB = snapshot.child(recipe).child("Cook_Time").getValue()
                    var prepTimeFromDB = snapshot.child(recipe).child("Prep_Time").getValue()
                    var servingsFromDB = snapshot.child(recipe).child("Servings").getValue()
                    snapshot.child(recipe).child("Directions").children.forEach {
                        directionsList.add(it.getValue().toString())
                    }
                    binding.singleRecipeName.text = nameFromDB.toString()
                    binding.singleRecipeCategory.text = categoryFromDB.toString()
                    binding.singleRecipeLevel.text = levelFromDB.toString()
                    binding.singleRecipeCookTime.text =
                        "Cook: " + cookTimeFromDB.toString() + " mins"
                    binding.singleRecipePrepTime.text =
                        "Prep: " + prepTimeFromDB.toString() + " mins"
                    //timer = (cookTimeFromDB.toString().toLong() + prepTimeFromDB.toString()
                    //    .toLong()) * 60000
                    //totalTime = (cookTimeFromDB.toString().toLong() + prepTimeFromDB.toString()
                    //    .toLong()) * 60000
                    timer = 60000
                    totalTime = 60000
                    binding.singleRecipeServing.text = servingsFromDB.toString() + " people"
                    Picasso.with(this@RecipeDirections).load(imageURLFromDB.toString())
                        .into(binding.directionsImage)

                    binding.directionStepNumber.text = "Step " + (count + 1).toString() + ": "
                    binding.direction.text = directionsList[count]

                    updateTimer()
                    startTimer()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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
                if(check == 1){
                    AlertDialog.Builder(this@RecipeDirections)
                        .setTitle("Times Up!")
                        .setMessage("You have run out of time! Better luck next time.")
                        .setPositiveButton("Ok") { dialogInterface, i ->
                            editor.putString("transition", (1).toString())
                            editor.commit()
                            var intent: Intent = Intent(this@RecipeDirections, UserInterface::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                        }
                        .show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You should be done or almost done!",
                        Toast.LENGTH_LONG
                    ).show()
                }
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

        binding.countDownTimer.text = timeLeft
    }

}