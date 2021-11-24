package com.android.healthcareapp.util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun getFormattedDate_unixTime_returnsFormattedDate() {
        val formattedDate = "23 November 2021"
        val unixTime = 1637669136862

        val result = getFormattedDate(unixTime)

        assertThat(formattedDate, `is`(result))
    }

    @Test
    fun getFormattedDate_null_returnsCurrentDate() {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = sdf.format(System.currentTimeMillis())

        val result = getFormattedDate(null)

        assertThat(currentDate, `is`(result))
    }
}