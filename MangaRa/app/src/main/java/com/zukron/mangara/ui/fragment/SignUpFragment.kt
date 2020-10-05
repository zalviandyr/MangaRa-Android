package com.zukron.mangara.ui.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.zukron.mangara.R
import com.zukron.mangara.ui.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(), View.OnClickListener {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view model
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        authViewModel.isActionSuccess.observe(requireActivity()) {
            if (it.status == AuthViewModel.Status.SUCCESS) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_signUpFragment_to_signInFragment)
            }

            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }

        // button listener
        signUpFrag_btnEnter.setOnClickListener(this)
        signUpFrag_btnSignInHere.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.signUpFrag_btnEnter -> {
                if (validate()) {
                    val name =
                        signUpFrag_inputFullName.editText?.text.toString().trim()
                    val email =
                        signUpFrag_inputEmail.editText?.text.toString().trim()
                    val password =
                        signUpFrag_inputPassword.editText?.text.toString().trim()
                    val confirmPassword =
                        signUpFrag_inputConfirmPassword.editText?.text.toString().trim()

                    if (password == confirmPassword) {
                        authViewModel.signUp(name, email, password)
                    } else {
                        signUpFrag_inputPassword.error = "Must be same"
                        signUpFrag_inputConfirmPassword.error = "Must be same"
                    }
                }
            }
            R.id.signUpFrag_btnSignInHere -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (signUpFrag_inputFullName.editText?.text.isNullOrEmpty()) {
            valid = false
            signUpFrag_inputFullName.error = "Required field"
        }

        if (signUpFrag_inputEmail.editText?.text.isNullOrEmpty()) {
            valid = false
            signUpFrag_inputEmail.error = "Required field"
        } else {
            if (!Patterns.EMAIL_ADDRESS
                    .matcher(signUpFrag_inputEmail.editText?.text.toString())
                    .matches()
            ) {
                valid = false
                signUpFrag_inputEmail.error = "Wrong email format"
            }
        }

        if (signUpFrag_inputPassword.editText?.text.isNullOrEmpty()) {
            valid = false
            signUpFrag_inputPassword.error = "Required field"
        }

        if (signUpFrag_inputConfirmPassword.editText?.text.isNullOrEmpty()) {
            valid = false
            signUpFrag_inputConfirmPassword.error = "Required field"
        }

        return valid
    }
}