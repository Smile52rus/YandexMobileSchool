package com.arzaapps.android.yamobile.views.detailscreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.arzaapps.android.yamobile.R
import com.arzaapps.android.yamobile.data.entities.CompanyProfile
import com.arzaapps.android.yamobile.utils.Resource
import com.arzaapps.android.yamobile.views.detailscreen.DetailViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var companyName: TextView
    private lateinit var companyImageSwitcher: ImageSwitcher
    private lateinit var countryCompany: TextView
    private lateinit var phoneCompany: TextView
    private lateinit var companyLogo: ImageView
    private val detailCompanyViewModel: DetailViewModel by viewModels()
    private lateinit var companyDetailDataObserver: Observer<Resource<CompanyProfile>>
    private lateinit var owner: LifecycleOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        owner = this
        companyDetailDataObserver = Observer<Resource<CompanyProfile>> {
            it?.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.let { companyProfile ->
                            companyName.text = companyProfile.name
                            countryCompany.text = companyProfile.country
                            phoneCompany.text = companyProfile.phone
                            showCompanyLogo(companyProfile.logo)
                            refreshFavouriteImageSwitcher(companyProfile.isFavorite)
                        }
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
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
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        val ticker = arguments?.getString("ticker", "")
        companyName = view.findViewById(R.id.detail_name_company)
        companyImageSwitcher = view.findViewById(R.id.detail_image_switcher)
        companyImageSwitcher.setOnClickListener {
            detailCompanyViewModel.setFavouriteCompany(ticker ?: "")
                .observe(owner, { resource ->
                    resource.data?.let { isFavourite ->
                        refreshFavouriteImageSwitcher(isFavourite)
                    }
                })
        }
        countryCompany = view.findViewById(R.id.detail_country_company)
        phoneCompany = view.findViewById(R.id.detail_phone_company)
        detailCompanyViewModel.getCompanyProfile(ticker ?: "null")
            .observe(owner, companyDetailDataObserver)
        companyLogo = view.findViewById(R.id.detail_company_logo)

        return view
    }

    private fun showCompanyLogo(urlLogo: String) {
        Picasso.get()
            .load(
                when {
                    urlLogo.isEmpty() -> "xxxxx"
                    else -> urlLogo
                }
            )
            .error(R.drawable.ic_enable)
            .into(companyLogo)
    }

    private fun refreshFavouriteImageSwitcher(isFavourite: Boolean) {
        if (isFavourite)
            companyImageSwitcher.setImageResource(R.drawable.ic_enable)
        else
            companyImageSwitcher.setImageResource(R.drawable.ic_disable)
    }
}