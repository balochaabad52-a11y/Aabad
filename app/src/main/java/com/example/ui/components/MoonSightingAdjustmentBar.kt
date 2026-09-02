package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun MoonSightingAdjustmentBar(
    currentOffset: Int,
    onOffsetChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = true
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = if (isUrdu) "رویتِ ہلال ایڈجسٹمنٹ" else "Moon Sighting Calibration",
                        tint = Emerald700,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isUrdu) "رویتِ ہلال ایڈجسٹمنٹ" else "Moon Sighting Calibration",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (currentOffset != 0) Emerald100 else Slate100,
                    border = BorderStroke(1.dp, if (currentOffset != 0) Emerald600.copy(alpha = 0.3f) else Slate200)
                ) {
                    Text(
                        text = when {
                            currentOffset > 0 -> if (isUrdu) "+$currentOffset دن" else "+$currentOffset Day"
                            currentOffset < 0 -> if (isUrdu) "$currentOffset دن" else "$currentOffset Day"
                            else -> if (isUrdu) "معمول (0)" else "Standard (0)"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (currentOffset != 0) Emerald800 else Slate700,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isUrdu) {
                    "اگر آپ کے علاقے میں چاند کی تاریخ مختلف ہو تو یہاں سے ایڈجسٹ کریں"
                } else {
                    "Adjust Hijri date if your regional moon sighting differs by a day"
                },
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Slate500
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Offset Chips: -2, -1, 0, +1, +2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val offsets = listOf(-2, -1, 0, 1, 2)
                offsets.forEach { offset ->
                    val isSelected = currentOffset == offset
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Emerald600 else Slate100,
                        border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOffsetChanged(offset) }
                            .testTag("offset_chip_$offset")
                    ) {
                        Text(
                            text = when {
                                offset > 0 -> "+$offset"
                                offset < 0 -> "$offset"
                                else -> if (isUrdu) "0 (خودکار)" else "0 (Auto)"
                            },
                            modifier = Modifier.padding(vertical = 7.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = if (isSelected) Color.White else Slate700
                        )
                    }
                }
            }
        }
    }
}
