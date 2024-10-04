package project.group3tztechcorp.chefitupapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerView (private val sampleList: List<ListItem>): RecyclerView.Adapter <MyRecyclerView.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView1: TextView = itemView.findViewById(R.id.text_view_1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = sampleList [position]
        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.text1
    }

    override fun getItemCount() = sampleList.size
}