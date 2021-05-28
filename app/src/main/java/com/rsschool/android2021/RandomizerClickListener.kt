package com.rsschool.android2021

interface RandomizerClickListener {

    fun sendMinMaxData(min: Int, max: Int)

    fun sendResult(result: Int)
}