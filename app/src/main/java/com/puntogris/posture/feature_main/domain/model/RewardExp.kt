package com.puntogris.posture.feature_main.domain.model

sealed class RewardExp {
    object Success : RewardExp()
    object Error : RewardExp()
    object ExpLimit : RewardExp()
}