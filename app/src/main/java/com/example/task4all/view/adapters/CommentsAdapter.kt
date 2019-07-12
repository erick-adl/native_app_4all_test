package com.example.task4all.view.adapters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.task4all.R
import com.example.task4all.model.Comentario
import com.example.task4all.helpers.ImageHelper

class CommentsAdapter(private val dataset: List<Item>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parentView.context)
        val view = inflater.inflate(R.layout.comantario_item, parentView, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataset[position].onBind(holder)
    }

    //The right way is to create both ViewHolder and Item in separated classes,
    //but this two are too small to be created separately. Inner classes are enough by now
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userPhotoView: ImageView = itemView.findViewById(R.id.user_photo)
        val usernameView: TextView = itemView.findViewById(R.id.user_name)
        val titleView: TextView = itemView.findViewById(R.id.title)
        val descriptionView: TextView = itemView.findViewById(R.id.description)
        val evaluationView: ImageView = itemView.findViewById(R.id.evaluation)
    }

    class Item(private val comentario: Comentario) {

        //Do this with observer
        fun onBind(holder: ViewHolder) {
            holder.usernameView.text = comentario.name
            holder.titleView.text = comentario.title
            holder.descriptionView.text = comentario.content

            val itemView = holder.itemView
            val context = itemView.context

            comentario.profilePicture.observe(context as LifecycleOwner, Observer { drawable ->
                if (drawable == null) {
                    return@Observer
                }
                val roundedBitmap = ImageHelper.getRoundedDrawable(drawable)
                holder.userPhotoView.setImageBitmap(roundedBitmap)
            })
        }
    }
}