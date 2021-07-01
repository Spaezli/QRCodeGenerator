
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.Stage
import javafx.util.*
import tornadofx.*
import javafx.event.*
import model.QR

import model.QRManager
import java.awt.Label
import java.util.*


import tornadofx.getValue
import tornadofx.setValue


class QRCodeGenerator2 : App(QRCodeView2::class) {
    init {

    }

    override fun start(stage: Stage) {
        stage.width=600.0
        stage.height=500.0
        super.start(stage)
    }

}


class QRCodeView2 : View("QRCodeViewer") {

    private val manager = QRManager()

    init {
        FX.locale = Locale.GERMAN
        FX.messages = ResourceBundle.getBundle("QRCodeGenerator",FX.locale)
       // manager.restore()



        print("")
    }






    override val root = vbox {
        //Could also be seperated to a variable
        button(FX.messages["btn_load"]) {
            action { manager.restore()

            print("sdf")
            }

        }

        tableview(manager.entries.asObservable()) {
            readonlyColumn(FX.messages["col_name"], QR::name){

            }
            readonlyColumn(FX.messages["col_type"],QR::type){

            }
            readonlyColumn(FX.messages["col_attr"],QR::attr){

            }



        }




    }




    




}


fun main(args: Array<String>) {
    launch<QRCodeGenerator2>()

}
