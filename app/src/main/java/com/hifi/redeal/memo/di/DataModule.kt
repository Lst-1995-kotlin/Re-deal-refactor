package com.hifi.redeal.memo.di

import com.hifi.redeal.memo.repository.OfflinePhotoMemosRepository
import com.hifi.redeal.memo.repository.OfflineRecordMemosRepository
import com.hifi.redeal.memo.repository.PhotoMemosRepository
import com.hifi.redeal.memo.repository.RecordMemosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsPhotoMemoRepository(
        photoMemosRepository: OfflinePhotoMemosRepository
    ): PhotoMemosRepository

    @Binds
    internal abstract fun bindsRecordMemoRepository(
        recordMemosRepository: OfflineRecordMemosRepository
    ): RecordMemosRepository
}