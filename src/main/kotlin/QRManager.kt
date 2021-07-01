package model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import tornadofx.*
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.io.Serializable

import tornadofx.getValue
import tornadofx.setValue

class QRManager()  {


    var entries = mutableListOf<QR>()
    val mapper = jacksonObjectMapper()


    fun save() {
        try {
            FileOutputStream("data/output/test.json").use { out ->
                mapper.writerWithDefaultPrettyPrinter().writeValue(out, entries)
            }
        } catch (ex: IOException) {
            println(ex.printStackTrace())
        }
    }

    /**
     * Due to the @see #save() the mapper with the .writerWithDefaultPrettyPrinter
     * prohibits the use of mapper within the FileInputStream().use function
     */
    fun restore() {
        var jsonStr = ""
        FileReader("data/output/test.json").use { reader -> reader.forEachLine {jsonStr+=it} }
        //val jsonString: String = File("data/output/test.json").readText(Charsets.UTF_8)
        entries = mapper.readValue<List<QR>>(jsonStr).toMutableList()

        //this one one for whatever ...
        /*
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())
        FileInputStream("data/output/test.json").use { it ->
            entries2.add(mapper.readValue(it,QR::class.java))
        }
         */
    }

    fun print() {


    }


    fun fill() {
        for (i in 0..10) {
            entries.add(QR("name$i", "url", listOf<String>("sd")))
        }
    }

}




class QR(
    val name: String = "",
    val type: String = "",
    val attr: List<String> = listOf<String>()
) : Serializable