package ru.dzera.test.ktpostman.service

import org.junit.jupiter.api.Test

internal class ConfigurationServiceTest {
    private val configurationService : ConfigurationService = ConfigurationService()
    @Test
    fun close() {
//        val configuration : Properties = mockk()
        configurationService.close()
//        verify(exactly = 1) { configuration.store(any(FileOutputStream::class), eq("")) }
    }
}