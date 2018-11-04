package seedu.address.model.contact;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.model.ContactType;
import seedu.address.model.tag.Tag;

/**
 * An abstract class the client and the service provider inherits.
 */
public abstract class Contact {
    // Identity fields
    protected final Name name;
    protected final Phone phone;
    protected final Email email;

    // Data fields
    protected final Address address;
    protected final Set<Tag> tags = new HashSet<>();
    protected final Map<String, Service> services = new HashMap<>();

    public Contact(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
    }

    public Contact(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Map<String, Service> services) {
        requireAllNonNull(name, phone, email, address, tags, services);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.services.putAll(services);
    }

    public abstract int getId();

    public boolean hasService(Service service) {
        return services.containsKey(service.getName());
    }

    // Get the name of the contact
    public Name getName() {
        return name;
    }

    // Get the address of the contact
    public Address getAddress() {
        return address;
    }

    // Get the email of the contact
    public Email getEmail() {
        return email;
    }

    // get the phone number of the contact
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    // Get the contact type of this contact
    public abstract ContactType getType();

    // Get the services of the contact.
    public Map<String, Service> getServices() {
        return services;
    }

    // Get the services of the contact in a stream.
    public Stream<Service> getServicesStream() {
        return services.values().stream();
    }

    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameContact(Contact otherContact) {
        if (otherContact == this) {
            return true;
        }

        return otherContact != null
                && otherContact.getName().equals(getName())
                && ((otherContact.getPhone().equals(getPhone()) || otherContact.getEmail().equals(getEmail())));
    }

    /**
     * Concatenates the contact data into a URL String.
     *
     * @return URL of contact data.
     */
    public String getUrlContactData() {
        return "type=" + getType() + "&"
                + "id=" + getId() + "&"
                + "name=" + getName() + "&"
                + "phone=" + getPhone() + "&"
                + "email=" + getEmail() + "&"
                + "address=" + getAddress() + "&"
                + "tags=" + String.join(",", getTags().toString()) + "&"
                + "services=" + String.join(",",
                getServicesStream().map(Service::getUrlDescription).collect(Collectors.toList()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Generates a {@code String} representation in the format "{@code type#id}".
     * @return The unique string.
     */
    public String toUniqueString() {
        return getType() + "#" + getId();
    }
}
