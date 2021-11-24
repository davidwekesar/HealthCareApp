package com.android.healthcareapp.util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun getFormattedDate_unixTime_returnsFormattedDate() {
        val expectedDate = "23 November 2021"
        val unixTime = 1637669136862

        val result = getFormattedDate(unixTime)

        assertThat(result, `is`(expectedDate))
    }

    @Test
    fun getFormattedDate_null_returnsCurrentDate() {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val expectedDate = sdf.format(System.currentTimeMillis())

        val result = getFormattedDate(null)

        assertThat(result, `is`(expectedDate))
    }

    @Test
    fun calculateBmi_heightWeight_returnsTwentyFive() {
        val height = 177.00
        val weight = 80.00
        val expectedResult = 25

        val result = calculateBmi(height, weight)

        assertThat(result, `is`(expectedResult))
    }
}