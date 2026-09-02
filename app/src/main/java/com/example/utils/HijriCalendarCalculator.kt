package com.example.utils

import com.example.data.GregorianDate
import com.example.data.HijriDate
import com.example.data.IslamicOccasion
import java.util.Calendar
import java.util.Date
import kotlin.math.ceil
import kotlin.math.floor

object HijriCalendarCalculator {

    /**
     * Converts Gregorian (year, month, day) to Julian Day Number (JDN)
     */
    fun gregorianToJulianDay(year: Int, month: Int, day: Int): Long {
        var y = year.toLong()
        var m = month.toLong()
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        return (365.25 * (y + 4716)).toLong() + (30.6001 * (m + 1)).toLong() + day + b - 1524
    }

    /**
     * Converts Julian Day Number to Gregorian Date (year, month, day, dayOfWeek)
     */
    fun julianDayToGregorian(jd: Long): GregorianDate {
        val z = jd
        val a = ((z - 1867216.25) / 36524.25).toLong()
        val a2 = z + 1 + a - (a / 4)
        val b = a2 + 1524
        val c = ((b - 122.1) / 365.25).toLong()
        val d = (365.25 * c).toLong()
        val e = ((b - d) / 30.6001).toLong()

        val day = (b - d - (30.6001 * e).toLong()).toInt()
        val month = (if (e < 14) e - 1 else e - 13).toInt()
        val year = (if (month > 2) c - 4716 else c - 4715).toInt()

        // 1=Sunday, 2=Monday, ..., 7=Saturday
        val dayOfWeek = (((jd + 1) % 7 + 7) % 7 + 1).toInt()

        return GregorianDate(day = day, month = month, year = year, dayOfWeek = dayOfWeek)
    }

    /**
     * Converts Julian Day Number to Hijri Date with moon-sighting adjustment
     */
    fun julianDayToHijri(jd: Long, offsetDays: Int = 0): HijriDate {
        val adjustedJd = jd + offsetDays
        val daysSinceEpoch = adjustedJd - 1948439
        if (daysSinceEpoch <= 0) return HijriDate(1, 1, 1)

        val cycles = (daysSinceEpoch - 1) / 10631
        var remDays = daysSinceEpoch - cycles * 10631
        var yInCycle = 1

        val leapYearsInCycle = setOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)

        while (yInCycle < 30) {
            val daysInYear = if (leapYearsInCycle.contains(yInCycle)) 355 else 354
            if (remDays <= daysInYear) break
            remDays -= daysInYear
            yInCycle++
        }

        val year = (cycles * 30 + yInCycle).toInt()

        var month = 1
        while (month < 12) {
            val daysInMonth = if (month % 2 == 1) 30 else 29
            if (remDays <= daysInMonth) break
            remDays -= daysInMonth
            month++
        }
        val day = remDays.toInt()

        return HijriDate(day = day, month = month, year = year)
    }

    /**
     * Converts Hijri Date to Julian Day Number
     */
    fun hijriToJulianDay(year: Int, month: Int, day: Int, offsetDays: Int = 0): Long {
        val y = year.toLong()
        val m = month.toLong()
        val d = day.toLong()

        val leapYearsInCycle = setOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)

        // Calculate days elapsed before this year
        val cycles = (y - 1) / 30
        val remYears = (y - 1) % 30
        var daysBeforeYear = cycles * 10631
        for (yi in 1..remYears) {
            daysBeforeYear += if (leapYearsInCycle.contains(yi.toInt())) 355 else 354
        }

        // Days before this month in the year
        val daysBeforeMonth = ceil(29.5 * (m - 1)).toLong()

        val jd = 1948439 + daysBeforeYear + daysBeforeMonth + d
        return jd - offsetDays
    }

    /**
     * Converts Gregorian Date to Hijri Date
     */
    fun toHijri(year: Int, month: Int, day: Int, offsetDays: Int = 0): HijriDate {
        val jd = gregorianToJulianDay(year, month, day)
        return julianDayToHijri(jd, offsetDays)
    }

    /**
     * Converts Hijri Date to Gregorian Date
     */
    fun toGregorian(hijriYear: Int, hijriMonth: Int, hijriDay: Int, offsetDays: Int = 0): GregorianDate {
        val jd = hijriToJulianDay(hijriYear, hijriMonth, hijriDay, offsetDays)
        return julianDayToGregorian(jd)
    }

    /**
     * Get Today's Gregorian Date
     */
    fun getTodayGregorian(): GregorianDate {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1=Sunday, 2=Monday, ..., 7=Saturday
        return GregorianDate(day = day, month = month, year = year, dayOfWeek = dayOfWeek)
    }

    /**
     * Get Today's Hijri Date with user adjustment
     */
    fun getTodayHijri(offsetDays: Int = 0): HijriDate {
        val today = getTodayGregorian()
        return toHijri(today.year, today.month, today.day, offsetDays)
    }

    /**
     * Check if a Hijri year is a leap year (has 355 days instead of 354)
     */
    fun isHijriLeapYear(year: Int): Boolean {
        // In the 30-year tabular cycle, leap years are 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29
        val leapYearsInCycle = setOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)
        val mod = ((year % 30) + 30) % 30
        return leapYearsInCycle.contains(mod)
    }

    /**
     * Number of days in a Hijri month (29 or 30)
     */
    fun getDaysInHijriMonth(year: Int, month: Int): Int {
        return if (month % 2 == 1) {
            30 // Odd months: Muharram, Rabi I, Jumada I, Rajab, Ramadan, Dhul Qi'dah
        } else if (month == 12 && isHijriLeapYear(year)) {
            30
        } else {
            29 // Even months: Safar, Rabi II, Jumada II, Sha'ban, Shawwal, Dhul Hijjah (non-leap)
        }
    }

    /**
     * Number of days in Gregorian month
     */
    fun getDaysInGregorianMonth(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
            else -> 30
        }
    }

    /**
     * Moon Phase description and icon for a Hijri day
     */
    fun getMoonPhaseInfo(hijriDay: Int): Pair<String, String> {
        return when (hijriDay) {
            1, 2 -> "Hilal (New Crescent Moon)" to "🌙"
            in 3..6 -> "Waxing Crescent (Hilal)" to "🌒"
            7, 8 -> "First Quarter Moon" to "🌓"
            in 9..13 -> "Waxing Gibbous" to "🌔"
            14, 15 -> "Full Moon (Badr / 14vin ka Chand)" to "🌕"
            in 16..20 -> "Waning Gibbous" to "🌖"
            21, 22 -> "Third Quarter Moon" to "🌗"
            in 23..27 -> "Waning Crescent" to "🌘"
            else -> "Dark Moon / Conjunction" to "🌑"
        }
    }

    /**
     * Moon Phase description in Urdu
     */
    fun getMoonPhaseInfoUrdu(hijriDay: Int): Pair<String, String> {
        return when (hijriDay) {
            1, 2 -> "ہلال (نیا چاند)" to "🌙"
            in 3..6 -> "ہلال (بڑھتا چاند)" to "🌒"
            7, 8 -> "پہلی چوتھائی" to "🌓"
            in 9..13 -> "کوبہ چاند" to "🌔"
            14, 15 -> "بدر (چودہویں کا چاند)" to "🌕"
            in 16..20 -> "گھٹتا چاند" to "🌖"
            21, 22 -> "آخری چوتھائی" to "🌗"
            in 23..27 -> "ہلال (آخری ایام)" to "🌘"
            else -> "محاق (تاریک چاند)" to "🌑"
        }
    }

    /**
     * Detects special Islamic occasions, sacred months, white days, and Friday blessings
     */
    fun getIslamicOccasion(hijriDate: HijriDate, gregorianDate: GregorianDate): IslamicOccasion? {
        val day = hijriDate.day
        val month = hijriDate.month
        val isFriday = gregorianDate.isFriday

        // Major Holiday Occasions
        when (month) {
            1 -> {
                if (day == 1) return IslamicOccasion("Islamic New Year", "رأس السنة الهجرية", "اسلامی نیا سال", "Hijri 1st", true)
                if (day == 9) return IslamicOccasion("Tasu'a (Fasting Day)", "تاسوعاء", "تاسوعہ", "Tasu'a", false)
                if (day == 10) return IslamicOccasion("Day of Ashura", "يوم عاشوراء", "یومِ عاشوراء", "Ashura", true)
            }
            3 -> {
                if (day == 12) return IslamicOccasion("Mawlid an-Nabi ﷺ", "المولد النبوي الشريف", "عید میلاد النبی ﷺ", "Mawlid", true)
            }
            7 -> {
                if (day == 27) return IslamicOccasion("Isra and Mi'raj", "الإسراء والمعراج", "شبِ معراج", "Shab-e-Miraj", true)
            }
            8 -> {
                if (day == 15) return IslamicOccasion("Laylat al-Bara'at", "ليلة البراءة", "شبِ برأت", "Shab-e-Barat", true)
            }
            9 -> {
                if (day == 1) return IslamicOccasion("1st Ramadan Mubarak", "أول رمضان المبارك", "پہلا روزہ رمضان مبارک", "Ramadan Day 1", true)
                if (day == 27) return IslamicOccasion("Laylat al-Qadr", "ليلة القدر", "شبِ قدر", "Laylat al-Qadr", true)
                if (isFriday && day >= 22) return IslamicOccasion("Jumu'at-ul-Wida", "جمعة الوداع", "جمعۃ الوداع", "Jumu'at-ul-Wida", true)
                if (day in listOf(21, 23, 25, 29)) return IslamicOccasion("Odd Night of Ramadan", "ليالي الوتر", "طاق رات", "Odd Night", false)
                return IslamicOccasion("Ramadan Mubarak (Day $day)", "رمضان المبارك", "رمضان المبارک روزه $day", "Ramadan $day", false)
            }
            10 -> {
                if (day == 1) return IslamicOccasion("Eid al-Fitr (Day 1)", "عيد الفطر المبارك", "عید الفطر مبارک (پہلا دن)", "Eid Mubarak", true)
                if (day == 2) return IslamicOccasion("Eid al-Fitr (Day 2)", "عيد الفطر المبارك", "عید الفطر (دوسرا دن)", "Eid Day 2", true)
                if (day == 3) return IslamicOccasion("Eid al-Fitr (Day 3)", "عيد الفطر المبارك", "عید الفطر (تیسرا دن)", "Eid Day 3", true)
            }
            12 -> {
                if (day == 8) return IslamicOccasion("Yawm al-Tarwiyah (Hajj)", "يوم التروية", "یومِ ترویہ (حج کا آغاز)", "Hajj Day 1", false)
                if (day == 9) return IslamicOccasion("Day of Arafah (Hajj)", "يوم عرفة", "یومِ عرفہ (حج اکبر)", "Day of Arafah", true)
                if (day == 10) return IslamicOccasion("Eid al-Adha (Day 1)", "عيد الأضحى المبارك", "عید الاضحیٰ مبارک", "Eid al-Adha", true)
                if (day == 11) return IslamicOccasion("Eid al-Adha (Day 2)", "أيام التشريق", "عید الاضحیٰ (دوسرا دن)", "Tashreeq Day 1", true)
                if (day == 12) return IslamicOccasion("Eid al-Adha (Day 3)", "أيام التشريق", "عید الاضحیٰ (تیسرا دن)", "Tashreeq Day 2", true)
                if (day in 1..9) return IslamicOccasion("Blessed 10 Days of Dhul Hijjah", "عشر ذي الحجة", "ذوالحجہ کے پہلے دس مبارک دن", "Dhul Hijjah", false)
            }
        }

        // White Days (Ayyam al-Beed - 13th, 14th, 15th)
        if (day in 13..15) {
            return IslamicOccasion("Ayyam al-Beed (Sunnah Fasting)", "الأيام البيض", "ایامِ بیض (سنت روزے)", "White Days", false)
        }

        // Friday Blessing
        if (isFriday) {
            return IslamicOccasion("Jumu'ah Mubarak", "جمعة مباركة", "جمعۃ المبارک", "Jumu'ah", false)
        }

        return null
    }
}
