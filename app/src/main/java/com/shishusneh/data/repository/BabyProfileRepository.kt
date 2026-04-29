package com.shishusneh.data.repository

import com.shishusneh.data.local.dao.BabyProfileDao
import com.shishusneh.data.local.entity.BabyProfileEntity
import com.shishusneh.domain.model.BabyProfile
import com.shishusneh.domain.model.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BabyProfileRepository @Inject constructor(
    private val dao: BabyProfileDao
) {
    fun getActiveProfile(): Flow<BabyProfile?> =
        dao.getActiveProfile().map { it?.toDomain() }

    suspend fun getActiveProfileOnce(): BabyProfile? =
        dao.getActiveProfileOnce()?.toDomain()

    suspend fun createProfile(profile: BabyProfile): Long =
        dao.insertProfile(profile.toEntity())

    suspend fun updateProfile(profile: BabyProfile) =
        dao.updateProfile(profile.toEntity())

    suspend fun hasProfile(): Boolean = dao.getProfileCount() > 0

    suspend fun deleteAll() = dao.deleteAll()

    private fun BabyProfileEntity.toDomain() = BabyProfile(
        id = id, name = name, motherName = motherName,
        dateOfBirth = dateOfBirth, birthWeightKg = birthWeightKg,
        gender = Gender.valueOf(gender), photoUri = photoUri,
        createdAt = createdAt
    )

    private fun BabyProfile.toEntity() = BabyProfileEntity(
        id = id, name = name, motherName = motherName,
        dateOfBirth = dateOfBirth, birthWeightKg = birthWeightKg,
        gender = gender.name, photoUri = photoUri,
        createdAt = createdAt
    )
}
