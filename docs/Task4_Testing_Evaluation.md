# Task 4: Testing & Evaluation

The following functional test cases were performed on the Cliently application to ensure it meets the requirements of a digital retail management system.

| Test Case | Feature | Input / Action | Expected Output | Actual Output | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **01** | User Login | Username: **Sam**<br>Password: **Sam 123** | Success message; navigate to Customer List screen. | "Welcome, Sam!" displayed; transferred to List screen. | ✅ PASS |
| **02** | Add Product | Name: **"Smart Watch"**<br>Price: **450,000** | Product added; list displays "Smart Watch" with price. | Product created; displayed in Catalog list. | ✅ PASS |
| **03** | Edit Product | Update price of "Smart Watch" to **400,000** | List reflects new price (400,000 UGX). | Price updated successfully in DB and UI. | ✅ PASS |
| **04** | Delete Product | Swipe/Tap delete on "Smart Watch" | Confirmation dialog shown; product removed from list. | Dialog displayed; product deleted from database. | ✅ PASS |
| **05** | Create Order | Select Customer "Alice"; Select Product "Laptop Pro 15" | Order saved; history list shows "Laptop Pro 15" under Alice. | Order inserted; detailed history view updated. | ✅ PASS |

## Evaluation
The application successfully manages the transition from manual records to digital data. 
- **Functional Goals**: Met. All CRUD operations on products and orders work as intended.
- **Relational Integrity**: Met. Deleting products or customers maintains relational consistency, and JOIN queries successfully link transaction data.
- **Technological Compliance**: Met. The use of SQLite ensures data persistence even after app restarts.
