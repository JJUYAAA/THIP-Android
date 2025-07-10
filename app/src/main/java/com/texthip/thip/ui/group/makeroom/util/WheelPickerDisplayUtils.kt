package com.texthip.thip.ui.group.makeroom.util

import kotlin.math.roundToInt

object WheelPickerUtils {
    @JvmStatic
    fun getCircularIndex(index: Int, size: Int): Int {
        return ((index % size) + size) % size
    }

    @JvmStatic
    fun normalizeOffset(offset: Float, itemSpacing: Float, size: Int, circular: Boolean): Float {
        if (!circular) return offset
        val total = size * itemSpacing
        return ((offset % total) + total) % total
    }

    @JvmStatic
    fun offsetToIndex(
        offset: Float,
        itemSpacing: Float,
        size: Int,
        circular: Boolean
    ): Int {
        val normalized = if (circular) normalizeOffset(offset, itemSpacing, size, circular) else offset
        val centerIndex = (-normalized / itemSpacing).roundToInt()
        return if (circular) getCircularIndex(centerIndex, size)
        else centerIndex.coerceIn(0, size - 1)
    }
}
