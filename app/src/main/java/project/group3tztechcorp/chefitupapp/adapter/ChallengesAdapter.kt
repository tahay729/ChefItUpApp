package project.group3tztechcorp.chefitupapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Recipe
import project.group3tztechcorp.chefitupapp.SingleRecipePage

class ChallengesAdapter (private val recipeList : ArrayList<Recipe>): RecyclerView.Adapter<ChallengesAdapter.ChallengesViewHolder>() {

    lateinit var context: Context
    private var challenge: Int = 0

    class ChallengesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.recipeName_challenges)
        val description = itemView.findViewById<TextView>(R.id.recipeDescription_challenges)
        val level = itemView.findViewById<TextView>(R.id.recipeLevel_challenges)
        val image = itemView.findViewById<ImageView>(R.id.image_view_challenges)
        val totalTime = itemView.findViewById<TextView>(R.id.recipeTotalTime_challenges)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_challenges, parent, false)

        context = parent.context

        return ChallengesAdapter.ChallengesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChallengesViewHolder, position: Int) {
        val currentItem = recipeList[position]
        var total = (currentItem.Prep_Time?.plus(currentItem.Cook_Time!!))
        holder.name.text = currentItem.Name
        holder.description.text = currentItem.Description
        holder.level.text = currentItem.Level
        Picasso.with(context).load(currentItem.Image).into(holder.image)
        holder.totalTime.text = total.toString() + "mins"

        holder.itemView.setOnClickListener {
            challenge = 1
            val intent = Intent(context, SingleRecipePage::class.java)
            intent.putExtra("Name", currentItem.Name.toString())
            intent.putExtra("Check", challenge)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}