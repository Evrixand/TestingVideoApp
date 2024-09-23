package com.videoapp.testingvideoapp.di

import com.videoapp.testingvideoapp.data.repository.login.LoginRepository
import com.videoapp.testingvideoapp.data.repository.login.LoginRepositoryImpl
import com.videoapp.testingvideoapp.data.repository.user.UserRepository
import com.videoapp.testingvideoapp.data.repository.user.UserRepositoryImpl
import com.videoapp.testingvideoapp.data.repository.video.SupportVideoDataRepository
import com.videoapp.testingvideoapp.data.repository.video.SupportVideoDataRepositoryImpl
import com.videoapp.testingvideoapp.data.repository.video.VideoRepository
import com.videoapp.testingvideoapp.data.repository.video.VideoRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    single<SupportVideoDataRepository> {
        SupportVideoDataRepositoryImpl(context = get())
    }
    single<UserRepository> {
        UserRepositoryImpl(context = get())
    }
    single<LoginRepository> {
        LoginRepositoryImpl(context = get(), userRepository = get())
    }
    single<VideoRepository> {
        VideoRepositoryImpl(supportVideoDataRepository = get(), userRepository = get())
    }
}
