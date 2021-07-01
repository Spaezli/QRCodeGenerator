
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.Stage
import javafx.util.*
import tornadofx.*
import javafx.event.*

import model.QRManager
import java.awt.Label
import java.util.*


class QRCodeGenerator : App(QRCodeView::class) {
    init {

    }

    override fun start(stage: Stage) {
        stage.width=600.0
        stage.height=500.0
        super.start(stage)
    }

}


class QRCodeView : View("QRCodeViewer") {
    init {
        FX.locale = Locale.GERMAN
        FX.messages = ResourceBundle.getBundle("QRCodeGenerator",FX.locale)
    }

    var manager = QRManager().restore()

    override val root: AnchorPane by fxml()
    private val btn_load: Button by fxml()
    private val btn_save: Button by fxml()
    private val txf_name : TextField by fxml()
    private val txf_type: TextField by fxml()
    private val txf_attr: TextField by fxml()
    private val lbl_status : Label by fxid()
    private val tbl : TableView<ColumnConstraints> by fxml()







}


fun main(args: Array<String>) {
    launch<QRCodeGenerator>()

}
