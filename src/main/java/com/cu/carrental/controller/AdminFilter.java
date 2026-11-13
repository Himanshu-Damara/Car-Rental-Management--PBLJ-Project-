package com.cu.carrental.controller;

import com.cu.carrental.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
  public void doFilter(ServletRequest q, ServletResponse p, FilterChain c)
      throws IOException, ServletException {
    HttpServletRequest r = (HttpServletRequest) q;
    HttpServletResponse s = (HttpServletResponse) p;
    User u = (User) r.getSession().getAttribute("user");
    if (u == null) {
      s.sendRedirect(r.getContextPath() + "/login?next=admin");
      return;
    }
    if (!"ADMIN".equals(u.getRole())) {
      s.sendError(403, "Administrator access required");
      return;
    }
    c.doFilter(q, p);
  }
}
