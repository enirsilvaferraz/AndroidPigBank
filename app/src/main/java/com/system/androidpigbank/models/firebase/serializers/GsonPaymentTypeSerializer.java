package com.system.androidpigbank.models.firebase.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.system.androidpigbank.controllers.vos.PaymentType;

import java.lang.reflect.Type;

/**
 * Created by Enir on 07/09/2016.
 */

public class GsonPaymentTypeSerializer implements JsonSerializer<PaymentType>, JsonDeserializer<PaymentType> {

    @Override
    public JsonElement serialize(PaymentType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }

    @Override
    public PaymentType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return PaymentType.getEnum(json.getAsInt());
    }
}
