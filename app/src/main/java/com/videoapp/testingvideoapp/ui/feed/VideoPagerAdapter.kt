package com.videoapp.testingvideoapp.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.videoapp.testingvideoapp.data.model.Video
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.databinding.FragmentVideoBinding

class VideoPagerAdapter(private val listener: OnVideoClickListener) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private var videos = emptyList<VideoComplete>()
    private lateinit var diffUtilResult: DiffUtil.DiffResult
    private var userInSystem = false

    class VideoViewHolder(private val binding: FragmentVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val player: ExoPlayer = ExoPlayer.Builder(itemView.context).build()
        private val playImg: ImageView = binding.playImg

        private lateinit var video: Video

        fun bind(item: VideoComplete, listener: OnVideoClickListener, userInSystem: Boolean) = with(binding) {

            likeBtn.isChecked = item.isFavourite

            video = item.video

            val mediaItem = MediaItem.fromUri(item.video.filePath)
            player.setMediaItem(mediaItem)
            player.prepare()
            playerView.player = player
            player.repeatMode = Player.REPEAT_MODE_ONE

            username.text = item.user.username

            title.text = item.video.title
            description.text = item.video.description

            locationBtn.setOnClickListener {
                listener.onViewLocation(item.video.location)
            }

            likeBtn.setOnClickListener {

                listener.onClickLike(item.video.videoId, !likeBtn.isChecked)

                if (!userInSystem) {
                    likeBtn.isChecked = false
                }
            }

            commentsBtn.setOnClickListener {
                listener.onClickComments(item.video)
            }

            playerView.setOnClickListener {
                if (player.isPlaying) {
                    player.stop()
                    playImg.isVisible = true
                } else {
                    player.play()
                    playImg.isVisible = false
                }
            }
        }

        fun playVideo() {
            player.playWhenReady = true
            playImg.isVisible = false
        }

        fun pauseVideo() {
            player.playWhenReady = false
            playImg.isVisible = true
        }

        fun releasePlayer() {
            player.release()
            playImg.isVisible = false
        }

        fun getVideoId(): Int {
            return video.videoId
        }

        companion object {
            fun create(parent: ViewGroup): VideoViewHolder {
                return VideoViewHolder(
                    FragmentVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(item = videos[position], listener = listener, userInSystem = userInSystem)
    }

    override fun getItemCount(): Int = videos.size

    fun setVideos(newList: List<VideoComplete>, isUserInSystem: Boolean) {

        userInSystem = isUserInSystem
        diffUtilResult = DiffUtil.calculateDiff(VideoDiffCallback(videos, newList))
        diffUtilResult.dispatchUpdatesTo(this)
        videos = newList
    }
}