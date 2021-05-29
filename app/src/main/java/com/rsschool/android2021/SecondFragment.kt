package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.random.Random


class SecondFragment : Fragment() {

    private var backButton: Button? = null
    private var result: TextView? = null
    private var listener: RandomizerClickListener? = null
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result = view.findViewById(R.id.result)
        backButton = view.findViewById(R.id.back)

        val min = arguments?.getInt(MIN_VALUE_KEY) ?: 0
        val max = arguments?.getInt(MAX_VALUE_KEY) ?: 0

        val previousNumber = generate(min, max)
        result?.text = previousNumber.toString()

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener?.sendResult(previousNumber)

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        backButton?.setOnClickListener {
            listener?.sendResult(previousNumber)

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as RandomizerClickListener

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        callback.remove()

    }

        private fun generate(min: Int, max: Int) = (min..max).random()

        companion object {

            private const val MIN_VALUE_KEY = "MIN_VALUE"
            private const val MAX_VALUE_KEY = "MAX_VALUE"

            @JvmStatic
            fun newInstance(min: Int, max: Int): SecondFragment {
                val fragment = SecondFragment()
                val args = Bundle()
                args.putInt(MIN_VALUE_KEY, min)
                args.putInt(MAX_VALUE_KEY, max)
                fragment.arguments = args
                return fragment
            }
        }
    }