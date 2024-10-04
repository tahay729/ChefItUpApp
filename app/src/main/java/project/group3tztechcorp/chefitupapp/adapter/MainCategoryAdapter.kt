package project.group3tztechcorp.chefitupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.group3tztechcorp.chefitupapp.R

class MainCategoryAdapter(private val categoryList : ArrayList<String>, var clickListener: OnCategoryItemClickListener): RecyclerView.Adapter<MainCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_main_category, parent, false)

        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        //val currentItem = categoryList[position]

        //holder.category.text = currentItem

        holder.initialize(categoryList.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val category = itemView.findViewById<TextView>(R.id.categoryName)

        fun initialize (item: String, action:OnCategoryItemClickListener){
            category.text = item

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }

    }

}

interface OnCategoryItemClickListener{
    fun onItemClick(item: String, position: Int)
}