package ru.dzera.test.ktpostman.controller;

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.inject.inject

/**
 * menu ui
 */
class MenuController : IController() {
    companion object {
        val LOG = LoggerFactory.getLogger(MenuController::class.java)
    }

    private val postmanController : PostmanController = inject()
    private val aboutController : AboutController = inject()
    private val configurationController : ConfigurationController = inject()

    @FXML
    lateinit var controllerMenu : MenuBar

    @FXML
    lateinit var postmanItem : MenuItem
    lateinit var aboutItem : MenuItem

    val aboutProperty = SimpleBooleanProperty(false)
    val postmanProperty = SimpleBooleanProperty(false)

    @FXML
    fun initialize() {
        val currentController = ComponentHelper.getInstance().currentController

        // it must be simplier
        postmanItem.disableProperty().bind(postmanProperty)
        aboutItem.disableProperty().bind(aboutProperty)
        aboutItem.onAction = EventHandler<ActionEvent>() {
          initAbout(it)
        }

        if (currentController != null) {
            if (currentController is PostmanController) {
                aboutItem.isDisable = false
                postmanItem.isDisable = true
            } else if (currentController is AboutController) {
                aboutItem.isDisable = true
                postmanItem.isDisable = false
            }
        }
    }

    override fun init() {
        LOG.info("init menu")
    }

    fun initAbout(actionEvent: ActionEvent) {
        aboutController.init()
        aboutProperty.set(true)
        postmanProperty.set(false)
    }

    fun initPostman(actionEvent: ActionEvent) {
        postmanController.init()
        postmanProperty.set(true)
        aboutProperty.set(false)
    }

    fun configure(actionEvent: ActionEvent) {
        configurationController.init()
    }

    fun exit(actionEvent: ActionEvent) {
        Platform.runLater(closeEvent())
    }
}
