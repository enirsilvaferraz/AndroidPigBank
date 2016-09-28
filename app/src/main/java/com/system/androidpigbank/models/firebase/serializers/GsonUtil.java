package com.system.androidpigbank.models.firebase.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.system.androidpigbank.controllers.helpers.PaymentType;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.DTOAbs;

import java.util.Date;

/**
     *
     */
    public  class GsonUtil {

        private Gson build;

        public static GsonUtil getInstance() {
            return new GsonUtil();
        }

        public GsonUtil fromTransaction() {
            build = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(Date.class, new GsonDateSerializer())
                    .registerTypeAdapter(PaymentType.class, new GsonPaymentTypeSerializer())
                    .registerTypeAdapter(CategoryVO.class, new GsonCategorySerializer())
                    .create();
            return this;
        }

        public GsonUtil fromCategory() {
            build = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            return this;
        }

        public GsonUtil fromMonth() {
            build = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            return this;
        }

        public GsonUtil fromHomeObject() {
            build = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            return this;
        }

        public DTOAbs toDTO(EntityAbs entity, Class<? extends DTOAbs> classe) {
            String json = build.toJson(entity);
            return build.fromJson(json, classe);
        }


        public EntityAbs toEntity(DTOAbs dto, Class<? extends EntityAbs> classe) {
            String json = build.toJson(dto);
            return build.fromJson(json, classe);
        }
    }