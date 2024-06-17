package ru.dzera.test.ktpostman.service

import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.controller.ConfigurationController
import ru.dzera.test.ktpostman.controller.IController
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

class ConfigurationService : IService {
    companion object {
        val LOG = LoggerFactory.getLogger(ConfigurationService::class.java)
    }

    private val configLocation: Path
    private val configuration: Properties

    init {
        configLocation = retrieveLocation()
        configuration = retrieveConfig()
    }

    var appWidth: Double
        get() = configuration.getProperty("main.app.initial.width", "926.0").toDouble()
        set(value) {
            configuration.setProperty("main.app.initial.width", value.toString())
        }
    var appHeight: Double
        get() = configuration.getProperty("main.app.initial.height", "737.0").toDouble()
        set(value) {
            configuration.setProperty("main.app.initial.height", value.toString())
        }

    var fileDirectory: String
        get() = configuration.getProperty("main.app.initial.directory", "")
        set(value) {
            configuration.setProperty("main.app.initial.directory", value)
        }

    var urlValue: String?
        get() = configuration.getProperty("main.app.initial.url", null)
        set(value) {
            configuration.setProperty("main.app.initial.url", value)
        }

    var parameterColumnWidthProperty = SimpleDoubleProperty(parameterColumnWidth)
    var parameterColumnWidth: Double
        get() = configuration.getProperty("main.app.postman.parameter.first.column", "40.0").toDouble()
        set(value) {
            configuration.setProperty("main.app.postman.parameter.first.column", value.toString())
        }

    var headerColumnWidthProperty = SimpleDoubleProperty(headerColumnWidth)
    var headerColumnWidth: Double
        get() = configuration.getProperty("main.app.postman.header.first.column", "40.0").toDouble()
        set(value) {
            configuration.setProperty("main.app.postman.header.first.column", value.toString())
        }

    var cookieColumnWidthProperty = SimpleDoubleProperty(cookieColumnWidth)
    var cookieColumnWidth: Double
        get() = configuration.getProperty("main.app.postman.cookie.first.column", "40.0").toDouble()
        set(value) {
            configuration.setProperty("main.app.postman.cookie.first.column", value.toString())
        }

    private fun retrieveLocation(): Path {
        LOG.info("create")
        return Paths.get(System.getProperty("user.home"), ".ktpostman", "config.properties")
    }

    private fun retrieveConfig(): Properties {
        LOG.debug("retrieve config from ${configLocation}")
        if (!Files.exists(configLocation)) {
            if (!Files.exists(configLocation.parent)) {
                Files.createDirectories(configLocation.parent)
            }
            LOG.debug("create initial config instance")
            ConfigurationService::class.java
                .getResourceAsStream("/config.properties").use { input ->
                    FileOutputStream(configLocation.toFile()).use { output ->
                        input.copyTo(output)
                    }
                }
        }
        val conf = Properties()
        Files.newBufferedReader(configLocation).use { reader ->
            conf.load(reader)
        }
        return conf
    }

    fun close() {
        IController.LOG.info("exit")
        FileOutputStream(configLocation.toFile()).use { output ->
            ConfigurationController.LOG.info("exit with configuration")
            configuration.store(output, "")
        }
        Platform.exit()
        exitProcess(0)
    }

    fun setSize(primaryStage: Stage) {
        primaryStage.width = this.appWidth
        primaryStage.maxWidth = 1000.0

        primaryStage.height = this.appHeight
        primaryStage.maxHeight = 800.0

        primaryStage.widthProperty().addListener { _: ObservableValue<out Number>,
                                                   _: Number, newValue: Number ->
            this.appWidth = newValue.toDouble()
        }
        primaryStage.heightProperty().addListener { _: ObservableValue<out Number>,
                                                    _: Number, newValue: Number ->
            this.appHeight = newValue.toDouble()
        }
    }

    fun help(): String {
        return ConfigurationService::class.java.getResource("/postman.history")!!.readText()
    }

    val bindParameterColumnWidth : ChangeListener<Number> = ChangeListener() {
            _: ObservableValue<out Number>,
                                     _: Number, newValue: Number ->
        this.parameterColumnWidth = newValue.toDouble()
    }

    val bindHeaderColumnWidth : ChangeListener<Number> = ChangeListener() {
            _: ObservableValue<out Number>,
                                     _: Number, newValue: Number ->
        this.headerColumnWidth = newValue.toDouble()
    }

    val bindCookieColumnWidth : ChangeListener<Number> = ChangeListener() {
            _: ObservableValue<out Number>,
                                     _: Number, newValue: Number ->
        this.cookieColumnWidth = newValue.toDouble()
    }
}