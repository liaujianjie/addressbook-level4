package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedContact.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalContacts.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ContactType;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.testutil.Assert;

public class XmlAdaptedClientTest {
    private static final int INVALID_ID = -1;
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final XmlAdaptedService INVALID_SERVICE = new XmlAdaptedService("photoshoot $200");

    private static final int VALID_ID = 1;
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<XmlAdaptedService> VALID_SERVICES = BENSON.getServicesStream()
            .map(XmlAdaptedService::new)
            .collect(Collectors.toList());

    private static final ContactType VALID_CONTACT_TYPE = ContactType.CLIENT;

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        XmlAdaptedContact person = new XmlAdaptedContact(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        XmlAdaptedContact person =
                new XmlAdaptedContact(INVALID_ID, INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedContact person =
                new XmlAdaptedContact(VALID_ID, INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedContact person =
                new XmlAdaptedContact(VALID_ID, VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedContact person =
                new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_TAGS, VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                INVALID_ADDRESS, VALID_TAGS, VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_TAGS, VALID_SERVICES, VALID_CONTACT_TYPE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                invalidTags, VALID_SERVICES, VALID_CONTACT_TYPE);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidServices_throwsIllegalValueException() {
        List<XmlAdaptedService> invalidServices = new ArrayList<>(VALID_SERVICES);
        invalidServices.add(INVALID_SERVICE);
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, invalidServices, VALID_CONTACT_TYPE);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedContact person = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_SERVICES, null);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }
}
