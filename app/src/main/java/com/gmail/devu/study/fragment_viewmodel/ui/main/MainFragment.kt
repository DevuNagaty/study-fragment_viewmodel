package com.gmail.devu.study.fragment_viewmodel.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gmail.devu.study.fragment_viewmodel.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Show 'Fragment No.'
        message.text = getString(R.string.msg_fragment_no, viewModel.count)

        // Disable buttons
        when (viewModel.count) {
            1 -> { button_prev.isEnabled = false }
            10 -> { button_next.isEnabled = false }     // Maximum number of Fragment is 10
        }

        // Set OnClickListeners
        button_next.setOnClickListener {
            viewModel.count += 1

            val transaction = getParentFragmentManager().beginTransaction()
            transaction.replace(R.id.container, MainFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        button_prev.setOnClickListener {
            viewModel.count -= 1

            parentFragmentManager.popBackStack()
        }
    }
}
