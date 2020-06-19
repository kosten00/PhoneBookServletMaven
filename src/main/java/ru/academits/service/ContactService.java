package ru.academits.service;

import ru.academits.PhoneBook;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactService {
    private ContactDao contactDao = PhoneBook.contactDao;
    private boolean hasInvalidValidation = false;

    public boolean hasAllContactsValid() {
        return !hasInvalidValidation;
    }

    public void resetValidationStatus() {
        hasInvalidValidation = false;
    }

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = contactDao.getAllContacts();
        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExistingContactInDatabase(Contact contactToCheck) {
        List<Contact> contactList = contactDao.getAllContacts();

        return contactList.contains(contactToCheck);
    }

    private ContactValidation removeContact(Contact contact) {
        ContactValidation contactValidation = validateContactOnRemoving(contact);

        if (!contactValidation.isValid()) {
            hasInvalidValidation = true;
        } else {
            contactDao.remove(contact);
        }
        return contactValidation;
    }

    public ContactValidation validateContactOnRemoving(Contact contact) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);

        if (!isExistingContactInDatabase(contact)) {
            contactValidation.setValid(false);
            contactValidation.setError("Попытка удаления контакта, данных о котором не содержится в телефонной книге.");
        }
        return contactValidation;
    }

    public ContactValidation validateContactOnAdding(Contact contact) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);
        if (contact.getFirstName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Имя должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Фамилия должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getPhone().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Телефон должно быть заполнено.");
            return contactValidation;
        }

        if (isExistContactWithPhone(contact.getPhone())) {
            contactValidation.setValid(false);
            contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return contactValidation;
        }

        if (!contact.getPhone().trim().matches("[0-9]+")) {
            contactValidation.setValid(false);
            contactValidation.setError("Номер телефона должен включать в себя только цифры.");
            return contactValidation;
        }
        return contactValidation;
    }

    public ContactValidation addContact(Contact contact) {
        ContactValidation contactValidation = validateContactOnAdding(contact);
        if (contactValidation.isValid()) {
            contactDao.add(contact);
        }
        return contactValidation;
    }

    public ContactValidation[] removeContacts(Contact[] contacts) {
        if (contacts.length == 1) {
            return new ContactValidation[]{removeContact(contacts[0])};
        }
        ContactValidation[] contactsValidation = new ContactValidation[contacts.length];

        for (int i = 0; i < contacts.length; i++) {
            contactsValidation[i] = removeContact(contacts[i]);
        }

        return contactsValidation;
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public List<Contact> getContacts(String term) {
        term = term.toLowerCase();

        List<Contact> allContacts = new ArrayList<>(contactDao.getAllContacts());
        List<Contact> filteredContacts = new ArrayList<>();

        for (Contact contact : allContacts) {
            if (contact.getPhone().contains(term)
                    || contact.getFirstName().toLowerCase().contains(term)
                    || contact.getLastName().toLowerCase().contains(term)) {
                filteredContacts.add(contact);
            }
        }

        return filteredContacts;
    }
}
