package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.DeselectRequestEvent;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.LackOfPrivilegeException;
import seedu.address.model.ContactType;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.contact.Service;
import seedu.address.model.tag.Tag;
import seedu.address.model.vendor.Vendor;

/**
 * Edits the details of an existing contact in the address book.
 */
public class UpdateCommand extends Command {


    public static final String COMMAND_WORD = "update";
    public static final String COMMAND_WORD_GENERAL = "%1$s%2$s update";
    public static final String COMMAND_WORD_CLIENT = "client update";
    public static final String COMMAND_WORD_VENDOR = "vendor update";

    public static final String MESSAGE_USAGE = COMMAND_WORD_GENERAL + ": Updates the details of the %1$s identified "
            + "by the assigned unique %1$s ID.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: #<ID> (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + String.format(COMMAND_WORD_GENERAL, "%1$s", "#3")
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_CONTACT_SUCCESS = "Updated %1$s: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to update must be provided.";
    public static final String MESSAGE_DUPLICATE_CONTACT = "This contact already exists in the address book.";

    private final Index id;
    private final EditContactDescriptor editContactDescriptor;
    private final ContactType contactType;

    /**
     * @param id of the contact in the filtered contact list to edit
     * @param editContactDescriptor details to edit the contact with
     * @param contactType
     */
    public UpdateCommand(Index id, EditContactDescriptor editContactDescriptor, ContactType contactType) {
        requireNonNull(id);
        requireNonNull(editContactDescriptor);

        this.id = id;
        this.editContactDescriptor = new EditContactDescriptor(editContactDescriptor);
        this.contactType = contactType;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException,
            LackOfPrivilegeException {
        requireNonNull(model);

        if (!model.getUserAccount().hasWritePrivilege()) {
            throw new LackOfPrivilegeException(String.format(COMMAND_WORD_GENERAL, contactType, "#<ID>"));
        }

        // id is unique
        model.updateFilteredContactList(contactType.getFilter().and(contact -> contact.getId() == id.getOneBased()));

        List<Contact> filteredList = model.getFilteredContactList();

        if (filteredList.size() == 0) {
            // filtered list size is 0, meaning there is no such contact
            model.updateFilteredContactList(contactType.getFilter());
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                    id.getOneBased()));
        }

        if (filteredList.size() > 1) {
            throw new RuntimeException("ID is not unique!");
        }

        Contact contactToEdit = filteredList.get(0);
        Contact editedContact = createEditedContact(contactToEdit, editContactDescriptor, contactType);

        if (!contactToEdit.isSameContact(editedContact) && model.hasContact(editedContact)) {
            model.updateFilteredContactList(contactType.getFilter());
            throw new CommandException(MESSAGE_DUPLICATE_CONTACT);
        }

        model.updateContact(contactToEdit, editedContact);
        model.updateFilteredContactList(contactType.getFilter());
        model.commitAddressBook();
        EventsCenter.getInstance().post(new DeselectRequestEvent());
        return new CommandResult(String.format(MESSAGE_EDIT_CONTACT_SUCCESS, contactToEdit.getType(), editedContact));
    }

    /**
     * Creates and returns a {
     * @code Contact} with the details of {@code contactToEdit}
     * edited with {@code editContactDescriptor}.
     */
    public static Contact createEditedContact(Contact contactToEdit, EditContactDescriptor editContactDescriptor,
                                               ContactType contactType) {
        assert contactToEdit != null;

        Name updatedName = editContactDescriptor.getName().orElse(contactToEdit.getName());
        Phone updatedPhone = editContactDescriptor.getPhone().orElse(contactToEdit.getPhone());
        Email updatedEmail = editContactDescriptor.getEmail().orElse(contactToEdit.getEmail());
        Address updatedAddress = editContactDescriptor.getAddress().orElse(contactToEdit.getAddress());
        Set<Tag> updatedTags = editContactDescriptor.getTags().orElse(contactToEdit.getTags());
        Map<String, Service> updatedServices = editContactDescriptor.getServices().orElse(contactToEdit.getServices());
        int id = contactToEdit.getId();

        switch (contactType) {
        case CLIENT:
            return new Client(id, updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags,
                    updatedServices);
        case VENDOR:
            return new Vendor(id, updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags,
                    updatedServices);
        default:
            // should nvr come in here
            throw new RuntimeException("No such Contact Type!");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdateCommand)) {
            return false;
        }

        // state check
        UpdateCommand e = (UpdateCommand) other;
        return id.equals(e.id)
                && editContactDescriptor.equals(e.editContactDescriptor);
    }

    /**
     * Stores the details to edit the contact with. Each non-empty field value will replace the
     * corresponding field value of the contact.
     */
    public static class EditContactDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private Map<String, Service> services;

        public EditContactDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditContactDescriptor(EditContactDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setServices(toCopy.services);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags, services);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code services} to this object's {@code services}.
         * A defensive copy of {@code services} is used internally.
         */
        public void setServices(Map<String, Service> services) {
            this.services = (services != null) ? new HashMap<>(services) : null;
        }

        /**
         * Returns an unmodifiable services map, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code services} is null.
         */
        public Optional<Map<String, Service>> getServices() {
            return (services != null) ? Optional.of(Collections.unmodifiableMap(services)) : Optional.empty();
        }

        /**
         * Adds the specified service.
         * @param service Service to be added.
         */
        public void addService(Service service) {
            this.services.put(service.getName(), service);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditContactDescriptor)) {
                return false;
            }

            // state check
            EditContactDescriptor e = (EditContactDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags())
                    && getServices().equals(e.getServices());
        }
    }
}
