package com.rohit.UniversalCompressor.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.io.File

class VideoCompressorViewModel : ViewModel() {

    var progressState : MutableLiveData<String> = MutableLiveData()
    var compressionSuccess : MutableLiveData<String> = MutableLiveData()

    fun startCompression(videoUriPath: String?, context: Context, bitrate: String) {
        val folderPath = File("${context.cacheDir}/CompressedVideos")
        if (!folderPath.exists()) {
            folderPath.mkdir()
        }
        val timeInMillis = System.currentTimeMillis()
        val outputPath = "${context.cacheDir}/CompressedVideos/video_compress$timeInMillis.mp4"


        val commandArray = arrayOf("-i", videoUriPath, "-b:v", bitrate+"k", outputPath)

        ffmpegCompress(context,commandArray,outputPath)
    }

    private fun ffmpegCompress(context: Context, commandArray: Array<String?>, outputPath: String) {

        progressState.postValue("START")

        FFmpeg.getInstance(context).execute(commandArray, object : ExecuteBinaryResponseHandler() {
            override fun onStart() {
                Log.i("onStart", "onStart")
            }

            override fun onFinish() {
                Log.i("onFinish", "onFinish")
            }

            override fun onSuccess(message: String) {
                compressionSuccess.postValue(outputPath)
            }

            override fun onProgress(message: String) {
            }

            override fun onFailure(message: String) {
                progressState.postValue("FAILURE")
            }
        })
    }

    fun versionFFmpeg(context: Context) {
        FFmpeg.getInstance(context)
            .execute(arrayOf("-version"), object : ExecuteBinaryResponseHandler() {
                override fun onSuccess(message: String) {
                    Log.i("onSuccess",message)
                }

                override fun onProgress(message: String) {
                    Log.i("onProgress",message)
                }
            })
    }
}