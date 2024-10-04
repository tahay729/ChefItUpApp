package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import project.group3tztechcorp.chefitupapp.Ingredient
import project.group3tztechcorp.chefitupapp.R

class IngredientAdapter (private val ingredientList : ArrayList<Ingredient>, context: Context): ArrayAdapter<Ingredient>(context, R.layout.ingredient_item, ingredientList) {

    class IngredientViewHolder {
        lateinit var name: TextView
        lateinit var check: CheckBox
    }

    override fun getCount(): Int {
        return ingredientList.size
    }

    override fun getItem(position: Int): Ingredient {
        return ingredientList[position] as Ingredient
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: IngredientViewHolder
        val result: View
        if(convertView == null){
            viewHolder = IngredientViewHolder()
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
            viewHolder.name = convertView.findViewById(R.id.ingredientName)
            viewHolder.check = convertView.findViewById(R.id.ingredientCheckbox)
            result = convertView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as IngredientViewHolder
            result = convertView
        }
        val item: Ingredient = getItem(position)
        viewHolder.name.text = item.Name
        viewHolder.check.isChecked = item.selected

        viewHolder.check.setOnClickListener {
            item.selected = !item.selected
            notifyDataSetChanged()
        }
        return result
    }
}