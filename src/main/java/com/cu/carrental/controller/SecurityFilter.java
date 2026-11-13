package com.cu.carrental.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

@WebFilter("/*")
public class SecurityFilter implements Filter {
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest r = (HttpServletRequest) req;
    HttpServletResponse s = (HttpServletResponse) res;
    s.setHeader("X-Content-Type-Options", "nosniff");
    s.setHeader("X-Frame-Options", "DENY");
    s.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
    HttpSession session = r.getSession(true);
    String token = (String) session.getAttribute("csrf");
    if (token == null) {
      token = UUID.randomUUID().toString();
      session.setAttribute("csrf", token);
    }
    r.setAttribute("csrf", token);
    if (!r.getMethod().equalsIgnoreCase("GET") && !r.getMethod().equalsIgnoreCase("HEAD")) {
      String submitted = r.getParameter("_csrf");
      if (submitted == null
          || !java.security.MessageDigest.isEqual(token.getBytes(), submitted.getBytes())) {
        s.sendError(403, "Invalid CSRF token");
        return;
      }
    }
    chain.doFilter(req, res);
  }
}
