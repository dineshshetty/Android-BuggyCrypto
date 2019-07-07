package com.dns.buggycrypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class MapperSingleton {

    private static MapperSingleton instance;
    private final ObjectMapper mapper;

    private MapperSingleton() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static MapperSingleton getInstance() {
        if (instance == null) {
            instance = new MapperSingleton();
        }
        return instance;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
