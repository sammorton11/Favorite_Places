package com.samm.imagesaver

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samm.imagesaver.domain.Place
import com.samm.imagesaver.presentation.MyAdapter
import com.samm.imagesaver.presentation.MyViewModel
import com.samm.imagesaver.util.Converters

// TODO - pass data to a new fragment for details screen - open maps from there instead of here

class PlaceListFragment : Fragment(), MyAdapter.OnCardClick {

    private lateinit var adapter: MyAdapter
    private lateinit var myViewModel: MyViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var openAddPlaceButton: Button
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place_list, container, false)

        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        recyclerView = view.findViewById(R.id.recycler_view)!!
        openAddPlaceButton = view.findViewById(R.id.open_add_place_screen)!!
        adapter = MyAdapter(this)

        navControllerSetup()
        recyclerViewSetup(recyclerView)
        observer(this)

        openAddPlaceButton.setOnClickListener {
            navController.navigate(R.id.newPlaceFragment)
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(title: String, latitude: Double, longitude: Double) =
            PlaceListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun navControllerSetup() {
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun observer(owner: LifecycleOwner){
        myViewModel.readPerson.observe(owner) {
            adapter.setData(it as ArrayList<Place>)
        }
    }

    private fun recyclerViewSetup(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCardClick(place: Place) {

        val image = Converters.getImageUri(place.image, requireContext())

        val bundle = Bundle()
        bundle.putString("title", place.title)
        bundle.putString("description", place.description)
        bundle.putString("image", image.toString())
        place.latitude?.let { bundle.putDouble("latitude", it) }
        place.longitude?.let { bundle.putDouble("longitude", it) }
        navController.navigate(R.id.placeDetailsFragment, bundle)
    }
}