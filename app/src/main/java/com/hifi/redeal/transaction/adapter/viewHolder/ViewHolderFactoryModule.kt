package com.hifi.redeal.transaction.adapter.viewHolder

import com.hifi.redeal.transaction.adapter.viewHolder.trade.CountHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.DepositHolderFactory
import com.hifi.redeal.transaction.adapter.viewHolder.trade.SalesHolderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ViewHolderFactoryModule {
    @Singleton
    @Provides
    fun provideCountHolderFactory(): CountHolderFactory {
        return CountHolderFactory()
    }

    @Singleton
    @Provides
    fun provideDepositHolderFactory(): DepositHolderFactory {
        return DepositHolderFactory()
    }

    @Singleton
    @Provides
    fun provideSalesHolderFactory(): SalesHolderFactory {
        return SalesHolderFactory()
    }
}
