package com.eventra.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password Hash Generator (PasswordHashGenerator.java)
 * 
 * This class is a utility tool that helps developers create secure password hashes
 * for testing and development purposes. Think of it like a "password security tester"
 * that shows you how passwords are safely stored in the database.
 * 
 * WHAT THIS CLASS DOES:
 * - Takes a plain text password and converts it to a secure hash
 * - Demonstrates how password hashing works in the Eventra system
 * - Verifies that the hashing process works correctly
 * - Helps developers test password security features
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Password Hashing: Converting passwords into unreadable, secure codes
 * - BCrypt: A strong encryption algorithm that makes passwords very hard to crack
 * - Salt: Random data added to passwords to make them even more secure
 * - Verification: Proving that a password matches its hash without storing the original
 * 
 * SECURITY FEATURES:
 * - Uses BCrypt, one of the strongest password hashing algorithms
 * - Automatically generates random "salt" for each password
 * - Makes it impossible to reverse-engineer original passwords
 * - Protects user accounts even if the database is compromised
 * 
 * WHEN TO USE THIS:
 * - During development to test password hashing
 * - To create test user accounts with secure passwords
 * - To demonstrate password security to stakeholders
 * - To verify that password systems are working correctly
 * 
 * IMPORTANT: This is a development tool, not for production use!
 * In the real application, passwords are hashed automatically when users register.
 */
public class PasswordHashGenerator {
    
    /**
     * main() - Command Line Password Hashing Tool
     * 
     * This is the main method that runs when you execute this class from the command line.
     * It's like a simple command-line tool that takes a password and shows you its secure hash.
     * 
     * WHAT IT DOES:
     * - Reads a password from the command line arguments
     * - Converts the password to a secure BCrypt hash
     * - Shows you both the original password and the hash
     * - Verifies that the hashing process works correctly
     * 
     * HOW TO USE:
     * 1. Compile the class: javac PasswordHashGenerator.java
     * 2. Run with a password: java PasswordHashGenerator mypassword123
     * 3. Copy the hash for use in your application
     * 
     * EXAMPLE OUTPUT:
     * Password: mypassword123
     * Hashed Password: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
     * Hash verification: true
     * 
     * @param args Command line arguments - first argument should be the password to hash
     */
    public static void main(String[] args) {
        // 
        // INPUT VALIDATION - Check if user provided a password
        // 
        // This checks if the user actually typed a password when running the program.
        // If no password is provided, we show helpful usage instructions.
        // 
        // THINK OF IT LIKE:
        // - A helpful error message when you forget to provide required information
        // - Like a form that tells you "this field is required" when you leave it empty
        // 
        if (args.length == 0) {
            // 
            // USAGE INSTRUCTIONS - Help users understand how to use the tool
            // 
            // These messages explain how to use the program correctly.
            // They're like instruction manual text that guides users.
            // 
            System.out.println("Usage: java PasswordHashGenerator <password>");
            System.out.println("Example: java PasswordHashGenerator mypassword123");
            return; // Exit the program early since no password was provided
        }
        
        // 
        // PASSWORD EXTRACTION - Get the password from command line
        // 
        // This takes the first thing the user typed after the program name
        // and treats it as the password to hash.
        // 
        // THINK OF IT LIKE:
        // - Reading what someone typed in a text field
        // - Taking the first word from a sentence
        // 
        String password = args[0];
        
        // 
        // PASSWORD HASHING - Convert password to secure hash
        // 
        // This is the main security operation that makes passwords safe to store.
        // BCrypt.hashpw() takes the plain text password and converts it to an
        // unreadable, secure hash that can't be reversed.
        // 
        // HOW IT WORKS:
        // 1. BCrypt.gensalt() creates random "salt" (extra random data)
        // 2. BCrypt.hashpw() combines the password with the salt
        // 3. The result is a long, secure string that's impossible to crack
        // 
        // SECURITY BENEFITS:
        // - Even if two users have the same password, their hashes will be different
        // - The salt makes it extremely hard to use pre-computed attack tables
        // - BCrypt is designed to be slow, making brute force attacks impractical
        // 
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        // 
        // OUTPUT DISPLAY - Show the results to the user
        // 
        // This displays both the original password and its secure hash.
        // The hash is what you would store in a database instead of the plain password.
        // 
        // THINK OF IT LIKE:
        // - A receipt showing what you bought and how much it cost
        // - A confirmation message showing what was processed
        // 
        System.out.println("Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
        
        // 
        // HASH VERIFICATION - Prove the hashing process works
        // 
        // This step verifies that the hashing process is working correctly.
        // It takes the original password and the hash, then checks if they match.
        // 
        // WHY THIS IS IMPORTANT:
        // - Proves that the hashing algorithm is working properly
        // - Shows that the same password will always produce the same verification result
        // - Demonstrates how the login system will work in the real application
        // 
        // HOW VERIFICATION WORKS:
        // 1. BCrypt.checkpw() takes the plain password and the hash
        // 2. It applies the same hashing process to the plain password
        // 3. It compares the result with the stored hash
        // 4. Returns true if they match, false if they don't
        // 
        boolean isValid = BCrypt.checkpw(password, hashedPassword);
        System.out.println("Hash verification: " + isValid);
    }
} 