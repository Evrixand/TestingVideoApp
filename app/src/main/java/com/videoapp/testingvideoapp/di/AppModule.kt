package com.videoapp.testingvideoapp.di

import com.videoapp.testingvideoapp.ui.feed.FeedViewModel
import com.videoapp.testingvideoapp.ui.login.LoginViewModel
import com.videoapp.testingvideoapp.ui.maps.MapViewModel
import com.videoapp.testingvideoapp.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<FeedViewModel> {
        FeedViewModel(videoRepository = get(), userRepository = get())
    }
    viewModel<LoginViewModel> {
        LoginViewModel(loginRepository = get())
    }
    viewModel<ProfileViewModel> {
        ProfileViewModel(loginRepository = get(), videoRepository = get())
    }
    viewModel<MapViewModel> {
        MapViewModel(videoRepository = get())
    }
}