package com.system.androidpigbank.models.firebase.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.system.architecture.utils.JavaUtils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Enir on 07/09/2016.
 */

public class GsonDateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private String format;

    public GsonDateSerializer(String format) {
        this.format = format;
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(JavaUtils.DateUtil.format(src, format));
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return JavaUtils.DateUtil.parse(json.getAsJsonPrimitive().getAsString(), format);
    }
}
