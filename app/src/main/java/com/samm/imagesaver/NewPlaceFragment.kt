package com.samm.imagesaver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.samm.imagesaver.core.Converters
import com.samm.imagesaver.domain.Place
import com.samm.imagesaver.presentation.MyViewModel
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val REQUEST_TAKE_PHOTO = 1

class NewPlaceFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var myViewModel: MyViewModel
    private lateinit var addButton: Button
    private lateinit var cameraButton: Button
    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var cancelButton: Button
    private lateinit var imageUri: Uri
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_place, container, false)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        fusedLocationClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }!!
        getLastLocation(fusedLocationClient)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.title_edit)
        description = view.findViewById(R.id.description_edit)
        imageView = view.findViewById(R.id.image_preview)
        addButton = view.findViewById(R.id.save_button)
        cameraButton = view.findViewById(R.id.open_camera_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        cameraButton.setOnClickListener {
            openCamera()
        }

        addButton.setOnClickListener {
            val title = title.text.toString()
            val description = description.text.toString()
            lifecycleScope.launch {
                createNewPlace(title, description, latitude,
                    longitude)
            }
            navController.navigate(R.id.placeListFragment)
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private suspend fun createNewPlace(
        title: String,
        description: String,
        latitude: Double,
        longitude: Double
    ) {
        val place = context?.let { context ->
            myViewModel.getBitmap(context, imageUri)?.let { image ->
                Place(
                    title = title,
                    description = description,
                    image = image,
                    latitude = latitude,
                    longitude = longitude
                )
            }
        }
        try {
            place?.let { newPlace ->
                myViewModel.insertPlace(newPlace)
            }
        }
        catch (e: Exception){
            Log.d("Error", "$e")
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageUri = Converters.getImageUri(imageBitmap, context)

            Glide.with(this)
                .load(imageUri)
                .override(850, 1000) // resizing
                .centerCrop()
                .into(imageView)
        }
    }

    // Permissions and Location
    override fun onResume() {
        super.onResume()
        getLastLocation(fusedLocationClient)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation(fusedLocationClient)
            } else {
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getLastLocation(fusedLocationClient: FusedLocationProviderClient) {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should show an explanation
            val activity = requireActivity()
            val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
            val rationale = ActivityCompat
                .shouldShowRequestPermissionRationale(activity, fineLocation)

            if (rationale) {
                Toast.makeText(activity,"Denied Permission", Toast.LENGTH_LONG).show()
            } else {
                // Request the permission.
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(fineLocation),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // Permission has been granted
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireActivity(), "Error trying to get last GPS location",
                        Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }
    }
}