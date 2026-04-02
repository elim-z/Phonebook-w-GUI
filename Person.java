import java.io.Serializable;

/*
 * Person.java
 * Represents one contact in the phone book.
 * 
 * NOTE: We implement Serializable so we can save/load the phone book
 * using ObjectOutputStream / ObjectInputStream.
 */
public class Person implements Serializable {

    // Good practice when using Serializable (not required, but helpful).
    private static final long serialVersionUID = 1L;

    // Basic contact fields
    private String name;
    private String address;
    private String phoneNumber;

    // Constructor
    public Person(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters (used when modifying a contact)
    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // How we want a contact to print on screen
    @Override
    public String toString() {
        return name + "\n" + address + "\n" + phoneNumber;
    }
}
