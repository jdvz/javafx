package ru.dzera.test.ktpostman.controller

import org.slf4j.LoggerFactory

/**
 * configuration
 */
class ConfigurationController : IController() {
    companion object {
        val LOG = LoggerFactory.getLogger(ConfigurationController::class.java)
    }

    fun close() {
        configurationService.close()
    }

    override fun init() {
        TODO("not implemented yet")
    }
}
