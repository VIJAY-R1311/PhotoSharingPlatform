# 📸 PhotoShare

A full-stack photo-sharing platform built for photography and event teams.

PhotoShare allows an Admin/Lead to create events, manage team members, review photographs uploaded by the team, select photographs for customer sharing, and publish a customer-facing gallery protected by a PIN.

Customers do not need to create an account. They receive a gallery link and PIN and can use them to access the published photographs.

---
## 🚀 Features

### Admin / Lead
- Register and login
- Create events
- Add team members
- Assign team members to events
- View all photographs uploaded for an event
- Select photographs for customer sharing
- Create and publish galleries
- Generate shareable gallery links
- Set gallery PINs

### Team Member
- Login securely
- View assigned events
- Upload multiple photographs
- View photographs uploaded by themselves
- Access only assigned events

### Customer
- No account required
- Access gallery using a shareable link
- Enter PIN to access the gallery
- View only published photographs

---
## 🔄 Application Workflow

1. Admin registers and logs in.
2. Admin creates an event.
3. Admin adds team members and assigns them to the event.
4. Team members log in and upload photographs.
5. Admin reviews all uploaded photographs.
6. Admin selects photographs for customer sharing.
7. Admin creates and publishes a gallery.
8. Admin sets a PIN for the gallery.
9. Customer opens the gallery link.
10. Customer enters the PIN.
11. Customer views the published photographs.

---
## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming |
| Spring Boot 4.1.1 | Backend framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| Thymeleaf | Server-side HTML rendering |
| Bootstrap | UI styling |
| HTML / CSS / JavaScript | Frontend |
| MySQL | Database |
| Cloudinary | Cloud photo storage |
| Maven | Build and dependency management |

---
## 🏗️ System Architecture

The application follows a layered Spring Boot architecture:

```text
Browser
   ↓
Thymeleaf / Bootstrap UI
   ↓
Spring MVC Controllers
   ↓
Service Layer
   ↓
Spring Data JPA / Hibernate
   ↓
MySQL Database

Photo Upload
   ↓
Spring Boot
   ↓
Cloudinary
   ↓
Secure Photo URL

## 🗄️ Database Design

### Users

Stores Admin and Team Member accounts.

Main fields:
- ID
- Username
- Email
- Password
- Role

### Events

Stores event information.

Main fields:
- ID
- Event name
- Description
- Created date
- Created by

### Event Team Members

Connects users with their assigned events.

### Photos

Stores photograph metadata.

Main fields:
- Photo ID
- Event ID
- Uploaded By
- Filename
- Storage Location
- File Size
- Created At
- Selection Status

Image files themselves are not stored inside the database.

### Galleries

Stores published gallery information.

Main fields:
- Gallery ID
- Event ID
- Gallery Token
- PIN
- Published Status
- Published At

---
## 🔗 Entity Relationships

```text
User
 ├── creates → Event
 ├── uploads → Photo
 └── assigned to → Event

Event
 ├── has → Team Members
 ├── contains → Photos
 └── has → Gallery

Gallery
 └── contains selected Photos
## ☁️ Photo Storage

Photo files are stored using Cloudinary.

The MySQL database stores only photo metadata and the Cloudinary secure URL.

Cloudinary folder structure:

```text
photo-sharing/
└── events/
    └── {eventId}/
        ├── photo-1
        ├── photo-2
        └── photo-3
## 🔐 Security

The application uses Spring Security for authentication and role-based authorization.

### Roles

```text
ADMIN
TEAM_MEMBER
## ✅ Validation & Error Handling

The application handles common invalid situations including:

- Duplicate email registration
- Duplicate username registration
- Invalid login credentials
- Unauthorized role access
- Access to events not assigned to the user
- Empty or invalid form fields
- Failed photo uploads
- Invalid gallery PIN
- Unpublished gallery access
- Missing gallery
- Unauthorized photo access

---
## 🧪 Testing

The project includes automated tests covering core application functionality.

Current test result:

```text
29 Tests
29 Passed
0 Failed

## 💻 Local Setup

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd Photosharing

CREATE DATABASE photo_sharing_platform;

DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret

## 📤 Photo Upload Limits

The application currently supports:

- Maximum size per photo: 10 MB
- Maximum request size: 50 MB
- Multiple photographs can be uploaded in a single upload operation.

---
## 🚀 Deployment

The application is designed to be deployed as a Spring Boot web application.

Production deployment requires:

- Java 21 runtime
- MySQL database
- Cloudinary configuration
- Environment variables
- HTTPS-enabled application URL

Deployment details will be added after the final deployment.

---
## ⚙️ Production Configuration

The following values must be configured through environment variables in production:

DB_USERNAME
DB_PASSWORD
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET

Sensitive credentials must never be committed to the source repository.

---

## 📁 Project Structure

```text
Photo_Sharing_Platform
├── config
│   ├── SecurityConfig.java
│   └── CloudinaryConfig.java
│
├── controller
│   ├── HomeController.java
│   ├── LoginController.java
│   ├── UserController.java
│   ├── TeamMemberController.java
│   ├── AdminGalleryController.java
│   ├── AdminPhotoController.java
│   └── CustomerGalleryController.java
│
├── model
│   ├── User.java
│   ├── Event.java
│   ├── Photo.java
│   └── Gallery.java
│
├── repository
│   ├── UserRepository.java
│   ├── EventRepository.java
│   ├── PhotoRepository.java
│   └── GalleryRepository.java
│
├── service
│   ├── CustomUserDetailsService.java
│   ├── UserService.java
│   ├── PhotoService.java
│   ├── CloudinaryService.java
│   └── GalleryService.java
│
└── resources
    ├── static
    │   └── css
    │       └── style.css
    │
    └── templates
        ├── home.html
        ├── login.html
        ├── signup.html
        ├── signup-success.html
        ├── team-dashboard.html
        ├── team-event.html
        ├── admin-dashboard.html
        ├── admin-event-photos.html
        ├── admin-gallery.html
        ├── gallery-pin.html
        ├── customer-gallery.html
        ├── gallery-unavailable.html
        ├── add-team-member.html
        └── assign-team-member.html

## ⚠️ Known Limitations

- Gallery PINs are currently stored as plain text so that the Admin can view and share the PIN with customers.
- Gallery expiration is not currently implemented.
- Pagination/infinite scrolling is not currently implemented.
- CDN configuration is not separately implemented.
- CI/CD automation is not currently implemented.

These features can be added as future improvements without changing the core application workflow.

---
## 🔮 Future Improvements

Possible future improvements include:

- Gallery expiration
- Photo pagination
- Infinite scrolling
- Photo search and filtering
- Thumbnail generation
- Image resizing
- Bulk photo management
- Photo downloads
- CDN optimization
- CI/CD pipeline
- Improved gallery sharing controls

---
## 📋 Challenge Requirements Coverage

| Requirement | Status |
|---|---|
| Admin registration/login | ✅ |
| Team Member login | ✅ |
| Event creation | ✅ |
| Team member management | ✅ |
| Event assignment | ✅ |
| Multiple photo uploads | ✅ |
| Cloud photo storage | ✅ |
| Photo metadata storage | ✅ |
| Admin photo review | ✅ |
| Photo selection | ✅ |
| Gallery creation | ✅ |
| Gallery publishing | ✅ |
| Shareable gallery link | ✅ |
| PIN-protected customer gallery | ✅ |
| Role-based authorization | ✅ |
| Validation and error handling | ✅ |
| Responsive UI | ✅ |
| Automated tests | ✅ |
| README documentation | ✅ |
| Cloud deployment | ⏳ |

---
## 👤 Demo Credentials

### Admin

Username: <FINAL_DEMO_ADMIN_USERNAME>  
Email: <FINAL_DEMO_ADMIN_EMAIL>  
Password: <FINAL_DEMO_ADMIN_PASSWORD>

### Team Member

Username: <FINAL_DEMO_TEAM_MEMBER_USERNAME>  
Email: <FINAL_DEMO_TEAM_MEMBER_EMAIL>  
Password: <FINAL_DEMO_TEAM_MEMBER_PASSWORD>

Demo credentials will be updated after the final demo data and deployment are completed.

---
## 🔗 Final Deployment Details

### Live Application

<FINAL_LIVE_APPLICATION_URL>

### GitHub Repository

<FINAL_GITHUB_REPOSITORY_URL>

### Demo Gallery

<FINAL_DEMO_GALLERY_URL>

### Gallery PIN

<FINAL_DEMO_GALLERY_PIN>

---

## 📊 Project Status

Project Setup — ✅ Complete  
Authentication — ✅ Complete  
Roles & Authorization — ✅ Complete  
Events & Team Assignment — ✅ Complete  
Photo Upload — ✅ Complete  
Cloudinary Storage — ✅ Complete  
Admin Photo Review — ✅ Complete  
Gallery Management — ✅ Complete  
Customer PIN Gallery — ✅ Complete  
Validation & Security — ✅ Complete  
UI/UX Polish — ✅ Complete  
Testing — ✅ 29/29 Passed  
README — ✅ Complete  
Deployment — ⏳ Pending  
Final Verification — ⏳ Pending  

---
## 🏆 Internship Challenge

Built as a full-stack implementation of the TrizenAI Full Stack Internship Challenge.