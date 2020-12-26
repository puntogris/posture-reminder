package com.puntogris.posture.data

interface IRepository {
    fun sendReportToFirestore(message: String)
}