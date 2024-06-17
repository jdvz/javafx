package ru.dzera.test.ktpostman.controller

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.MainApp

/**
 * stub
 */
class AboutController : IController() {
    companion object {
        val LOG = LoggerFactory.getLogger(PostmanController::class.java)
    }

    override fun init() {
        LOG.info("About controller")
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("/views/About.fxml"))
        val component = fxmlLoader.load<Pane>()
        val workingPane = ComponentHelper.getInstance().workingPane
        workingPane.pannableProperty().value = true
        workingPane.content = component
        ComponentHelper.getInstance().primaryStage.title = "KtAbout"
    }
}