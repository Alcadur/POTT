package pl.hamsterdev.pott

import org.json.JSONObject
import java.time.Duration
import java.util.*

class ItemModel {
    val id: String
    var name: String
    var quantity: Int = 0
    var expireAt: Long = 0
    private var _duration: Duration? = null

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

    val duration: Duration
        get() {
            if (_duration == null) {
                val timeZone = TimeZone.getDefault()
                val today = Calendar.getInstance(timeZone)

                val expireDate = Calendar.getInstance(timeZone)
                expireDate.time = Date(expireAt)
                expireDate.set(Calendar.HOUR_OF_DAY, 10)
                expireDate.set(Calendar.MINUTE, 0)
                expireDate.set(Calendar.SECOND, 0)

                _duration = Duration.between(today.toInstant(), expireDate.toInstant())
            }

            return _duration!!
        }

    fun validate(): List<Int> {
        val errors = mutableListOf<Int>()

        if (name.isNullOrEmpty()) {
            errors.add(R.string.validation_item_modal_name_empty)
        }

        if (quantity < 1) {
            errors.add(R.string.validation_item_modal_quantity_empty)
        }

        if (duration.toMillis() < 1) {
            errors.add(R.string.validation_item_model_expire_at)
        }

        return errors
    }

    override fun toString(): String {
        return "{ id: \"$id\", name: \"$name\", quantity: $quantity, expireAt: $expireAt }"
    }
}
