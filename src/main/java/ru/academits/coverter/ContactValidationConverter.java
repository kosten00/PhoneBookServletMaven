package ru.academits.coverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.academits.service.ContactValidation;

public class ContactValidationConverter {
    private Gson gson = new GsonBuilder().create();

    public String convertToJson(ContactValidation contactValidation) {
        return gson.toJson(contactValidation);
    }

    public String convertToJson(ContactValidation[] contactsValidation) {
        return gson.toJson(contactsValidation);
    }
}
