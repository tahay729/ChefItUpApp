package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.GroceryListPage
import project.group3tztechcorp.chefitupapp.Ingredient
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.UserGroceryList

class GroceryListAdapter (private val groceryList : ArrayList<String>, context: Context): ArrayAdapter<String>(context, R.layout.grocery_list_view, groceryList){
    private lateinit var reference: DatabaseReference
    lateinit var username: String
    lateinit var sharedPreferences: SharedPreferences
    private final val myPreferences: String = "MyPref"

    class GroceryViewHolder(){
        lateinit var number: TextView
        lateinit var name: TextView
        lateinit var remove: ImageView
    }

    override fun getCount(): Int {
        return groceryList.size
    }

    override fun getItem(position: Int): String {
        return groceryList[position] as String
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: GroceryViewHolder
        val result: View
        if(convertView == null){
            viewHolder = GroceryViewHolder()
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.grocery_list_view, parent, false)
            viewHolder.name = convertView.findViewById(R.id.groceryName)
            viewHolder.number = convertView.findViewById(R.id.groceryNumber)
            viewHolder.remove = convertView.findViewById(R.id.groceryRemove)
            result = convertView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as GroceryViewHolder
            result = convertView
        }
        val item: String = getItem(position)
        viewHolder.name.text = item
        viewHolder.number.text = (position + 1).toString() + "."
        viewHolder.remove.setOnClickListener {
            groceryList.removeAt(position)
            notifyDataSetChanged()

            sharedPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
            username = sharedPreferences.getString("username", "").toString()
            reference = FirebaseDatabase.getInstance().getReference("GroceryList")
            var checkGroceryList : Query = reference.orderByChild("username").equalTo(username)

            checkGroceryList.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        reference.child(username).child("groceryList").setValue(groceryList)
                    }
                    else {
                        if (!groceryList.isEmpty()){
                            var groceryList = UserGroceryList(username, groceryList)
                            reference.child(username).setValue(groceryList)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        return result
    }
}