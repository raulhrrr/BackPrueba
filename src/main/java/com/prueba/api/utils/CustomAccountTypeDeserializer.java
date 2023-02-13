package com.prueba.api.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CustomAccountTypeDeserializer extends JsonDeserializer<AccountType> {

    @Override
    public AccountType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return AccountType.valueOf(jsonParser.getText().trim().toUpperCase());
    }

}
