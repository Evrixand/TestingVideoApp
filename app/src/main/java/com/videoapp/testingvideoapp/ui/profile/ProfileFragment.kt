package com.videoapp.testingvideoapp.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.videoapp.testingvideoapp.FEED_VARIANT_PARAM
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.SYS_PREF
import com.videoapp.testingvideoapp.USER_DISPLAY_NAME_KEY
import com.videoapp.testingvideoapp.USER_PHOTO_URL_KEY
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.databinding.FragmentProfileBinding
import com.videoapp.testingvideoapp.infrasctructure.FeedVariant
import com.videoapp.testingvideoapp.infrasctructure.Variant
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), OnClickFavVideoListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        FavVideoAdapter(this)
    }
    private val vm by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vm.signOutResult.observe(viewLifecycleOwner) { result ->
            
            when(result) {
                is Result.Success -> {
                    findNavController().navigate(R.id.action_navigation_profile_to_navigation_feed)
                    Toast.makeText(requireContext(), getString(R.string.sign_out_success_lable), Toast.LENGTH_SHORT).show()
                }
                is Result.Failure -> {
                    Toast.makeText(requireContext(), "${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        vm.favVideos.observe(viewLifecycleOwner){ videos ->
            setVideosData(videos)
        }

        vm.getFavVideos()

        setProfileData()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            vm.signOut()
        }

        binding.mapVideos.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_map)
        }
    }

    private fun setVideosData(list: List<VideoComplete>) {
        binding.apply {
            if (list.isNotEmpty()){
                noFavVideosLb.isVisible = false
                recyclerView.isVisible = true
                recyclerView.adapter = adapter
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                adapter.setList(list)
            }else {
                noFavVideosLb.isVisible = true
                recyclerView.isVisible = false
            }
        }
    }

    private fun setProfileData() {

        val pref = requireContext().getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)

        val username = pref.getString(USER_DISPLAY_NAME_KEY, "")
        val profileIcon = pref.getString(USER_PHOTO_URL_KEY, "") ?: ""

        binding.username.text = getString(R.string.profile_username, username)

        Picasso.get().load(profileIcon.toUri()).into(binding.iconProfile)
    }

    override fun onClickFavVideo(position: Int) {

        val feedVariant = FeedVariant(variant = Variant.FAVOURITES, position = position)

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
}