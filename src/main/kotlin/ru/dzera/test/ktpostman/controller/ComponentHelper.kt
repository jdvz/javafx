package ru.dzera.test.ktpostman.controller

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.stage.Stage

class ComponentHelper private constructor() {
    companion object {
        @Volatile var INSTANCE : ComponentHelper? = null

        fun getInstance() : ComponentHelper {
            return INSTANCE ?: initialize()
        }

        private fun initialize() : ComponentHelper {
            synchronized(this) {
                val init = ComponentHelper()
                INSTANCE = init
                return init
            }
        }
    }

    lateinit var primaryStage: Stage
    lateinit var workingPane: ScrollPane
    var currentController: IController? = null
}