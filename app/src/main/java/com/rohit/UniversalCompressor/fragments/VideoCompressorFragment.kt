package com.rohit.UniversalCompressor.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.rohit.UniversalCompressor.R
import com.rohit.UniversalCompressor.utils.Utils
import com.rohit.UniversalCompressor.viewModels.VideoCompressorViewModel
import kotlinx.android.synthetic.main.video_compressor_fragment.*
import nl.bravobit.ffmpeg.FFmpeg

class VideoCompressorFragment : Fragment() {

    private lateinit var viewModel: VideoCompressorViewModel
    private var videoUriPath : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoUriPath = arguments?.getString(Utils.KEY_VIDEO_PATH)

        if(videoUriPath.isNullOrEmpty()) {
            Utils.showToast(requireContext(),getString(R.string.something_went_wrong))
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.video_compressor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(VideoCompressorViewModel::class.java)

        //init ffmpeg

        if(FFmpeg.getInstance(requireActivity()).isSupported())
            viewModel.versionFFmpeg(requireActivity())


        val videoUri = Uri.parse(videoUriPath)
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener{
            videoView.start()
            it.isLooping = true
        }

        compressBtn.setOnClickListener {
            checkBitrate()
        }

        viewModel.progressState.observe(requireActivity(), Observer {
            if(it=="START"){
                Utils.showToast(requireContext(),getString(R.string.started_compression))
                progressLl.visibility = View.VISIBLE
                bitrateLl.visibility = View.GONE
            }else{
                Utils.showToast(requireContext(),getString(R.string.something_went_wrong))
                progressLl.visibility = View.GONE
                bitrateLl.visibility = View.VISIBLE
            }
        })

        viewModel.compressionSuccess.observe(requireActivity(), Observer {
            val bundle = Bundle()
            bundle.putSerializable(Utils.KEY_VIDEO_PATH, it)
            findNavController().navigate(R.id.action_videoCompressorFragment_to_videoViewFragment, bundle)
        })

    }

    private fun checkBitrate() {
        bitrateEt.error = null
        val bitrate = bitrateEt.text.toString()
        if(bitrate.isEmpty()){
            bitrateEt.error = getString(R.string.enter_bitrate)
        }else{
            viewModel.startCompression(videoUriPath,requireContext(),bitrate)
        }
    }

}