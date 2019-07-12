package com.example.task4all.view.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.task4all.R
import com.example.task4all.model.Lista
import com.example.task4all.view.activities.TarefaActivity

class MainListAdapter(val dataset: List<Item>) : RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parentView.context)
        val view = inflater.inflate(R.layout.simple_item, parentView, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        dataset[position].onBind(viewHolder)
    }

    //The right way is to create both ViewHolder and Item in separated classes,
    //but this two are too small to be created separately. Inner classes are enough by now
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.simple_text)
    }

    class Item(private val content: String) {
        fun onBind(viewHolder: ViewHolder) {
            viewHolder.textView.text = content
            viewHolder.itemView.setOnClickListener(onClickListener)
        }

        private val onClickListener = View.OnClickListener {
            val context = it.context
            val activityIntent = Intent(it.context, TarefaActivity::class.java)
            activityIntent.putExtra(Lista.ID, content)
            context.startActivity(activityIntent)
        }
    }
}
