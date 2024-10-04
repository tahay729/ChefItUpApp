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
import project.group3tztechcorp.chefitupapp.Reward

class RewardsAdapter(private val rewardList : ArrayList<Reward>): RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder>() {

    lateinit var context: Context

    class RewardsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.rewardName)
        val description = itemView.findViewById<TextView>(R.id.rewardDescription)
        val image = itemView.findViewById<ImageView>(R.id.reward_Image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rewards_item, parent, false)

        context = parent.context

        return RewardsAdapter.RewardsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RewardsViewHolder, position: Int) {
        val currentItem = rewardList[position]
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
        Picasso.with(context).load(currentItem.image).into(holder.image)
    }

    override fun getItemCount(): Int {
        return rewardList.size
    }
}