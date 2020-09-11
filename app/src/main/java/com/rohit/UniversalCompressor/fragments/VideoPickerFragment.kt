package com.rohit.UniversalCompressor.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rohit.UniversalCompressor.R
import com.rohit.UniversalCompressor.utils.FileUriUtils
import com.rohit.UniversalCompressor.utils.Utils
import kotlinx.android.synthetic.main.fragment_video_picker.*

class VideoPickerFragment : Fragment() {

    private val REQUEST_TAKE_GALLERY_VIDEO = 101
    private val REQUEST_PERMISSIONS_CODE_READ_STORAGE = 102

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        galleryBtn.setOnClickListener {
            checkPermissions()
        }


    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS_CODE_READ_STORAGE
            )
        } else
            openGallery()


    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            REQUEST_TAKE_GALLERY_VIDEO
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE_READ_STORAGE) {
            if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                val selectedImageUri = data!!.data
                if (selectedImageUri != null) {
                    val selectedVideoPath = FileUriUtils.getPath(requireContext(), selectedImageUri)
                    openCompressionFragment(selectedVideoPath)


                } else
                    Utils.showToast(requireContext(), getString(R.string.something_went_wrong))

            }
        }

    }

    private fun openCompressionFragment(selectedVideoPath: String?) {
        val bundle = Bundle()
        bundle.putSerializable(Utils.KEY_VIDEO_PATH, selectedVideoPath)
        findNavController().navigate(R.id.action_videoPickerFragment_to_videoCompressorFragment, bundle)
    }
}