package com.example.hafezdivination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hafezdivination.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AboutUs : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val _aboutUs = "نویسنده: بهار کاویانی\nشماره نسخه: نسخه ۱"
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutUsText.text = _aboutUs
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}