package com.cu.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {
  private String id, userId, carId, status, customerName, carName;
  private LocalDate pickupDate, returnDate;
  private BigDecimal totalPrice;

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String v) {
    userId = v;
  }

  public String getCarId() {
    return carId;
  }

  public void setCarId(String v) {
    carId = v;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    status = v;
  }

  public LocalDate getPickupDate() {
    return pickupDate;
  }

  public void setPickupDate(LocalDate v) {
    pickupDate = v;
  }

  public LocalDate getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDate v) {
    returnDate = v;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal v) {
    totalPrice = v;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String v) {
    customerName = v;
  }

  public String getCarName() {
    return carName;
  }

  public void setCarName(String v) {
    carName = v;
  }
}
