package com.recommendmenu.mechulee.view.recommend_menu.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R

class TutorialViewPagerAdapter(
    private val finishListener: FinishListener
) : RecyclerView.Adapter<TutorialViewPagerAdapter.ViewHolder>() {

    val tutorialImageList = ArrayList<Int>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tutorialImageView: AppCompatImageView = itemView.findViewById(R.id.tutorialImageView)
        val finishTextView: AppCompatTextView = itemView.findViewById(R.id.finishTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_pager_tutorial, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tutorialImageView.setBackgroundResource(tutorialImageList[position])

        if (position == tutorialImageList.size - 1) {
            holder.finishTextView.visibility = View.VISIBLE
            holder.finishTextView.setOnClickListener {
                finishListener.onFinishButtonClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return tutorialImageList.size
    }

    interface FinishListener {
        fun onFinishButtonClick()
    }
}