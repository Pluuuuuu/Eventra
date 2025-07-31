package com.eventra.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        String password = "staff@gmail.com";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        System.out.println("Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
        
        // Verify the hash works
        boolean isValid = BCrypt.checkpw(password, hashedPassword);
        System.out.println("Hash verification: " + isValid);
    }
} 