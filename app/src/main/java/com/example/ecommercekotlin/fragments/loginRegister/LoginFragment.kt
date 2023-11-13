package com.example.ecommercekotlin.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommercekotlin.R
import com.example.ecommercekotlin.activities.ShoppingActivity
import com.example.ecommercekotlin.databinding.FragmentLoginBinding
import com.example.ecommercekotlin.databinding.FragmentRegisterBinding
import com.example.ecommercekotlin.utils.Resource
import com.example.ecommercekotlin.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            binding.buttonLoginLogin.setOnClickListener {

                val email = binding.editTextEmailLogin.text.toString().trim()
                val password = binding.editTextPasswordRegister.text.toString()

                viewModel.login(email, password)

            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonLoginLogin.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonLoginLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        binding.buttonLoginLogin.revertAnimation()

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit

                }
            }

        }


    }


}