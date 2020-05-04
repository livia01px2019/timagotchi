package edu.brown.cs.final_project.timagotchi.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that hashes the passwords of users.
 */
public final class PasswordHashing {
  private PasswordHashing() {
  }

  public static String hashSHA256(String pass) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(pass.getBytes(StandardCharsets.UTF_8));
    byte[] digest = md.digest();
    String passwordHash = String.format("%064x", new BigInteger(1, digest));
    return passwordHash;
  }
}
