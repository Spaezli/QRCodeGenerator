import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleListProperty
import javafx.collections.ObservableList
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.io.Serializable

import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue


class QRManager()  {
    //The given mutableListOf<OR> is just the data which the entries/entriesProperty
    //Shall be built on you could also just use use a variable provided from a constructor
    //when initializing the class
    private val entriesProperty = SimpleListProperty<QR>(this, "entries", mutableListOf<QR>().asObservable())
    var entries: ObservableList<QR> by entriesProperty
    private val mapper = jacksonObjectMapper()

    fun save() {
        try {
            FileOutputStream("data/output/test.json").use { out ->
                mapper.writerWithDefaultPrettyPrinter().writeValue(out, entries)
            }
        } catch (ex: IOException) {
            println(ex.printStackTrace())
        }
    }

    fun restore() {
        var jsonStr = ""
        FileReader("data/output/test.json").use { reader -> reader.forEachLine {jsonStr+=it} }
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
/*
fun main(){
    val t = mutableListOf<String>()
    t.add("asdf")
    val manager = QRManager()
    manager.restore()
    manager.entries.add(QR("newenew","",listOf("")))
    print("sdf")

}

 */