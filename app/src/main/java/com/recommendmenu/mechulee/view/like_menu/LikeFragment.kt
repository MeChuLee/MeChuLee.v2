package com.recommendmenu.mechulee.view.like_menu

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.databinding.FragmentLikeBinding

class LikeFragment : Fragment() {
    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!

    val tempList = ArrayList<String>()
    val IMAGEVIEW_TAG = "MY TAG"
    var myDel = ""

    private lateinit var viewModel: LikeViewModel

    private var likeAdapter: LikeAdapter? =
        LikeAdapter(object : LikeAdapter.LikeListener {
            override fun selectMenu(menu: String) {
                myDel = menu
            }
        })


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LikeViewModel::class.java]

//        binding.trashIcon.apply {
//            tag = IMAGEVIEW_TAG
//            setOnLongClickListener { v:View ->
//                val dragData = ClipData(
//                    v.tag as? CharSequence,
//                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
//                    ClipData.Item(v.tag as? CharSequence)
//                )
//                val myShadow = View.DragShadowBuilder(v)
//
//                v.startDragAndDrop(dragData, myShadow, v, 0)
//            }
//        }

        val dragListen = View.OnDragListener { v, event ->
            when(event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> true
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> true
                DragEvent.ACTION_DROP -> {
                    when (v) {
                        binding.topLayout -> {
                            val ad = AlertDialog.Builder(requireContext())
                            ad.setIcon(R.drawable.like_icon)
                            ad.setTitle("경고")
                            ad.setMessage("삭제하시겠습니까?")

                            ad.setPositiveButton("확인") { dialog, _ ->
                                Toast.makeText(requireContext(), "삭제합니다", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                viewModel.removeMenu(myDel)
                            }
                            ad.setNegativeButton("취소") { dialog, _ ->
                                Toast.makeText(requireContext(), "취소에 성공", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            val alertDialog = ad.create()
                            alertDialog.show()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    when(event.result) {
                        true -> Log.d("DRAGANDDROP", "Success")
                        false -> Log.d("DRAGANDDROP", "Failed")
                    }
                    true
                }
                else ->
                    false
            }
        }

        binding.topLayout.setOnDragListener(dragListen)

        viewModel.nowList.observe(requireActivity()) { myLikeList ->
            likeAdapter?.datas?.clear()
            likeAdapter?.datas?.addAll(myLikeList)
            likeAdapter?.notifyDataSetChanged()
        }

        binding.likeRecyclerView.apply {
            setHasFixedSize(true)
            adapter = likeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}