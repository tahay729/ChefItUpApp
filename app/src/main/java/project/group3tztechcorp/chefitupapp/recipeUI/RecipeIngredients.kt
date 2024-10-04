package project.group3tztechcorp.chefitupapp.recipeUI

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.adapter.IngredientAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipeIngredientsBinding
import project.group3tztechcorp.chefitupapp.databinding.ActivitySingleRecipePageBinding
import project.group3tztechcorp.chefitupapp.splash.SplashScreens

private const val TAG = "MyActivity"

class RecipeIngredients : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private var ingredientArrayList: ArrayList<Ingredient> = ArrayList<Ingredient>()
    private var groceryList: ArrayList<String> = ArrayList<String>()
    private var groceryList2: ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityRecipeIngredientsBinding
    private lateinit var adapter: IngredientAdapter
    private lateinit var context: Context
    lateinit var name: String
    private var check: Int = 0
    private var ingredients: String = ""
    private var checked: Boolean = false
    lateinit var sharedPreferences: SharedPreferences
    private var username: String = "user"

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipe_ingredients)
        var intent = getIntent()
        name = intent.getStringExtra("recipe").toString().trim()
        check = intent.getIntExtra("Check", 0)

        context = this

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()

        if(check == 1){
            this.window.decorView.setBackgroundColor(Color.RED)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_ingredients)


        getAllIngredients()

        binding.backButton.setOnClickListener {
            var intent: Intent = Intent(this@RecipeIngredients, SingleRecipePage::class.java)
            intent.putExtra("username", username)
            intent.putExtra("Name", name)
            intent.putExtra("Check", check)
            startActivity(intent)
        }

        binding.recipeIngredientList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val ingredients: Ingredient = ingredientArrayList!![position] as Ingredient
            ingredients.selected = !ingredients.selected
            adapter.notifyDataSetChanged()
            for(i in 0 until adapter.count) {
                var ingredient: Ingredient = adapter.getItem(i)
                if (!ingredient.selected) {
                    checked = false
                    break
                } else {
                    checked = true
                }
            }
            if(checked){
                binding.nextButton.setBackgroundColor(getColor(R.color.yellow))
            } else {
                binding.nextButton.setBackgroundColor(getColor(R.color.gray))
            }
        }

        binding.nextButton.setOnClickListener {
            for(i in 0 until adapter.count) {
                var ingredient: Ingredient = adapter.getItem(i)
                if (!ingredient.selected) {
                    checked = false
                    break
                } else {
                    checked = true
                }
            }
            if(checked) {
                if (check == 0) {
                    var intent: Intent = Intent(this@RecipeIngredients, SplashScreens::class.java)
                    intent.putExtra("recipe", name)
                    intent.putExtra("Check", check)
                    intent.putExtra("splash", 5)
                    startActivity(intent)
                } else {
                    var intent: Intent = Intent(this@RecipeIngredients, SplashScreens::class.java)
                    intent.putExtra("recipe", name)
                    intent.putExtra("Check", check)
                    intent.putExtra("splash", 6)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this,"You have not checked off all the ingredients", Toast.LENGTH_SHORT).show()
            }
        }

        binding.checkAll.setOnClickListener {
            for(i in 0 until adapter.count){
                var ingredient: Ingredient = ingredientArrayList[i]
                ingredient.selected = true
            }
            adapter.notifyDataSetChanged()
            binding.nextButton.setBackgroundColor(getColor(R.color.yellow))
        }

        binding.checkNone.setOnClickListener {
            for(i in 0 until adapter.count){
                var ingredient: Ingredient = ingredientArrayList[i]
                ingredient.selected = false
            }
            adapter.notifyDataSetChanged()
            binding.nextButton.setBackgroundColor(getColor(R.color.gray))
        }

        binding.sendToList.setOnClickListener {

            groceryList.clear()
            groceryList2.clear()

            reference2 = FirebaseDatabase.getInstance().getReference("GroceryList")
            var checkGroceryList : Query = reference2.orderByChild("username").equalTo(username)

            for(i in 0 until adapter.count){
                var ingredient: Ingredient = ingredientArrayList[i]
                if (!ingredient.selected){
                    checked = false
                    break
                } else {
                    checked = true
                }
            }

            if(!checked) {
                checkGroceryList.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Log.i(TAG, "User exists")
                            snapshot.child(username).child("groceryList").children.forEach {
                                groceryList.add(it.getValue().toString())
                            }
                            for (i in 0 until adapter.count) {
                                var ingredient: Ingredient = ingredientArrayList[i]
                                if (!ingredient.selected) {
                                    groceryList.add(ingredient.Name)
                                    groceryList2.add(ingredient.Name)
                                }
                            }

                            reference2.child(username).child("groceryList").setValue(groceryList)

                            for(i in 0 until groceryList2.size){
                                if(i == groceryList2.size - 1){
                                    ingredients += groceryList2[i] + "."
                                } else if (i == 0) {
                                    ingredients = groceryList2[i] + ", "
                                } else {
                                    ingredients += groceryList2[i] + ", "
                                }
                            }
                            Toast.makeText(context,
                                "The following ingredients have been added: \n" + ingredients,
                                Toast.LENGTH_LONG).show()
                        } else {
                            Log.i(TAG, "User doesnt exist")
                            for (i in 0 until adapter.count) {
                                var ingredient: Ingredient = ingredientArrayList[i]
                                if (!ingredient.selected) {
                                    groceryList.add(ingredient.Name)
                                    groceryList2.add(ingredient.Name)
                                }
                            }
                            var userGroceryList = UserGroceryList(username, groceryList)
                            reference2.child(username).setValue(userGroceryList)

                            for(i in 0 until groceryList2.size){
                                if(i == groceryList2.size){
                                    ingredients += groceryList2[i] + "."
                                } else {
                                    ingredients += groceryList2[i] + ", "
                                }
                            }
                            Toast.makeText(context,
                                "The following ingredients have been added: \n" + ingredients,
                                Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            } else {
                Toast.makeText(this,
                    "All the ingredients have been checked! You must uncheck ingredient to add to list",
                Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getAllIngredients(){
        var recipe = name
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        var checkRecipe : Query = reference.orderByChild("Name").equalTo(recipe)

        checkRecipe.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    snapshot.child(recipe).child("Ingredients").children.forEach {
                        ingredientArrayList!!.add(Ingredient(it.getValue().toString(), false))
                    }
                    adapter = IngredientAdapter(ingredientArrayList!!, applicationContext)
                    binding.recipeIngredientList.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}