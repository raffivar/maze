package data

import com.google.gson.*
import java.lang.reflect.Type

class Siri1 : JsonSerializer<ItemData>, JsonDeserializer<ItemData> {
    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val INSTANCE = "INSTANCE"
    }

    override fun serialize(src: ItemData, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val retValue = JsonObject()
        val className = src.javaClass.name
        retValue.addProperty(CLASSNAME, className)
        val elem = context.serialize(src)
        retValue.add(INSTANCE, elem)
        return retValue
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemData {
        val jsonObject = json.asJsonObject
        val prim = jsonObject[CLASSNAME] as JsonPrimitive
        val className = prim.asString
        var klass: Class<*>? = null
        klass = try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw JsonParseException(e.message)
        }
        return context.deserialize(jsonObject[INSTANCE], klass)
    }
}