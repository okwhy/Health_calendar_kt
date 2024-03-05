package com.sus.calendar.dtos

class PressureValue(@kotlin.jvm.JvmField var high: Int, @kotlin.jvm.JvmField var low: Int) {
    override fun toString(): String {
        return "$high/$low"
    }
}
