package com.ferdifir.menitcom.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavView()
    }

    private fun setBottomNavView() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.nav_home -> binding.bottomNavView.visibility = View.VISIBLE
                R.id.nav_search -> binding.bottomNavView.visibility = View.VISIBLE
                R.id.nav_bookmark -> binding.bottomNavView.visibility = View.VISIBLE
                else -> binding.bottomNavView.visibility = View.GONE
            }
        }
    }

}