package com.cu.carrental.controller;

import com.cu.carrental.model.*;
import com.cu.carrental.service.RentalService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.time.*;
import java.util.*;

@WebServlet(
    urlPatterns = {
      "/home",
      "/cars",
      "/login",
      "/register",
      "/logout",
      "/bookings",
      "/book",
      "/cancel",
      "/admin/*"
    })
public class RentalServlet extends HttpServlet {
  private final RentalService svc = new RentalService();

  private void go(HttpServletRequest r, HttpServletResponse s, String view)
      throws ServletException, IOException {
    r.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(r, s);
  }

  private void flash(HttpServletRequest r, String m) {
    r.setAttribute("message", m);
  }

  private String route(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    return request.getServletPath() + (pathInfo == null ? "" : pathInfo);
  }

  protected void doGet(HttpServletRequest r, HttpServletResponse s)
      throws ServletException, IOException {
    String p = route(r);
    try {
      switch (p) {
        case "/home", "/cars" -> {
          String q = r.getParameter("q"), category = r.getParameter("category");
          List<Car> cars =
              svc.repo().cars().stream()
                  .filter(Car::isAvailable)
                  .filter(
                      c ->
                          q == null
                              || q.isBlank()
                              || c.getName().toLowerCase().contains(q.toLowerCase())
                              || c.getCategory().equalsIgnoreCase(q))
                  .filter(
                      c ->
                          category == null
                              || category.isBlank()
                              || c.getCategory().equalsIgnoreCase(category))
                  .toList();
          r.setAttribute("cars", cars);
          r.setAttribute(
              "categories", svc.repo().cars().stream().map(Car::getCategory).distinct().toList());
          go(r, s, p.equals("/home") ? "customer/home" : "customer/cars");
        }
        case "/login" -> go(r, s, "customer/login");
        case "/register" -> go(r, s, "customer/register");
        case "/bookings" -> {
          User u = (User) r.getSession().getAttribute("user");
          if (u == null) {
            s.sendRedirect("login");
            return;
          }
          r.setAttribute(
              "bookings",
              svc.enriched().stream().filter(b -> b.getUserId().equals(u.getId())).toList());
          go(r, s, "customer/bookings");
        }
        case "/admin/dashboard" -> {
          r.setAttribute("cars", svc.repo().cars());
          r.setAttribute("users", svc.repo().users());
          r.setAttribute("bookings", svc.enriched());
          r.setAttribute("revenue", svc.revenue());
          go(r, s, "admin/dashboard");
        }
        case "/admin/cars" -> {
          r.setAttribute("cars", svc.repo().cars());
          go(r, s, "admin/cars");
        }
        case "/admin/customers" -> {
          r.setAttribute(
              "users",
              svc.repo().users().stream().filter(u -> u.getRole().equals("CUSTOMER")).toList());
          go(r, s, "admin/customers");
        }
        default -> {
          s.sendError(404);
        }
      }
    } catch (RuntimeException e) {
      getServletContext().log("Unable to load route " + p, e);
      s.sendError(500, "Unable to load requested data");
    }
  }

  protected void doPost(HttpServletRequest r, HttpServletResponse s)
      throws ServletException, IOException {
    String p = route(r);
    try {
      switch (p) {
        case "/login" -> {
          User u = svc.login(val(r, "email"), val(r, "password"));
          if (u == null) {
            flash(r, "Email or password is incorrect.");
            go(r, s, "customer/login");
            return;
          }
          r.changeSessionId();
          r.getSession().setAttribute("user", u);
          String next = r.getParameter("next");
          s.sendRedirect(
              r.getContextPath()
                  + ("admin".equals(next) && u.getRole().equals("ADMIN")
                      ? "/admin/dashboard"
                      : "/"));
        }
        case "/register" -> {
          svc.register(val(r, "name"), val(r, "email"), val(r, "phone"), val(r, "password"));
          flash(r, "Account created. Please sign in.");
          go(r, s, "customer/login");
        }
        case "/logout" -> {
          r.getSession().invalidate();
          s.sendRedirect("");
        }
        case "/book" -> {
          User u = (User) r.getSession().getAttribute("user");
          if (u == null) {
            s.sendRedirect("login");
            return;
          }
          Booking b =
              svc.book(
                  u.getId(),
                  val(r, "carId"),
                  LocalDate.parse(val(r, "pickupDate")),
                  LocalDate.parse(val(r, "returnDate")));
          r.setAttribute("booking", b);
          go(r, s, "customer/confirmation");
        }
        case "/cancel" -> {
          User u = (User) r.getSession().getAttribute("user");
          String id = val(r, "id");
          for (Booking b : svc.repo().bookings())
            if (b.getId().equals(id)
                && b.getUserId().equals(u.getId())
                && b.getStatus().equals("PENDING")) {
              b.setStatus("CANCELLED");
              svc.repo().saveBooking(b);
            }
          s.sendRedirect("bookings");
        }
        case "/admin/car/save" -> {
          Car c = new Car();
          c.setId(val(r, "id").isBlank() ? "CU" + System.currentTimeMillis() : val(r, "id"));
          c.setMake(val(r, "make"));
          c.setModel(val(r, "model"));
          c.setCategory(val(r, "category"));
          c.setImage(val(r, "image"));
          c.setSeats(Integer.parseInt(val(r, "seats")));
          c.setYear(Integer.parseInt(val(r, "year")));
          c.setPricePerDay(new java.math.BigDecimal(val(r, "price")));
          c.setTransmission(val(r, "transmission"));
          c.setFuel(val(r, "fuel"));
          c.setAvailable("true".equals(val(r, "available")));
          svc.repo().saveCar(c);
          s.sendRedirect("../cars");
        }
        case "/admin/car/delete" -> {
          svc.repo().deleteCar(val(r, "id"));
          s.sendRedirect("../cars");
        }
        case "/admin/booking/status" -> {
          String id = val(r, "id"), status = val(r, "status");
          if (!List.of("APPROVED", "REJECTED", "COMPLETED").contains(status))
            throw new IllegalArgumentException();
          for (Booking b : svc.repo().bookings())
            if (b.getId().equals(id)) {
              b.setStatus(status);
              svc.repo().saveBooking(b);
            }
          s.sendRedirect("dashboard");
        }
        default -> s.sendError(404);
      }
    } catch (Exception e) {
      r.setAttribute(
          "message",
          e instanceof IllegalArgumentException
              ? e.getMessage()
              : "The request could not be completed.");
      if (p.equals("/book")) {
        r.setAttribute("cars", svc.repo().cars());
        go(r, s, "customer/cars");
      } else if (p.equals("/register")) go(r, s, "customer/register");
      else s.sendError(400, "Invalid request");
    }
  }

  private String val(HttpServletRequest r, String k) {
    String v = r.getParameter(k);
    return v == null ? "" : v.trim();
  }
}
