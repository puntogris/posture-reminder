package com.puntogris.posture

import com.puntogris.posture.model.Reminder
import org.amshove.kluent.*
import org.junit.Test

class ReminderAlarmTest {

    @Test
    fun `should return past midnight in range alarm`(){
        Reminder(startTime = 500, endTime = 100).apply {
            isAlarmInRange(0) `should be`  true
            isAlarmInRange(99) `should be`  true
            isAlarmInRange(100) `should be`  false
            isAlarmInRange(101) `should be`  false
            isAlarmInRange(500) `should be`  false
            isAlarmInRange(501) `should be`  true
            isAlarmInRange(1000) `should be` true
        }
    }

    @Test
    fun `should return not past midnight in range alarm`(){
        Reminder(startTime = 100, endTime = 500).apply {
            isAlarmInRange(0) `should be`  false
            isAlarmInRange(99) `should be`  false
            isAlarmInRange(100) `should be`  true
            isAlarmInRange(101) `should be`  true
            isAlarmInRange(500) `should be`  true
            isAlarmInRange(501) `should be`  false
            isAlarmInRange(1000) `should be` false
        }
    }
}