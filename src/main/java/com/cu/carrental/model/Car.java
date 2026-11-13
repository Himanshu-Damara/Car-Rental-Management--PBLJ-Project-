package com.cu.carrental.model;

import java.math.BigDecimal;

public class Car {
  private String id, make, model, category, image, transmission, fuel;
  private int year, seats;
  private BigDecimal pricePerDay;
  private boolean available = true;

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String v) {
    make = v;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String v) {
    model = v;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String v) {
    category = v;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String v) {
    image = v;
  }

  public String getTransmission() {
    return transmission;
  }

  public void setTransmission(String v) {
    transmission = v;
  }

  public String getFuel() {
    return fuel;
  }

  public void setFuel(String v) {
    fuel = v;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int v) {
    year = v;
  }

  public int getSeats() {
    return seats;
  }

  public void setSeats(int v) {
    seats = v;
  }

  public BigDecimal getPricePerDay() {
    return pricePerDay;
  }

  public void setPricePerDay(BigDecimal v) {
    pricePerDay = v;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean v) {
    available = v;
  }

  public String getName() {
    return make + " " + model;
  }
}
