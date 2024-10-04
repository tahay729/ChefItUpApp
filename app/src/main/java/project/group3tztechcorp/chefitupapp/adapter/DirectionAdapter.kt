package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import project.group3tztechcorp.chefitupapp.Direction
import project.group3tztechcorp.chefitupapp.R

class DirectionAdapter(private val directionList : ArrayList<Direction>, context: Context): ArrayAdapter<Direction>(context, R.layout.list_directions, directionList) {

    class DirectionViewHolder{
        lateinit var name: TextView
        lateinit var check: CheckBox
    }

    override fun getCount(): Int {
        return directionList.size
    }

    override fun getItem(position: Int): Direction {
        return directionList[position] as Direction
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: DirectionViewHolder
        val result: View
        if(convertView == null){
            viewHolder = DirectionViewHolder()
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.list_directions, parent, false)
            viewHolder.name = convertView.findViewById(R.id.recipe_direction)
            viewHolder.check = convertView.findViewById(R.id.checkBoxes)
            result = convertView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as DirectionViewHolder
            result = convertView
        }
        val item: Direction = getItem(position)
        viewHolder.name.text = item.Name
        viewHolder.check.isChecked = item.selected

        viewHolder.check.setOnClickListener {
            item.selected = !item.selected
            notifyDataSetChanged()
        }

        return result
    }
}