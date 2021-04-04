package com.arzaapps.android.yamobile.views.actionslistscreen.view

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
import com.arzaapps.android.yamobile.views.actionslistscreen.ActionsListViewModel
import com.arzaapps.android.yamobile.views.detailscreen.view.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActionsListFragment : Fragment(), ActionViewHolder.OnActionClickListener {
    private lateinit var recyclerViewActions: RecyclerView
    private lateinit var loadProgressBar: ProgressBar
    private val actionsViewModel: ActionsListViewModel by viewModels()
    private lateinit var actionsDataObserver: Observer<Resource<List<ActionData>>>
    private lateinit var owner: LifecycleOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        owner = this
        actionsDataObserver = Observer<Resource<List<ActionData>>> {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.let { actionsData -> showList(actionsData) }
                        loadProgressBar.visibility = View.GONE
                    }
                    Resource.Status.ERROR -> {
                        loadProgressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        loadProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.actionslist_fragment, container, false)
        recyclerViewActions = view.findViewById(R.id.recyclerActionsDataList)
        loadProgressBar = view.findViewById(R.id.progressbar)
        loadProgressBar.visibility = View.INVISIBLE

        actionsViewModel.getActions()
            .observe(owner, actionsDataObserver)
        return view
    }

    private fun showList(actionsList: List<ActionData>) {
        val listAdapter = ListAdapter(actionsList, this)
        recyclerViewActions.apply {
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