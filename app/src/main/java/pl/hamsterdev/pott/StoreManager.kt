package pl.hamsterdev.pott

import android.content.Context
import org.json.JSONArray
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

    private fun saveFile(items: List<ItemModel>) {
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
        val jsonArray = JSONArray(resultString)
        val result = mutableListOf<ItemModel>()

        (0 until jsonArray.length()).forEach {
            val itemJson = jsonArray.getString(it)
            result.add(ItemModel(jsonString = itemJson))
        }

        return result
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

    fun removeExpiredItems() {
        val items = getItems()

        val filteredItems = items.filter { item -> item.daysLeft > -2 }

        saveFile(filteredItems)
    }
}