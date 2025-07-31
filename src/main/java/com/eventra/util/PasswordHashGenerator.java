package com.eventra.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java PasswordHashGenerator <password>");
            System.out.println("Example: java PasswordHashGenerator mypassword123");
            return;
        }
        
        String password = args[0];
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        System.out.println("Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
        
        // Verify the hash works
        boolean isValid = BCrypt.checkpw(password, hashedPassword);
        System.out.println("Hash verification: " + isValid);
    }
} 