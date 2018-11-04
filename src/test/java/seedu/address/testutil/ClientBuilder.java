package seedu.address.testutil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import seedu.address.model.client.Client;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.contact.Service;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Client objects.
 */
public class ClientBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Map<String, Service> services;
    private int id;

    public ClientBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        services = new HashMap<>();
        id = 0;
    }

    /**
     * Initializes the ClientBuilder with the data of {@code personToCopy}.
     */
    public ClientBuilder(Contact contactToCopy) {
        name = contactToCopy.getName();
        phone = contactToCopy.getPhone();
        email = contactToCopy.getEmail();
        address = contactToCopy.getAddress();
        tags = new HashSet<>(contactToCopy.getTags());
        services = new HashMap<>(contactToCopy.getServices());
        id = contactToCopy.getId();
    }

    /**
     * Sets the {@code Name} of the {@code Client} that we are building.
     */
    public ClientBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Client} that we are building.
     */
    public ClientBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Client} that we are building.
     */
    public ClientBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Client} that we are building.
     */
    public ClientBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Client} that we are building.
     */
    public ClientBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     *  Sets the {@code id} of the {@code Client} that we are building
     */
    public ClientBuilder withId(int id) {
        this.id = id;
        return this;
    }

    /**
     *  Sets the {@code service} of the {@code Client} that we are building
     */
    public ClientBuilder withServices(Service service) {
        this.services.put(service.getName(), service);
        return this;
    }

    public Contact build() {
        return new Client(id, name, phone, email, address, tags, services);
    }

}
