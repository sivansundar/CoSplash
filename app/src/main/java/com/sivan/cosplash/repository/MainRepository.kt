package com.sivan.cosplash.repository

import com.sivan.cosplash.network.CoSplashInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val coSplashInterface: CoSplashInterface) {
}