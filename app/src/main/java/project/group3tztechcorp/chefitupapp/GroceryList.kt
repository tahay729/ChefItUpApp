package project.group3tztechcorp.chefitupapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

class GroceryList : AppCompatActivity() {
    private lateinit var listView: ListView
    private val arrayList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<*>
    private var adBtn: Button? = null
    private lateinit var groceryitem : EditText

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)

        groceryitem = findViewById<EditText>(R.id.add_items)
        listView = findViewById<View>(R.id.listView) as ListView
        adapter = ArrayAdapter<Any?>(this, android.R.layout.simple_list_item_1,
            arrayList as List<Any?>
        )
        listView!!.adapter = adapter

        //When you press the Add button, it takes the text that the user entered
        //and adds it to the list
        adBtn = findViewById<View>(R.id.btnAdd) as Button
        adBtn!!.setOnClickListener {
            var value = groceryitem.text.toString()
            //if the textfield is blank, it tells the user to enter text
            if (value == ""){
                Toast.makeText(
                    this@GroceryList,
                    "You didn't enter any items, please enter an item",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //if the user enters text, its added to the list as a grocery item
            else {
                arrayList.add("$value")
                (adapter as ArrayAdapter<Any?>).notifyDataSetChanged()
                groceryitem.setText("")
            }
        }

        // To remove item from list, you just have to click and hold the list item, then an alert box
        // will appear that confirms whether you want to delet or not
        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
                val item = i
                AlertDialog.Builder(this@GroceryList)
                    .setTitle("Are you sure ?")
                    .setMessage("Do you want to delete this item")
                    .setPositiveButton("Yes") { dialogInterface, i ->
                        arrayList.removeAt(item)
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
    }
}