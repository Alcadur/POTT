package pl.hamsterdev.pott

import org.json.JSONObject

class ItemModel {
    val id: String
    var name: String
    var quantity: Int = 0
    var expireAt: Long = 0

    constructor(id: String = UUID.randomUUID().toString(), name: String, quantity: Int, expireAt: Long) {
        this.id = id
        this.name = name
        this.quantity = quantity
        this.expireAt = expireAt
    }

    constructor(jsonString: String) {
        val json = JSONObject(jsonString)
        this.id = json.get("id").toString()
        this.name = json.get("name").toString()
        this.quantity = json.get("quantity").toString().toInt()
        this.expireAt = json.get("expireAt").toString().toLong()
    }

    override fun toString(): String {
        return "{ id: $id, name: $name, quantity: $quantity, expireAt: $expireAt }"
    }
}
