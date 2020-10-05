package com.zukron.mangara.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.zukron.mangara.R
import com.zukron.mangara.ui.home.HomeActivity
import com.zukron.mangara.ui.viewmodel.AuthViewModel

class SplashScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thread = object : Thread() {
            override fun run() {
                super.run()

                sleep(3000)

                val authViewModel =
                    ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
                val firebaseUser = authViewModel.firebaseAuth.currentUser

                if (firebaseUser != null) {
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
            }
        }

        thread.start()
    }
}