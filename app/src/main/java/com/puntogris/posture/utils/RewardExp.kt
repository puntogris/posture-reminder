package com.puntogris.posture.utils

sealed class RewardExp {
    object Success : RewardExp()
    object Error : RewardExp()
    object ExpLimit : RewardExp()
}