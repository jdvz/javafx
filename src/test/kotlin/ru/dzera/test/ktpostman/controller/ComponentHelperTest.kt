package ru.dzera.test.ktpostman.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class ComponentHelperTest {
    companion object {
        val LOG = LoggerFactory.getLogger(ComponentHelperTest::class.java)
    }

    @Test
    fun testExists() {
        LOG.info("start test")
        assertNotNull(ComponentHelper.getInstance())
    }

    /**
     * just for fun
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSingletonInstance() {
        // Create multiple threads to access the singleton instance concurrently
        val thread1 = Thread {
            val instance1: ComponentHelper = ComponentHelper.getInstance()
            try {
                Thread.sleep(100) // Simulate some work in the thread
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val instance2: ComponentHelper = ComponentHelper.getInstance()

            // Assert that both instances obtained in the same thread are the same
            assertEquals(instance1, instance2)
        }
        val thread2 = Thread {
            val instance3: ComponentHelper = ComponentHelper.getInstance()
            try {
                Thread.sleep(100) // Simulate some work in the thread
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val instance4: ComponentHelper = ComponentHelper.getInstance()

            // Assert that both instances obtained in the same thread are the same
            assertEquals(instance3, instance4)
        }

        // Start the threads
        thread1.start()
        thread2.start()

        // Wait for threads to finish
        thread1.join()
        thread2.join()
    }
}