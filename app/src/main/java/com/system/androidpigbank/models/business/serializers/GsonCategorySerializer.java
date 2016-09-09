package com.system.androidpigbank.models.business.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.system.androidpigbank.models.entities.Category;

import java.lang.reflect.Type;

/**
 * Created by Enir on 07/09/2016.
 */

public class GsonCategorySerializer implements JsonSerializer<Category>, JsonDeserializer<Category> {

    @Override
    public JsonElement serialize(Category src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Category category = new Category();
        category.setName(json.getAsString());
        return category;
    }
}
