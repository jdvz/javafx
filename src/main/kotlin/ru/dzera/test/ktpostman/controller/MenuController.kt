package ru.dzera.test.ktpostman.controller;

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.MainApp
import ru.dzera.test.ktpostman.inject.inject

class MenuController : IController() {
    companion object {
        val LOG = LoggerFactory.getLogger(MenuController::class.java)
    }

    private val postmanController : PostmanController = inject()
    private val configurationController : ConfigurationController = inject()

    @FXML
    val controllerMenu = MenuBar()

    @FXML
    val postmanItem = MenuItem("Postman")

    val postmanProperty = SimpleBooleanProperty(false)

    @FXML
    fun initialize() {
        val currentController = ComponentHelper.getInstance().currentController
        postmanItem.disableProperty().bind(postmanProperty)
        if (currentController != null) {
            if (currentController is PostmanController) {
                postmanItem.isDisable = true
            }
        }
    }

    override fun init() {
//        val fxmlLoader = FXMLLoader( MainApp::class.java.getResource("/views/menu/Main.fxml"))
//        fxmlLoader.setController(this)
    }

    fun initPostman(actionEvent: ActionEvent) {
        postmanController.init()
        postmanProperty.set(true)
    }

    fun configure(actionEvent: ActionEvent) {
        configurationController.init()
    }

    fun exit(actionEvent: ActionEvent) {
        Platform.runLater(closeEvent())
    }
}
