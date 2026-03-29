# Cliently Retail Management App

A comprehensive "Client-First" management application built for Android using Kotlin and SQLite. This project was developed as a university coursework assignment to transition a retail business from manual notebooks to a digital workflow.

## 🚀 Key Features
*   **Secure Authentication**: Login screen validating against a local `Users` table.
*   **Customer Directory**: Manage customer profiles, contact info, and view their personalized order history.
*   **Product Inventory (CRUD)**: Full suite to **View, Add, Edit, and Delete** products from the catalog.
*   **Order Management**: Link customers to products and track transaction dates using a `JOIN` query.
*   **Modern UI**: High-fidelity Dark Mode aesthetic using Material3 components and custom gradients.

## 🛠️ Technical Stack
*   **Language**: Kotlin (with View Binding)
*   **Database**: SQLite (`SQLiteOpenHelper`)
*   **UI Components**: RecyclerView, CoordinatorLayout, ConstraintLayout, Material Design 3
*   **Navigation**: Intent-based Activity navigation

## 📂 Project Structure
*   `ClientlyDBHelper.kt`: The central database controller managing four tables and relational logic.
*   `Activities/`: Contains logic for Login, Products, Customers, and Orders.
*   `Adapters/`: Efficient list-binding logic using the ViewHolder pattern.
*   `res/layout/`: XML layouts styled with custom backgrounds and Material design principles.

## 🔑 Default Credentials
For testing and grading, use the following admin account:
*   **Username**: Sam
*   **Password**: Sam 123

## 🎓 Academic Compliance
This project fulfills the requirements for:
*   **Full Technical Guide**: [Beginner-Friendly Project Documentation](file:///Users/andama/AndroidStudioProjects/Cliently/docs/FULL_TECHNICAL_GUIDE.md)
*   **Task 1**: [Requirements Analysis & Use Cases](file:///Users/andama/AndroidStudioProjects/Cliently/docs/Task1_Requirements_Analysis.md)
*   **Task 2**: System Design & Database Schema (Found in Task 5)
*   **Task 3**: Core Application Development (Source Code)
*   **Task 4**: [Functional Testing](file:///Users/andama/AndroidStudioProjects/Cliently/docs/Task4_Testing_Evaluation.md)
*   **Task 5**: [Project Report](file:///Users/andama/AndroidStudioProjects/Cliently/docs/Task5_Project_Report.md)

---
**Coursework Submission - April 2026**
