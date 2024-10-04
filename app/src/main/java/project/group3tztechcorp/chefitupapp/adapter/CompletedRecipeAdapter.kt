package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Recipe

class CompletedRecipeAdapter (private val recipeList : ArrayList<Recipe>): RecyclerView.Adapter<CompletedRecipeAdapter.CompletedRecipeViewHolder>(){
    lateinit var context: Context

    class CompletedRecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.recipeName)
        val description = itemView.findViewById<TextView>(R.id.recipeDescription)
        val level = itemView.findViewById<TextView>(R.id.recipeLevel)
        val image = itemView.findViewById<ImageView>(R.id.image_view)
        val totalTime = itemView.findViewById<TextView>(R.id.recipeTotalTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedRecipeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_recipes_completed, parent, false)

        context = parent.context

        return CompletedRecipeAdapter.CompletedRecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompletedRecipeViewHolder, position: Int) {
        val currentItem = recipeList[position]
        var total = (currentItem.Prep_Time?.plus(currentItem.Cook_Time!!))
        holder.name.text = currentItem.Name
        holder.description.text = currentItem.Description
        holder.level.text = currentItem.Level
        Picasso.with(context).load(currentItem.Image).into(holder.image)
        holder.totalTime.text = total.toString() + "mins"
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}