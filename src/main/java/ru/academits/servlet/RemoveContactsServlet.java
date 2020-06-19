package ru.academits.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.academits.PhoneBook;
import ru.academits.coverter.ContactConverter;
import ru.academits.coverter.ContactValidationConverter;
import ru.academits.model.Contact;
import ru.academits.service.ContactService;
import ru.academits.service.ContactValidation;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RemoveContactsServlet extends HttpServlet {

    private ContactService phoneBookService = PhoneBook.phoneBookService;
    private ContactConverter contactConverter = PhoneBook.contactConverter;
    private ContactValidationConverter contactValidationConverter = PhoneBook.contactValidationConverter;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (OutputStream responseStream = resp.getOutputStream()) {
            String contacts = req.getReader().readLine();

//            JsonParser parser = new JsonParser();
//            JsonElement contactElements = parser.parse(contacts);
            JsonElement contactElements = JsonParser.parseString(contacts);

            JsonArray jsonArray = contactElements.getAsJsonArray();

            Contact[] contactsToRemove = new Contact[jsonArray.size()];

            for (int i = 0; i < contactsToRemove.length; i++) {
                contactsToRemove[i] = contactConverter.convertFormJson(jsonArray.get(i).toString());
            }
            ContactValidation[] contactsValidation = phoneBookService.removeContacts(contactsToRemove);

            String contactValidationJson = contactValidationConverter.convertToJson(contactsValidation);

            if (!phoneBookService.hasAllContactsValid()) {
                resp.setStatus(500);
            }
            responseStream.write(contactValidationJson.getBytes(StandardCharsets.UTF_8));

            phoneBookService.resetValidationStatus();
        } catch (Exception e) {
            System.out.println("error in RemoveContactsServlet GET: ");
            e.printStackTrace();
        }
    }
}
