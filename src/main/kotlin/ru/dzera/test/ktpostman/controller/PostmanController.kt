package ru.dzera.test.ktpostman.controller

import com.novardis.test.utils.StringResponseTransformer
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.css.PseudoClass
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import javafx.util.StringConverter
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.MainApp
import ru.dzera.test.ktpostman.enums.Method
import ru.dzera.test.ktpostman.inject.Autowire
import ru.dzera.test.ktpostman.inject.inject
import ru.dzera.test.ktpostman.model.Couple
import ru.dzera.test.ktpostman.model.ExchangeProperty
import ru.dzera.test.ktpostman.model.UrlText
import ru.dzera.test.ktpostman.service.PostmanService
import ru.dzera.test.ktpostman.utils.Converter
import java.util.function.Consumer
import java.util.function.UnaryOperator

/**
 * postman operation ui
 */
class PostmanController : IController() {
    companion object {
        val LOG = LoggerFactory.getLogger(PostmanController::class.java)

        val URL_REGEX = Regex("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
        val URL_SYMBOLS_REGEX = Regex("[-a-zA-Z0-9+&@#/%?:.=~_|!,;]*")

        val URL_INITIAL_VALUE = "http://example.com"
        val SET_COOKIE_HEADER = "set-cookie"
    }

    val postmanService: PostmanService = inject()

    val httpValidator: UnaryOperator<TextFormatter.Change?> =
        UnaryOperator { t -> if (t?.controlNewText?.matches(URL_SYMBOLS_REGEX) == true) t else null }

    val urlProperty : UrlText = UrlText()
    val exchangeProperty : ExchangeProperty = ExchangeProperty()

    @FXML
    lateinit var url: TextField

    @FXML
    lateinit var methods: ChoiceBox<Method>
    var currentMethod: Method = Method.GET

    @FXML
    lateinit var submit: Button
    val urlDisabledProperty = SimpleBooleanProperty(validateUrl(postmanService.getInitialUrlValue()))

    @FXML
    lateinit var requestParameters: TabPane

    @FXML
    lateinit var addParameter: Button
    @FXML
    lateinit var removeParameter: Button
    @FXML
    lateinit var addHeader: Button
    @FXML
    lateinit var removeHeader: Button
    @FXML
    lateinit var addCookie: Button
    @FXML
    lateinit var removeCookie: Button
    @FXML
    lateinit var response: ScrollPane
    @FXML
    lateinit var body: TextArea
    @FXML
    lateinit var helpData: ScrollPane

    @FXML
    lateinit var parameterView: TableView<Couple>
    @FXML
    lateinit var headerView: TableView<Couple>
    @FXML
    lateinit var cookieView: TableView<Couple>

    @FXML
    fun initialize() {
        LOG.debug("init Postman controller")
        urlProperty.setUrlText(postmanService.getInitialUrlValue())
        url.textProperty().bindBidirectional(urlProperty.urlTextProperty())
        url.prefWidthProperty().bind(ComponentHelper.getInstance().primaryStage.widthProperty()
            .subtract(methods.widthProperty())
            .subtract(submit.widthProperty())
            .subtract(28.0) // insets
        )
        url.focusedProperty()
            .addListener { _: ObservableValue<out Boolean>, _: Boolean, newValue: Boolean ->
                if (!newValue) { // focus lost
                    if (validateUrl(this.url.text)) {
                        LOG.info("correct url stored from ${urlProperty.getUrlText()}")
                        configurationService.urlValue = urlProperty.getUrlText()
                        urlDisabledProperty.value = false
                        url.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false)
                        url.styleClass.remove("error1")
                    } else {
                        LOG.info("incorrect url resolved ${urlProperty.getUrlText()}")
                        urlDisabledProperty.value = true
                        url.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true)
                        url.styleClass.add("error1")
                        url.style += " -fx-color: cyan"
                        val alert = Alert(AlertType.WARNING)
                        alert.title = "Incorrect url"
                        alert.headerText = "Please check url"
                        alert.contentText = "Invalid url ${urlProperty.getUrlText()}"

                        alert.showAndWait()
                        urlProperty.setUrlText(configurationService.urlValue ?: postmanService.getInitialUrlValue())
                    }
                }
            }
        methods.items.addAll(Method.values().asList())
        methods.value = currentMethod
        val methodConverter = MethodConverter()
        methods.converter = methodConverter
        submit.disableProperty().bind(urlDisabledProperty)

        submit.textProperty().bindBidirectional(methods.valueProperty(), methodConverter)
        submit.onAction = EventHandler<ActionEvent> {
            LOG.info("click on buttom submit: {}", urlProperty)
            this.send()
        }

        val responseContent = Text()
        responseContent.textProperty().bind(exchangeProperty.getResponseBodyProperty())
        response.content = responseContent
        response.pannableProperty().set(true)
        response.fitToWidthProperty().set(true)
        response.prefWidthProperty().bind(parameterView.widthProperty())

        requestParameters.prefWidth = 300.0
        requestParameters.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        initializeParameterTab(parameterView, addParameter, removeParameter, exchangeProperty.parameters)
        { column ->
            column.prefWidthProperty().bind(configurationService.parameterColumnWidthProperty)
            column.widthProperty().addListener(configurationService.bindParameterColumnWidth)
        }
        initializeParameterTab(headerView, addHeader, removeHeader, exchangeProperty.headers)
        { column ->
            column.prefWidthProperty().bind(configurationService.headerColumnWidthProperty)
            column.widthProperty().addListener(configurationService.bindHeaderColumnWidth)
        }
        initializeParameterTab(cookieView, addCookie, removeCookie, exchangeProperty.cookies)
        { column ->
            column.prefWidthProperty().bind(configurationService.cookieColumnWidthProperty)
            column.widthProperty().addListener(configurationService.bindCookieColumnWidth)
        }
        body.textProperty().bindBidirectional(exchangeProperty.bodyProperty())
        body.isEditable = true

        val helpText = Text(configurationService.help())
        helpData.content = helpText
        helpData.prefWidthProperty().bind(parameterView.widthProperty())
    }

    /**
     * initialize tabs
     */
    private fun initializeParameterTab(
        tableView: TableView<Couple>, addButton: Button, removeButton: Button, params: MutableList<Couple>,
        bindColumnWidth: Consumer<TableColumn<Couple, String>>
    ) {
        tableView.prefWidthProperty().bind(
            ComponentHelper.getInstance().primaryStage.widthProperty()
            .subtract(56.0))
        tableView.maxWidthProperty().bind(
            ComponentHelper.getInstance().primaryStage.widthProperty()
            .subtract(28.0)) // insets
        tableView.items.addAll(params)
        tableView.isEditable = true
        tableView.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        val selectionModel = tableView.selectionModel
        selectionModel.selectionMode = SelectionMode.MULTIPLE

        addButton.onAction = EventHandler {
            LOG.info("adding new")
            tableView.items.add(Couple())
        }
        removeButton.onAction = EventHandler {
            LOG.info("removing selected")
            tableView.items.removeAll(selectionModel.selectedItems)
        }

        val tableNameColumn = TableColumn<Couple, String>("name")
        val tableContentColumn = TableColumn<Couple, String>("value")

        tableNameColumn.editableProperty().value = true
        tableNameColumn.cellValueFactory = PropertyValueFactory("name")
        tableNameColumn.cellFactory = TextFieldTableCell.forTableColumn()
        tableNameColumn.minWidth = 100.0
        // why 5000
        bindColumnWidth.accept(tableContentColumn)

        tableContentColumn.editableProperty().value = true
        tableContentColumn.cellValueFactory = PropertyValueFactory("content")
        tableContentColumn.cellFactory = TextFieldTableCell.forTableColumn()

        tableView.columns.addAll(tableNameColumn, tableContentColumn)

        val tablePlaceholderLabel = Label("no rows to display")
        tablePlaceholderLabel.styleClass.add("table-placeholder")
        tableView.placeholder = tablePlaceholderLabel
    }

    override fun init() {
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("/views/postman/Postman.fxml"))
        val component = fxmlLoader.load<Pane>()
        val workingPane = ComponentHelper.getInstance().workingPane
        workingPane.pannableProperty().value = true
        workingPane.content = component
        ComponentHelper.getInstance().primaryStage.title = "KtPostman"
    }

    class MethodConverter : StringConverter<Method>() {
        override fun toString(method: Method?): String {
            return method?.name ?: "-"
        }

        override fun fromString(methodName: String?): Method {
            return Method.valueOf(methodName!!)
        }

    }

    fun send() {
        val method = methods.value
        LOG.info("Send request to ${urlProperty.getUrlText()}")
        exchangeProperty.parameters.removeAll(Couple::isEmpty)
        exchangeProperty.headers.removeAll(Couple::isEmpty)
        exchangeProperty.cookies.removeAll(Couple::isEmpty)

        // temp
        exchangeProperty.setBody("</>")

        val response = postmanService.send(method, urlProperty.getUrlText(), exchangeProperty)

        if (response != null) {
            exchangeProperty.cookies.addAll(response.headers.filter { it.name.equals(SET_COOKIE_HEADER, true) }
                .map { Couple.fromArray(it.value.split("=")) })
            exchangeProperty.headers.addAll(response.headers.filter { !it.name.equals(SET_COOKIE_HEADER, true) }
                .map { Couple(it.name, it.value) })
            exchangeProperty.setResponseBody(Converter.transformResponse(Autowire.autowire(StringResponseTransformer::class.java), response)!!)
        }
    }

    fun validateUrl(text: String): Boolean {
        return text.matches(URL_REGEX)
    }
}
