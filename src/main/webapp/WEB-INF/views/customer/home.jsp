<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>

<section class="simple-hero">
  <div class="container hero-grid">
    <div class="hero-copy">
      <div class="eyebrow"><span class="eyebrow-dot"></span> CU DRIVE · CHANDIGARH</div>
      <h1>Find the car<br>for your <span>next trip.</span></h1>
      <p>Easy, comfortable car rentals for campus days, weekend plans, and everything in between.</p>
      <form class="hero-search" action="cars">
        <label for="heroSearch">Where do you want to go?</label>
        <div class="hero-search-row">
          <input id="heroSearch" name="q" type="search" placeholder="Try SUV, Audi, or Fiat">
          <button class="btn btn-accent" type="submit">Find a car <span aria-hidden="true">→</span></button>
        </div>
      </form>
      <div class="hero-points"><span>✓ Clear daily pricing</span><span>✓ Quick booking</span><span>✓ Local support</span></div>
    </div>
    <div class="hero-visual">
      <img src="https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&amp;fit=crop&amp;w=1400&amp;q=85" alt="Premium sports car ready for a drive">
      <div class="hero-image-label"><span>YOUR NEXT JOURNEY</span><strong>Starts right here.</strong></div>
      <div class="hero-image-index">01 <span>/</span> 04</div>
    </div>
  </div>
</section>

<section class="brand-strip" aria-label="Popular rental categories">
  <div class="container brand-strip-inner"><span>THE RIGHT RIDE FOR</span><strong>City days</strong><i></i><strong>Weekend escapes</strong><i></i><strong>Family trips</strong><i></i><strong>Campus life</strong></div>
</section>

<section class="container content-section">
  <div class="section-heading">
    <div><div class="eyebrow">A GOOD PLACE TO START</div><h2>Popular cars</h2><p>Comfortable, reliable rides for wherever the day takes you.</p></div>
    <a class="simple-link" href="cars">Browse all cars <span aria-hidden="true">→</span></a>
  </div>
  <div class="row g-4">
    <c:forEach items="${cars}" var="car" begin="0" end="2">
      <div class="col-md-6 col-lg-4">
        <article class="car-card">
          <div class="car-image-wrap"><img src="<c:out value='${car.image}'/>" alt="<c:out value='${car.name}'/>" loading="lazy"><span class="car-category"><c:out value="${car.category}"/></span></div>
          <div class="card-body">
            <div class="car-title-row"><h3><c:out value="${car.name}"/></h3><span class="car-year"><c:out value="${car.year}"/></span></div>
            <div class="car-specs"><span><c:out value="${car.seats}"/> seats</span><i></i><span><c:out value="${car.transmission}"/></span><i></i><span><c:out value="${car.fuel}"/></span></div>
            <div class="car-card-bottom"><div class="car-price">₹<c:out value="${car.pricePerDay}"/><small> / day</small></div><a class="btn btn-dark btn-sm" href="cars">Choose car</a></div>
          </div>
        </article>
      </div>
    </c:forEach>
  </div>
</section>

<section class="how-section">
  <div class="container">
    <div class="section-heading"><div><div class="eyebrow">THREE EASY STEPS</div><h2>And you’re on your way.</h2></div><a class="simple-link" href="cars">Get started <span aria-hidden="true">→</span></a></div>
    <div class="steps-grid">
      <div class="step-card"><span class="step-number">01</span><div class="step-icon">⌕</div><h3>Choose your car</h3><p>Find a ride that fits your plans and your budget.</p></div>
      <div class="step-card"><span class="step-number">02</span><div class="step-icon">▦</div><h3>Set your dates</h3><p>Pick your pickup and return dates. See the price upfront.</p></div>
      <div class="step-card"><span class="step-number">03</span><div class="step-icon">↗</div><h3>Enjoy the drive</h3><p>Send your request and track it from your bookings page.</p></div>
    </div>
  </div>
</section>

<section class="container bottom-cta">
  <div><div class="eyebrow">YOUR NEXT DRIVE IS CLOSER</div><h2>Make a little room<br>for the unexpected.</h2></div>
  <a href="cars" class="btn btn-accent">Explore the fleet <span aria-hidden="true">→</span></a>
</section>

<%@ include file="/WEB-INF/views/footer.jspf" %>
