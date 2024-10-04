package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.databinding.ActivitySingleRecipePageBinding
import project.group3tztechcorp.chefitupapp.recipeUI.RecipeIngredients
import project.group3tztechcorp.chefitupapp.splash.SplashScreens

class SingleRecipePage : AppCompatActivity() {

    lateinit var name: String
    private var check: Int = 0
    private lateinit var binding: ActivitySingleRecipePageBinding
    private lateinit var reference: DatabaseReference
    private var ingredientList: ArrayList<String> = ArrayList()
    private var directionsList: ArrayList<String> = ArrayList()
    lateinit var menu: MenuItem
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var username: String = "user"
    private var fullName: String = "user"

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_single_recipe_page)

        binding =DataBindingUtil.setContentView(this, R.layout.activity_single_recipe_page)

        var ingredientListAdapter = ArrayAdapter<String>(this, R.layout.list_view, ingredientList)
        var directionListAdapter = ArrayAdapter<String>(this, R.layout.list_view, directionsList)

        var intent = getIntent()
        name = intent.getStringExtra("Name").toString().trim()
        check = intent.getIntExtra("Check", 0)
        if(check == 1){
            this.window.decorView.setBackgroundColor(Color.RED)
        }

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        username = sharedPreferences.getString("username", "").toString()
        fullName = sharedPreferences.getString("fullName", "").toString()


        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(name)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var nameFromDB = snapshot.child(name).child("Name").getValue()
                    var categoryFromDB = snapshot.child(name).child("Category").getValue()
                    var levelFromDB = snapshot.child(name).child("Level").getValue()
                    var imageURLFromDB = snapshot.child(name).child("Image").getValue()
                    var descriptionFromDB = snapshot.child(name).child("Description").getValue()
                    var cookTimeFromDB = snapshot.child(name).child("Cook_Time").getValue()
                    var prepTimeFromDB = snapshot.child(name).child("Prep_Time").getValue()
                    var servingsFromDB = snapshot.child(name).child("Servings").getValue()
                    snapshot.child(name).child("Ingredients").children.forEach {
                        ingredientList.add(it.getValue().toString())
                    }
                    var count = 0
                    snapshot.child(name).child("Directions").children.forEach{
                        count = count + 1
                        directionsList.add("Step " + count.toString() + ": " + it.getValue().toString())
                    }
                    binding.singleRecipeName.text = nameFromDB.toString()
                    binding.singleRecipeCategory.text = categoryFromDB.toString()
                    binding.singleRecipeLevel.text = levelFromDB.toString()
                    binding.singleRecipeDescription.text = descriptionFromDB.toString()
                    binding.singleRecipeCookTime.text = "Cook: " + cookTimeFromDB.toString() + " mins"
                    binding.singleRecipePrepTime.text = "Prep: " + prepTimeFromDB.toString() + " mins"
                    binding.singleRecipeServing.text = servingsFromDB.toString() + " people"
                    Picasso.with(this@SingleRecipePage).load(imageURLFromDB.toString()).into(binding.singleRecipeImage)
                    binding.singleRecipeIngredientList.adapter = ingredientListAdapter
                    binding.singleRecipeDirectionList.adapter = directionListAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.backButton.setOnClickListener {
            if (check == 1){
                editor.putString("transition", (3).toString())
                editor.commit()
            } else {
                editor.putString("transition", (2).toString())
                editor.commit()
            }
            var intent: Intent = Intent(this@SingleRecipePage, UserInterface::class.java)
            intent.putExtra("username", username)
            intent.putExtra("fullName", fullName)
            startActivity(intent)
        }

        binding.startButton.setOnClickListener {
            var intent: Intent = Intent(this@SingleRecipePage, SplashScreens::class.java)
            intent.putExtra("recipe", name)
            intent.putExtra("Check", check)
            intent.putExtra("splash", 4)
            startActivity(intent)
        }

        binding.ingredientsButton.setOnClickListener {
            binding.singleRecipeIngredients.text = "Ingredients"
            binding.singleRecipeDirectionList.visibility = View.INVISIBLE
            binding.singleRecipeIngredientList.visibility = View.VISIBLE
            binding.directionsButton.setTextColor(getColor(R.color.gray))
            binding.ingredientsButton.setTextColor(getColor(R.color.black))
        }

        binding.directionsButton.setOnClickListener {
            binding.singleRecipeIngredients.text = "Directions"
            binding.singleRecipeDirectionList.visibility = View.VISIBLE
            binding.singleRecipeIngredientList.visibility = View.INVISIBLE
            binding.directionsButton.setTextColor(getColor(R.color.black))
            binding.ingredientsButton.setTextColor(getColor(R.color.gray))
        }
    }
}