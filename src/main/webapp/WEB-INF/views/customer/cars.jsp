<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="page-hero">
  <div class="container">
    <div class="eyebrow">
      THE CU DRIVE COLLECTION
    </div>
    <h1>
      Your next ride awaits.
    </h1>
    <p>
      Find the right car for every plan.
    </p>
    <form class="row g-2 mt-4" action="cars" method="get">
      <div class="col-md-5">
        <label class="visually-hidden" for="carSearch">Search cars</label>
        <input id="carSearch" class="form-control" name="q" placeholder="Search make, model or category" value="<c:out value='${param.q}'/>" />
      </div>
      <div class="col-md-4">
        <label class="visually-hidden" for="carCategory">Category</label>
        <select id="carCategory" class="form-select" name="category"><option value="">All categories</option>
        <c:forEach items="${categories}" var="cat">
          <option value="<c:out value='${cat}'/>" <c:if test="${param.category eq cat}">selected</c:if>><c:out value="${cat}"/></option>
        </c:forEach>
        </select>
      </div>
      <div class="col-md-3">
        <button class="btn btn-accent w-100" type="submit">Search cars</button>
      </div>
    </form>
  </div>
</section>
<section class="container section-pad">
  <c:if test="${not empty message}">
    <div class="alert alert-warning">
      <c:out value="${message}"/>
    </div>
  </c:if>
  <div class="row g-4 justify-content-center">
    <c:forEach items="${cars}" var="car">
      <div class="col-md-6 col-lg-4">
        <article class="car-card">
          <div class="car-image-wrap">
            <img src="<c:out value='${car.image}'/>" alt="<c:out value='${car.name}'/>" loading="lazy">
            <span class="car-category"><c:out value="${car.category}"/></span>
          </div>
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <span class="car-year"><c:out value="${car.year}"/></span>
            </div>
            <h3>
              <c:out value="${car.name}"/>
            </h3>
            <div class="car-specs">
              <c:out value="${car.seats}"/> seats · <c:out value="${car.transmission}"/> · <c:out value="${car.fuel}"/>
            </div>
            <div class="car-price">
              ₹<c:out value="${car.pricePerDay}"/><small> / day</small>
            </div>
            <form method="post" action="book" class="booking-inline">
              <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="carId" value="<c:out value='${car.id}'/>">
              <div class="row g-2">
                <div class="col-6">
                  <label for="pickup-${car.id}">Pickup</label><input id="pickup-${car.id}" required type="date" name="pickupDate" class="form-control" data-min-today>
                </div>
                <div class="col-6">
                  <label for="return-${car.id}">Return</label><input id="return-${car.id}" required type="date" name="returnDate" class="form-control" data-min-today>
                </div>
              </div>
              <div class="estimate mt-2" data-price="${car.pricePerDay}">
                Select dates to estimate total
              </div>
              <button class="btn btn-dark w-100 mt-2" type="submit">Book this car</button>
            </form>
          </div>
        </article>
      </div>
    </c:forEach>
    <c:if test="${empty cars}">
      <div class="col-12 empty-state">
        No cars match your search. Try another filter.
      </div>
    </c:if>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
