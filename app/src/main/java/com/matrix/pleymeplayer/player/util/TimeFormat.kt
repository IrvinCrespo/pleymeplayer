package com.matrix.pleymeplayer.player.util

object TimeFormat {

    fun timeFromMilis(time:Long) : TimeObject{

        val hours = (time / (1000 * 60 * 60)).toInt()
        val minutes = ((time % (1000 * 60 * 60)) / (1000 * 60)).toInt()
        val seconds = ((time % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()
        return TimeObject(seconds,minutes,hours)
    }

}

data class TimeObject(
    val seconds:Int,
    val minutes:Int,
    val hours:Int
)