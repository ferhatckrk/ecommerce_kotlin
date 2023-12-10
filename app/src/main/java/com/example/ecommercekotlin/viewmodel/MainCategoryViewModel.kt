package com.example.ecommercekotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercekotlin.data.Product
import com.example.ecommercekotlin.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {


    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProduct =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts: StateFlow<Resource<List<Product>>> = _bestDealsProduct

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProduct

    private val pagingInfo = PagingInfo()


    init {
        fetchBestDeals()
        fetchSpecialProducts()

        fetchBestProducts()
    }


    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())

            firestore.collection("Products").whereEqualTo("category", "Special Products").get()
                .addOnSuccessListener { result ->
                    val specialProductsList = result.toObjects(Product::class.java)
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.Success(specialProductsList))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.Error(it.message.toString()))
                    }
                }

        }
    }


    fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDealsProduct.emit(Resource.Loading())

        }
        firestore.collection("Products").whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDeals = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealsProduct.emit(Resource.Success(bestDeals))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealsProduct.emit(Resource.Error(it.message.toString()))
                }
            }

    }


    fun fetchBestProducts() {

        if (!pagingInfo.isPagingEnd) {

            viewModelScope.launch {
                _bestProduct.emit(Resource.Loading())
            }
            firestore.collection("Products").limit(pagingInfo.page * 10)
                 .get()
                .addOnSuccessListener { result ->
                    val bestProduct = result.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = bestProduct == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = bestProduct
                    viewModelScope.launch {
                        _bestProduct.emit(Resource.Success(bestProduct))
                    }
                    pagingInfo.page++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProduct.emit(Resource.Error(it.message.toString()))
                    }
                }
        }


    }


}


internal data class PagingInfo(
    var page: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)