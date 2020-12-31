package com.puntogris.posture.model

sealed class RepoResult{
    object Success: RepoResult()
    object Failure: RepoResult()
}