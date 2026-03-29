package com.example.cliently

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Data classes to represent our entities in the code.
 * These are used to pass data between the database and the UI.
 */
data class Customer(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String
)

data class Product(
    val id: Int,
    val name: String,
    val price: Double
)

data class Order(
    val id: Int,
    val customerId: Int,
    val productId: Int,
    val date: String,
    // These extra fields are populated using a SQL JOIN to show product details in the order list
    val productName: String = "",
    val productPrice: Double = 0.0,
    val customerName: String = ""
)

/**
 * ClientlyDBHelper handles all database operations: creation, upgrades, and CRUD functions.
 * It extends SQLiteOpenHelper, which is the standard Android way to manage local databases.
 */
class ClientlyDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database credentials
        const val DATABASE_NAME = "cliently.db"
        const val DATABASE_VERSION = 1

        // Users table - storing login credentials
        const val TABLE_USERS = "Users"
        const val COL_USER_ID = "id"
        const val COL_USER_USERNAME = "username"
        const val COL_USER_PASSWORD = "password"

        // Customers table - storing basic contact info
        const val TABLE_CUSTOMERS = "Customers"
        const val COL_CUST_ID = "id"
        const val COL_CUST_NAME = "name"
        const val COL_CUST_PHONE = "phone"
        const val COL_CUST_EMAIL = "email"

        // Products table - catalog of items available for sale
        const val TABLE_PRODUCTS = "Products"
        const val COL_PROD_ID = "id"
        const val COL_PROD_NAME = "name"
        const val COL_PROD_PRICE = "price"

        // Orders table - links customers to products (Transaction record)
        const val TABLE_ORDERS = "Orders"
        const val COL_ORDER_ID = "id"
        const val COL_ORDER_CUST_FK = "cust_id_fk" // Foreign Key to Customers
        const val COL_ORDER_PROD_FK = "prod_id_fk" // Foreign Key to Products
        const val COL_ORDER_DATE = "date"
    }

    /**
     * Called when the database is created for the first time.
     * We execute SQL commands to create our tables and seed them with initial data.
     */
    override fun onCreate(db: SQLiteDatabase) {
        // 1. Create Users table
        db.execSQL(
            """CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_USERNAME TEXT NOT NULL,
                $COL_USER_PASSWORD TEXT NOT NULL
            )"""
        )

        // 2. Insert default admin user for testing purposes
        db.execSQL(
            "INSERT INTO $TABLE_USERS ($COL_USER_USERNAME, $COL_USER_PASSWORD) VALUES ('Sam', 'Sam 123')"
        )

        // 3. Create Customers table
        db.execSQL(
            """CREATE TABLE $TABLE_CUSTOMERS (
                $COL_CUST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CUST_NAME TEXT NOT NULL,
                $COL_CUST_PHONE TEXT,
                $COL_CUST_EMAIL TEXT
            )"""
        )

        // 4. Create Products table
        db.execSQL(
            """CREATE TABLE $TABLE_PRODUCTS (
                $COL_PROD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PROD_NAME TEXT NOT NULL,
                $COL_PROD_PRICE REAL NOT NULL
            )"""
        )

        // 5. Create Orders table with Foreign Key constraints to ensure data integrity
        db.execSQL(
            """CREATE TABLE $TABLE_ORDERS (
                $COL_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ORDER_CUST_FK INTEGER NOT NULL,
                $COL_ORDER_PROD_FK INTEGER NOT NULL,
                $COL_ORDER_DATE TEXT NOT NULL,
                FOREIGN KEY ($COL_ORDER_CUST_FK) REFERENCES $TABLE_CUSTOMERS($COL_CUST_ID),
                FOREIGN KEY ($COL_ORDER_PROD_FK) REFERENCES $TABLE_PRODUCTS($COL_PROD_ID)
            )"""
        )

        // SEED DATA: Adding sample records so the app isn't empty on first run
        db.execSQL("INSERT INTO $TABLE_CUSTOMERS ($COL_CUST_NAME, $COL_CUST_PHONE, $COL_CUST_EMAIL) VALUES ('Alice Nakato', '0701234567', 'alice@example.com')")
        db.execSQL("INSERT INTO $TABLE_CUSTOMERS ($COL_CUST_NAME, $COL_CUST_PHONE, $COL_CUST_EMAIL) VALUES ('Brian Okello', '0712345678', 'brian@example.com')")
        db.execSQL("INSERT INTO $TABLE_CUSTOMERS ($COL_CUST_NAME, $COL_CUST_PHONE, $COL_CUST_EMAIL) VALUES ('Carol Auma', '0723456789', 'carol@example.com')")

        db.execSQL("INSERT INTO $TABLE_PRODUCTS ($COL_PROD_NAME, $COL_PROD_PRICE) VALUES ('Laptop Pro 15', 1299.99)")
        db.execSQL("INSERT INTO $TABLE_PRODUCTS ($COL_PROD_NAME, $COL_PROD_PRICE) VALUES ('Wireless Mouse', 29.99)")
        db.execSQL("INSERT INTO $TABLE_PRODUCTS ($COL_PROD_NAME, $COL_PROD_PRICE) VALUES ('USB-C Hub', 49.99)")
        db.execSQL("INSERT INTO $TABLE_PRODUCTS ($COL_PROD_NAME, $COL_PROD_PRICE) VALUES ('Mechanical Keyboard', 89.99)")

        db.execSQL("INSERT INTO $TABLE_ORDERS ($COL_ORDER_CUST_FK, $COL_ORDER_PROD_FK, $COL_ORDER_DATE) VALUES (1, 1, '2024-01-15')")
        db.execSQL("INSERT INTO $TABLE_ORDERS ($COL_ORDER_CUST_FK, $COL_ORDER_PROD_FK, $COL_ORDER_DATE) VALUES (1, 2, '2024-02-20')")
        db.execSQL("INSERT INTO $TABLE_ORDERS ($COL_ORDER_CUST_FK, $COL_ORDER_PROD_FK, $COL_ORDER_DATE) VALUES (2, 3, '2024-03-05')")
    }

    /**
     * Called when DATABASE_VERSION is incremented.
     * We drop current tables and recreate them (standard for development).
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // ── AUTHENTICATION ────────────────────────────────────────────────────────

    /**
     * Checks if a user exists with matching username and password.
     * Returns true if a record is found.
     */
    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COL_USER_USERNAME = ? AND $COL_USER_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ── CUSTOMER CRUD ─────────────────────────────────────────────────────────

    /**
     * Inserts a new customer record into the database.
     */
    fun addCustomer(name: String, phone: String, email: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CUST_NAME, name)
            put(COL_CUST_PHONE, phone)
            put(COL_CUST_EMAIL, email)
        }
        return db.insert(TABLE_CUSTOMERS, null, values)
    }

    /**
     * Retrieves all customers sorted by name.
     */
    fun getAllCustomers(): List<Customer> {
        val customers = mutableListOf<Customer>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CUSTOMERS, null, null, null, null, null, "$COL_CUST_NAME ASC")
        while (cursor.moveToNext()) {
            customers.add(
                Customer(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CUST_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_NAME)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_PHONE)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_EMAIL))
                )
            )
        }
        cursor.close()
        return customers
    }

    /**
     * Fetches a single customer by their ID.
     */
    fun getCustomerById(id: Int): Customer? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CUSTOMERS, null,
            "$COL_CUST_ID = ?", arrayOf(id.toString()),
            null, null, null
        )
        var customer: Customer? = null
        if (cursor.moveToFirst()) {
            customer = Customer(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CUST_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_NAME)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_PHONE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUST_EMAIL))
            )
        }
        cursor.close()
        return customer
    }

    // ── PRODUCT QUERIES & CRUD ──────────────────────────────────────────────

    /**
     * Retrieves the entire product catalog.
     */
    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, "$COL_PROD_NAME ASC")
        while (cursor.moveToNext()) {
            products.add(
                Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_NAME)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PROD_PRICE))
                )
            )
        }
        cursor.close()
        return products
    }

    /**
     * Update an existing product's details.
     * Required for: "Edit Product" feature.
     */
    fun updateProduct(id: Int, name: String, price: Double): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PROD_NAME, name)
            put(COL_PROD_PRICE, price)
        }
        // Returns the number of rows affected
        return db.update(TABLE_PRODUCTS, values, "$COL_PROD_ID = ?", arrayOf(id.toString()))
    }

    /**
     * Delete a product by its ID.
     * Required for: "Delete Product" feature.
     */
    fun deleteProduct(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_PRODUCTS, "$COL_PROD_ID = ?", arrayOf(id.toString()))
    }

    // ── ORDER TRANSACTIONS ────────────────────────────────────────────────────

    /**
     * Fetch order history for a specific customer.
     * Uses an INNER JOIN to fetch product details (Name, Price) along with the Order info.
     * This is a "Client-First" feature.
     */
    fun getOrdersByCustomer(customerId: Int): List<Order> {
        val orders = mutableListOf<Order>()
        val db = readableDatabase
        val query = """
            SELECT o.$COL_ORDER_ID, o.$COL_ORDER_CUST_FK, o.$COL_ORDER_PROD_FK, o.$COL_ORDER_DATE,
                   p.$COL_PROD_NAME, p.$COL_PROD_PRICE
            FROM $TABLE_ORDERS o
            INNER JOIN $TABLE_PRODUCTS p ON o.$COL_ORDER_PROD_FK = p.$COL_PROD_ID
            WHERE o.$COL_ORDER_CUST_FK = ?
            ORDER BY o.$COL_ORDER_DATE DESC
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(customerId.toString()))
        while (cursor.moveToNext()) {
            orders.add(
                Order(
                    id = cursor.getInt(0),
                    customerId = cursor.getInt(1),
                    productId = cursor.getInt(2),
                    date = cursor.getString(3),
                    productName = cursor.getString(4),
                    productPrice = cursor.getDouble(5)
                )
            )
        }
        cursor.close()
        return orders
    }

    /**
     * Adds a new transaction linking a customer to a product.
     */
    fun addOrder(customerId: Int, productId: Int, date: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ORDER_CUST_FK, customerId)
            put(COL_ORDER_PROD_FK, productId)
            put(COL_ORDER_DATE, date)
        }
        return db.insert(TABLE_ORDERS, null, values)
    }

    // ── EXTRA CRUD FOR CUSTOMERS ─────────────────────────────────────────────

    /**
     * Update an existing customer's contact info.
     */
    fun updateCustomer(id: Int, name: String, phone: String, email: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CUST_NAME, name)
            put(COL_CUST_PHONE, phone)
            put(COL_CUST_EMAIL, email)
        }
        return db.update(TABLE_CUSTOMERS, values, "$COL_CUST_ID = ?", arrayOf(id.toString()))
    }

    /**
     * Remove a customer from the database.
     */
    fun deleteCustomer(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_CUSTOMERS, "$COL_CUST_ID = ?", arrayOf(id.toString()))
    }
}
