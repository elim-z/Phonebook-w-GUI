import java.io.*;
import java.util.TreeMap;

/*
 * PhoneBookDriver.java
 *
 * This class represents the "address book itself" (from the assignment).
 * It stores all contacts in a TreeMap.
 *
 * IMPORTANT:
 * - TreeMap is a self-balancing BST (it uses a Red-Black Tree internally).
 * - Key   = person's name (we treat it as unique)
 * - Value = Person object
 */
public class PhoneBookDriver implements Serializable {

    private static final long serialVersionUID = 1L;

    // Self-balancing BST (Red-Black Tree) storing contacts
    private TreeMap<String, Person> phoneBook;

    // When the program starts, we create a NEW empty phone book.
    public PhoneBookDriver() {
        phoneBook = new TreeMap<>();
    }

    /*
     * Add a new contact.
     * Returns true if added successfully.
     * Returns false if the name already exists (duplicate key).
     */
    public boolean addContact(String name, String address, String phone) {
        if (phoneBook.containsKey(name)) {
            return false; // duplicate name not allowed
        }
        phoneBook.put(name, new Person(name, address, phone));
        return true;
    }

    /*
     * Remove a contact by name.
     * Returns true if removed, false if the contact did not exist.
     */
    public boolean removeContact(String name) {
        return phoneBook.remove(name) != null;
    }

    /*
     * Modify an existing contact.
     *
     * Rubric says: update address and/or phone number.
     * So we update ONLY the fields that are NOT empty.
     *
     * Returns true if the contact exists and at least one field was updated.
     * Returns false if contact does not exist OR no new info was provided.
     */
    public boolean modifyContact(String name, String newAddress, String newPhone) {
        Person p = phoneBook.get(name);
        if (p == null) return false;

        boolean updated = false;

        if (newAddress != null && !newAddress.trim().isEmpty()) {
            p.setAddress(newAddress.trim());
            updated = true;
        }

        if (newPhone != null && !newPhone.trim().isEmpty()) {
            p.setPhoneNumber(newPhone.trim());
            updated = true;
        }

        return updated;
    }

    // Search a contact by name (returns null if not found)
    public Person getContact(String name) {
        return phoneBook.get(name);
    }

    // Return the whole phone book (for printing/display)
    public TreeMap<String, Person> getAllContacts() {
        return phoneBook;
    }

    /*
     * Save the phone book to a file using serialization.
     * We save the TreeMap (and each Person inside it).
     */
    public void saveToFile(File file) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(phoneBook);
        }
    }

    /*
     * Load the phone book from a file using serialization.
     * This replaces the current TreeMap with the loaded one.
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            phoneBook = (TreeMap<String, Person>) obj;
        }
    }
}
