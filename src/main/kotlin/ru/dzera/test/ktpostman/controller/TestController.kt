package ru.dzera.test.ktpostman.controller

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.slf4j.LoggerFactory

class TestController {
    companion object {
        val LOG = LoggerFactory.getLogger(TestController::class.java)
    }

    @FXML
    private var welcomeText: Label = Label("init")

    @FXML
    private fun onHelloButtonClick() {
        LOG.info("on hello button click")
        welcomeText.text = "Welcome to JavaFX Application!"
    }

    fun exit(actionEvent: ActionEvent) {
        Platform.runLater(closeEvent())
    }

    private fun closeEvent(): () -> Unit {
        return {
            LOG.info("exit")
//            configuration.close()
            Platform.exit()
            System.exit(0)
        }
    }

    fun refresh() {
        LOG.info("test controller refreshed")
    }
}