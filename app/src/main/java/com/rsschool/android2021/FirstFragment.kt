package com.rsschool.android2021

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class FirstFragment : Fragment() {

    private var generateButton: Button? = null
    private var previousResult: TextView? = null
    private var minValueEditText: EditText? = null
    private var maxValueEditText: EditText? = null
    private var minMaxEnabled: CheckBox? = null
    private var listener: RandomizerClickListener? = null
    private var minOk = false
    private var maxOk = false
    private var snack: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousResult = view.findViewById(R.id.previous_result)
        generateButton = view.findViewById(R.id.generate)
        minValueEditText = view.findViewById(R.id.min_value)
        maxValueEditText = view.findViewById(R.id.max_value)
        minMaxEnabled = view.findViewById(R.id.checkBox)

        generateButton?.isEnabled = false

        val result = arguments?.getInt(PREVIOUS_RESULT_KEY)
        previousResult?.text = "Previous result: ${result.toString()}"


        snack = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

        minValueEditText?.addTextChangedListener {
           testMinInputData(it.toString())

        }

        minMaxEnabled?.setOnCheckedChangeListener { buttonView, isChecked ->
            testMinInputData(minValueEditText?.text.toString())
            testMaxInputData(maxValueEditText?.text.toString())

        }

        maxValueEditText?.addTextChangedListener {
            testMaxInputData(it.toString())
        }

        generateButton?.setOnClickListener {
            val min = minValueEditText?.text.toString().toInt()
            val max = maxValueEditText?.text.toString().toInt()
            listener?.sendMinMaxData(min, max)

        }
    }

    private fun testMaxInputData(inputData: String) {

        if (inputData == "") {
            maxOk = false
            if(minValueEditText?.text?.isEmpty() == true) {
                snack?.setText("В поле Max и Min ничего не введено")?.show()
                generateButton?.isEnabled = false

            } else {
                snack?.setText("В поле Max ничего не введено")?.show()
                generateButton?.isEnabled = false

            }
        } else if (inputData.length >= 2 && inputData[0] == '0') {
            maxOk = false
            snack?.setText("Число в поле Max не должно начинаться с нуля")?.show()
            generateButton?.isEnabled = false

        } else if (inputData.length >= 10 && inputData.toLong() > Int.MAX_VALUE) {
            maxOk = false
            snack?.setText("Работаем только в диапаоне от 0 до ${Int.MAX_VALUE}")?.show()
            generateButton?.isEnabled = false

        } else {
            maxOk = true
            val min = minValueEditText?.text.toString()
            val max = maxValueEditText?.text.toString()
            testInputData(minOk, maxOk, min, max)
        }
    }

    private fun testMinInputData(inputData: String) {
        if (inputData == "") {
            minOk = false
            if (maxValueEditText?.text?.isEmpty() == true) {
                snack?.setText("В поле Max и Min ничего не введено")?.show()
                generateButton?.isEnabled = false

            } else {
                snack?.setText("В поле Min ничего не введено")?.show()
                generateButton?.isEnabled = false

            }
        } else if (inputData.length >= 2 && inputData[0] == '0') {
            minOk = false
            snack?.setText("Число в поле Min не должно начинаться с нуля")?.show()
            generateButton?.isEnabled = false

        }
        else if (inputData.length >= 10 && inputData.toLong() > Int.MAX_VALUE) {
            minOk = false
            snack?.setText("Работаем только в диапаоне от 0 до ${Int.MAX_VALUE}")?.show()
            generateButton?.isEnabled = false

        }
        else {
            minOk = true
            val min = minValueEditText?.text.toString()
            val max = maxValueEditText?.text.toString()
            testInputData(minOk, maxOk, min, max)
        }
    }

    private fun testInputData (minOk: Boolean, maxOk: Boolean, min: String, max: String) {

        if (minOk && maxOk) {

            val maxInt = max.toInt()
            val minInt = min.toInt()

            if (minMaxEnabled?.isChecked == false) {
                if (maxInt == minInt) {
                    snack?.setText("Значения Max и Min не должны быть равны")?.show()
                    generateButton?.isEnabled = false

                } else if (maxInt - minInt < 0) {
                    snack?.setText("Значение Min больше Max, нужно наоборот")?.show()
                    generateButton?.isEnabled = false

                } else {
                    snack?.setText("Введенные значения подходят")?.show()
                    generateButton?.isEnabled = true

                }

            }   else {
                    if (maxInt - minInt < 0) {
                        snack?.setText("Значение Min больше Max, нужно наоборот")?.show()
                        generateButton?.isEnabled = false

                    } else {
                        snack?.setText("Введенные значения подходят")?.show()
                        generateButton?.isEnabled = true
                    }
                }
        }
    }

    companion object {

        private const val PREVIOUS_RESULT_KEY = "PREVIOUS_RESULT"

        @JvmStatic
        fun newInstance(previousResult: Int): FirstFragment {
            val fragment = FirstFragment()
            val args = Bundle()
            args.putInt(PREVIOUS_RESULT_KEY, previousResult)
            fragment.arguments = args
            return fragment

        }
    }
}