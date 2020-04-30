package com.gmail.devu.study.fragment_viewmodel.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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

        // Setup preferences: color
        setColor()
        preferences_color.setSelection(viewModel.color)
//      The following code has been removed because the item list is added by the entries property.
//        ArrayAdapter.createFromResource(
//            view.context,
//            R.array.color_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//            preferences_color.adapter = adapter
//            preferences_color.setSelection(viewModel.color)
//        }
        preferences_color.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.color = parent!!.selectedItemPosition
                setColor()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Setup preferences: animation
        preference_animation.setSelection(viewModel.animation)
        preference_animation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.animation = parent!!.selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Setup prev /next buttons
        when (viewModel.count) {
            1 -> { button_prev.isEnabled = false }
            10 -> { button_next.isEnabled = false }     // Maximum number of Fragment is 10
        }

        button_prev.setOnClickListener {
            viewModel.count -= 1

            parentFragmentManager.popBackStack()
        }

        button_next.setOnClickListener {
            viewModel.count += 1

            getParentFragmentManager()
                .beginTransaction()
                .also {
                    when (viewModel.animation) {
                        0 -> it.setTransition(FragmentTransaction.TRANSIT_NONE)
                        1 -> it.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        2 -> it.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        3 -> it.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        4 -> it.setCustomAnimations(    // Slide from left
                            R.anim.fragment_slide_in_right,
                            R.anim.fragment_slide_out_left,
                            R.anim.fragment_slide_in_left,
                            R.anim.fragment_slide_out_right)
                    }
               }
                .replace(R.id.container, newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    fun setColor() {
        val colorList = arrayOf(
            Color.WHITE,
            Color.CYAN,
            Color.MAGENTA,
            Color.YELLOW,
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.GRAY
        )
        this.view?.setBackgroundColor(colorList[viewModel.color])
    }
}
