package com.example.ecommercekotlin.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommercekotlin.R
import com.example.ecommercekotlin.data.User
import com.example.ecommercekotlin.databinding.FragmentRegisterBinding
import com.example.ecommercekotlin.utils.RegisterValidation
import com.example.ecommercekotlin.utils.Resource
import com.example.ecommercekotlin.utils.validateEmail
import com.example.ecommercekotlin.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.tvDontHaveAccountRegister.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }




        binding.apply {
            binding.buttonRegisterRegister.setOnClickListener {
                val user = User(
                    editTextFirstNameRegister.text.toString().trim(),
                    editTextLastNameRegister.text.toString().trim(),
                    editTextEmailRegister.text.toString().trim()

                )
                val passwod = editTextPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, passwod)


            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {

                when (it) {
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()



                    }

                    is Resource.Error -> {
                        Log.d(TAG, it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()

                    }

                    else -> Unit

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.editTextEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed ){
                    withContext(Dispatchers.Main){
                        binding.editTextPasswordRegister.apply {
                            requestFocus()
                            error=validation.password.message
                        }
                    }
                }
            }
        }


    }




}