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

  /**
   * Method to hash a String using the SHA256 algorithm.
   *
   * @param pass The string to be hashed.
   * @return The hashed string.
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   */
  public static String hashSHA256(String pass) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(pass.getBytes(StandardCharsets.UTF_8));
    byte[] digest = md.digest();
    String passwordHash = String.format("%064x", new BigInteger(1, digest));
    return passwordHash;
  }
}
