package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.PasswordUtil.assertPasswordCorrect;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.security.PasswordAuthentication;
import seedu.address.model.AddressBook;
import seedu.address.model.ContactType;
import seedu.address.model.account.Account;
import seedu.address.storage.XmlAdaptedContact;
import seedu.address.storage.XmlAdaptedService;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlSerializableAccountList;
import seedu.address.storage.XmlSerializableAddressBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalAccount;

public class XmlUtilTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validAddressBook.xml");
    private static final Path MISSING_PERSON_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingPersonField.xml");
    private static final Path INVALID_PERSON_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidPersonField.xml");
    private static final Path VALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("validPerson.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempAddressBook.xml");

    private static final String INVALID_PHONE = "9482asf424";

    private static final int VALID_ID = 1;
    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "hans@example";
    private static final String VALID_ADDRESS = "4th street";
    private static final List<XmlAdaptedTag> VALID_TAGS =
            Collections.singletonList(new XmlAdaptedTag("friends"));
    private static final List<XmlAdaptedService> VALID_SERVICES =
            Collections.singletonList(new XmlAdaptedService("photographer $200"));
    private static final ContactType VALID_CONTACT_TYPE = ContactType.CLIENT;

    private static final Path VALID_ACCOUNTFILE = TEST_DATA_FOLDER.resolve("accountlist.xml");
    private static final Path INVALID_ACCOUNTFILE = TEST_DATA_FOLDER.resolve("randomInvalidPath.xml");
    private static final Path EMPTYUSERNAME_ACCOUNTFILE = TEST_DATA_FOLDER.resolve("accountlistEmptyUsername.xml");
    private static final Path EMPTYPASSWORD_ACCOUNTFILE = TEST_DATA_FOLDER.resolve("accountlistEmptyPassword.xml");
    private static final Path EMPTYROLE_ACCOUNTFILE = TEST_DATA_FOLDER.resolve("accountlistEmptyRole.xml");

    private static final String USERNAME = "whiterose";
    private static final String OLD_PASSWORD = "@myPassword";
    private static final String OLD_HASHEDPASSWORD =
            PasswordAuthentication.getHashedPasswordFromPlainText(OLD_PASSWORD);
    private static final String NEW_PASSWORD = "someN3wP@ssw0rd";
    private static final String NEW_HASHEDPASSWORD =
            PasswordAuthentication.getHashedPasswordFromPlainText(NEW_PASSWORD);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, AddressBook.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, AddressBook.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        AddressBook dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableAddressBook.class).toModelType();
        assertEquals(9, dataFromFile.getContactList().size());
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithMissingPersonField_validResult() throws Exception {
        XmlAdaptedContact actualPerson = XmlUtil.getDataFromFile(
                MISSING_PERSON_FIELD_FILE, XmlAdaptedContactWithRootElement.class);
        XmlAdaptedContact expectedPerson = new XmlAdaptedContact(VALID_ID, null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS, VALID_CONTACT_TYPE);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithInvalidPersonField_validResult() throws Exception {
        XmlAdaptedContact actualPerson = XmlUtil.getDataFromFile(
                INVALID_PERSON_FIELD_FILE, XmlAdaptedContactWithRootElement.class);
        XmlAdaptedContact expectedPerson = new XmlAdaptedContact(VALID_ID, VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS, VALID_CONTACT_TYPE);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithValidPerson_validResult() throws Exception {
        XmlAdaptedContact actualPerson = XmlUtil.getDataFromFile(
                VALID_PERSON_FILE, XmlAdaptedContactWithRootElement.class);
        XmlAdaptedContact expectedPerson = new XmlAdaptedContact(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS, VALID_CONTACT_TYPE);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new AddressBook());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new AddressBook());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableAddressBook dataToWrite = new XmlSerializableAddressBook(new AddressBook());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);

        AddressBookBuilder builder = new AddressBookBuilder(new AddressBook());
        dataToWrite = new XmlSerializableAddressBook(
                builder.withPerson(new ClientBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableAddressBook.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    @Test
    public void updatePasswordInFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.updatePasswordInFile(null, USERNAME, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_nullUsername_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.updatePasswordInFile(VALID_ACCOUNTFILE, null, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_nullPassword_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.updatePasswordInFile(VALID_ACCOUNTFILE, USERNAME, null);
    }

    @Test
    public void updatePasswordInFile_invalidFile_throwsFileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.updatePasswordInFile(INVALID_ACCOUNTFILE, USERNAME, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_emptyFile_throwsDataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.updatePasswordInFile(EMPTY_FILE, USERNAME, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_emptyUsername_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        XmlUtil.updatePasswordInFile(EMPTYUSERNAME_ACCOUNTFILE, USERNAME, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_emptyPassword_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        XmlUtil.updatePasswordInFile(EMPTYPASSWORD_ACCOUNTFILE, USERNAME, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_emptyRole_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        XmlUtil.updatePasswordInFile(EMPTYROLE_ACCOUNTFILE, USERNAME, NEW_PASSWORD);
    }

    @Test
    public void updatePasswordInFile_validFile_validResult() throws Exception {
        int accountIndex = 2;
        XmlSerializableAccountList oldXmlSerializableAccountList = XmlUtil
                .getDataFromFile(VALID_ACCOUNTFILE, XmlSerializableAccountList.class);

        // this is the old password before change
        String oldPassword = oldXmlSerializableAccountList.toModelType().getList().get(accountIndex).getPassword();
        assertPasswordCorrect(OLD_PASSWORD, oldPassword);

        // change password to NEW_PASSWORD
        XmlUtil.updatePasswordInFile(VALID_ACCOUNTFILE, USERNAME, NEW_HASHEDPASSWORD);

        // this is the new password after changing password
        XmlSerializableAccountList newXmlSerializableAccountList = XmlUtil
                .getDataFromFile(VALID_ACCOUNTFILE, XmlSerializableAccountList.class);

        String newPassword = newXmlSerializableAccountList.toModelType().getList().get(accountIndex).getPassword();
        assertPasswordCorrect(NEW_PASSWORD, newPassword);

        // change new password back to old password
        Account newAccount = new Account(TypicalAccount.ROSE.getUserName(), newPassword, TypicalAccount.ROSE.getRole());
        XmlUtil.updatePasswordInFile(VALID_ACCOUNTFILE, newAccount.getUserName(), OLD_HASHEDPASSWORD);
        XmlSerializableAccountList xmlSerializableAccountList = XmlUtil
                .getDataFromFile(VALID_ACCOUNTFILE, XmlSerializableAccountList.class);
        String newPassword2 = xmlSerializableAccountList.toModelType().getList().get(accountIndex).getPassword();
        assertPasswordCorrect(OLD_PASSWORD, newPassword2);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedContact}
     * objects.
     */
    // TODO: change test cases to fit context
    @XmlRootElement(name = "person")
    private static class XmlAdaptedContactWithRootElement extends XmlAdaptedContact {
    }
}
