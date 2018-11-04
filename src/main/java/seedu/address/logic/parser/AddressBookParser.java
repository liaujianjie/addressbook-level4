package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddServiceCommand;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.commands.AutoMatchCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditPasswordCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.commands.LogoutCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RegisterAccountCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UpdateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ContactType;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern COMMAND_FORMAT =
            Pattern.compile("(?<firstWord>[a-zA-Z]+)(?<identifier>#[\\d\\w-]*)?(?<secondWord>[\\s]+(?!./)"
                    + "[a-zA-Z]+)?(?<arguments>.*)");

    /**
     * Parses user input into command for execution. This method is use before user has successfully logged in.
     *
     * @param userInput full user input string.
     * @return the command based on the user input.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Command parseCommandBeforeLoggedIn(String userInput) throws ParseException {
        final Matcher matcher = COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = getCommandWord(matcher.group("firstWord"), matcher.group("secondWord"));
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case LoginCommand.COMMAND_WORD:
            return new LoginCommandParser().parse(arguments);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Combines the first and second words to form the command word.
     */
    private String getCommandWord(String firstWord, String secondWord) {
        String commandWord;

        if (secondWord == null) {
            commandWord = firstWord;
        } else {
            commandWord = String.format("%s %s", firstWord, secondWord.trim());
        }

        return commandWord;
    }

    /**
     * Parses user input into command for execution. This method will only be called after
     * user has successfully log in.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            //TODO: update HelpCommand.MESSAGE_USAGE
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String firstWord = matcher.group("firstWord");
        final String identifier = matcher.group("identifier");
        final String secondWord = matcher.group("secondWord");
        final String arguments = matcher.group("arguments");

        final String commandWord = getCommandWord(firstWord, secondWord);

        switch (commandWord) {

        case RegisterAccountCommand.COMMAND_WORD:
            return new RegisterAccountCommandParser().parse(arguments);

        case LogoutCommand.COMMAND_WORD:
            return new LogoutCommand();

        case EditPasswordCommand.COMMAND_WORD:
            return new EditPasswordCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD_CLIENT:
            return new SelectCommandParser(ContactType.CLIENT).parse(identifier);

        case SelectCommand.COMMAND_WORD_VENDOR:
            return new SelectCommandParser(ContactType.VENDOR).parse(identifier);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case AddCommand.COMMAND_WORD_CLIENT:
            return new AddCommandParser(ContactType.CLIENT).parse(arguments);

        case DeleteCommand.COMMAND_WORD_CLIENT:
            requireEmptyArguments(arguments);
            return new DeleteCommandParser(ContactType.CLIENT).parse(identifier);

        case ListCommand.COMMAND_WORD_CLIENT:
            return new ListCommandParser(ContactType.CLIENT).parse(arguments);

        case UpdateCommand.COMMAND_WORD_CLIENT:
            return new UpdateCommandParser(ContactType.CLIENT)
                    .parse(String.format("%s %s", requireIdentifierNonNull(identifier, ContactType.CLIENT,
                            UpdateCommand.MESSAGE_USAGE).substring(1), arguments));

        case AddServiceCommand.COMMAND_WORD_CLIENT:
            return new AddServiceCommandParser(ContactType.CLIENT)
                    .parse(String.format("%s %s", requireIdentifierNonNull(identifier, ContactType.CLIENT,
                            AddServiceCommand.MESSAGE_USAGE).substring(1), arguments));

        case AutoMatchCommand.COMMAND_WORD_CLIENT:
        case AutoMatchCommand.COMMAND_WORD_VENDOR:
            return new AutoMatchCommandParser().parse(firstWord + identifier);

        case AssignCommand.COMMAND_WORD_CLIENT:
            return new AssignCommand(ContactType.CLIENT, identifier.substring(1)).parse(arguments);

        case AddCommand.COMMAND_WORD_VENDOR:
            return new AddCommandParser(ContactType.VENDOR).parse(arguments);

        case DeleteCommand.COMMAND_WORD_VENDOR:
            requireEmptyArguments(arguments);
            return new DeleteCommandParser(ContactType.VENDOR).parse(identifier);

        case ListCommand.COMMAND_WORD_VENDOR:
            return new ListCommandParser(ContactType.VENDOR).parse(arguments);

        case UpdateCommand.COMMAND_WORD_VENDOR:
            return new UpdateCommandParser(ContactType.VENDOR)
                    .parse(String.format("%s %s",
                            requireIdentifierNonNull(identifier, ContactType.VENDOR,
                                    UpdateCommand.MESSAGE_USAGE).substring(1), arguments));

        case AddServiceCommand.COMMAND_WORD_VENDOR:
            return new AddServiceCommandParser(ContactType.VENDOR)
                    .parse(String.format("%s %s",
                            requireIdentifierNonNull(identifier, ContactType.VENDOR,
                                    AddServiceCommand.MESSAGE_USAGE).substring(1), arguments));

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Ensure that the {@code String} object is non-null
     * @param identifier The {@code String} object that is to be non-null
     * @return The {@code String} object, if it's non-null
     * @throws ParseException if {@code String} object is actually null
     */
    private String requireIdentifierNonNull(String identifier, ContactType contactType, String messageUsage)
            throws ParseException {
        if (identifier == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    String.format(messageUsage, contactType, "#<ID>")));
        }

        return identifier;
    }

    /**
     * Ensures that there are no arguments specified.
     * @param args
     * @throws ParseException if there are arguments present i.e not null
     */
    private void requireEmptyArguments(String args) throws ParseException {
        if (args.trim().length() > 0) {
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
