package com.videoapp.testingvideoapp.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.videoapp.testingvideoapp.FEED_VARIANT_PARAM
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.VIDEO_ID_FOR_DETAILS_PARAM
import com.videoapp.testingvideoapp.databinding.ModalMapVideoDetailSheetContentBinding
import com.videoapp.testingvideoapp.infrasctructure.FeedVariant
import com.videoapp.testingvideoapp.infrasctructure.Variant
import org.koin.androidx.viewmodel.ext.android.viewModel

class ModalBottomVideoDetailsSheet : BottomSheetDialogFragment() {

    private var _binding: ModalMapVideoDetailSheetContentBinding? = null
    private val binding get() = _binding!!

    private var videoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoId = it.getInt(VIDEO_ID_FOR_DETAILS_PARAM)
        }
    }

    private val vm by viewModel<MapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModalMapVideoDetailSheetContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        standardBottomSheetBehavior.setState(STATE_EXPANDED)

        setVideoData(videoId)

        return root
    }

    private fun setVideoData(videoId: Int?) {

        val item = vm.getWatchedVideoById(videoId)

        binding.apply {

            if (item != null) {

                noDetailsLayout.isVisible = false
                detailsLayout.isVisible = true

                username.text = getString(R.string.profile_username, item.user.username)

                videoDescription.text = getString(R.string.video_description, item.video.description)

                val location = if (item.video.location != null) {
                    "${item.video.location.latitude}, ${item.video.location.longitude}"
                } else { getString(R.string.undefined)}

                videoLocation.text = getString(R.string.video_location, location)

                favVideo.videoThumbnail.setImageBitmap(item.thumbnail)

            } else {
                noDetailsLayout.isVisible = true
                detailsLayout.isVisible = false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favVideo.root.setOnClickListener {
            dismiss()
            showVideFragment()
        }
    }

    private fun showVideFragment() {

        if (videoId == null) return

        val feedVariant = FeedVariant(variant = Variant.WATCHED_VIDEO, videId = videoId!!)

        val gson = Gson()
        val jsonString = gson.toJson(feedVariant)

        val bundle = Bundle().apply {
            putString(FEED_VARIANT_PARAM, jsonString)
        }
        findNavController().navigate(R.id.navigation_feed, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "ModalBottomVideoDetailsSheet"

        fun newInstance(videoId: Int) =
            ModalBottomVideoDetailsSheet().apply {
                arguments = Bundle().apply {
                    putInt(VIDEO_ID_FOR_DETAILS_PARAM, videoId)
                }
            }
    }
}