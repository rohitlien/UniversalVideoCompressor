package com.rohit.UniversalCompressor.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.rohit.UniversalCompressor.R
import com.rohit.UniversalCompressor.utils.Utils
import kotlinx.android.synthetic.main.fragment_video_view.videoView

class VideoViewFragment : Fragment() {

    private var outputVideoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            outputVideoPath = it.getString(Utils.KEY_VIDEO_PATH)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val videoUri = Uri.parse(outputVideoPath)
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener{
            videoView.start()
            it.isLooping = true
        }

        val mediaController = MediaController(requireContext())
        videoView.setMediaController(mediaController)

    }
}