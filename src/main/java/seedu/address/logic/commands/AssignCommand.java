package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.DeselectRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ContactType;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Service;
import seedu.address.model.vendor.Vendor;

public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";
    public static final String COMMAND_WORD_CLIENT = "client" + " " + COMMAND_WORD;
    public static final String COMMAND_WORD_VENDOR = "vendor" + " " + COMMAND_WORD;

    private final ContactType targetType;
    private final Index targetId;
    private final ContactType assigneeType;
    private final Index assigneeId;
    private final String serviceName;

    public AssignCommand(ContactType targetType, Index targetId, ContactType assigneeType, Index assigneeId,
                         String serviceName) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.assigneeType = assigneeType;
        this.assigneeId = assigneeId;
        this.serviceName = serviceName;
    }

    public AssignCommand(ContactType targetType, String targetId) throws ParseException {
        this(targetType, ParserUtil.parseIndex(targetId), null, null, null);
    }

    public AssignCommand parse(String arguments) throws ParseException {
        requireNonNull(arguments);

        // Parse into argument map
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(arguments, PREFIX_SERVICE);

        // Get individual components from the map
        Matcher matcher = ParserUtil.parseContact(argumentMultimap.getPreamble());
        ContactType assigneeType = ParserUtil.parseContactType(matcher.group(ParserUtil.CONTACT_TYPE));
        Index assigneeId = ParserUtil.parseIndex(matcher.group(ParserUtil.CONTACT_IDENTIFIER));
        Optional<String> serviceName = argumentMultimap.getValue(PREFIX_SERVICE);

        if (!serviceName.isPresent()) {
            throw new ParseException("valid syntax help thingy");
        }

        return new AssignCommand(targetType, targetId, assigneeType, assigneeId, serviceName.get());
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        Contact originalTarget;
        Contact originalAssignee;
        Contact updatedTarget;
        Contact updatedAssignee;
        Service targetService;
        Service assigneeService;

        // Find the contacts
        try {
            originalTarget = getContactForEntityDetails(model.getAddressBook().getContactList(), targetType, targetId);
        } catch (NoSuchElementException exception) {
            throw new CommandException(String.format("Non-existent target entity %s#%s.", targetType, targetId));
        }
        try {
            originalAssignee = getContactForEntityDetails(model.getAddressBook().getContactList(), assigneeType,
                    assigneeId);
        } catch (NoSuchElementException exception) {
            throw new CommandException(String.format("Non-existent assignee entity %s#%s.", assigneeType, assigneeId));
        }

        if (originalAssignee.getType() == originalTarget.getType()) {
            throw new CommandException("You can only assign vendors to clients or clients to vendors.");
        }

        // Make copies
        switch (originalTarget.getType()) {
        case CLIENT:
            updatedTarget = new Client(originalTarget.getName(), originalTarget.getPhone(), originalTarget.getEmail(),
                    originalTarget.getAddress(), originalTarget.getTags(), originalTarget.getServices(),
                    originalTarget.getId());
            break;
        case VENDOR:
            updatedTarget = new Vendor(originalTarget.getName(), originalTarget.getPhone(), originalTarget.getEmail(),
                    originalTarget.getAddress(), originalTarget.getTags(), originalTarget.getServices(),
                    originalTarget.getId());
            break;
        default:
            throw new CommandException("Invalid entity type encountered.");
        }
        switch (originalAssignee.getType()) {
        case CLIENT:
            updatedAssignee = new Client(originalAssignee.getName(), originalAssignee.getPhone(),
                    originalAssignee.getEmail(), originalAssignee.getAddress(), originalAssignee.getTags(),
                    originalAssignee.getServices(), originalAssignee.getId());
            break;
        case VENDOR:
            updatedAssignee = new Vendor(originalAssignee.getName(), originalAssignee.getPhone(),
                    originalAssignee.getEmail(), originalAssignee.getAddress(), originalAssignee.getTags(),
                    originalAssignee.getServices(), originalAssignee.getId());
            break;
        default:
            throw new CommandException("Invalid entity type encountered.");
        }

        // Get the services that are to be updated and update them
        try {
            targetService = getServiceForContact(updatedTarget, serviceName);
        } catch (NoSuchElementException exception) {
            throw new CommandException(String.format("Contact %s#%s does not have the %s service associated with it.",
                    targetType, targetId, serviceName));
        }
        try {
            assigneeService = getServiceForContact(updatedAssignee, serviceName);
        } catch (NoSuchElementException exception) {
            throw new CommandException(String.format("Contact %s#%s does not have the %s service associated with it.",
                    targetType, targetId, serviceName));
        }
        targetService.addAssignee(updatedAssignee);
        assigneeService.addAssignee(updatedTarget);

        // Commit updates
        model.updateContact(originalTarget, updatedTarget);
        model.updateContact(originalAssignee, updatedAssignee);
        model.commitAddressBook();
        EventsCenter.getInstance().post(new DeselectRequestEvent());

        return new CommandResult("done lol");
    }

    private static Service getServiceForContact(Contact contact, String serviceName) throws NoSuchElementException {
        Optional<Service> optionalService = contact.getServicesStream().filter(service ->
                service.getName().equals(serviceName)).findFirst();
        if (optionalService.isPresent()) {
            return optionalService.get();
        }
        throw new NoSuchElementException("No such service found for contact.");
    }

    private static Contact getContactForEntityDetails(Collection<Contact> contacts, ContactType contactType,
                                                      Index contactId) throws NoSuchElementException {
        return contacts
                .stream()
                .filter(c -> (contactType.equals(ContactType.CLIENT) && c instanceof Client)
                        || (contactType.equals(ContactType.VENDOR) && c instanceof Vendor))
                .filter(c -> c.getId() == contactId.getOneBased())
                .findFirst()
                .get();
    }
}
