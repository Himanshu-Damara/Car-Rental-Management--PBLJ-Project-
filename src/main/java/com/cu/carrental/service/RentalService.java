package com.cu.carrental.service;

import com.cu.carrental.model.*;
import com.cu.carrental.repository.XmlRepository;
import com.cu.carrental.util.PasswordUtil;
import java.math.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RentalService {
  private final XmlRepository repo = new XmlRepository();

  public XmlRepository repo() {
    return repo;
  }

  public User login(String email, String pass) {
    User u = repo.byEmail(email);
    return u != null && PasswordUtil.verify(pass, u.getPasswordHash()) ? u : null;
  }

  public void register(String name, String email, String phone, String pass) {
    if (name == null
        || name.isBlank()
        || email == null
        || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        || pass == null
        || pass.length() < 8
        || phone == null
        || phone.isBlank())
      throw new IllegalArgumentException(
          "Enter a valid name, email, phone and password of at least 8 characters.");
    if (repo.byEmail(email) != null)
      throw new IllegalArgumentException("An account already exists for that email.");
    repo.addUser(
        new User(
            UUID.randomUUID().toString(),
            name.trim(),
            email.trim(),
            PasswordUtil.hash(pass),
            "CUSTOMER",
            phone.trim()));
  }

  public Booking book(String user, String carId, LocalDate from, LocalDate to) {
    synchronized (repo) {
      Car c = repo.car(carId);
      if (c == null || !c.isAvailable())
        throw new IllegalArgumentException("This car is unavailable.");
      if (from == null || to == null || from.isBefore(LocalDate.now()) || !to.isAfter(from))
        throw new IllegalArgumentException("Choose valid pickup and return dates.");
      for (Booking b : repo.bookings())
        if (b.getCarId().equals(carId)
            && !b.getStatus().equals("REJECTED")
            && !b.getStatus().equals("CANCELLED")
            && from.isBefore(b.getReturnDate())
            && to.isAfter(b.getPickupDate()))
          throw new IllegalArgumentException("Those dates overlap an existing booking.");
      long days = ChronoUnit.DAYS.between(from, to);
      Booking b = new Booking();
      b.setId("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
      b.setUserId(user);
      b.setCarId(carId);
      b.setPickupDate(from);
      b.setReturnDate(to);
      b.setTotalPrice(c.getPricePerDay().multiply(BigDecimal.valueOf(days)));
      b.setStatus("PENDING");
      repo.saveBooking(b);
      return b;
    }
  }

  public List<Booking> enriched() {
    List<Booking> bs = repo.bookings();
    for (Booking b : bs) {
      Car c = repo.car(b.getCarId());
      User u =
          repo.users().stream()
              .filter(x -> x.getId().equals(b.getUserId()))
              .findFirst()
              .orElse(null);
      b.setCarName(c == null ? "Removed car" : c.getName());
      b.setCustomerName(u == null ? "Unknown" : u.getName());
    }
    return bs;
  }

  public long revenue() {
    return enriched().stream()
        .filter(b -> b.getStatus().equals("APPROVED") || b.getStatus().equals("COMPLETED"))
        .map(Booking::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .longValue();
  }
}
