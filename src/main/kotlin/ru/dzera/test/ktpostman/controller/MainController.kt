package ru.dzera.test.ktpostman.controller

import javafx.fxml.FXML
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory

class MainController : IController() {
    companion object {
        val LOG = LoggerFactory.getLogger(MainController::class.java)
    }

    @FXML
    lateinit var workingPane : ScrollPane

    @FXML
    override fun init() {
        LOG.info("Start main controller")
    }

    fun refresh() {
        LOG.info("test")
        // TODO not implemented yet
    }
}