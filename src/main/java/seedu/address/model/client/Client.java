package seedu.address.model.client;

import java.util.Map;
import java.util.Set;

import seedu.address.model.ContactType;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.contact.Service;
import seedu.address.model.tag.Tag;

/**
 * Represents a client in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client extends Contact {
    private static int clientId = 1;

    private final int id;

    /**
     * Every field must be present and not null.
     */
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.id = clientId++;
    }

    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Map<String, Service> services) {
        super(name, phone, email, address, tags, services);
        this.id = clientId++;
    }

    public Client(int id, Name name, Phone phone, Email email, Address address, Set<Tag> tags, Map<String, Service>
            services) {
        super(name, phone, email, address, tags, services);
        this.id = id;
        if (id > clientId) {
            setClientId(id);
        }
    }

    public static void resetClientId() {
        clientId = 1;
    }

    public static void setClientId(int id) {
        clientId = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ContactType getType() {
        return ContactType.CLIENT;
    }

    /**
     * Returns true if both clients have the same identity and data fields.
     * This defines a stronger notion of equality between two clients.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Client)) {
            return false;
        }

        Client otherContact = (Client) other;
        return otherContact.getName().equals(getName())
                && otherContact.getPhone().equals(getPhone())
                && otherContact.getEmail().equals(getEmail())
                && otherContact.getAddress().equals(getAddress())
                && otherContact.getTags().equals(getTags())
                && otherContact.getServices().equals(getServices());
    }
}
