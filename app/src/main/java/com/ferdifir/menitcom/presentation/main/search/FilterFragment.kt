package com.ferdifir.menitcom.presentation.main.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.presentation.main.news.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.ZoneId
import java.time.ZonedDateTime

class FilterFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        val sortener = view.findViewById<Spinner>(R.id.spinner_sortener)
        val language = view.findViewById<Spinner>(R.id.spinner_language)
        val date = view.findViewById<Spinner>(R.id.spinner_date)
        val button = view.findViewById<AppCompatButton>(R.id.btn_apply)

        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Const.sortener)
        val bb = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Const.languageAdapter)
        val cc = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Const.date)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortener.adapter = aa
        language.adapter = bb
        date.adapter = cc

        val zoneId = ZoneId.systemDefault()
        button.setOnClickListener {
            val sorter = Const.sorter[sortener.selectedItem.toString()]
            val selectedLanguage = Const.lang[language.selectedItem.toString()]
            val selectedDate = when(date.selectedItem.toString()) {
                Const.date[0] -> ZonedDateTime.now(zoneId).minusDays(0).toString()
                Const.date[1] -> ZonedDateTime.now(zoneId).minusDays(7).toString()
                Const.date[2] -> ZonedDateTime.now(zoneId).minusDays(30).toString()
                else -> ZonedDateTime.now(zoneId).minusDays(30).toString()
            }
            viewModel.saveSortPreferences(sorter!!)
            viewModel.saveDatePreferences(selectedDate)
            viewModel.saveLanguagePreferences(selectedLanguage!!)
            dismiss()
        }
    }

}