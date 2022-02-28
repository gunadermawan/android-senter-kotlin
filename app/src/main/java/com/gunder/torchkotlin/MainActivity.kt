package com.gunder.torchkotlin

import android.Manifest
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gunder.torchkotlin.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {
    private lateinit var imageButton: ImageButton
    private var state = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageButton = binding.ivTorch
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    runFlashlight()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "izin kamera diperlukan!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }
            }).check()
    }

    private fun runFlashlight() {
        imageButton.setOnClickListener {
            if (!state) {
                val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                try {
                    val camId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(camId, true)
                    state = true
                    imageButton.setImageResource(R.drawable.flash_on)
                } catch (e: CameraAccessException) {
                }
            } else {
                val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                try {
                    val camId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(camId, false)
                    state = false
                    imageButton.setImageResource(R.drawable.flash_off)
                } catch (e: CameraAccessException) {
                }
            }
        }
    }
}