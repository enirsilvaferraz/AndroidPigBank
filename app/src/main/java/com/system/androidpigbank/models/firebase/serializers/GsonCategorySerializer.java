package com.system.androidpigbank.models.firebase.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.system.androidpigbank.controllers.vos.CategoryVO;

import java.lang.reflect.Type;

/**
 * Created by Enir on 07/09/2016.
 */

public class GsonCategorySerializer implements JsonSerializer<CategoryVO>, JsonDeserializer<CategoryVO> {

    @Override
    public JsonElement serialize(CategoryVO src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getKey());
    }

    @Override
    public CategoryVO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CategoryVO category = new CategoryVO();
        category.setKey(json.getAsString());
        return category;
    }
}
