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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.title_text)
        image = view.findViewById(R.id.image_preview)
        description = view.findViewById(R.id.description_text)
        location = view.findViewById(R.id.open_maps_button)

        title.text = titleArgument
        description.text = descriptionArgument
        image.setImageURI(imageArgument?.toUri())

        location.setOnClickListener {
            val url = "geo:0,0?q=$latitudeArgument,$longitudeArgument(Label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
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


    companion object {
        @JvmStatic
        fun newInstance(title: String, latitude: Double, longitude: Double) =
            PlaceDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(titleParameter, title)
                    putDouble(latitudeParameter, latitude)
                    putDouble(longitudeParameter, longitude)
                }
            }
    }
}