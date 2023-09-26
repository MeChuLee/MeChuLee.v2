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
import com.recommendmenu.mechulee.proto.likedMenuDataStore

class LikeFragment : Fragment() {
    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!

    var myDel = ""

    private lateinit var viewModel: LikeViewModel

    private var likeAdapter: LikeAdapter? =
        LikeAdapter(object : LikeAdapter.LikeListener {
            override fun selectMenu(menu: String) {
                myDel = menu
                binding.trashLayout.visibility = View.VISIBLE
                binding.likeTitle.alpha = 0.5f
                binding.likeView.alpha = 0.5f
                binding.likeRecyclerView.alpha = 0.5f
            }
        })

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikeBinding.inflate(layoutInflater)

        val likeViewModelFactory = LikeViewModelFactory(requireContext().likedMenuDataStore)
        viewModel = ViewModelProvider(this, likeViewModelFactory)[LikeViewModel::class.java]

        val dragListen = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }

                DragEvent.ACTION_DRAG_ENTERED -> true
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> true
                DragEvent.ACTION_DROP -> {
                    when (v) {
                        binding.trashLayout -> {
                            val ad = AlertDialog.Builder(requireContext())
                            ad.setIcon(R.drawable.baseline_priority_high_24)
                            ad.setTitle("경고")
                            ad.setMessage("삭제하시겠습니까?")

                            ad.setPositiveButton("확인") { dialog, _ ->
                                Toast.makeText(requireContext(), "삭제했습니다", Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()
                                viewModel.removeMenu(myDel)
                            }
                            ad.setNegativeButton("취소") { dialog, _ ->
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
                    when (event.result) {
                        true -> Log.d("DRAGANDDROP", "Success")
                        false -> Log.d("DRAGANDDROP", "Failed")
                    }
                    binding.trashLayout.visibility = View.GONE
                    binding.likeTitle.alpha = 1.0f
                    binding.likeView.alpha = 1.0f
                    binding.likeRecyclerView.alpha = 1.0f
                    true
                }

                else ->
                    false
            }
        }

        binding.trashLayout.setOnDragListener(dragListen)

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

    override fun onPause() {
        super.onPause()
        viewModel.storeLikeMenu()
    }

    override fun onResume() {
        super.onResume()
        viewModel.ready()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        likeAdapter = null
    }
}