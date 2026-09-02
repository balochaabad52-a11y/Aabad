package com.example

import com.example.data.HijriDate
import com.example.utils.HijriCalendarCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testDateConversions() {
    // Test conversion of a known date
    val greg = HijriCalendarCalculator.getTodayGregorian()
    assertTrue(greg.year >= 2024)
    assertTrue(greg.month in 1..12)
    assertTrue(greg.day in 1..31)

    val hijri = HijriCalendarCalculator.toHijri(greg.year, greg.month, greg.day, 0)
    assertTrue(hijri.year >= 1445)
    assertTrue(hijri.month in 1..12)
    assertTrue(hijri.day in 1..30)

    // Test reverse conversion consistency
    val backToGreg = HijriCalendarCalculator.toGregorian(hijri.year, hijri.month, hijri.day, 0)
    assertEquals(greg.year, backToGreg.year)
    assertEquals(greg.month, backToGreg.month)
  }

  @Test
  fun testRamadanOccasion() {
    val ramadanDate = HijriDate(1, 9, 1447)
    val gregDate = HijriCalendarCalculator.toGregorian(1447, 9, 1, 0)
    val occasion = HijriCalendarCalculator.getIslamicOccasion(ramadanDate, gregDate)
    assertNotNull(occasion)
    assertTrue(occasion!!.titleEn.contains("Ramadan"))
  }
}

