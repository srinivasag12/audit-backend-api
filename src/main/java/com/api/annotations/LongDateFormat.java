package com.api.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.api.annotations.JsonLongDateFormat;
import com.api.annotations.SerializeJsonLongDateFormat;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = SerializeJsonLongDateFormat.class)
@JsonDeserialize(using = JsonLongDateFormat.class)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
public @interface LongDateFormat {}
