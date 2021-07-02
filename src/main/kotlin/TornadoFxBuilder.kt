import javafx.beans.binding.Binding
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import tornadofx.*
import java.beans.EventHandler
import java.util.*
import javax.swing.Spring.height

class QRCodeGenerator2 : App(QRCodeView2::class) {
    init {

    }

    override fun start(stage: Stage) {
        stage.width = 600.0
        stage.height = 500.0
        super.start(stage)
    }

}


class QRCodeView2 : View("QRCodeViewer") {

    //private val manager = QRManager()
    private val controller = MyController()


    init {
        FX.locale = Locale.GERMAN
        FX.messages = ResourceBundle.getBundle("QRCodeGenerator", FX.locale)
        controller.qrList.add(QR("first", "url", listOf("someURL")))
    }

    var txf_name: TextField by singleAssign()
    var txf_type: TextField by singleAssign()
    var txf_attr: TextField by singleAssign()
    var btn_submit: Button by singleAssign()


    override val root = vbox {
        //Could also be seperated to a variable
        button(FX.messages["btn_load"]) {
            action { controller.restore() }

        }
        hbox {
            txf_name = textfield { promptText = "name" }
            txf_type = textfield { promptText = "type" }
            txf_attr = textfield { promptText = "attribute , attribute , .." }
            btn_submit = button(FX.messages["btn_submit"]) { onLeftClick { controller.submit(txf_name.text,txf_type.text,txf_attr.text) }}
        }


        tableview(controller.qrList) {
            column(FX.messages["col_name"], QR::name) {
                isEditable = true
            }.makeEditable()
            column(FX.messages["col_attr"], QR::attr) {
                isEditable = true

            }
            readonlyColumn(FX.messages["col_qr"], QR::img) {}
            //readonlyColumn(FX.messages["col_qr"],QR::imgPath,renderImg())
            //readonlyColumn(FX.messages["col_qr"],imageview("dasf"))
        }
        //imageview("qrTemplate.png"){}
    }
}


private class MyController() : Controller() {
    private var manager = QRManager()
    var qrList = manager.entries
    fun restore() = manager.restore()

    fun submit(_name: String, _type: String, _attr: String) {



    }
}


fun main(args: Array<String>) {
    launch<QRCodeGenerator2>()

}
