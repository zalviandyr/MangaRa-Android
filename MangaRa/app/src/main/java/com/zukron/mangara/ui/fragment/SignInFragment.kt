package com.zukron.mangara.ui.fragment

import android.content.Intent
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
import com.zukron.mangara.ui.home.HomeActivity
import com.zukron.mangara.ui.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment(), View.OnClickListener {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view model
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        authViewModel.isActionSuccess.observe(requireActivity()) {
            if (it.status == AuthViewModel.Status.SUCCESS) {
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
            }

            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }

        // button listener
        signInFrag_btnEnter.setOnClickListener(this)
        signInFrag_btnGoogle.setOnClickListener(this)
        signInFrag_btnSignUpHere.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.signInFrag_btnEnter -> {
                if (validate()) {
                    val email = signInFrag_inputEmail.editText?.text.toString().trim()
                    val password = signInFrag_inputPassword.editText?.text.toString().trim()

                    authViewModel.signIn(email, password)
                }
            }
            R.id.signInFrag_btnGoogle -> {
                // todo implement for the next feature
                Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_LONG).show()
            }
            R.id.signInFrag_btnSignUpHere -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_singInFragment_to_signUpFragment)
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (signInFrag_inputEmail.editText?.text.isNullOrEmpty()) {
            valid = false
            signInFrag_inputEmail.error = "Required field"
        } else {
            if (!Patterns.EMAIL_ADDRESS
                    .matcher(signInFrag_inputEmail.editText?.text.toString())
                    .matches()
            ) {
                valid = false
                signInFrag_inputEmail.error = "Wrong email format"
            }
        }

        if (signInFrag_inputPassword.editText?.text.isNullOrEmpty()) {
            valid = false
            signInFrag_inputPassword.error = "Required field"
        }

        return valid
    }
}