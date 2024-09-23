package com.videoapp.testingvideoapp.ui.feed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.videoapp.testingvideoapp.FEED_VARIANT_PARAM
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.data.model.LocationTag
import com.videoapp.testingvideoapp.data.model.Video
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.databinding.FragmentFeedBinding
import com.videoapp.testingvideoapp.infrasctructure.FeedVariant
import com.videoapp.testingvideoapp.infrasctructure.Variant
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment(), OnVideoClickListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var pager: ViewPager2

    private var feedVariant = FeedVariant()

    private var playerViewHolder: VideoPagerAdapter.VideoViewHolder? = null

    private var videoPosition = 0

    private val adapter by lazy {
        VideoPagerAdapter(this)
    }

    private val vm by viewModel<FeedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->

            val feedVar = args.getString(FEED_VARIANT_PARAM)

            val gson = Gson()
            val type = object : TypeToken<FeedVariant>() {}.type

            feedVariant = gson.fromJson(feedVar, type)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {

            pager = viewPager

            pager.adapter = adapter

            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                var previousPosition: Int = -1

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    playerViewHolder =
                        (pager.getChildAt(0) as RecyclerView).findViewHolderForAdapterPosition(
                            position
                        ) as? VideoPagerAdapter.VideoViewHolder

                    playerViewHolder?.playVideo()

                    if (previousPosition != -1 && previousPosition != position) {
                        val previousViewHolder =
                            (pager.getChildAt(0) as RecyclerView).findViewHolderForAdapterPosition(
                                previousPosition
                            ) as? VideoPagerAdapter.VideoViewHolder
                        previousViewHolder?.pauseVideo()
                    }
                    previousPosition = position

                    if (feedVariant.variant == Variant.MAIN_FEED) {

                        playerViewHolder?.getVideoId()?.let { vm.addVideoToWatched(it) }

                        vm.saveVideoPos(position)
                    }
                }
            })
        }

        vm.videos.observe(viewLifecycleOwner) { list ->

            if (list.isNotEmpty()) {
                setVideosToAdapter(list)
            }
        }

        vm.favVideos.observe(viewLifecycleOwner) { list ->

            if (list.isNotEmpty()) {
                setVideosToAdapter(list)
            }
        }

        setupFeed()

        return root
    }

    private fun setupFeed() {

        when(feedVariant.variant) {

            Variant.MAIN_FEED -> {

                binding.mapVideos.isVisible = true
                binding.feedTitle.text = getString(R.string.title_feed)
                videoPosition = vm.getVideoPos()
                vm.getAllVideos()
            }
            Variant.FAVOURITES -> {

                binding.mapVideos.isVisible = false
                binding.feedTitle.text = getString(R.string.title_favorites)
                videoPosition = feedVariant.position
                vm.getFavoritesVideos()
            }
            Variant.WATCHED_VIDEO -> {

                binding.mapVideos.isVisible = false
                binding.feedTitle.text = ""
                vm.getWatchedVideosById(feedVariant.videId)
            }
        }
    }

    private fun setVideosToAdapter(list: List<VideoComplete>) {

        adapter.setVideos(list, vm.isUserInSystem())

        pager.setCurrentItem(videoPosition, false)

        pager.post {
            val recyclerView = pager.getChildAt(0) as RecyclerView
            val playerViewHolder =
                recyclerView.findViewHolderForAdapterPosition(videoPosition) as? VideoPagerAdapter.VideoViewHolder
            playerViewHolder?.playVideo()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapVideos.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_feed_to_navigation_map)
        }

        binding.profileBtn.setOnClickListener {
            if (vm.isUserInSystem()) {
                findNavController().navigate(R.id.action_navigation_feed_to_navigation_profile)
            } else {
                findNavController().navigate(R.id.action_navigation_feed_to_loginFragment)
            }
        }

        binding.profileBtn.setOnLongClickListener {

            vm.resetWatchedVideos()

            Toast.makeText(requireContext(), getString(R.string.reset_watched_message), Toast.LENGTH_SHORT)
                .show()

            return@setOnLongClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        playerViewHolder?.playVideo()
    }

    override fun onPause() {
        super.onPause()
        playerViewHolder?.pauseVideo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        playerViewHolder?.releasePlayer()
    }

    override fun onClickLike(videoId: Int, isFavorite: Boolean) {

        if (vm.isUserInSystem()) {

            if (isFavorite) {
                vm.removeVideFromFavorites(videoId)

                Toast.makeText(
                    requireContext(),
                    getString(R.string.remove_video_from_fav_lable),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                vm.addVideToFavorites(videoId)

                Toast.makeText(
                    requireContext(),
                    getString(R.string.video_saved_to_fav_lable),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.login_for_like_lable),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClickComments(video: Video) {

        val bottomSheet = ModalBottomCommentsSheet.newInstance(video.title)

        bottomSheet.show(requireActivity().supportFragmentManager, ModalBottomCommentsSheet.TAG)
    }

    override fun onViewLocation(locationTag: LocationTag?) {

        if (locationTag != null) {
            val gmmIntentUri =
                Uri.parse("geo:=${locationTag.latitude},${locationTag.longitude}?q=${locationTag.latitude},${locationTag.longitude}")

            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            mapIntent.setPackage("com.google.android.apps.maps")

            startActivity(mapIntent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.video_no_location_lable), Toast.LENGTH_SHORT).show()
        }
    }
}