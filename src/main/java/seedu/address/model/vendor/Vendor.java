package seedu.address.model.vendor;

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
 * Represents a Vendor in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Vendor extends Contact {
    private static int vendorId = 1;

    private final int id;

    /**
     * Every field must be present and not null.
     *
     */
    public Vendor(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.id = vendorId++;
    }

    public Vendor(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Map<String, Service> service) {
        super(name, phone, email, address, tags, service);
        this.id = vendorId++;
    }

    public Vendor(int id, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  Map<String, Service> services) {
        super(name, phone, email, address, tags, services);
        this.id = id;
    }

    @Override
    public ContactType getType() {
        return ContactType.VENDOR;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns true if both service providers have the same identity and data fields.
     * This defines a stronger notion of equality between two service providers.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Vendor)) {
            return false;
        }

        Vendor otherVendor = (Vendor) other;
        return otherVendor.getName().equals(getName())
                && otherVendor.getPhone().equals(getPhone())
                && otherVendor.getEmail().equals(getEmail())
                && otherVendor.getAddress().equals(getAddress())
                && otherVendor.getTags().equals(getTags())
                && otherVendor.getServices().equals(getServices());
    }
}
