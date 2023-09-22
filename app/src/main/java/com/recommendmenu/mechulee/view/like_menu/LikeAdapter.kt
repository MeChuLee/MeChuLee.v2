package com.recommendmenu.mechulee.view.like_menu

import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.MenuInfo
import okhttp3.internal.notifyAll

class LikeAdapter(private val likeListener: LikeListener) :
    RecyclerView.Adapter<LikeAdapter.ViewHolder>() {
    var datas = ArrayList<String>()
    val IMAGEVIEW_TAG = "MY TAG"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val likeMenuImage: ImageView = itemView.findViewById(R.id.likeImageView)
        val likeMenuText: TextView = itemView.findViewById(R.id.likeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_like, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.likeMenuImage.setImageResource(R.drawable.example_result)
        holder.likeMenuText.text = datas[position]
        holder.itemView.visibility = View.VISIBLE

        holder.itemView.apply {
            tag = IMAGEVIEW_TAG
            setOnLongClickListener { v: View ->
                // TODO 추후 개선 사항 longclick시 사라지게 하는 기능
//                v.visibility = View.INVISIBLE
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    ClipData.Item(v.tag as? CharSequence)
                )
                val myShadow = View.DragShadowBuilder(v)

                v.startDragAndDrop(dragData, myShadow, v, 0)
                likeListener.selectMenu(datas[position])
                return@setOnLongClickListener(true)
            }
        }
        val dragListen = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> false
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> true
                DragEvent.ACTION_DROP -> true
                DragEvent.ACTION_DRAG_ENDED -> {
                    when (event.result) {
                        true -> Log.d("DRAGANDDROP", "Success")
                        false -> Log.d("DRAGANDDROP", "Failed")
                    }
                    true
                }
                else ->
                    false
            }
        }
        holder.itemView.setOnDragListener(dragListen)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    interface LikeListener {
        fun selectMenu(menu: String)
    }
}