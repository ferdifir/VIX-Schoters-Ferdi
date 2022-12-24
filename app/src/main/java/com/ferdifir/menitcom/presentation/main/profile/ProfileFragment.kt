package com.ferdifir.menitcom.presentation.main.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.utils.Const.myGithub
import com.ferdifir.menitcom.data.utils.Const.myInstagram
import com.ferdifir.menitcom.data.utils.Const.myLinkedin
import com.ferdifir.menitcom.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.add(R.id.my_address, MapsFragment())
            ?.commit()

        val i = Intent(Intent.ACTION_VIEW)

        binding.myGithub.setOnClickListener {
            i.data = Uri.parse(myGithub)
            startActivity(i)
        }
        binding.myLinkedin.setOnClickListener {
            i.data = Uri.parse(myLinkedin)
            startActivity(i)
        }
        binding.myInstagam.setOnClickListener {
            i.data = Uri.parse(myInstagram)
            startActivity(i)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}