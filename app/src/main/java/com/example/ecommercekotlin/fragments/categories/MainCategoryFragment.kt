package com.example.ecommercekotlin.fragments.categories

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercekotlin.R
import com.example.ecommercekotlin.adapters.BestDealsAdapter
import com.example.ecommercekotlin.adapters.BestProductAdapter
import com.example.ecommercekotlin.adapters.SpecialProductsAdapter
import com.example.ecommercekotlin.databinding.FragmentMainCategoryBinding
import com.example.ecommercekotlin.utils.Resource
import com.example.ecommercekotlin.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {


    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductAdapter


    private val viewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()

        lifecycleScope.launch {

            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }

            }
        }

        lifecycleScope.launch {
            viewModel.bestDealsProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }

            }
        }



        lifecycleScope.launch {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                    binding.bestProductsProgressBar.visibility=View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressBar.visibility=View.INVISIBLE
                    }

                    is Resource.Error -> {
                        binding.bestProductsProgressBar.visibility=View.INVISIBLE
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }


            }
        }





        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <=  v.height +scrollY) {
                viewModel.fetchBestProducts()
            }
        })

    }

    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }


}