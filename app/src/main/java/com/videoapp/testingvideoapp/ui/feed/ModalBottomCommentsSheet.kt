package com.videoapp.testingvideoapp.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.videoapp.testingvideoapp.COMMENT_FOR_VIDEO_PARAM
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.databinding.ModalBottomCommentsSheetContentBinding

class ModalBottomCommentsSheet : BottomSheetDialogFragment() {

    private var _binding: ModalBottomCommentsSheetContentBinding? = null
    private val binding get() = _binding!!

    private var comment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comment = it.getString(COMMENT_FOR_VIDEO_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModalBottomCommentsSheetContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        standardBottomSheetBehavior.setState(STATE_EXPANDED)

        binding.commentsForVideoTx.text = getString(R.string.comments_for_video, comment)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "ModalBottomCommentsSheet"

        fun newInstance(value: String) =
            ModalBottomCommentsSheet().apply {
                arguments = Bundle().apply {
                    putString(COMMENT_FOR_VIDEO_PARAM, value)
                }
            }
    }
}