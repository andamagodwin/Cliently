# Task 5: Project Report - Cliently Retail App

## 1. Introduction
The **Cliently** application is a mobile-first retail management tool developed using Android Studio and Kotlin. In today's digital age, manual record-keeping practices hinder business growth and lead to data loss. This application provides a modern, localized solution for small businesses to track their relationships and transactions from their smartphones.

---

## 2. Problem Statement
The manual approach of recording orders in small retail shops using notebooks poses several risks:
- **Data Integrity**: Handwritten records can be misinterpreted or smudged.
- **Data Loss**: Notebooks can be physically misplaced or damaged (e.g., by water or fire).
- **Inaccessibility**: Searching for a specific customer's purchase history in a large stack of notebooks is slow and inefficient for customers.

---

## 3. System Design

### User Interface (UI)
The application follows a linear navigation flow to ensure simple usability for staff:
- **Login Screen**: Secure gateway to the app's administrative features.
- **Directory**: The main hub for customer profiles.
- **Catalog**: A dedicated inventory screen for product management.
- **Form Screen**: Used for adding/editing both products and customers.

### Database Schema
The app uses a relational SQLite database schema with four primary tables:
- **Users**: (id, username, password)
- **Customers**: (id, name, phone, email)
- **Products**: (id, name, price)
- **Orders**: (id, customer_fk, product_fk, date) — *Links the above three entities.*

---

## 4. Implementation
The application was built using **Kotlin** for robust coding and **Material Design 3** for aesthetics.

- **Intents**: Used to pass Customer and Product IDs between activities (e.g., passing a CustomerID to the OrderHistory screen).
- **Adapters**: Custom `RecyclerView.Adapter` classes populate lists dynamically from SQLite Cursor objects.
- **Relational Logic**: A SQL `INNER JOIN` was implemented to pull Product information into the Customer Order History for a unified "Transaction" view.

---

## 5. Screenshots of the Application
*(Attached in the submission folder: `login.png`, `inventory.png`, `add_product.png`, `customer_history.png`)*

---

## 6. Conclusion and Future Improvements
**Cliently** successfully digitizes basic retail operations. While it meets all core assignment requirements, future versions could include:
- **Cloud Sync**: Firebase integration for real-time multi-device access.
- **Barcode Scanning**: Using the camera to quickly identify and add products.
- **Analytics Dashboard**: Visual charts showing monthly sales and top-performing products.
