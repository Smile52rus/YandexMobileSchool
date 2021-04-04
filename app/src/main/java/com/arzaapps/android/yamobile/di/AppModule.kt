package com.arzaapps.android.yamobile.di

import android.content.Context
import com.arzaapps.android.yamobile.data.source.ActionsRepository
import com.arzaapps.android.yamobile.data.source.local.ActionDao
import com.arzaapps.android.yamobile.data.source.local.AppDataBase
import com.arzaapps.android.yamobile.data.source.remote.ActionsRemoteDataSource
import com.arzaapps.android.yamobile.data.source.remote.retrofit.ActionService
import com.arzaapps.android.yamobile.utils.Contacts
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Contacts.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideActionService(retrofit: Retrofit): ActionService =
        retrofit.create(ActionService::class.java)

    @Singleton
    @Provides
    fun provideActionRemoteDataSource(actionService: ActionService) =
        ActionsRemoteDataSource(actionService)

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext appContext: Context) =
        AppDataBase.getDataBase(appContext)

    @Singleton
    @Provides
    fun provideActionDao(db: AppDataBase) = db.actionDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: ActionsRemoteDataSource,
        localDataSource: ActionDao
    ) = ActionsRepository(remoteDataSource, localDataSource)
}