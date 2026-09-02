package com.example.data

/**
 * Data representation of a Hijri (Islamic) Date
 */
data class HijriDate(
    val day: Int,
    val month: Int, // 1 to 12
    val year: Int   // e.g. 1447 AH
) {
    val monthNameEn: String
        get() = HIJRI_MONTHS_EN.getOrElse(month - 1) { "Unknown" }

    val monthNameAr: String
        get() = HIJRI_MONTHS_AR.getOrElse(month - 1) { "" }

    val monthNameUr: String
        get() = HIJRI_MONTHS_UR.getOrElse(month - 1) { "" }

    val formattedEn: String
        get() = "$day $monthNameEn $year AH"

    val formattedAr: String
        get() = "$day $monthNameAr $year هـ"

    val formattedUr: String
        get() = "$day $monthNameUr $year ھ"

    val isSacredMonth: Boolean
        get() = month == 1 || month == 7 || month == 11 || month == 12

    companion object {
        val HIJRI_MONTHS_EN = listOf(
            "Muharram",
            "Safar",
            "Rabi' al-Awwal",
            "Rabi' al-Thani",
            "Jumada al-Awwal",
            "Jumada al-Thani",
            "Rajab",
            "Sha'ban",
            "Ramadan",
            "Shawwal",
            "Dhu al-Qi'dah",
            "Dhu al-Hijjah"
        )

        val HIJRI_MONTHS_AR = listOf(
            "المحرّم",
            "صفر",
            "ربيع الأول",
            "ربيع الثاني",
            "جمادى الأولى",
            "جمادى الآخرة",
            "رجب",
            "شعبان",
            "رمضان",
            "شوّال",
            "ذو القعدة",
            "ذو الحجة"
        )

        val HIJRI_MONTHS_UR = listOf(
            "محرم الحرام",
            "صفر المظفر",
            "ربیع الاول",
            "ربیع الثانی",
            "جمادی الاول",
            "جمادی الثانی",
            "رجب المرجب",
            "شعبان المعظم",
            "رمضان المبارک",
            "شوال المکرم",
            "ذوالقعدۃ",
            "ذوالحجۃ"
        )
    }
}

/**
 * Data representation of a Gregorian (Solar/English) Date
 */
data class GregorianDate(
    val day: Int,
    val month: Int, // 1 to 12
    val year: Int,
    val dayOfWeek: Int // 1 = Sunday, 2 = Monday, ... 7 = Saturday
) {
    val monthNameEn: String
        get() = GREGORIAN_MONTHS_EN.getOrElse(month - 1) { "Unknown" }

    val dayOfWeekNameEn: String
        get() = DAYS_OF_WEEK_EN.getOrElse(dayOfWeek - 1) { "" }

    val dayOfWeekNameAr: String
        get() = DAYS_OF_WEEK_AR.getOrElse(dayOfWeek - 1) { "" }

    val dayOfWeekNameUr: String
        get() = DAYS_OF_WEEK_UR.getOrElse(dayOfWeek - 1) { "" }

    val monthNameUr: String
        get() = GREGORIAN_MONTHS_UR.getOrElse(month - 1) { "" }

    val formattedEn: String
        get() = "$day $monthNameEn $year"

    val formattedFull: String
        get() = "$dayOfWeekNameEn, $day $monthNameEn $year"

    val formattedUr: String
        get() = "$day $monthNameUr $year"

    val formattedFullUr: String
        get() = "$dayOfWeekNameUr، $day $monthNameUr ${year}ء"

    val isFriday: Boolean
        get() = dayOfWeek == 6

    companion object {
        val GREGORIAN_MONTHS_EN = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val GREGORIAN_MONTHS_UR = listOf(
            "جنوری", "فروری", "مارچ", "اپریل", "مئی", "جون",
            "جولائی", "اگست", "ستمبر", "اکتوبر", "نومبر", "دسمبر"
        )

        val DAYS_OF_WEEK_EN = listOf(
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        )

        val DAYS_OF_WEEK_AR = listOf(
            "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"
        )

        val DAYS_OF_WEEK_UR = listOf(
            "اتوار", "پیر", "منگل", "بدھ", "جمعرات", "جمعہ", "ہفتہ"
        )
    }
}

/**
 * Significant Islamic event or blessing associated with a date
 */
data class IslamicOccasion(
    val titleEn: String,
    val titleAr: String,
    val titleUr: String,
    val badgeLabel: String,
    val isMajor: Boolean = false
)

/**
 * Visual styles for the video date card / sticker
 */
enum class OverlayTemplateStyle(
    val label: String,
    val labelUr: String,
    val description: String,
    val descriptionUr: String,
    val isDark: Boolean,
    val isTransparentBg: Boolean = false
) {
    EMERALD_GOLD("Royal Emerald", "شاہی زمرد", "Luxury Islamic gold & deep emerald pattern", "شاہی سنہری اور گہرا زمردی پیٹرن", true),
    NIGHT_CELESTIAL("Midnight Moon", "چاند رات", "Deep night sky with glowing crescent & minarets", "ستاروں بھری رات اور چمکتا ہلال", true),
    DESERT_SAND("Aesthetic Arch", "صحرائی محراب", "Warm cream minimalist boho archway", "گرم ریتلا اور پرسکون روایتی محراب", false),
    TRANSPARENT_OVERLAY("Video Sticker (PNG)", "شفاف ویڈیو اسٹیکر", "Clean transparent cut-out badge for video timeline", "ویڈیو ایڈیٹنگ کے لیے بغیر بیک گراؤنڈ اسٹیکر", true, true),
    MINIMAL_BADGE("Modern Pill Badge", "جدید گلاس پِل", "Translucent glass sticker for TikTok/Reels corners", "ریلز اور ٹک ٹاک کے لیے فروسٹڈ گلاس بیج", true),
    GOLDEN_CHIC("Golden Chic", "سنہری حسن", "Warm gold border with sacred typography", "خوبصورت سنہری بارڈر اور مقدس خطاطی", true)
}
