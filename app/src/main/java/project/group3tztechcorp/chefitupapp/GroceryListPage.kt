package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.adapter.GroceryListAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityGroceryListPageBinding

class GroceryListPage : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var groceryArrayList: ArrayList<String>
    private lateinit var binding: ActivityGroceryListPageBinding
    private lateinit var adapter: GroceryListAdapter
    lateinit var username: String
    private var input: String = "apple"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_grocery_list_page)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_list_page)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()

        editor = sharedPreferences.edit()

        groceryArrayList = arrayListOf<String>()

        getAllGroceries()

        binding.groceryListView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                removeItem(position)
                false
            }

        adapter = GroceryListAdapter(groceryArrayList, applicationContext)

        binding.groceryListView.adapter = adapter

        binding.buttonAdd.setOnClickListener {
            input = binding.groceryListItem.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show()
            } else {
                addItem(input)
                binding.groceryListItem.setText("")
            }
        }

        binding.backButton.setOnClickListener {
            editor.putString("transition", (1).toString())
            editor.commit()
            var intent: Intent = Intent(this@GroceryListPage, UserInterface::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    fun addItem(item: String) {
        groceryArrayList.add(item)
        adapter.notifyDataSetChanged()

        reference = FirebaseDatabase.getInstance().getReference("GroceryList")
        var checkGroceryList: Query = reference.orderByChild("username").equalTo(username)

        checkGroceryList.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    reference.child(username).child("groceryList").setValue(groceryArrayList)
                } else {
                    var groceryList = UserGroceryList(username, groceryArrayList)
                    reference.child(username).setValue(groceryList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun removeItem(position: Int) {
        groceryArrayList.removeAt(position)
        adapter.notifyDataSetChanged()

        reference = FirebaseDatabase.getInstance().getReference("GroceryList")
        var checkGroceryList: Query = reference.orderByChild("username").equalTo(username)

        checkGroceryList.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    reference.child(username).child("groceryList").setValue(groceryArrayList)
                } else {
                    if (!groceryArrayList.isEmpty()) {
                        var groceryList = UserGroceryList(username, groceryArrayList)
                        reference.child(username).setValue(groceryList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getAllGroceries() {
        reference = FirebaseDatabase.getInstance().getReference("GroceryList")
        var checkGroceryList: Query = reference.orderByChild("username").equalTo(username)

        checkGroceryList.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.child(username).child("groceryList").children.forEach {
                        groceryArrayList!!.add(it.getValue().toString())
                    }
                    adapter = GroceryListAdapter(groceryArrayList!!, applicationContext)
                    binding.groceryListView.adapter = adapter
                } else {
                    if (!groceryArrayList.isEmpty()) {
                        var groceryList = UserGroceryList(username, groceryArrayList)
                        reference.child(username).setValue(groceryList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}