import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import javafx.beans.property.SimpleListProperty
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.image.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.utils.addToStdlib.safeAs
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.ImageIO
import java.awt.*

const val saveDir = "data/save/"
const val qrDir = "data/qr"

//For testing purposes
fun main() {
    //val manager = QRManager()
    //manager.restore()
    if (File("${qrDir}109208388.jpg").exists()) println("true")


    //writeQRCode(manager.entries.first())
   // println(manager.entries.first().toString())
    //manager.save()
    //manager.writeQRCode()
}

class QRManager() {
    //The given mutableListOf<OR> is just the data which the entries/entriesProperty
    //Shall be built on you could also just use use a variable provided from a constructor
    //when initializing the class
    private val entriesProperty = SimpleListProperty<QR>(this, "entries", mutableListOf<QR>().asObservable())
    var entries: ObservableList<QR> by entriesProperty
    private val mapper = jacksonObjectMapper()

    init {
        //As soon a new QR elemnt is added
        entriesProperty.addListener(ListChangeListener { onListChangeHandler(it) })
        //entriesProperty.addListener(InvalidationListener { it -> print("changed $it")})
    }

    private fun onListChangeHandler(change: ListChangeListener.Change<out QR>) {
        runBlocking(Dispatchers.IO + SupervisorJob()) {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.addedSubList.forEach { element ->
                        if (!File("${qrDir}${element.qrId}.jpg").exists()) {
                            launch { writeQRCode(element) }
                        }
                    }
                }
            }
        }
    }


    fun save() {
        try {
            FileOutputStream("$saveDir test.json").use { out ->
                mapper.writerWithDefaultPrettyPrinter().writeValue(out, entries)
            }
        } catch (ex: IOException) {
            println(ex.printStackTrace())
        }
    }

    fun restore() {
        var jsonStr = ""
        FileReader("$saveDir test.json").use { reader -> reader.forEachLine { jsonStr += it } }
        //val jsonString: String = File("data/output/test.json").readText(Charsets.UTF_8)
        entries.addAll(mapper.readValue<List<QR>>(jsonStr).toObservable())

        //this one for whatever does not work ...
        //My guess...because of the writerWithDefaultPrettyPrinter of the save() function
        /*
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())
        FileInputStream("data/output/test.json").use { it ->
            entries2.add(mapper.readValue(it,QR::class.java))
        }
         */
    }

    fun fill() {
        for (i in 0..10) {
            entries.add(QR("name$i", "url", listOf<String>("someURL")))
        }
    }
}


class QR(
    var name: String = "",
    var type: String = "",
    var attr: List<String> = listOf<String>()
) : Serializable {
    @JsonIgnore
    val qrId = attr.hashCode().toString()
    override fun toString(): String {
        return ("name: $name | type: $type | attr: ${attr.joinToString(",")}")
    }
    @JsonIgnore
    var img = ImageView("320807582.jpg")
    // var img = ImageView("qrTemplate-small.jpg")
   //var img = if(File("/src/main/resources/qr/109208388.jpg").exists()) ImageView("qr/109208388.jpg") else ImageView("qrTemplate-small.jpg")
}

/*
// The image is located in default package of the classpath
Image image1 = new Image("/flower.png");

// The image is located in my.res package of the classpath
Image image2 = new Image("my/res/flower.png");

// The image is downloaded from the supplied URL through http protocol
Image image3 = new Image("http://sample.com/res/flower.png");

// The image is located in the current working directory
Image image4 = new Image("file:flower.png");
 */








//Jet only take the first attribute of attr for the qr code
private fun writeQRCode(element: QR) {
    println(Thread.currentThread())
    val writer = QRCodeWriter()
    val width = 256
    val height = 256
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB) // create an empty image
    val white = 255 shl 16 or (255 shl 8) or 255
    val black = 0
    try {
        val bitMatrix = writer.encode(
            element.attr.joinToString(),
            BarcodeFormat.QR_CODE,
            width,
            height
        )
        for (i in 0 until width) {
            for (j in 0 until height) {
                image.setRGB(i, j, if (bitMatrix[i, j]) black else white) // set pixel one by one
            }
        }
        try {
            ImageIO.write(
                image, "jpg", File(
                    "$qrDir${element.qrId}.jpg"
                )
            ) // save QR image to disk
        } catch (e: IOException) {
            println("IOException. Failed to generate QRCode for : ${element.toString()}")
            println(e.message)
            e.printStackTrace()
        }
    } catch (e: WriterException) {
        println("WriterException. Failed to generate QRCode for : ${element.toString()}")
        println(e.message)
        e.printStackTrace()
    }
}


