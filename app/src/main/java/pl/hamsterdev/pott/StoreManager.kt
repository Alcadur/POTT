package pl.hamsterdev.pott

import android.content.Context
import java.io.*

class StoreManager {

    private lateinit var context: Context

    constructor(context: Context) {
        this.context = context
    }

    private val fileName: String
        get() = context.resources.getString(R.string.fileName)

    private val filesDir: File
        get() = context.filesDir

    private fun saveFile(items: MutableList<ItemModel>) {
        val file = File(filesDir, fileName)
        val fileWriter = FileWriter(file)
        val bufferWriter = BufferedWriter(fileWriter)
        bufferWriter.write(items.toString())
        bufferWriter.close();
    }

    fun addItem(item: ItemModel) {
        val items = getItems()
        items.add(item)

        saveFile(items)
    }

    fun getItems(): MutableList<ItemModel> {
        val file =  File(filesDir, fileName)
        var items = mutableListOf<ItemModel>()

        if (file.exists()) {
            val resultString = getFileContent(file)
            items = parseStringResult(resultString)
        }

        return items
    }

    private fun parseStringResult(resultString: String): MutableList<ItemModel> {
        val jsonStrings = resultString.substring(1, resultString.length - 1).split("},")
        return jsonStrings.map { jsonString ->
            if (jsonString.get(jsonString.length - 1) != '}') {
                return@map ItemModel("$jsonString}")
            }

            ItemModel(jsonString)
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