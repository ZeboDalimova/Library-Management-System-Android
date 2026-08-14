# Kutubxona - Digital Library Android App

**Kutubxona** is a mobile application developed as a university course project. It serves as a digital library platform where users can discover, read, and listen to books, while administrators can manage the library's content.

This project demonstrates core Android development skills, including local database management, complex UI layouts, and legacy code modernization.

## 🚀 Key Features

### For Users:
*   **Authentication:** Secure Login and Registration system with profile management.
*   **Discovery:** Browse books by categories with a clean, organized interface.
*   **Reading Experience:** Integrated PDF viewer for reading digital books directly in the app.
*   **Audiobooks:** Built-in audio player for listening to book narrations.
*   **Personalization:** "Bookmarks" feature to track reading progress (Plans, Currently Reading, Completed).

### For Administrators:
*   **Content Management (CRUD):** Add, update, or delete books, categories, and UI designs.
*   **User Oversight:** View and manage registered users.
*   **Database Control:** Real-time synchronization with the local SQLite database.

## 🛠 Tech Stack & Tools

*   **Language:** Java
*   **UI/UX:** XML, Material Design Components, ViewBinding.
*   **Local Database:** SQLite (handled via `SQLiteOpenHelper` with custom query logic).
*   **Storage:** `SharedPreferences` for user sessions and state management.
*   **Architecture:** Fragment-based navigation with a single-activity approach for the main flow.
*   **Build System:** Gradle (Updated to support the latest Android Studio versions).

## 📈 Development Highlights

*   **Legacy Code Migration:** Successfully migrated the project from an older version of Android Studio to the latest environment, resolving dependency conflicts and updating the Gradle build system to support **Java 21** and **SDK 34**.
*   **Database Optimization:** Implemented robust SQLite handling to ensure data integrity across multiple tables (Users, Books, Categories, Bookmarks).
*   **UX Improvements:** Refined the UI for better Right-to-Left (RTL) support and accessibility (SP/DP unit optimizations).

## ⚙️ Installation

1.  Clone the repository:
    ```sh
    git clone git@github.com:ZeboDalimova/Library-Management-System-Android.git
    ```
2.  Open the project in **Android Studio (Hedgehog or newer)**.
3.  Sync the project with Gradle files.
4.  Run the application on an emulator or a physical device (API 24+).

---
*Developed as a University Course Project.*
