# AgriCare 🌱

**AgriCare** is a modern, local-first Android application designed for farmers and agricultural enterprises. It streamlines operations by offering guided business setup, fields and environmental classification, workforce organization, and real-time inventory management with physical stock audit logging and low-threshold automated alerts.

---

## 🚀 Core Features

### 1. Guided Enterprise Setup Wizard 🧙‍♂️
*   **Step-by-Step Onboarding:** Interactive wizard to establish your agricultural enterprise profile.
*   **Modular Setup Configuration:** Separate steps for naming the enterprise, registering cultivating fields, adding workers, and pre-loading inventory items.
*   **Progress Indicators:** Built-in modular flags (`fieldsSetupComplete`, `workersSetupComplete`, `inventorySetupComplete`) allow sections to be skipped and completed post-setup.

### 2. Field Management & Cultivation tracking 🚜
*   **Hectare Tracking:** Log physical field dimensions accurately in hectares.
*   **Environmental Classification:** Classify fields by type:
    *   `OPEN_FIELD`
    *   `GREENHOUSE`
    *   `SHADE_NET`
*   **Irrigation Infrastructure:** Tag irrigation methods utilized:
    *   `DRIP`
    *   `SPRINKLER`
    *   `FLOOD`
*   **Active Crop Association:** Tie fields to current crops being cultivated.

### 3. Inventory Management & Material Audits 📦
*   **Categorization:** Group inputs into structured categories:
    *   `SEED`
    *   `FERTILIZER`
    *   `PESTICIDE`
    *   `FUNGICIDE`
    *   `OTHER`
*   **Double-entry Quantities:** Tracks both **Book Quantity** (system transactions) and **Physical Quantity** (real-world audits).
*   **Minimum Threshold Warnings:** Configure critical replenishment stock levels.
*   **Physical Stock Checks:** Conduct reconciliation audits. Any deviation is logged in the custom historical database table (`stock_check`) with automatic variance calculation.

### 4. Background Processing & Intelligent Notifications 🔔
*   **Low Stock Monitor (`LowStockWorker`):** Runs periodically via WorkManager to inspect inventory. Automatically pushes system notifications if items drop below their minimum thresholds.
*   **Stock Check Reminders (`StockCheckReminderWorker`):** Automated monthly reminders prompting managers to perform physical stock checks to minimize variance.

---

## 🛠 Tech Stack & Architecture

AgriCare is built using modern Android development practices:

*   **Language:** Kotlin (JVM 17 Toolchain)
*   **UI Framework:** Jetpack Compose (Declarative UI, Material Design 3 via `LionicoTheme`)
*   **Architecture Pattern:** Clean MVVM (Model-View-ViewModel) with structured domain segregation:
    *   *View Layer:* Compose Screens & Composables.
    *   *ViewModel Layer:* UI state management utilizing Kotlin Coroutines Flows.
    *   *Repository Layer:* Single source of truth abstracting Local DB DAOs.
    *   *Data Layer (Room DB):* SQLite ORM representing relational farm data.
*   **Dependency Injection:** Dagger Hilt (with Hilt‑Work integration for Worker dependencies)
*   **Database:** Room (v3, featuring custom TypeConverters for LocalDate dates)
*   **Background Jobs:** Android WorkManager

---

## 📂 Project Architecture

```text
app/src/main/java/com/lionico/agricare/
├── LionicoApplication.kt       # Application initialization and WorkManager queue setup
├── MainActivity.kt             # Single activity entry point with custom Compose Tab Navigation
├── data/
│   ├── local/                  # Room local database implementation
│   │   ├── AgricareDatabase.kt # Room Database definition (v3)
│   │   ├── Converters.kt       # LocalDate Type Converters
│   │   ├── dao/                # Enterprise, Field, Inventory, StockCheck, and Worker DAOs
│   │   └── entity/             # Relational entities and categories (Enums)
│   └── repository/             # Repositories exposing flow-based local data
├── di/
│   └── AppModule.kt            # Hilt Module providing Database, DAOs, and Repositories
├── ui/                         # Presentation Layer
│   ├── field/                  # Field list, create/update screens & FieldViewModel
│   ├── inventory/              # Inventory management, audits, category filter & InventoryViewModel
│   ├── setup/                  # Enterprise multi-step wizard & EnterpriseViewModel
│   └── theme/                  # Theme configuration (Colors, Typography, Shape, and Theme)
└── worker/                     # WorkManager background tasks (LowStockWorker & StockCheckReminderWorker)
```

---

## 🏁 Getting Started

### Prerequisites
*   Android Studio (Ladybug or newer recommended)
*   JDK 17 installed and configured in your environment
*   Android SDK Platform 35

### Running the App

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/lionico/Agricare.git
    cd Agricare
    ```

2.  **Build the Project**
    Assemble the debug package using the Gradle wrapper:
    ```bash
    ./gradlew assembleDebug
    ```

3.  **Run Tests**
    Run the unit test suite:
    ```bash
    ./gradlew test
    ```

4.  **Install & Run**
    Deploy directly to a connected Android emulator or physical device:
    ```bash
    ./gradlew installDebug
    ```

---

## 🔒 Security & Offline-First

AgriCare is designed as an **offline-first** application. All operational farm data (field parameters, active crop IDs, employee logs, material volumes, and historical audit entries) are stored securely on-device inside a local SQLite database using Room. No internet connection is required to run daily farm audits or check inventory levels, ensuring utility in remote farming fields.
