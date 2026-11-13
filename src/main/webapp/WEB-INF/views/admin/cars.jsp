<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container section-pad">
  <div class="eyebrow">
    FLEET MANAGEMENT
  </div>
  <h1>
    Manage cars
  </h1>
  <div class="form-panel mt-4">
    <h2>
      Add or update a car
    </h2>
    <form method="post" action="car/save" class="row g-3">
      <input type="hidden" name="_csrf" value="${csrf}">
      <div class="col-md-2">
        <label>Car ID (blank = new)</label><input name="id" class="form-control">
      </div>
      <div class="col-md-3">
        <label>Make</label><input required name="make" class="form-control">
      </div>
      <div class="col-md-3">
        <label>Model</label><input required name="model" class="form-control">
      </div>
      <div class="col-md-2">
        <label>Category</label><input required name="category" class="form-control">
      </div>
      <div class="col-md-2">
        <label>Seats</label><input required type="number" min="2" max="12" name="seats" class="form-control">
      </div>
      <div class="col-md-2">
        <label>Year</label><input required type="number" min="2000" max="2035" name="year" class="form-control">
      </div>
      <div class="col-md-2">
        <label>Price/day ₹</label><input required type="number" min="1" name="price" class="form-control">
      </div>
      <div class="col-md-2">
        <label>Transmission</label><select name="transmission" class="form-select"><option>Manual</option><option>Automatic</option></select>
      </div>
      <div class="col-md-2">
        <label>Fuel</label><select name="fuel" class="form-select"><option>Petrol</option><option>Diesel</option><option>Electric</option><option>CNG</option><option>Hybrid</option></select>
      </div>
      <div class="col-md-4">
        <label>Image URL</label><input name="image" class="form-control" placeholder="https://...">
      </div>
      <div class="col-md-2 form-check mt-5">
        <input class="form-check-input" type="checkbox" checked name="available" value="true"><label class="form-check-label">Available</label>
      </div>
      <div class="col-12">
        <button class="btn btn-lime">Save car</button>
      </div>
    </form>
  </div>
  <h2 class="mt-5">
    Current fleet
  </h2>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th>
            ID
          </th>
          <th>
            Car
          </th>
          <th>
            Category
          </th>
          <th>
            Rate
          </th>
          <th>
            Availability
          </th>
          <th>
          </th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${cars}" var="car">
          <tr>
            <td>
              <c:out value="${car.id}"/>
            </td>
            <td>
              <c:out value="${car.name}"/>
            </td>
            <td>
              <c:out value="${car.category}"/>
            </td>
            <td>
              ₹<c:out value="${car.pricePerDay}"/>
            </td>
            <td>
              <c:out value="${car.available}"/>
            </td>
            <td>
              <form method="post" action="car/delete" onsubmit="return confirm('Delete this car?')">
                <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="id" value="<c:out value='${car.id}'/>"><button class="btn btn-sm btn-outline-danger">Delete</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
