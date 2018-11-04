package seedu.address.model;

import java.util.function.Predicate;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.client.Client;
import seedu.address.model.contact.Contact;
import seedu.address.model.vendor.Vendor;

/**
 * Specifies the different contact types. This enum includes the correct filter required to filter a specific
 * contact type from a list of contacts of different contact types.
 */
public enum ContactType {
    CLIENT {
        @Override
        public Predicate<Contact> getFilter() {
            return contact -> contact instanceof Client;
        }

        @Override
        public String toString() {
            return "client";
        }
    },
    VENDOR {
        @Override
        public Predicate<Contact> getFilter() {
            return contact -> contact instanceof Vendor;
        }

        @Override
        public String toString() {
            return "vendor";
        }
    };

    public abstract Predicate<Contact> getFilter();
}
