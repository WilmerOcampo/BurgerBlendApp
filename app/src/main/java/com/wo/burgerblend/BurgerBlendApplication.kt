package com.wo.burgerblend

import android.app.Application
import android.content.Context

class BurgerBlendApplication: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: BurgerBlendApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Inicializar cosas globales si es necesario
    }
}
