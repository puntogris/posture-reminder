package com.puntogris.posture.domain.model

sealed class RewardExp {
    object Success : RewardExp()
    object Error : RewardExp()
    object ExpLimit : RewardExp()
}
