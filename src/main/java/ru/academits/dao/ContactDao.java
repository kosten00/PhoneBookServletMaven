package ru.academits.dao;

import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Anna on 17.06.2017.
 */
public class ContactDao {
    private List<Contact> contactList = new ArrayList<>();
    private AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact0 = new Contact();
        contact0.setId(getNewId());
        contact0.setFirstName("Иван");
        contact0.setLastName("Иванов");
        contact0.setPhone("9123456789");
        contactList.add(contact0);

        Contact contact1 = new Contact();
        contact1.setId(getNewId());
        contact1.setFirstName("Петр");
        contact1.setLastName("Петрович");
        contact1.setPhone("912312212");
        contactList.add(contact1);

        Contact contact2 = new Contact();
        contact2.setId(getNewId());
        contact2.setFirstName("Сидр");
        contact2.setLastName("Сидорович");
        contact2.setPhone("88005553535");
        contactList.add(contact2);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }

    public void remove(Contact contact) {
        contactList.remove(contact);
    }
}
