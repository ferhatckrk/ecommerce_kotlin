package com.example.ecommercekotlin.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommercekotlin.R
import com.example.ecommercekotlin.adapters.HomeViewPagerAdapter
import com.example.ecommercekotlin.databinding.FragmentHomeBinding
import com.example.ecommercekotlin.fragments.categories.AccessoryFragment
import com.example.ecommercekotlin.fragments.categories.ChairFragment
import com.example.ecommercekotlin.fragments.categories.CupboardFragment
import com.example.ecommercekotlin.fragments.categories.FurniteFragment
import com.example.ecommercekotlin.fragments.categories.MainCategoryFragment
import com.example.ecommercekotlin.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            TableFragment(),
            CupboardFragment(),
            AccessoryFragment(),
            FurniteFragment(),
            )

        val viewPager2Adapter =
            HomeViewPagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Table"
                3 -> tab.text = "Cupboard"
                4 -> tab.text = "Accesory"
                5 -> tab.text = "Furnite"

            }
        }.attach()
    }


}