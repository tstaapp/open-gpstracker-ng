package nl.sogeti.android.gpstracker.v2.sharedwear.util

import android.content.Context
import android.content.res.Resources
import android.support.annotation.StringRes
import android.text.format.DateFormat
import nl.sogeti.android.gpstracker.v2.sharedwear.R
import java.util.*

class StatisticsFormatting(private val locale: Locale, private val timeSpanUtil: TimeSpanCalculator) {

    fun convertMetersToDistance(context: Context, meters: Float): String {
        val distance: String
        distance = when {
            meters >= 100000 -> {
                val convert = context.resources.getFloat(R.string.m_to_big_distance)
                context.getString(R.string.format_big_100_kilometer).format(locale, meters / convert)
            }
            meters >= 1000 -> {
                val convert = context.resources.getFloat(R.string.m_to_big_distance)
                context.getString(R.string.format_big_kilometer).format(locale, meters / convert)
            }
            meters >= 100 -> {
                val convert = context.resources.getFloat(R.string.m_to_small_distance)
                context.getString(R.string.format_small_100_meters).format(locale, meters / convert)
            }
            else -> {
                val convert = context.resources.getFloat(R.string.m_to_small_distance)
                context.getString(R.string.format_small_meters).format(locale, meters / convert)
            }
        }
        return distance
    }

    fun convertTimestampToStart(context: Context, timestamp: Long?): String {
        val start: CharSequence
        if (timestamp == null || timestamp == 0L) {
            start = context.getString(R.string.empty_dash)
        } else {
            start = timeSpanUtil.getRelativeTimeSpanString(timestamp)
        }

        return start.toString()
    }

    fun convertStartEndToDuration(context: Context, startTimestamp: Long, endTimestamp: Long): String {
        val msPerMinute = 1000L * 60L
        val msPerHour = msPerMinute * 60L
        val msPerDay = msPerHour * 24L
        val msPerSecond = 1000L
        val msDuration = endTimestamp - startTimestamp
        val days = (msDuration / msPerDay).toInt()
        val hours = ((msDuration - (days * msPerDay)) / msPerHour).toInt()
        val minutes = ((msDuration - (days * msPerDay) - (hours * msPerHour)) / msPerMinute).toInt()
        val seconds = ((msDuration - (days * msPerDay) - (hours * msPerHour) - (minutes * msPerMinute)) / msPerSecond).toInt()
        var duration: String
        if (days > 0) {
            duration = context.resources.getQuantityString(R.plurals.track_duration_days, days, days)
            if (hours > 0) {
                duration += " "
                duration += context.resources.getQuantityString(R.plurals.track_duration_hours, hours, hours)
            }
        } else if (hours > 0) {
            duration = context.resources.getQuantityString(R.plurals.track_duration_hours, hours, hours)
            if (minutes > 0) {
                duration += " "
                duration += context.resources.getQuantityString(R.plurals.track_duration_minutes, minutes, minutes)
            }
        } else if (minutes > 0) {
            duration = context.resources.getQuantityString(R.plurals.track_duration_minutes, minutes, minutes)
        } else {
            duration = context.resources.getQuantityString(R.plurals.track_duration_seconds, seconds, seconds)
        }

        return duration
    }

    fun convertMeterPerSecondsToSpeed(context: Context, meters: Float, seconds: Long): String {
        val kph = if (seconds > 0) {
            val conversion = context.resources.getFloat(R.string.mps_to_speed)
            meters / seconds * conversion
        } else {
            0.0F
        }

        val unit = context.resources.getString(R.string.speed_unit)
        return context.getString(R.string.format_speed).format(locale, kph, unit)
    }

    fun convertTimestampToDate(context: Context, startTimestamp: Long): String {
        val date = Date(startTimestamp)
        return DateFormat.getDateFormat(context).format(date)
    }

    fun convertTimestampToTime(context: Context, startTimestamp: Long): String {
        val date = Date(startTimestamp)
        return DateFormat.getTimeFormat(context).format(date)
    }


    private fun Resources.getFloat(@StringRes resourceId: Int): Float {
        val stringValue = this.getString(resourceId)
        return stringValue.toFloat()
    }
}
