package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.adapter.CompletedRecipeAdapter
import project.group3tztechcorp.chefitupapp.adapter.SubCategoryAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRecipesCompletedBinding

private const val TAG = "MyActivity"

class RecipesCompleted : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    private var completedList: ArrayList<String> = ArrayList<String>()
    lateinit var binding: ActivityRecipesCompletedBinding
    private var username: String = "user"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_recipes_completed)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_completed)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()

        editor = sharedPreferences.edit()

        recipeRecyclerView = binding.recycleViewCompletedRecipes
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recipeRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        Log.i(TAG, "hello" + username)

        getAllRecipies()

        binding.backButton.setOnClickListener {
            editor.putString("transition", (1).toString())
            editor.commit()
            var intent: Intent = Intent(this@RecipesCompleted, UserInterface::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

    }

    fun getAllRecipies() {
        reference = FirebaseDatabase.getInstance().getReference("CompletedRecipes")
        reference2 = FirebaseDatabase.getInstance().getReference("recipes")

        var check: Query = reference.orderByChild("username").equalTo(username)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.i(TAG, username)
                    if (completedList.isEmpty()) {
                        snapshot.child(username).child("completedList").children.forEach {
                                completedList.add(it.getValue().toString())
                        }
                    }
                    reference2.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (recipeSnapshot in snapshot.children) {
                                    for (item in completedList){
                                        if (item.equals(
                                                recipeSnapshot.child("Name").getValue().toString()
                                                    .trim())){
                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                            recipeArrayList.add(recipe!!)
                                            for (recipe in recipeArrayList){
                                                Log.i(TAG, "recipes" + recipe.Name)
                                            }
                                        }
                                    }
                                }
                                recipeRecyclerView.adapter = CompletedRecipeAdapter(recipeArrayList)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}