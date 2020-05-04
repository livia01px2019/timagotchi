package edu.brown.cs.final_project.timagotchi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class PasswordHashingTest {

  /**
   * Test that SHA256 hashing works and is deterministic.
   *
   * @throws NoSuchAlgorithmException
   */
  @Test
  public void hashingDeterministicTest() throws NoSuchAlgorithmException {
    assertEquals(PasswordHashing.hashSHA256("hello"),
        "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
    assertEquals(PasswordHashing.hashSHA256(""),
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
    assertEquals(PasswordHashing.hashSHA256("123"),
        "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
  }

  /**
   * Test that SHA256 hashing comparison works, which is used when we verify
   * passwords.
   *
   * @throws NoSuchAlgorithmException
   */
  @Test
  public void hashingComparisonTest() throws NoSuchAlgorithmException {
    assertTrue(PasswordHashing.hashSHA256("hello")
        .equals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"));
    assertTrue(PasswordHashing.hashSHA256("")
        .equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
    assertTrue(PasswordHashing.hashSHA256("123")
        .equals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3"));
  }

}
