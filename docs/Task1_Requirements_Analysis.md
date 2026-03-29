# Task 1: Requirements Analysis

## 1. Purpose of the Mobile Application
The **Cliently** application is designed to solve the problem of manual record-keeping for a small retail business. Its primary objective is to digitize the management of products, customers, and their order history. By providing a secure, local platform for tracking transactions, the app reduces human error, provides instant access to sales history, and ensures that no customer records or orders are lost.

---

## 2. Functional Requirements
These define the specific functions the system must perform:

1.  **User Authentication**: The system shall allow authorized users (e.g., admin "Sam") to log in securely before accessing sensitive customer or product data.
2.  **Product Management (CRUD)**: The system shall allow employees to Add, View, Edit, and Delete products from the digital catalog.
3.  **Customer Directory**: The system shall maintain a database of customers, including their names, phone numbers, and email addresses.
4.  **Order Creation**: The system shall allow users to link a specific customer to a selected product from the inventory to create a new order record.
5.  **Relational History View**: The system shall allow users to view a complete order history for any specific customer, displaying joined data (Product Name, Price, and Date).

---

## 3. Non-Functional Requirements
These define how the system should behave:

1.  **Usability**: The application shall use a consistent, high-contrast "Dark Mode" theme with modern Material3 components to ensure a premium user experience and clear readability.
2.  **Performance**: The local SQLite database shall ensure near-instant data retrieval and insertion without requiring an active internet connection.
3.  **Security**: The app shall prevent access to the main administrative dashboard unless valid credentials (stored in the local database) are provided on the login screen.

---

## 4. Use Case Analysis

### Use Case 1: Manage Inventory
*   **Actor**: Employee / Administrator
*   **Description**: The user logs in, navigates to the "Catalog" section, and adds a new product or updates the price of an existing item.
*   **Outcome**: The Product table is updated, and changes are reflected in future order dropdowns.

### Use Case 2: Record Customer Order
*   **Actor**: Sales Representative
*   **Description**: The user selects a customer from the directory, chooses a product from the spinner dropdown, and saves the transaction.
*   **Outcome**: A new record is inserted into the Orders table linking the Customer and Product IDs.
