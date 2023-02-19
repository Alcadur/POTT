package pl.hamsterdev.pott

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.UUID

class StoreManager {

    private lateinit var context: Context

    constructor(context: Context) {
        this.context = context
    }

    private val fileName: String
        get() = context.resources.getString(R.string.fileName)

    private val filesDir: File
        get() = context.filesDir

    private fun saveFile(items: MutableList<JSONObject>) {
        val file = File(filesDir, fileName)
        val fileWriter = FileWriter(file)
        val bufferWriter = BufferedWriter(fileWriter)
        bufferWriter.write(items.toString())
        bufferWriter.close();
    }

    fun addItem(item: JSONObject) {
        item.put("id", UUID.randomUUID().toString())
        val items = getItems()
        items.add(item)

        saveFile(items)
    }

    fun getItems(): MutableList<JSONObject> {
        val file =  File(filesDir, fileName)
        var items = mutableListOf<JSONObject>()

        if (file.exists()) {
            val resultString = getFileContent(file)
            items = parseStringResult(resultString)
        }

        return items
    }

    private fun parseStringResult(resultString: String): MutableList<JSONObject> {
        val jsonStrings = resultString.substring(1, resultString.length - 1).split("},")
        return jsonStrings.map { jsonString ->
            if (jsonString.get(jsonString.length - 1) != '}') {
                return@map JSONObject("$jsonString}")
            }

            JSONObject(jsonString)
        }.toMutableList()
    }

    private fun getFileContent(file: File): String {
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        var line = bufferedReader.readLine()
        while (line != null){
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
        bufferedReader.close()

        return stringBuilder.toString()
    }

    fun removeItem(id: String) {}
}