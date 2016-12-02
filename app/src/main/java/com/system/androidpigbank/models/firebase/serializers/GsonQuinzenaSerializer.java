package com.system.androidpigbank.models.firebase.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.system.androidpigbank.controllers.helpers.PaymentType;
import com.system.androidpigbank.controllers.helpers.Quinzena;

import java.lang.reflect.Type;

/**
 * Created by Enir on 07/09/2016.
 */

public class GsonQuinzenaSerializer implements JsonSerializer<Quinzena>, JsonDeserializer<Quinzena> {

    @Override
    public JsonElement serialize(Quinzena src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }

    @Override
    public Quinzena deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Quinzena.getEnum(json.getAsInt());
    }
}
