package com.cu.carrental.util;

import java.security.*;
import java.util.*;

public final class PasswordUtil {
  private PasswordUtil() {}

  public static String hash(String p) {
    try {
      byte[] s = new byte[16];
      new SecureRandom().nextBytes(s);
      return "pbkdf2$120000$"
          + Base64.getEncoder().encodeToString(s)
          + "$"
          + Base64.getEncoder().encodeToString(derive(p.toCharArray(), s, 120000));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static boolean verify(String p, String h) {
    try {
      String[] a = h.split("\\$");
      return a.length == 4
          && a[0].equals("pbkdf2")
          && MessageDigest.isEqual(
              derive(p.toCharArray(), Base64.getDecoder().decode(a[2]), Integer.parseInt(a[1])),
              Base64.getDecoder().decode(a[3]));
    } catch (Exception e) {
      return false;
    }
  }

  private static byte[] derive(char[] p, byte[] s, int n) throws Exception {
    var k = new javax.crypto.spec.PBEKeySpec(p, s, n, 256);
    try {
      return javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
          .generateSecret(k)
          .getEncoded();
    } finally {
      k.clearPassword();
    }
  }
}
