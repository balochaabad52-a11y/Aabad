package com.example.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.R
import com.example.data.GregorianDate
import com.example.data.HijriDate
import com.example.data.IslamicOccasion
import com.example.data.OverlayTemplateStyle
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

enum class OverlayFormat(
    val label: String,
    val labelUr: String,
    val width: Int,
    val height: Int,
    val isVideoSticker: Boolean
) {
    STICKER_BADGE("Video Overlay Sticker (PNG)", "ویڈیو اوورلے اسٹیکر (PNG)", 1080, 480, true),
    STORY_REEL_9_16("Story / Reel (9:16)", "ریلز و اسٹوری (9:16)", 1080, 1920, false),
    SQUARE_POST_1_1("Square Card (1:1)", "مربع پوسٹ (1:1)", 1080, 1080, false)
}

object OverlayImageGenerator {

    /**
     * Renders a high-resolution stylish date card/sticker onto a Bitmap
     */
    fun renderOverlayBitmap(
        context: Context,
        hijriDate: HijriDate,
        gregorianDate: GregorianDate,
        occasion: IslamicOccasion?,
        style: OverlayTemplateStyle,
        format: OverlayFormat,
        customTitle: String = "",
        showUrdu: Boolean = false
    ): Bitmap {
        val width = format.width
        val height = format.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw Background
        drawBackground(context, canvas, width, height, style, format)

        // Draw Content Card / Framing
        drawCardContent(canvas, width, height, hijriDate, gregorianDate, occasion, style, format, customTitle, showUrdu)

        return bitmap
    }

    private fun drawBackground(
        context: Context,
        canvas: Canvas,
        width: Int,
        height: Int,
        style: OverlayTemplateStyle,
        format: OverlayFormat
    ) {
        if (style.isTransparentBg || format.isVideoSticker) {
            // Keep transparent for video stickers, or draw subtle transparent rounded container
            canvas.drawColor(Color.TRANSPARENT)
            return
        }

        // Draw Themed Backgrounds for Story / Square formats
        when (style) {
            OverlayTemplateStyle.EMERALD_GOLD -> {
                try {
                    val bgBmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_bg_emerald_gold)
                    if (bgBmp != null) {
                        val src = Rect(0, 0, bgBmp.width, bgBmp.height)
                        val dst = Rect(0, 0, width, height)
                        canvas.drawBitmap(bgBmp, src, dst, null)
                        // Dark gradient overlay for contrast
                        val dimPaint = Paint().apply {
                            shader = LinearGradient(0f, 0f, 0f, height.toFloat(),
                                Color.argb(120, 3, 45, 35), Color.argb(220, 2, 28, 22), Shader.TileMode.CLAMP)
                        }
                        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)
                        return
                    }
                } catch (_: Exception) {}

                // Fallback gradient
                val paint = Paint().apply {
                    shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
                        Color.parseColor("#04382A"), Color.parseColor("#011B14"), Shader.TileMode.CLAMP)
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }

            OverlayTemplateStyle.NIGHT_CELESTIAL -> {
                try {
                    val bgBmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_bg_night_mosque)
                    if (bgBmp != null) {
                        val src = Rect(0, 0, bgBmp.width, bgBmp.height)
                        val dst = Rect(0, 0, width, height)
                        canvas.drawBitmap(bgBmp, src, dst, null)
                        val dimPaint = Paint().apply {
                            shader = LinearGradient(0f, 0f, 0f, height.toFloat(),
                                Color.argb(80, 5, 12, 25), Color.argb(200, 2, 5, 15), Shader.TileMode.CLAMP)
                        }
                        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)
                        return
                    }
                } catch (_: Exception) {}

                val paint = Paint().apply {
                    shader = LinearGradient(0f, 0f, 0f, height.toFloat(),
                        Color.parseColor("#0B132B"), Color.parseColor("#030712"), Shader.TileMode.CLAMP)
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }

            OverlayTemplateStyle.DESERT_SAND -> {
                try {
                    val bgBmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_bg_aesthetic_sand)
                    if (bgBmp != null) {
                        val src = Rect(0, 0, bgBmp.width, bgBmp.height)
                        val dst = Rect(0, 0, width, height)
                        canvas.drawBitmap(bgBmp, src, dst, null)
                        return
                    }
                } catch (_: Exception) {}

                val paint = Paint().apply {
                    shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
                        Color.parseColor("#FAF7F0"), Color.parseColor("#EDE4D3"), Shader.TileMode.CLAMP)
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }

            OverlayTemplateStyle.GOLDEN_CHIC -> {
                val paint = Paint().apply {
                    shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
                        Color.parseColor("#1C1508"), Color.parseColor("#0D0B05"), Shader.TileMode.CLAMP)
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }

            else -> {
                canvas.drawColor(Color.parseColor("#081210"))
            }
        }
    }

    private fun drawCardContent(
        canvas: Canvas,
        width: Int,
        height: Int,
        hijriDate: HijriDate,
        gregorianDate: GregorianDate,
        occasion: IslamicOccasion?,
        templateStyle: OverlayTemplateStyle,
        format: OverlayFormat,
        customTitle: String,
        showUrdu: Boolean
    ) {
        val isSticker = format.isVideoSticker
        val isDarkTheme = templateStyle.isDark

        // Card Boundaries
        val paddingHorizontal = if (isSticker) 36f else 64f
        val cardWidth = width - (paddingHorizontal * 2)

        val cardRect = if (isSticker) {
            RectF(paddingHorizontal, 30f, width - paddingHorizontal, height - 30f)
        } else if (format == OverlayFormat.STORY_REEL_9_16) {
            // Centered floating card on story canvas
            val cardHeight = 760f
            val top = (height - cardHeight) / 2f
            RectF(paddingHorizontal, top, width - paddingHorizontal, top + cardHeight)
        } else {
            // Square Card
            val cardHeight = 700f
            val top = (height - cardHeight) / 2f
            RectF(paddingHorizontal, top, width - paddingHorizontal, top + cardHeight)
        }

        // Draw Card Background
        val cardBgPaint = Paint().apply {
            isAntiAlias = true
            color = when {
                templateStyle == OverlayTemplateStyle.TRANSPARENT_OVERLAY -> Color.argb(195, 10, 20, 18) // Semi-transparent glass for sticker
                templateStyle == OverlayTemplateStyle.DESERT_SAND -> Color.argb(220, 255, 252, 247)
                templateStyle == OverlayTemplateStyle.MINIMAL_BADGE -> Color.argb(215, 14, 25, 22)
                isDarkTheme -> Color.argb(225, 6, 26, 20)
                else -> Color.argb(240, 255, 255, 255)
            }
        }
        val cornerRadius = if (isSticker) 42f else 52f
        canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, cardBgPaint)

        // Draw Gold / Emerald Border Rim
        val borderPaint = Paint().apply {
            isAntiAlias = true
            this.style = Paint.Style.STROKE
            strokeWidth = if (isSticker) 4f else 5f
            color = when {
                templateStyle == OverlayTemplateStyle.DESERT_SAND -> Color.parseColor("#C49A6C")
                templateStyle == OverlayTemplateStyle.MINIMAL_BADGE -> Color.argb(160, 212, 175, 55)
                else -> Color.parseColor("#E5C365") // Elegant metallic gold
            }
        }
        canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, borderPaint)

        // Text Colors
        val goldColor = Color.parseColor("#F5C344")
        val primaryTextColor = if (templateStyle == OverlayTemplateStyle.DESERT_SAND) Color.parseColor("#2C2416") else Color.parseColor("#FFFFFF")
        val secondaryTextColor = if (templateStyle == OverlayTemplateStyle.DESERT_SAND) Color.parseColor("#6B5B43") else Color.parseColor("#D1E7DD")
        val accentGoldColor = if (templateStyle == OverlayTemplateStyle.DESERT_SAND) Color.parseColor("#A67C1E") else goldColor

        val centerX = width / 2f

        // Dynamic vertical layout inside the card
        var currentY = cardRect.top + if (isSticker) 60f else 85f

        // 1. Top Bar: Bismillah / Crescent / Custom Watermark Title
        val topTitleText = if (customTitle.isNotBlank()) customTitle.trim() else "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
        val topPaint = Paint().apply {
            isAntiAlias = true
            color = accentGoldColor
            textSize = if (isSticker) 34f else 38f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(topTitleText, centerX, currentY, topPaint)
        currentY += if (isSticker) 50f else 65f

        // 2. Main Hijri Date (Hero Display)
        val hijriDateText = if (showUrdu) hijriDate.formattedUr else "${hijriDate.day} ${hijriDate.monthNameEn} ${hijriDate.year} AH"
        val hijriPaint = Paint().apply {
            isAntiAlias = true
            color = primaryTextColor
            textSize = if (isSticker) 64f else 78f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            // Subtle shadow for legibility over video
            setShadowLayer(8f, 0f, 3f, Color.argb(160, 0, 0, 0))
        }
        canvas.drawText(hijriDateText, centerX, currentY, hijriPaint)
        currentY += if (isSticker) 54f else 68f

        // Arabic Month Script Calligraphy
        val arabicDateText = "${hijriDate.day} ${hijriDate.monthNameAr} ${hijriDate.year} هـ"
        val arabicPaint = Paint().apply {
            isAntiAlias = true
            color = accentGoldColor
            textSize = if (isSticker) 42f else 50f
            typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(arabicDateText, centerX, currentY, arabicPaint)
        currentY += if (isSticker) 60f else 78f

        // 3. Divider Line with decorative diamond
        val dividerPaint = Paint().apply {
            isAntiAlias = true
            color = Color.argb(100, 229, 195, 101)
            strokeWidth = 2f
        }
        val dividerMargin = if (isSticker) 180f else 220f
        canvas.drawLine(centerX - dividerMargin, currentY, centerX + dividerMargin, currentY, dividerPaint)
        // Diamond in center
        val diamondPaint = Paint().apply {
            isAntiAlias = true
            color = accentGoldColor
        }
        canvas.drawCircle(centerX, currentY, 6f, diamondPaint)
        currentY += if (isSticker) 54f else 70f

        // 4. Gregorian Date & Day of Week
        val dayOfWeekText = if (showUrdu) gregorianDate.dayOfWeekNameUr else gregorianDate.dayOfWeekNameEn
        val gregorianDateText = if (showUrdu) {
            "$dayOfWeekText  •  ${gregorianDate.day} ${gregorianDate.monthNameUr} ${gregorianDate.year}ء"
        } else {
            "${gregorianDate.dayOfWeekNameEn.uppercase()}  •  ${gregorianDate.day} ${gregorianDate.monthNameEn} ${gregorianDate.year}"
        }

        val gregPaint = Paint().apply {
            isAntiAlias = true
            color = secondaryTextColor
            textSize = if (isSticker) 38f else 44f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(gregorianDateText, centerX, currentY, gregPaint)
        currentY += if (isSticker) 50f else 65f

        // 5. Special Occasion / Blessing Tag (e.g. Jumu'ah Mubarak, Ramadan Mubarak, etc.)
        val occasionLabel = occasion?.let {
            if (showUrdu) it.titleUr else it.titleEn
        } ?: if (gregorianDate.isFriday) (if (showUrdu) "جمعۃ المبارک" else "Jumu'ah Mubarak") else null

        if (occasionLabel != null) {
            val tagText = "✨  $occasionLabel  ✨"
            val tagPaint = Paint().apply {
                isAntiAlias = true
                color = accentGoldColor
                textSize = if (isSticker) 34f else 40f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(tagText, centerX, currentY, tagPaint)
        }
    }

    /**
     * Saves bitmap to device Pictures / MediaStore gallery (Android 10+ scoped storage compliant)
     */
    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, fileNamePrefix: String = "HijriDate"): Uri? {
        val fileName = "${fileNamePrefix}_${System.currentTimeMillis()}.png"
        var uri: Uri? = null
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HijriDateStudio")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    outputStream = resolver.openOutputStream(uri)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                }
            } else {
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(picturesDir, "HijriDateStudio")
                if (!appDir.exists()) appDir.mkdirs()
                val imageFile = File(appDir, fileName)
                outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                uri = Uri.fromFile(imageFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            outputStream?.close()
        }

        return uri
    }

    /**
     * Shares the rendered bitmap via system Android Share Sheet
     */
    fun shareBitmap(context: Context, bitmap: Bitmap, title: String = "Islamic Date Video Overlay") {
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "islamic_date_overlay.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            if (contentUri != null) {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                    putExtra(Intent.EXTRA_STREAM, contentUri)
                    putExtra(Intent.EXTRA_SUBJECT, title)
                    putExtra(Intent.EXTRA_TEXT, "Created with Hijri Date Studio for Video Creators 🌙")
                    type = "image/png"
                }
                val chooser = Intent.createChooser(shareIntent, "Share Video Date Overlay")
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
