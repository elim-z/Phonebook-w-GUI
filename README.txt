PhoneBook Application (CS 302 - Lab Assignment 3)

FILES INCLUDED
- Person.java
- PhoneBookDriver.java
- PhoneBookGUI.java

HOW TO RUN (Terminal)
1) Open a terminal in this folder.
2) Compile:
   javac Person.java PhoneBookDriver.java PhoneBookGUI.java
3) Run:
   java PhoneBookGUI

WHAT THIS PROGRAM DOES
- Stores contacts in a TreeMap (self-balancing Red-Black Tree).
- Name is the unique key.
- Add / Remove / Modify / Search contacts.
- Print all contacts (alphabetical order).
- Save to file / Load from file using Java serialization.

NOTES
- For Save/Open, choose any file name (example: phonebook.dat).
- When the app starts, it begins with an empty phone book (per assignment).
