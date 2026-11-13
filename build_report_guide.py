from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_CELL_VERTICAL_ALIGNMENT
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.enum.section import WD_SECTION
from pathlib import Path

OUT = Path('Car_Rental_Project_Report_Guide.docx')
doc = Document()
sec = doc.sections[0]
sec.top_margin = Inches(.68); sec.bottom_margin = Inches(.68); sec.left_margin = Inches(.78); sec.right_margin = Inches(.78)
sec.header_distance = Inches(.32); sec.footer_distance = Inches(.32)
DARK='17352D'; GREEN='426B4F'; LIME='DDF29B'; PALE='F3F6F1'; GREY='5F6B65'; WHITE='FFFFFF'; INK='1E2924'
styles=doc.styles
styles['Normal'].font.name='Aptos'; styles['Normal'].font.size=Pt(9.5); styles['Normal'].font.color.rgb=RGBColor.from_string(INK)
styles['Normal'].paragraph_format.space_after=Pt(5); styles['Normal'].paragraph_format.line_spacing=1.08
for name,size,color in [('Title',30,DARK),('Heading 1',20,DARK),('Heading 2',13,GREEN),('Heading 3',10,DARK)]:
 st=styles[name]; st.font.name='Aptos Display' if name!='Heading 3' else 'Aptos'; st.font.size=Pt(size); st.font.bold=True; st.font.color.rgb=RGBColor.from_string(color); st.paragraph_format.keep_with_next=True
 if name.startswith('Heading'): st.paragraph_format.space_before=Pt(10); st.paragraph_format.space_after=Pt(4)
# Remove Word's built-in Title paragraph rule.
title_ppr=styles['Title']._element.get_or_add_pPr()
for border in title_ppr.findall(qn('w:pBdr')): title_ppr.remove(border)

# header/footer
h=sec.header.paragraphs[0]; h.text='CU DRIVE  /  PROJECT REPORT GUIDE'; h.style='Caption'; h.runs[0].font.color.rgb=RGBColor.from_string(GREEN); h.runs[0].font.bold=True
f=sec.footer.paragraphs[0]; f.alignment=WD_ALIGN_PARAGRAPH.RIGHT
r=f.add_run('CHANDIGARH UNIVERSITY  |  '); r.font.size=Pt(8); r.font.color.rgb=RGBColor.from_string(GREY)
fld=OxmlElement('w:fldSimple'); fld.set(qn('w:instr'),'PAGE'); f._p.append(fld)

def shade(cell,fill):
 tcPr=cell._tc.get_or_add_tcPr(); shd=OxmlElement('w:shd'); shd.set(qn('w:fill'),fill); tcPr.append(shd)
def set_cell(cell,text,bold=False,color=INK,size=8.5):
 cell.text=''; p=cell.paragraphs[0]; p.paragraph_format.space_after=Pt(2); p.paragraph_format.space_before=Pt(2)
 rr=p.add_run(str(text)); rr.bold=bold; rr.font.name='Aptos'; rr.font.size=Pt(size); rr.font.color.rgb=RGBColor.from_string(color)
 cell.vertical_alignment=WD_CELL_VERTICAL_ALIGNMENT.CENTER

def table(headers,rows,widths=None):
 t=doc.add_table(rows=1, cols=len(headers)); t.alignment=WD_TABLE_ALIGNMENT.CENTER; t.style='Table Grid'
 for i,x in enumerate(headers): set_cell(t.rows[0].cells[i],x,True,WHITE,8.5); shade(t.rows[0].cells[i],DARK)
 for ri,row in enumerate(rows):
  cells=t.add_row().cells
  for i,x in enumerate(row): set_cell(cells[i],x,False,INK,8.2); shade(cells[i], 'F5F7F4' if ri%2==0 else WHITE)
  trPr=t.rows[-1]._tr.get_or_add_trPr(); trPr.append(OxmlElement('w:cantSplit'))
 if widths:
  for row in t.rows:
   for i,w in enumerate(widths): row.cells[i].width=Inches(w)
 doc.add_paragraph().paragraph_format.space_after=Pt(0)
 return t

def para(text='',boldlead=None):
 p=doc.add_paragraph()
 if boldlead and text.startswith(boldlead):
  p.add_run(boldlead).bold=True; p.add_run(text[len(boldlead):])
 else: p.add_run(text)
 return p
def bullet(text):
 p=doc.add_paragraph(style='List Bullet'); p.paragraph_format.space_after=Pt(2); p.add_run(text); return p
def numbered(text):
 global step_no
 if text.startswith(('The visitor opens Register','A signed-in customer chooses','The dashboard loads','Build target/car-rental.war','Open home page')): step_no=0
 step_no += 1
 p=doc.add_paragraph(); p.paragraph_format.left_indent=Inches(.28); p.paragraph_format.first_line_indent=Inches(-.28); p.paragraph_format.space_after=Pt(3); p.add_run(str(step_no)+'.  '+text); return p
step_no=0
def heading(text,level=1): doc.add_heading(text,level=level)
def page(): doc.add_page_break()

# cover
p=doc.add_paragraph(); p.paragraph_format.space_before=Pt(65); p.alignment=WD_ALIGN_PARAGRAPH.LEFT
r=p.add_run('CHANDIGARH UNIVERSITY  /  3RD YEAR PBL'); r.bold=True; r.font.size=Pt(10); r.font.color.rgb=RGBColor.from_string(GREEN)
p=doc.add_paragraph(style='Title'); p.paragraph_format.space_before=Pt(20); p.add_run('Car Rental Management System')
p=doc.add_paragraph(); rr=p.add_run('Project report guide and technical documentation'); rr.font.size=Pt(17); rr.font.color.rgb=RGBColor.from_string(GREY)
p=doc.add_paragraph(); p.paragraph_format.space_before=Pt(18); rr=p.add_run('JAVA 17  |  JAKARTA SERVLETS  |  JSP  |  XML DOM  |  TOMCAT 10.1'); rr.bold=True; rr.font.size=Pt(9); rr.font.color.rgb=RGBColor.from_string(GREEN)
# accent block via table
T=doc.add_table(rows=1,cols=1); T.alignment=WD_TABLE_ALIGNMENT.LEFT; cell=T.cell(0,0); shade(cell,DARK); set_cell(cell,'A web-based system for browsing vehicles, managing reservations, and administering a local rental fleet.',False,WHITE,12)
cell.width=Inches(6.7)
para('Prepared as a reference for writing the project report, presenting the system, and explaining the implementation from the browser request through XML persistence.')
para('Project name: CU Drive  |  Course: Project Based Learning in Java  |  University: Chandigarh University')
para('Student name and roll number can be added to the final college submission cover page.')
page()

heading('Contents and report outline')
table(['Section','What it explains'],[
('1. Project overview','Problem, proposed system, objectives, scope and users'),('2. Requirements and technology','Functional requirements and selected stack'),('3. Architecture','MVC layers, request flow, project folders and class responsibilities'),('4. Data model and XML storage','Entities, XML records, parser safety and persistence'),('5. Customer workflows','Registration, login, search, booking, history and cancellation'),('6. Administrator workflows','Dashboard, vehicle management, booking decisions and customer records'),('7. Business rules and security','Price, date overlap, authorization, CSRF, output encoding and password storage'),('8. Build and deployment','Prerequisites, Maven build, data folder, Tomcat deployment and launch'),('9. Functional verification','Test checklist, expected results and demonstration plan'),('10. Conclusion and extensions','Summary and sensible future enhancements')], [2.0,4.8])
heading('How to use this document',2)
para('The material below is written from the project source and README in the workspace. It can be adapted into a formal academic report. Keep the implementation description accurate to the submitted code; add your own screenshots, student details, acknowledgements and evaluation results where required by your department.')

page(); heading('1. Project overview')
heading('Abstract',2)
para('CU Drive is a web-based car rental management system developed for a Java Project Based Learning course. Customers can create accounts, browse a vehicle fleet, select pickup and return dates, submit booking requests, and manage pending requests. Administrators can maintain vehicle records, review bookings, update booking status, and view customer information. The application uses Java 17, Jakarta Servlets, JSP, Bootstrap 5, and XML files accessed with the Java DOM API. It follows a Model-View-Controller structure and is packaged as a WAR for Apache Tomcat 10.1.')
heading('Problem statement',2)
para('Small rental operations need a consistent way to publish available vehicles, capture rental dates, calculate charges, and track requests. Manual records can make it difficult to notice date conflicts, retrieve customer bookings, and keep vehicle information current. This project brings those workflows into one browser-based application and persists its records in XML files.')
heading('Aim and objectives',2)
for x in ['Provide a responsive interface for customers to discover cars and request rentals.','Store customer accounts, vehicles, and bookings in persistent XML files outside the deployed WAR.','Calculate rental charges from vehicle daily rates and selected rental days.','Reject bookings whose date ranges overlap active bookings for the same vehicle.','Give administrators tools to manage the fleet and make booking decisions.','Apply baseline web protections for passwords, sessions, request forgery, output encoding, and XML parsing.']: bullet(x)
heading('Scope and user roles',2)
table(['Role','Main responsibilities'],[
('Customer','Register and sign in; search and filter cars; review vehicle details; choose dates; see booking history; cancel a pending request.'),
('Administrator','Sign in with the admin role; view fleet, customer, booking and approved-revenue figures; create/update/delete cars; set availability; approve, reject or complete bookings; view customers.')],[1.35,5.45])
heading('Project boundaries',2)
para('The system is an educational single-application project using local XML files rather than a database. It records the booking request and admin status but does not implement payment processing, identity verification, email/SMS notifications, pickup logistics, or multi-location inventory. Vehicle photographs use remote image URLs unless an administrator changes them.')

page(); heading('2. Requirements and technology')
table(['Area','Selected technology','Purpose in this project'],[
('Language/runtime','Java 17','Models, controllers, service rules, XML operations and password utilities.'),('Web container','Apache Tomcat 10.1','Runs the Jakarta Servlet 6 and JSP application.'),('Controller layer','Jakarta Servlets','Routes HTTP requests and dispatches responses to JSP views.'),('View layer','JSP, JSTL, HTML5, CSS3, JavaScript','Renders pages, escapes values, and provides date/price interaction.'),('UI framework','Bootstrap 5','Responsive navigation, layout grid, forms, alerts and tables.'),('Persistence','XML with Java DOM','Stores users.xml, cars.xml and bookings.xml outside the WAR.'),('Build/package','Maven WAR','Resolves dependencies, compiles Java and packages deployable application.')],[1.35,2.0,3.45])
heading('Functional requirements',2)
for x in ['Customer account registration, sign-in and sign-out with hashed passwords.','Home page, vehicle listing, text search, category filtering and vehicle specifications.','Rental request with pickup and return dates, a calculated total, confirmation, history and pending cancellation.','Prevention of overlapping active reservations for the same car.','Administrator-only dashboard, vehicle management, availability setting, booking management and customer listing.']: bullet(x)
heading('Non-functional requirements',2)
for x in ['Persistence across application restarts using files outside Tomcat’s deployment directory.','Session-based identity and role checks for administrative routes.','Safe parsing of XML input and synchronization of repository operations.','Responsive interface and clear input feedback on desktop and mobile screens.','Portable build and deployment using Maven and Tomcat 10.1.']: bullet(x)

page(); heading('3. Application architecture')
para('The application separates HTTP handling, business rules, persistence, and presentation. A browser submits a request to a servlet. The servlet obtains data or invokes a service, then forwards to a JSP or redirects to another route. The service applies rental rules. The XML repository translates between Java objects and XML documents.')
table(['Layer','Implementation','Responsibilities'],[
('View','JSP pages in WEB-INF/views, shared JSP fragments, CSS and JavaScript','Render customer/admin pages; use JSTL c:out for output encoding; send forms with CSRF tokens.'),
('Controller','RentalServlet, SecurityFilter, AdminFilter','Map URLs; manage sessions; validate request flow; protect POST requests; restrict admin paths; dispatch JSPs.'),
('Service','RentalService','Register and authenticate users; check car availability and date conflicts; calculate totals; enrich booking data; compute dashboard revenue.'),
('Repository','XmlRepository','Load/save users, cars and bookings as Java model objects; synchronize data access; update and delete records.'),
('XML utility','XmlStore','Create DOM documents; configure parser hardening; serialize documents to temp files; atomically replace persistent files.'),
('Model','User, Car, Booking','Represent the domain data shared across application layers.')],[1.05,2.3,3.45])
heading('Request and data flow',2)
table(['Step','Component','Action'],[
('1','Browser','Requests a page or submits a form.'),('2','SecurityFilter','Adds response security headers and validates the session CSRF token on every non-GET/HEAD request.'),('3','AdminFilter (admin URL only)','Requires a signed-in user whose role is ADMIN.'),('4','RentalServlet','Reads route and parameters, handles session identity, and calls the service/repository.'),('5','RentalService','Applies registration, booking, overlap, pricing, or aggregation rules.'),('6','XmlRepository / XmlStore','Reads or updates the XML document on disk.'),('7','JSP view or redirect','Renders the response; dynamic text is escaped with JSTL c:out.')],[.65,1.75,4.4])
heading('Project structure',2)
for x in ['pom.xml - Maven dependencies, Java release level, WAR packaging and final artifact name.','src/main/java/com/cu/carrental/model - User.java, Car.java, Booking.java.','src/main/java/com/cu/carrental/controller - RentalServlet.java, SecurityFilter.java, AdminFilter.java.','src/main/java/com/cu/carrental/service - RentalService.java business operations.','src/main/java/com/cu/carrental/repository - XmlRepository.java and XmlStore.java.','src/main/java/com/cu/carrental/util - PasswordUtil.java PBKDF2 hashing/verification.','src/main/webapp/WEB-INF/web.xml - web application and session configuration.','src/main/webapp/WEB-INF/views - JSP pages and shared header/footer fragments.','src/main/webapp/assets - custom site CSS and client-side date/estimate script.','src/main/resources - empty XML templates packaged with the WAR; runtime data is created in the configured external data directory.','data - example XML files for reference; runtime initialization is performed by XmlRepository.']: bullet(x)

page(); heading('4. Data model and XML persistence')
heading('Entities and relationships',2)
table(['Entity','Key fields','Relationship / use'],[
('User','id, name, email, passwordHash, role, phone','A customer may have many bookings. Role is CUSTOMER or ADMIN.'),
('Car','id, make, model, category, year, seats, transmission, fuel, pricePerDay, image, available','A vehicle may be referenced by multiple bookings across non-overlapping dates.'),
('Booking','id, userId, carId, pickupDate, returnDate, totalPrice, status','Connects one customer and one car over a date range. Status includes PENDING, APPROVED, REJECTED, CANCELLED and COMPLETED.')],[1.0,3.1,2.7])
heading('XML file responsibilities',2)
table(['File','Root and records','Stored information'],[
('users.xml','<users><user>…</user></users>','Account ID, name, email, password hash, role and phone.'),
('cars.xml','<cars><car>…</car></cars>','Car ID, name fields, category, image, seating, transmission, fuel, year, daily price and availability.'),
('bookings.xml','<bookings><booking>…</booking></bookings>','Booking ID, user/car references, dates, total price and status.')],[1.3,2.2,3.3])
heading('Data directory and startup behavior',2)
para('The repository resolves its data directory from the JVM property carrental.data.dir, then the CAR_RENTAL_DATA environment variable, and otherwise uses a car-rental-data directory under the operating-system user home. On first use, XmlStore creates missing files with their expected root element. XmlRepository seeds the default administrator and demo customer only when users.xml has no user records, and seeds example cars only when cars.xml has no car records. Existing data is retained.')
heading('DOM read/write and safety',2)
para('The XML utility parses with DOCTYPE declarations disabled, external general and parameter entities disabled, external DTD loading disabled, XInclude turned off, and entity expansion disabled. Repository methods synchronize operations. Writes serialize to a sibling temporary file and replace the destination using an atomic move where the filesystem supports it, with a replace move fallback.')
para('The current code relies on the repository’s expected element names and Java parsing for record conversion; it does not attach an XSD schema validator. Malformed XML or invalid field text causes a data access error. For a production-grade extension, add explicit XSD validation and safer recovery/backups for corrupt files.')

page(); heading('5. Customer workflows')
heading('Registration and authentication',2)
numbered('The visitor opens Register and submits name, email, phone and password over a form containing the session CSRF token.')
numbered('RentalService validates the required values, email shape, and minimum password length, and rejects duplicate email addresses.')
numbered('PasswordUtil generates a random salt and derives a 256-bit PBKDF2WithHmacSHA256 value with 120,000 iterations. The stored form contains the algorithm label, iteration count, salt and derived key; the plain password is not stored.')
numbered('The user is saved in users.xml with role CUSTOMER. The user signs in with email/password; successful authentication rotates the session ID and stores the User in the session.')
numbered('Sign out invalidates the session.')
heading('Browse and select a vehicle',2)
para('The home and cars routes load available cars. Text search matches a car name or category, and the category selector narrows the result. Cards show make/model, category, year, seats, transmission, fuel type and daily price. The browser JavaScript updates the return-date minimum and displays an estimate when both dates are selected.')
heading('Submit and confirm a booking',2)
numbered('A signed-in customer chooses a car, pickup date and return date, then submits the booking form.')
numbered('RentalService verifies that the car exists and is marked available. Pickup cannot be in the past and the return date must be after pickup.')
numbered('While synchronized on the repository, the service reads existing bookings for the car. REJECTED and CANCELLED bookings do not block dates; other states are treated as active for overlap checking.')
numbered('The ranges are considered overlapping when requested pickup is before an existing return AND requested return is after an existing pickup. Adjacent ranges where one rental ends on the next rental’s pickup date are allowed.')
numbered('Rental days are the number of nights/days between pickup and return. Total = price per day × rental days. A new unique booking ID is created, status is set to PENDING, and the record is written to bookings.xml.')
numbered('A confirmation page displays the booking ID, pending status and total. The customer can later view it on My bookings.')
heading('Booking history and cancellation',2)
para('The bookings route filters records to the signed-in customer. Each row shows car name, date range, total and status. The Cancel action is offered for PENDING entries. The server verifies ownership and current PENDING status before setting the booking to CANCELLED.')

page(); heading('6. Administrator workflows')
heading('Administrator access',2)
para('Admin requests are matched by the /admin/* route and pass through AdminFilter. A missing session user is redirected to sign-in. A signed-in user without the ADMIN role receives HTTP 403. The seeded administrator is admin@admin with the initial password admin; rotate this credential before sharing or deploying the application.')
heading('Dashboard and booking decisions',2)
numbered('The dashboard loads all cars, users and enriched bookings. Booking rows show customer name, car name, date range, total and status.')
numbered('Revenue is the sum of booking totals whose status is APPROVED or COMPLETED. The dashboard also shows fleet size, customer count, and booking count.')
numbered('For PENDING requests the administrator can approve or reject. For APPROVED requests the administrator can mark the booking COMPLETED. Each status change is submitted as a CSRF-protected POST.')
heading('Vehicle and customer management',2)
para('The fleet page allows an administrator to create a car or update an existing record by supplying its ID. A blank ID generates a new ID. The form captures make, model, category, seats, year, daily price, transmission, fuel, image URL, and availability. A separate row action deletes a car. The customer page lists records whose role is CUSTOMER.')
heading('Admin route summary',2)
table(['Route','Method','Purpose'],[
('/admin/dashboard','GET','Dashboard metrics and booking queue.'),('/admin/cars','GET','Fleet list and add/update form.'),('/admin/car/save','POST','Create or update car record.'),('/admin/car/delete','POST','Delete a car record.'),('/admin/booking/status','POST','Approve, reject or complete a booking.'),('/admin/customers','GET','Customer directory.')],[2.25,.85,3.7])

page(); heading('7. Business rules and security')
heading('Key business rules',2)
table(['Rule','Server behavior'],[
('User email','Validated for a basic email pattern; duplicate lookup is case-insensitive.'),('Password','Minimum 8 characters; salted PBKDF2-HMAC-SHA256 hash stored.'),('Booking dates','Pickup must be today or later; return strictly after pickup.'),('Car availability','A missing or unavailable car cannot be booked.'),('Overlap','Any existing state except REJECTED or CANCELLED blocks intersecting date ranges for that car.'),('Total price','BigDecimal daily rate × whole days between pickup and return.'),('Customer cancellation','Only the owner may cancel their own PENDING booking.'),('Admin actions','Admin role required; status values restricted to APPROVED, REJECTED or COMPLETED.')],[1.55,5.25])
heading('Security controls in the source',2)
for x in ['Password hashing uses a per-password SecureRandom salt, PBKDF2WithHmacSHA256 and constant-time comparison for verification.','Successful login calls changeSessionId; logout invalidates the session. Session timeout is configured to 30 minutes and the session cookie is HttpOnly.','SecurityFilter creates a session CSRF token and compares it on non-GET/HEAD requests. State-changing forms include the token.','AdminFilter checks session identity and ADMIN role for /admin/* routes.','JSP dynamic data is generally rendered using JSTL c:out to HTML-escape output.','Response headers include X-Content-Type-Options: nosniff, X-Frame-Options: DENY, and Referrer-Policy: strict-origin-when-cross-origin.','The DOM parser disallows DOCTYPE and external entities to reduce XML external entity risks.']: bullet(x)
heading('Security considerations for an academic deployment',2)
para('The seeded credentials are intentionally documented for local demonstration and are not production credentials. Use HTTPS for real traffic, use strong unique credentials, restrict filesystem permissions on the XML directory, and back up the XML files. Review all administrator-provided values and enforce stronger validation and URL policies before any public deployment.')

page(); heading('8. Build, configuration and deployment')
heading('Prerequisites',2)
for x in ['JDK 17 or later','Maven 3.8 or later','Apache Tomcat 10.1.x','A writable directory outside the Tomcat webapps/deployment directory']: bullet(x)
heading('Build the WAR',2)
para('Open PowerShell or a terminal in the project root (the folder containing pom.xml) and run:')
code=doc.add_paragraph(); code.paragraph_format.left_indent=Inches(.2); rr=code.add_run('mvn clean package'); rr.font.name='Consolas'; rr.font.size=Pt(10); rr.font.color.rgb=RGBColor.from_string(GREEN)
para('A successful build creates target/car-rental.war. The project uses Jakarta Servlet API 6.0 as a provided dependency because Tomcat supplies the servlet runtime. JSTL is packaged with the application.')
heading('Choose the external XML folder',2)
para('Recommended PowerShell setting for the current terminal:')
code=doc.add_paragraph(); rr=code.add_run('$env:CAR_RENTAL_DATA = "$env:USERPROFILE\\car-rental-data"'); rr.font.name='Consolas'; rr.font.size=Pt(9); rr.font.color.rgb=RGBColor.from_string(GREEN)
para('Alternatively, pass the JVM system property -Dcarrental.data.dir=C:/data/car-rental to Tomcat. Ensure the Tomcat service account can create and modify that folder. The property takes precedence over the environment variable.')
heading('Deploy on Tomcat',2)
numbered('Build target/car-rental.war with Maven.')
numbered('Copy the WAR into Tomcat 10.1 webapps, or use the Tomcat Manager application to deploy it.')
numbered('Start or restart Tomcat and allow it to expand the WAR.')
numbered('Open http://localhost:8080/car-rental/ in a browser.')
numbered('Sign in using a sample account and confirm that the external data directory contains users.xml, cars.xml and bookings.xml.')
heading('Sample local accounts',2)
table(['Role','Email','Initial password'],[('Administrator','admin@admin','admin'),('Customer','demo@demo','demo')],[1.5,2.7,2.6])
para('The default data files initialize on first run only when the corresponding users or cars collection is empty. Do not replace files on a live instance without backing them up.')

page(); heading('9. Functional verification and demonstration')
heading('Functional verification checklist',2)
table(['Test','Action','Expected result'],[
('Account registration','Create a customer; try the same email twice.','First account is saved; duplicate is rejected.'),('Password storage','Inspect users.xml after registration.','Password is stored as a salted PBKDF2 record, not plain text.'),('Authentication','Sign in with correct and incorrect passwords; sign out.','Correct credentials establish a session; incorrect credentials fail; sign-out clears it.'),('Search/filter','Search by car name/category and choose a category.','Only matching available vehicles are shown.'),('Price estimate','Select pickup and return dates on a vehicle card.','Browser estimate and server total equal daily rate multiplied by rental days.'),('Invalid dates','Submit a past pickup or return on/before pickup.','Server rejects the request.'),('Overlap','Submit two intersecting requests for the same car; then try another car.','Overlap for the same car is rejected; another car is not blocked.'),('Customer history','View bookings and cancel a pending booking.','Customer sees own records and can cancel own pending request.'),('Admin authorization','Open an admin path as a customer.','Request is denied with 403.'),('Admin booking flow','Approve/reject a pending request; complete an approved one.','Status changes persist and revenue includes approved/completed totals.'),('Admin fleet','Create, update, mark unavailable, and delete a car.','Car record and availability are reflected in the listing and XML.'),('CSRF check','Submit a POST without a valid token.','Security filter returns 403.'),('Persistence','Restart Tomcat after writing records.','Users, cars and bookings remain available.')],[1.15,2.55,3.1])
heading('Suggested live demonstration order',2)
for x in ['Open home page and show responsive navigation and car cards.','Search/filter the fleet and explain the car fields and daily rate.','Register or sign in as the demo customer; select dates and point out the estimated price.','Submit a request and show its confirmation and booking history.','Try an overlapping reservation and explain the server-side conflict rule.','Sign in as the administrator and process a request; show dashboard totals and fleet/customer management.','Point to the XML files in the external data directory and explain persistence.']: numbered(x)
heading('Evidence to capture for the college report',2)
for x in ['Home page and responsive car listing','Registration/login screen and customer dashboard','Booking form showing selected dates and estimated total','Booking confirmation and customer booking history','Admin dashboard, booking status actions, fleet form and customer list','A brief project folder/MVC architecture image and a sample XML record (hide password hashes if sharing publicly)','Build output showing the generated WAR and Tomcat deployment URL']: bullet(x)

page(); heading('10. Conclusion and future scope')
heading('Conclusion',2)
para('CU Drive demonstrates an end-to-end rental workflow using the required Java web stack. The MVC design keeps presentation, request handling, business rules and XML persistence in distinct areas. Customers can request rentals and track their status; administrators can maintain the fleet and decide booking outcomes. The XML store, DOM parser hardening, password hashing, session authorization, CSRF validation and server-side date conflict checks form the core engineering elements of the project.')
heading('Possible future improvements',2)
for x in ['Add XSD-backed validation and XML backup/restore tooling.','Add richer filters such as seats, price range, transmission and fuel.','Add an individual vehicle detail page and optional pickup-location selection.','Add payment gateway integration, receipts, and email/SMS notifications.','Add pagination, audit history, user profile/password change, and stronger account lockout controls.','Add automated unit and integration tests, plus role-based browser tests on Tomcat.','Move from XML to a database only if project requirements later permit it; retain service/repository interfaces to isolate that change.']: bullet(x)
heading('Presentation summary',2)
para('In one sentence: CU Drive is a Java 17 Servlet/JSP MVC car rental application for customers and administrators, with XML DOM persistence, date-based booking validation, and a Tomcat 10.1 WAR deployment.')

# metadata and table typography consistency
props=doc.core_properties; props.title='Car Rental Management System Project Report Guide'; props.subject='Architecture, workflows, XML storage, security, deployment and verification'; props.author='Chandigarh University Project Based Learning'
doc.save(OUT)
print(OUT.resolve())
