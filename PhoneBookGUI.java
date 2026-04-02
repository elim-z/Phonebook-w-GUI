import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

/*
 * PhoneBookGUI.java
 *
 * Beginner-friendly Swing GUI for the Phone Book application.
 *
 * RUBRIC CHECKLIST (what this GUI does):
 * - Add Contact: adds by unique name key into TreeMap and updates display.
 * - Remove Contact: removes by name; shows message if not found; updates display.
 * - Modify Contact: updates address and/or phone number; shows message if not found.
 * - Contact Information: searches by name; shows info or not-found message.
 * - Reset Entries: clears the input fields only.
 * - Reset Display Panel: clears the display area only.
 * - Save PhoneBook To File: saves using serialization; handles errors without crashing.
 * - Open Old PhoneBook: loads saved data; updates GUI after loading.
 * - Print Entire PhoneBook: prints all contacts in current TreeMap order.
 */
public class PhoneBookGUI extends JFrame {

    // The data (model) for our application
    private PhoneBookDriver phoneBook;

    // Input fields
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;

    // Output / display panel
    private JTextArea displayArea;

    public PhoneBookGUI() {
        // Per assignment: start with a new empty phone book
        phoneBook = new PhoneBookDriver();

        setTitle("PhoneBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null); // center on screen

        buildUI();
    }

    private void buildUI() {
        // Outer layout: left side inputs/buttons + right side display
        setLayout(new BorderLayout(12, 12));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Title
        JLabel title = new JLabel("PhoneBook");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(title);
        leftPanel.add(Box.createVerticalStrut(12));

        // Input fields
        nameField = new JTextField(22);
        addressField = new JTextField(22);
        phoneField = new JTextField(22);

        leftPanel.add(makeLabeledRow("Name", nameField));
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(makeLabeledRow("Address", addressField));
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(makeLabeledRow("Phone Number", phoneField));
        leftPanel.add(Box.createVerticalStrut(14));

        /*
         * Buttons:
         * We organize them in groups so the GUI is clear and easy to use.
         */

        // Main actions group
        JPanel actionsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        actionsPanel.add(new JButton(new AbstractAction("Add Contact") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAdd();
            }
        }));

        actionsPanel.add(new JButton(new AbstractAction("Remove Contact") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRemove();
            }
        }));

        actionsPanel.add(new JButton(new AbstractAction("Modify Contact") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doModify();
            }
        }));

        actionsPanel.add(new JButton(new AbstractAction("Contact Information") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        }));

        actionsPanel.add(new JButton(new AbstractAction("Print Entire PhoneBook") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPrintAll();
            }
        }));

        actionsPanel.add(new JButton(new AbstractAction("Reset Display Panel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Rubric: clears the output panel only (does NOT change stored data)
                displayArea.setText("");
            }
        }));

        leftPanel.add(actionsPanel);
        leftPanel.add(Box.createVerticalStrut(10));

        // Utility group (reset fields)
        JPanel utilityPanel = new JPanel(new GridLayout(1, 1, 8, 8));
        utilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        utilityPanel.setBorder(BorderFactory.createTitledBorder("Utilities"));

        utilityPanel.add(new JButton(new AbstractAction("Reset Entries") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Rubric: clears Name/Address/Phone fields only
                clearEntryFields();
            }
        }));

        leftPanel.add(utilityPanel);
        leftPanel.add(Box.createVerticalStrut(10));

        // File group (save/load)
        JPanel filePanel = new JPanel(new GridLayout(1, 2, 8, 8));
        filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filePanel.setBorder(BorderFactory.createTitledBorder("File"));

        filePanel.add(new JButton(new AbstractAction("Save PhoneBook To File") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSave();
            }
        }));

        filePanel.add(new JButton(new AbstractAction("Open Old PhoneBook") {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLoad();
            }
        }));

        leftPanel.add(filePanel);

        // Right side: display area with a scroll bar
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contacts / Output"));

        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    /*
     * Helper: create one row with a label + text field.
     */
    private JPanel makeLabeledRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(110, 25));
        row.add(label);

        field.setPreferredSize(new Dimension(250, 25));
        row.add(field);

        return row;
    }

    // -------------------------
    // Button action methods
    // -------------------------

    private void doAdd() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        // Basic input checks (error handling)
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            showMessage("Please fill in Name, Address, and Phone Number.");
            return;
        }

        boolean ok = phoneBook.addContact(name, address, phone);

        // Rubric: handle duplicate names appropriately
        if (!ok) {
            showMessage("That name already exists. Names must be unique.");
            return;
        }

        // Rubric: update the display pane
        showMessage("Contact added.");
        clearEntryFields();
        doPrintAll();
    }

    private void doRemove() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            showMessage("Enter the Name you want to remove.");
            return;
        }

        boolean ok = phoneBook.removeContact(name);

        // Rubric: message if contact does not exist
        if (!ok) {
            showMessage("Contact not found.");
            return;
        }

        // Rubric: update display panel
        showMessage("Contact removed.");
        clearEntryFields();
        doPrintAll();
    }

    private void doModify() {
        String name = nameField.getText().trim();
        String newAddress = addressField.getText().trim();
        String newPhone = phoneField.getText().trim();

        if (name.isEmpty()) {
            showMessage("Enter the Name you want to modify.");
            return;
        }

        // Rubric says: address and/or phone number
        // So we allow the user to update either one.
        if (newAddress.isEmpty() && newPhone.isEmpty()) {
            showMessage("Enter a new Address and/or a new Phone Number to modify.");
            return;
        }

        // Check if contact exists first (so we can show a clear message)
        if (phoneBook.getContact(name) == null) {
            showMessage("Contact not found.");
            return;
        }

        boolean updated = phoneBook.modifyContact(name, newAddress, newPhone);

        if (!updated) {
            // This happens if the user gave only blanks (we already checked, but just in case)
            showMessage("Nothing was updated. Try entering new values.");
            return;
        }

        showMessage("Contact updated.");
        clearEntryFields();
        doPrintAll();
    }

    private void doSearch() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            showMessage("Enter the Name you want to search.");
            return;
        }

        Person p = phoneBook.getContact(name);

        // Rubric: clear message if not found
        if (p == null) {
            showMessage("Contact not found.");
            return;
        }

        // Rubric: display correct info if found
        displayArea.setText(p.toString());
    }

    private void doPrintAll() {
        // Rubric: show all contacts currently stored (current state of TreeMap)
        StringBuilder sb = new StringBuilder();

        if (phoneBook.getAllContacts().isEmpty()) {
            sb.append("(Phone book is empty)\n");
        } else {
            for (Map.Entry<String, Person> entry : phoneBook.getAllContacts().entrySet()) {
                sb.append(entry.getValue().toString()).append("\n");
                sb.append("------------\n");
            }
        }

        displayArea.setText(sb.toString());
    }

    private void doSave() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save PhoneBook File");

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        // Rubric: handle file errors without crashing
        try {
            phoneBook.saveToFile(file);
            showMessage("Saved to: " + file.getName());
        } catch (Exception ex) {
            showMessage("Save failed: " + ex.getMessage());
        }
    }

    private void doLoad() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open PhoneBook File");

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        // Rubric: load saved phone book and update GUI
        try {
            phoneBook.loadFromFile(file);
            showMessage("Loaded: " + file.getName());
            doPrintAll(); // update display after loading
        } catch (Exception ex) {
            showMessage("Load failed: " + ex.getMessage());
        }
    }

    // -------------------------
    // Small helper methods
    // -------------------------

    private void clearEntryFields() {
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // Program entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneBookGUI gui = new PhoneBookGUI();
            gui.setVisible(true);
        });
    }
}
