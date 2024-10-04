package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.Achievement
import project.group3tztechcorp.chefitupapp.R

class AchievementsAdapter (private val achievementsList : ArrayList<Achievement>): RecyclerView.Adapter<AchievementsAdapter.AchievementsViewHolder>(){

    lateinit var context: Context

    class AchievementsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        val name = itemView.findViewById<TextView>(R.id.achievementName)
        val description = itemView.findViewById<TextView>(R.id.achievementDescription)
        val image = itemView.findViewById<ImageView>(R.id.achievementImage)
        var completed:Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.achievement_view, parent, false)

        context = parent.context

        return AchievementsAdapter.AchievementsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AchievementsViewHolder, position: Int) {
        val currentItem = achievementsList[position]
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
        holder.completed = currentItem.completed!!

        if (holder.completed){
            holder.image.setBackgroundColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return achievementsList.size
    }
}