package ru.dzera.test.ktpostman
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import ru.dzera.test.ktpostman.controller.ComponentHelper
import ru.dzera.test.ktpostman.controller.MainController
import ru.dzera.test.ktpostman.inject.Autowire
import ru.dzera.test.ktpostman.service.ConfigurationService


class MainApp : Application() {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            // (MainApp::class.java, args) array will be passed as vararg later
            launch(MainApp::class.java)
        }
    }

    private val controller = Autowire.autowire(MainController::class.java)
    private val configuration = Autowire.autowire(ConfigurationService::class.java)

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) {
            configuration.close()
            return
        }
        val fxmlLoader = FXMLLoader( MainApp::class.java.getResource("/views/Master.fxml"))
        fxmlLoader.setController(controller)
        val component : Parent = fxmlLoader.load()

        val scene = Scene( component )
        configuration.setSize(primaryStage)
        scene.stylesheets.add("css/stylesheet.css");

        primaryStage.setOnCloseRequest {
            Platform.runLater(controller.closeEvent())
        }

        primaryStage.scene = scene
        primaryStage.onShown = EventHandler { controller.refresh() }
        primaryStage.show()

        ComponentHelper.getInstance().primaryStage = primaryStage
        ComponentHelper.getInstance().workingPane = controller.workingPane
        controller.init()
    }
}