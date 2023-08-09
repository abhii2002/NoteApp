package com.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.models.CategoryButtonModel

class CategoryButtonAdapter(private val categoriesList: List<CategoryButtonModel>): RecyclerView.Adapter<CategoryButtonAdapter.ViewHolder>()
{

    private var mItemClickListener: onRecyclerViewItemClickListener? = null
    var oldposition = -1


    private val listener: OnItemsClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvCategoryName)
        val buttonCategory = itemView.findViewById<LinearLayout>(R.id.buttonCategory)
        val view = itemView


        fun setName(name: String) {
            this.name.text = name
        }


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryButtonAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryButtonAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryButtonAdapter.ViewHolder, position: Int) {
          val categories = categoriesList[position]
          holder.setName(categoriesList[position].getName()!!)

        holder.buttonCategory.setOnClickListener(View.OnClickListener { v ->
             if (mItemClickListener != null){
                 mItemClickListener!!.onItemClickListener(v, holder.adapterPosition, oldposition, categories)
             }
            listener?.onItemClick(categories)


            if (oldposition == position){
                oldposition = position
                notifyDataSetChanged()
                return@OnClickListener
            }
            oldposition = position
            notifyDataSetChanged()
        })

        if (position == oldposition){
            holder.name.setTextColor(holder.view.context.resources.getColor(R.color.theme))
        }else {

            holder.name.setTextColor(holder.view.context.resources.getColor(R.color.black))
        }


    }

    override fun getItemCount(): Int {
          return categoriesList.size
    }

    //Interface to perform events on Main-List item click
    interface OnItemsClickListener {
        fun onItemClick(buttonCategoryModel: CategoryButtonModel)
    }

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }


    interface onRecyclerViewItemClickListener {
        fun onItemClickListener(view: View?, position: Int, oldPosition: Int, categoryModel: CategoryButtonModel)
    }
}