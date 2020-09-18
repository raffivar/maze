package data

import com.google.gson.*
import java.lang.reflect.Type

class MazeSerializer<T : Any> : JsonSerializer<T>, JsonDeserializer<T> {
    companion object {
        private const val CLASSNAME = "CLASSNAME"
        private const val INSTANCE = "INSTANCE"
    }

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val retValue = JsonObject()
        val className = src::class.java.name
        retValue.addProperty(CLASSNAME, className)
        val elem = context.serialize(src)
        retValue.add(INSTANCE, elem)
        return retValue
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
        val jsonObject = json.asJsonObject
        val prim = jsonObject[CLASSNAME] as JsonPrimitive
        val className = prim.asString
        val klass = try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw JsonParseException(e.message)
        }
        return context.deserialize(jsonObject[INSTANCE], klass)
    }
}