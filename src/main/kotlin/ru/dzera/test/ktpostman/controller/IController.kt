package ru.dzera.test.ktpostman.controller

import javafx.scene.Parent
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.inject.Bean
import ru.dzera.test.ktpostman.inject.inject
import ru.dzera.test.ktpostman.service.ConfigurationService

sealed class IController : Bean {
    companion object {
        val LOG = LoggerFactory.getLogger(IController::class.java)
    }

    protected lateinit var stage: Parent

    protected val configurationService : ConfigurationService = inject()

    abstract fun init()

    fun closeEvent(): () -> Unit {
        return {
            configurationService.close()
        }
    }
}