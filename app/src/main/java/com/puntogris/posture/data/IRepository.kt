package com.puntogris.posture.data

import com.puntogris.posture.model.RepoResult

interface IRepository {
    suspend fun sendReportToFirestore(message: String): RepoResult
}