## StayNest –(Full Stack Project)

StayNest is a **full-stack application** built with **Spring Boot (Backend)**, **Stripe Payment Microservice**, and **React.js (Frontend)**.  
It allows property owners to list their properties, customers to browse & book, and admins to manage the system.

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Project Structure
StayNest/
   backend/ # Spring Boot backend (StayNest)
   payment/ # Stripe Payment Microservice (Spring Boot)
   frontend/ # React.js frontend
   README.md # Project documentation

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

 ## Features

-  **User Roles**
  - Admin: Manage users(only single admin in a system), amenities
  - Owner: Add/update/delete(soft delete)/get  properties, assign amenities
  - Customer: Browse, book, and pay for properties

- 💳 **Payment Integration**
  - Secure online payments using Stripe API
  - Booking total passed to Payment Microservice

- 🔐 **Authentication**
  - JWT-based authentication & authorization

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

