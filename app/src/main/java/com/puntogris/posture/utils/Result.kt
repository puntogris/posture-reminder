package com.puntogris.posture.utils

sealed class RepoResult{
    object Success: RepoResult()
    object Failure: RepoResult()
}