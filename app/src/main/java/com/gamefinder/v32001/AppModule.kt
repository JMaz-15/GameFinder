package com.gamefinder.v32001

import com.gamefinder.service.GameService
import com.gamefinder.service.IGameService
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IGameService> {GameService()}
}