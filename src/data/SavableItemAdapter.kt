package data

import com.google.gson.*
import java.lang.reflect.Type

class SavableItemAdapter : JsonSerializer<SavableRoom?>, JsonDeserializer<SavableRoom?> {
    override fun serialize(src: SavableRoom?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
        val returnValue = JsonObject()
        val className: String = src!!::class.java.name
        returnValue.addProperty(CLASSNAME, className)
        val element = context.serialize(src)
        returnValue.add(INSTANCE, element)
        return returnValue
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): SavableRoom {
        val jsonObject = json.asJsonObject
        val primitive = jsonObject[CLASSNAME] as JsonPrimitive
        val className = primitive.asString
        val klass = try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw JsonParseException(e.message)
        }
        return context.deserialize(jsonObject[INSTANCE], klass)
    }

    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val INSTANCE = "INSTANCE"
    }



}