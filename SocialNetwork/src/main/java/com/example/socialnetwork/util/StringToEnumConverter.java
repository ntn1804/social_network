package com.example.socialnetwork.util;

import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, PostStatus> {
    @Override
    public PostStatus convert(String source) {
            return PostStatus.valueOf(source.toUpperCase());
    }
}
