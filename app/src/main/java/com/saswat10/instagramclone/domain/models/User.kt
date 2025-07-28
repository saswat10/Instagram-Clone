package com.saswat10.instagramclone.domain.models

import com.google.firebase.Timestamp
import com.google.type.DateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

data class User(
    val userId: String = "",
    val email: String = "",
    val username: String ="",
    val name: String = "",
    val bio: String = "",
    val profilePic: String = "",
    val posts: Int = 0,
    val friends: Int = 0,
    val createdAt: LocalDateTime? = null
){

    fun getCreatedAt(): String{
        return createdAt?.let { dateTime ->
            val now = LocalDateTime.now()
            val minutes = ChronoUnit.MINUTES.between(dateTime, now)
            val hours = ChronoUnit.HOURS.between(dateTime, now)
            val days = ChronoUnit.DAYS.between(dateTime, now)
            val months = ChronoUnit.MONTHS.between(dateTime, now)
            val years = ChronoUnit.YEARS.between(dateTime, now)

            when {
                minutes < 1 -> "Joined recently"
                minutes < 60 -> "Joined $minutes minute${if (minutes > 1) "s" else ""} ago"
                hours < 24 -> "Joined $hours hour${if (hours > 1) "s" else ""} ago"
                days < 30 -> "Joined $days day${if (days > 1) "s" else ""} ago"
                months < 12 -> "Joined $months month${if (months > 1) "s" else ""} ago"
                else -> "Joined $years year${if (years > 1) "s" else ""} ago"
            }
        } ?: "Not available"
    }
}