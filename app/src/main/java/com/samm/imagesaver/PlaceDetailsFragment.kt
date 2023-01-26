package com.samm.imagesaver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide

// TODO: Rename parameter arguments, choose names that match
private const val titleParameter = "title"
private const val latitudeParameter = "latitude"
private const val longitudeParameter = "longitude"
private const val imageParameter = "image"
private const val descriptionParameter = "description"


class PlaceDetailsFragment : Fragment() {

    private var titleArgument: String? = null
    private var latitudeArgument: Double? = null
    private var longitudeArgument: Double? = null
    private var imageArgument: String? = null
    private var descriptionArgument: String? = null

    private lateinit var title: TextView
    private lateinit var image: ImageView
    private lateinit var description: TextView
    private lateinit var location: Button
    private lateinit var backButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        title = view.findViewById(R.id.title_text)
        image = view.findViewById(R.id.image_preview)
        description = view.findViewById(R.id.description_text)
        location = view.findViewById(R.id.open_maps_button)
        backButton = view.findViewById(R.id.back_button)

        title.text = titleArgument
        description.text = descriptionArgument
//        image.setImageURI(imageArgument?.toUri())
        Glide.with(requireContext())
            .load(imageArgument?.toUri())
            .override(650, 500) // resizing
            .centerCrop()
            .into(image)

        location.setOnClickListener {
            val url = "geo:0,0?q=$latitudeArgument,$longitudeArgument(Label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        backButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        arguments?.let {
            titleArgument = it.getString(titleParameter)
            latitudeArgument = it.getDouble(latitudeParameter)
            longitudeArgument = it.getDouble(longitudeParameter)
            imageArgument = it.getString(imageParameter)
            descriptionArgument = it.getString(descriptionParameter)
        }

        return inflater.inflate(R.layout.fragment_place_details, container, false)
    }
}