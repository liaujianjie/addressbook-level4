package seedu.address.model.contact;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Defines the service types
 */
enum ServiceType {
    photographer, hotel, catering, dress, ring, transport, invitation
}

/**
 * Represents a Contact's Service in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidServiceName(String)}
 */
public class Service {

    public static final String COST_REGEX = "(\\d*)\\.(\\d{2})";
    public static final String NON_ZERO_REGEX = "[1-9]";

    public static final String MESSAGE_SERVICE_NAME_CONSTRAINTS =
            "Valid Services: photographer, hotel, catering, dress, ring, transport, invitation";
    public static final String MESSAGE_SERVICE_COST_CONSTRAINTS =
            "Service cost must be more than $0.00 and given to 2 decimal places\n"
            + "Please also omit all symbols except the decimal point";

    public final String serviceName;
    public final BigDecimal serviceCost;

    private Set<Contact> assignees = new HashSet<>();

    // Id list of clients / service providers for service providers / clients respectively.
    private List<Integer> idList;

    /**
     * Constructs a {@code Service}.
     *
     * @param service A valid service name.
     * @param cost Cost of this service.
     */
    public Service(String service, String cost) {
        requireNonNull(service);

        checkArgument(isValidServiceName(service), MESSAGE_SERVICE_NAME_CONSTRAINTS);
        checkArgument(isValidServiceCost(cost), MESSAGE_SERVICE_COST_CONSTRAINTS);

        serviceName = service;
        serviceCost = new BigDecimal(cost);;
        // Set to 2 decimal places
        serviceCost.setScale(2);
        idList = new ArrayList<>();
    }

    public String getName() {
        return serviceName;
    }

    public BigDecimal getCost() {
        return serviceCost;
    }

    public void addAssignee(Contact assignee) {
        this.assignees.add(assignee);
    }

    public Set<Contact> getAssignees() { return assignees; }

    /**
     * Returns the URL description for the service.
     * @return string describing service in URL format.
     */
    public String getUrlDescription() {
        return serviceName + ":" + serviceCost.toPlainString() + ":" + String.join(":", assignees.stream()
                .map(a -> a.toUniqueString() + " - " + a.getName()).collect(Collectors.toList()));
    }

    public List<Integer> getIdList() {
        return idList;
    }
    /**
     * Returns true if a given string is a valid service name.
     */
    public static boolean isValidServiceName(String test) {
        for (ServiceType s : ServiceType.values()) {
            if (s.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if a given cost is above $0, has 2 decimal places and has no other symbols
     */
    public static boolean isValidServiceCost(String test) {
        Pattern pattern = Pattern.compile(NON_ZERO_REGEX);
        Matcher matcher = pattern.matcher(test);
        return test.matches(COST_REGEX) && matcher.find();
    }

    /**
     * Returns true if the service type is the same as the {@code other} service specified.
     * @param other The Service type.
     * @return True if is the same type.
     */
    public boolean isSameServiceTypeAs(Service other) {
        return other != null
                && serviceName.equals(other.serviceName);
    }

    @Override
    public String toString() {
        String assigneesString = String.join("\n* ", assignees.stream().map(a ->
                a.toUniqueString() + " - " + a.getName()).collect(Collectors.toList()));
        return String.format("%s $%s %s", serviceName, serviceCost.toPlainString(), assigneesString);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Service // instanceof handles nulls
                && serviceName.equals(((Service) other).serviceName)) // state check
                && serviceCost.compareTo(((Service) other).serviceCost) == 0;
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }

}
