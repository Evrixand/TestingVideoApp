package com.videoapp.testingvideoapp.ui.maps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.databinding.FragmentMapBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var videos = emptyList<VideoComplete>()

    private lateinit var googleMap: GoogleMap

    private val vm by viewModel<MapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        vm.videos.observe(viewLifecycleOwner) { list ->

            videos = list

            mapFragment?.getMapAsync(this)
        }

        vm.getAllVideos()

        return root
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        if (videos.isNotEmpty()) {

            val latLngs = mutableListOf<LatLng>()

            videos.forEach { item ->

                if (item.video.location != null) {

                    val location = LatLng(item.video.location.latitude, item.video.location.longitude)

                    latLngs.add(location)

                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(item.video.title)

                    )

                    marker?.tag = item.video.videoId
                }
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[0], 5f))

            setupMapClickListener()
        }
    }

    private fun setupMapClickListener() {

        googleMap.setOnMarkerClickListener { marker ->

            val id = marker.tag.toString().toInt()

            val bottomSheet = ModalBottomVideoDetailsSheet.newInstance(id)

            bottomSheet.show(requireActivity().supportFragmentManager, ModalBottomVideoDetailsSheet.TAG)

            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}