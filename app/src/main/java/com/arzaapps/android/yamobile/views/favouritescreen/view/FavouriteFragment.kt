package com.arzaapps.android.yamobile.views.favouritescreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arzaapps.android.yamobile.R
import com.arzaapps.android.yamobile.data.entities.ActionData
import com.arzaapps.android.yamobile.utils.Resource
import com.arzaapps.android.yamobile.views.ActionViewHolder
import com.arzaapps.android.yamobile.views.ListAdapter
import com.arzaapps.android.yamobile.views.detailscreen.view.DetailFragment
import com.arzaapps.android.yamobile.views.favouritescreen.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment(), ActionViewHolder.OnActionClickListener {
    private lateinit var recyclerViewFavourite: RecyclerView
    private lateinit var favouritesLoadProgressBar: ProgressBar
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    private lateinit var favouriteDataObserver: Observer<Resource<List<ActionData>>>
    private lateinit var owner: LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.favourite_fragment, container, false)
        recyclerViewFavourite = view.findViewById(R.id.favourites_recyclerDataList)
        favouritesLoadProgressBar = view.findViewById(R.id.favourites_progressbar)
        favouritesLoadProgressBar.visibility = View.INVISIBLE
        favouriteViewModel.getFavouriteActions().observe(owner, favouriteDataObserver)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        owner = this
        favouriteDataObserver = Observer<Resource<List<ActionData>>> {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.let { favouriteData -> showList(favouriteData) }
                        favouritesLoadProgressBar.visibility = View.GONE
                    }
                    Resource.Status.ERROR -> {
                        favouritesLoadProgressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        favouritesLoadProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showList(actionsList: List<ActionData>) {
        val listAdapter = ListAdapter(actionsList, this)
        recyclerViewFavourite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }

    override fun onClickedAction(ticker: String) {
        val detailFragment = DetailFragment()
        detailFragment.arguments = bundleOf("ticker" to ticker)
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container_view, detailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}