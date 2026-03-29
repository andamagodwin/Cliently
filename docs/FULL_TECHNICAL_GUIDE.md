# Cliently: Full Technical & Beginner's Guide 📘

This document serves as a comprehensive "Project Bible" for the Cliently retail management application. It is designed to help a beginner understand every part of the codebase, from the logic to the UI, making it perfect for a university project presentation.

---

## 🏗️ 1. High-Level Architecture
Cliently follows a simplified **Model-View-Controller (MVC)** architectural pattern:

1.  **Model (The Data)**: Handled by `ClientlyDBHelper.kt` and SQLite. This is where the truth about "who bought what" lives.
2.  **View (The Design)**: Handled by **XML files** in `res/layout/`. This is what the user touches.
3.  **Controller (The Logic)**: Handled by **Activities** and **Adapters**. They take data from the Model and put it into the View.

---

## 🗺️ 2. Project Folder Map
Where does everything live?

*   **`src/main/java/com/example/cliently/`**: The heart of the app. All Kotlin logic files are here.
*   **`src/main/res/layout/`**: All the screen designs (XML).
*   **`src/main/res/drawable/`**: Images, icons, and custom background gradients.
*   **`src/main/res/values/`**: Global settings like colors (`colors.xml`) and app name (`strings.xml`).
*   **`AndroidManifest.xml`**: The "Registration" of the app. Every screen (Activity) must be listed here for Android to know they exist.
*   **`build.gradle.kts`**: The "Contract" where we tell Android what libraries we are borrowing (like `MaterialDesign` or `CardView`).

---

## 🧠 3. The "Brain": Database & Logic

### 📄 `ClientlyDBHelper.kt`
This is the most important file in your project. It manages the **SQLite** database.
*   **Terms to Know**:
    *   `SQLiteOpenHelper`: A built-in Android class we "extend" to handle database creation.
    *   `Cursor`: Imagine this as a "pointer" or "index finger" that moves through the rows of your database to read them.
    *   `ContentValues`: A bucket where we put data (like a name and price) before "pouring" it into a database table.
*   **What it does**:
    *   Creates 4 tables: `Users`, `Customers`, `Products`, and `Orders`.
    *   **INNER JOIN Query**: It uses a special SQL command to link the `Orders` table with the `Products` table so we can see the product *name* instead of just a *number ID*.

---

## 📺 4. File-by-File Breakdown (Source Code)

### 🔑 Authentication
*   **`LoginActivity.kt`**: Validates the user. It asks the database: "Does this username and password match any row in the `Users` table?"
*   **`activity_login.xml`**: The aesthetic entry point with the app logo and yellow branding.

### 👥 Customer Management
*   **`CustomerListActivity.kt`**: Displays all clients. It uses a **Floating Action Button (FAB)** to trigger an `AlertDialog` (popup) to add new customers.
*   **`CustomerAdapter.kt`**: A "Bridge" file. Its job is to take a list of Customers and create a `CardView` for each one.
*   **`CustomerDetailActivity.kt`**: Shows a single customer's details and their specific order history.

### 📦 Product Inventory (The Catalog)
*   **`ProductListActivity.kt`**: The screen showing all items for sale.
*   **`ProductAdapter.kt`**: Similar to the CustomerAdapter, but it also adds "Edit" and "Delete" buttons for every item.
*   **`AddProductActivity.kt`**: A multi-purpose screen. It can **Add** a new product or **Edit** an existing one depending on what data was passed to it.

### 🛒 Transactions
*   **`AddOrderActivity.kt`**: The "Checkout" screen.
    *   `Spinner`: Uses a dropdown menu to let the user choose a product.
    *   `java.util.Date`: Automatically records the current date when the order is saved.
*   **`OrderAdapter.kt`**: Formats the transaction list. It specifically uses `NumberFormat` to ensure prices show as **UGX**.

---

## 📚 5. Technical Glossary (The "Why")

### **What is an `Intent`?**
Think of an `Intent` as a **Taxi**. It carries the user from one screen to another. Sometimes, it carries **Extras** (like a CustomerID) in its "trunk" so the next screen knows who to show information for.

### **What is `ViewBinding`?**
In older Android apps, you had to find buttons using `findViewById`. It was slow and prone to errors. `ViewBinding` creates a direct "Nerve System" between your Kotlin code and your XML logic, making it faster and safer.

### **What is a `RecyclerView` & `ViewHolder`?**
If you have 1,000 customers, creating 1,000 views would crash your phone. A `RecyclerView` **recycles** the views that go off-screen. The `ViewHolder` is like a "Cache" that holds references to the text fields so you don't have to find them every time you scroll.

### **What are "Foreign Keys"?**
In the `Orders` table, we store a `customer_fk`. This is a "Key" that points to a specific person in the `Customers` table. This ensures you can't have an order for a customer who doesn't exist!

---

## 🎓 6. Student Presentation Cheat Sheet

**Common Questions from Lecturers:**

1.  **Q: How do you handle data persistence?**
    *   *A: We use a local SQLite database managed by the `SQLiteOpenHelper` class. Data stays on the phone even if the app is closed.*
2.  **Q: Why use Kotlin instead of Java?**
    *   *A: Kotlin is the modern standard for Android. It is more concise, safer against "null pointer" errors, and recommended by Google.*
3.  **Q: How do you show product names in the order history if they are in different tables?**
    *   *A: We use a SQL `INNER JOIN` in the `getOrdersByCustomer` method to combine the `Orders` and `Products` tables in a single query.*
4.  **Q: How do you clear the status bar for a "Premium" look?**
    *   *A: We used `fitsSystemWindows="true"` in the XML layouts and customized the AppBar theme to match our yellow brand colors.*

---
**Guide Created for:** Cliently Final Submission
**Target Readship**: Beginner/Intermediate Android Developers
