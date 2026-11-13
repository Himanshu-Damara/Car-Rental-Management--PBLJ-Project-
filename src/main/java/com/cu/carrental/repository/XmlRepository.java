package com.cu.carrental.repository;

import com.cu.carrental.model.*;
import com.cu.carrental.util.PasswordUtil;
import java.math.*;
import java.nio.file.*;
import java.util.*;
import org.w3c.dom.*;

public class XmlRepository {
  private final Path dir;

  public XmlRepository() {
    String p =
        System.getProperty(
            "carrental.data.dir",
            System.getenv()
                .getOrDefault(
                    "CAR_RENTAL_DATA", System.getProperty("user.home") + "/car-rental-data"));
    dir = Paths.get(p).toAbsolutePath();
    ensureSeed();
  }

  private Path file(String n) {
    return dir.resolve(n);
  }

  private void ensureSeed() {
    synchronized (XmlStore.class) {
      Document u = XmlStore.read(file("users.xml"), "users");
      if (u.getDocumentElement().getElementsByTagName("user").getLength() == 0) {
        addUser(
            new User(
                "admin",
                "System Administrator",
                "admin@admin",
                PasswordUtil.hash("admin"),
                "ADMIN",
                "+91 98765 43210"));
        addUser(
            new User(
                "demo",
                "Aarav Sharma",
                "demo@demo",
                PasswordUtil.hash("demo"),
                "CUSTOMER",
                "+91 98765 43211"));
      }
      Document c = XmlStore.read(file("cars.xml"), "cars");
      if (c.getDocumentElement().getElementsByTagName("car").getLength() == 0) {
        for (String[] x :
            new String[][] {
              {
                "CU101",
                "Audi",
                "Q7",
                "SUV",
                "7",
                "Automatic",
                "Diesel",
                "2024",
                "2499",
                "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?auto=format&fit=crop&w=1200&q=85"
              },
              {
                "CU102",
                "Mercedes-Benz",
                "CLA",
                "Sedan",
                "5",
                "Automatic",
                "Petrol",
                "2023",
                "1899",
                "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=1200&q=85"
              },
              {
                "CU103",
                "Fiat",
                "500",
                "Hatchback",
                "4",
                "Manual",
                "Petrol",
                "2024",
                "1299",
                "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=1200&q=85"
              },
              {
                "CU104",
                "Porsche",
                "Panamera",
                "Sedan",
                "4",
                "Automatic",
                "Petrol",
                "2024",
                "3299",
                "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=1200&q=85"
              }
            }) {
          Car a = new Car();
          a.setId(x[0]);
          a.setMake(x[1]);
          a.setModel(x[2]);
          a.setCategory(x[3]);
          a.setSeats(Integer.parseInt(x[4]));
          a.setTransmission(x[5]);
          a.setFuel(x[6]);
          a.setYear(Integer.parseInt(x[7]));
          a.setPricePerDay(new BigDecimal(x[8]));
          a.setImage(x[9]);
          a.setAvailable(true);
          saveCar(a);
        }
      }
    }
  }

  public synchronized List<User> users() {
    Document d = XmlStore.read(file("users.xml"), "users");
    List<User> out = new ArrayList<>();
    NodeList ns = d.getDocumentElement().getElementsByTagName("user");
    for (int i = 0; i < ns.getLength(); i++) {
      Element e = (Element) ns.item(i);
      out.add(
          new User(
              XmlStore.text(e, "id"),
              XmlStore.text(e, "name"),
              XmlStore.text(e, "email"),
              XmlStore.text(e, "passwordHash"),
              XmlStore.text(e, "role"),
              XmlStore.text(e, "phone")));
    }
    return out;
  }

  public synchronized User byEmail(String email) {
    return users().stream()
        .filter(u -> u.getEmail().equalsIgnoreCase(email))
        .findFirst()
        .orElse(null);
  }

  public synchronized void addUser(User u) {
    Document d = XmlStore.read(file("users.xml"), "users");
    Element e = d.createElement("user");
    for (String[] x :
        new String[][] {
          {"id", u.getId()},
          {"name", u.getName()},
          {"email", u.getEmail()},
          {"passwordHash", u.getPasswordHash()},
          {"role", u.getRole()},
          {"phone", u.getPhone()}
        }) XmlStore.put(d, e, x[0], x[1]);
    d.getDocumentElement().appendChild(e);
    XmlStore.write(file("users.xml"), d);
  }

  public synchronized List<Car> cars() {
    Document d = XmlStore.read(file("cars.xml"), "cars");
    List<Car> out = new ArrayList<>();
    NodeList ns = d.getDocumentElement().getElementsByTagName("car");
    for (int i = 0; i < ns.getLength(); i++) {
      Element e = (Element) ns.item(i);
      Car c = new Car();
      c.setId(XmlStore.text(e, "id"));
      c.setMake(XmlStore.text(e, "make"));
      c.setModel(XmlStore.text(e, "model"));
      c.setCategory(XmlStore.text(e, "category"));
      c.setSeats(Integer.parseInt(XmlStore.text(e, "seats")));
      c.setTransmission(XmlStore.text(e, "transmission"));
      c.setFuel(XmlStore.text(e, "fuel"));
      c.setYear(Integer.parseInt(XmlStore.text(e, "year")));
      c.setPricePerDay(new BigDecimal(XmlStore.text(e, "pricePerDay")));
      c.setImage(XmlStore.text(e, "image"));
      c.setAvailable(Boolean.parseBoolean(XmlStore.text(e, "available")));
      out.add(c);
    }
    return out;
  }

  public synchronized Car car(String id) {
    return cars().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
  }

  public synchronized void saveCar(Car c) {
    Document d = XmlStore.read(file("cars.xml"), "cars");
    Element root = d.getDocumentElement();
    Element e = null;
    NodeList ns = root.getElementsByTagName("car");
    for (int i = 0; i < ns.getLength(); i++)
      if (XmlStore.text((Element) ns.item(i), "id").equals(c.getId())) e = (Element) ns.item(i);
    if (e == null) {
      e = d.createElement("car");
      root.appendChild(e);
    }
    for (Object[] x :
        new Object[][] {
          {"id", c.getId()},
          {"make", c.getMake()},
          {"model", c.getModel()},
          {"category", c.getCategory()},
          {"seats", c.getSeats()},
          {"transmission", c.getTransmission()},
          {"fuel", c.getFuel()},
          {"year", c.getYear()},
          {"pricePerDay", c.getPricePerDay()},
          {"image", c.getImage()},
          {"available", c.isAvailable()}
        }) XmlStore.put(d, e, (String) x[0], x[1]);
    XmlStore.write(file("cars.xml"), d);
  }

  public synchronized void deleteCar(String id) {
    Document d = XmlStore.read(file("cars.xml"), "cars");
    NodeList ns = d.getDocumentElement().getElementsByTagName("car");
    for (int i = ns.getLength() - 1; i >= 0; i--)
      if (XmlStore.text((Element) ns.item(i), "id").equals(id))
        ns.item(i).getParentNode().removeChild(ns.item(i));
    XmlStore.write(file("cars.xml"), d);
  }

  public synchronized List<Booking> bookings() {
    Document d = XmlStore.read(file("bookings.xml"), "bookings");
    List<Booking> out = new ArrayList<>();
    NodeList ns = d.getDocumentElement().getElementsByTagName("booking");
    for (int i = 0; i < ns.getLength(); i++) {
      Element e = (Element) ns.item(i);
      Booking b = new Booking();
      b.setId(XmlStore.text(e, "id"));
      b.setUserId(XmlStore.text(e, "userId"));
      b.setCarId(XmlStore.text(e, "carId"));
      b.setPickupDate(java.time.LocalDate.parse(XmlStore.text(e, "pickupDate")));
      b.setReturnDate(java.time.LocalDate.parse(XmlStore.text(e, "returnDate")));
      b.setTotalPrice(new BigDecimal(XmlStore.text(e, "totalPrice")));
      b.setStatus(XmlStore.text(e, "status"));
      out.add(b);
    }
    return out;
  }

  public synchronized void saveBooking(Booking b) {
    Document d = XmlStore.read(file("bookings.xml"), "bookings");
    Element root = d.getDocumentElement(), e = null;
    NodeList ns = root.getElementsByTagName("booking");
    for (int i = 0; i < ns.getLength(); i++)
      if (XmlStore.text((Element) ns.item(i), "id").equals(b.getId())) e = (Element) ns.item(i);
    if (e == null) {
      e = d.createElement("booking");
      root.appendChild(e);
    }
    for (Object[] x :
        new Object[][] {
          {"id", b.getId()},
          {"userId", b.getUserId()},
          {"carId", b.getCarId()},
          {"pickupDate", b.getPickupDate()},
          {"returnDate", b.getReturnDate()},
          {"totalPrice", b.getTotalPrice()},
          {"status", b.getStatus()}
        }) XmlStore.put(d, e, (String) x[0], x[1]);
    XmlStore.write(file("bookings.xml"), d);
  }
}
