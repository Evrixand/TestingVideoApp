package com.videoapp.testingvideoapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.videoapp.testingvideoapp.R
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.googleSignInBtn.setOnClickListener {
            vm.signInGoogle()
        }

        vm.signInResult.observe(viewLifecycleOwner) { result ->

            when(result) {

                is Result.Success -> {

                    findNavController().navigate(R.id.action_loginFragment_to_navigation_feed)
                    Toast.makeText(requireContext(), getString(R.string.sign_in_success_lable), Toast.LENGTH_LONG).show()
                }

                is Result.Failure -> {
                    Toast.makeText(requireContext(), "${result.exception}", Toast.LENGTH_LONG).show()
                }
            }
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}