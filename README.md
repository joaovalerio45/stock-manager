# StockManager

A full-stack warehouse and inventory management system designed to streamline physical stock tracking, manage internal department requisitions, and digitize paper-based workflows. 

---

## 🛠 Tech Stack

### Backend
- **Language:** Java 25
- **Framework:** Spring Boot (Data JPA, Web MVC, Validation)
- **Database:** PostgreSQL
- **Architecture:** Layered (Controller → Service → Repository), clean DTO mapping, standard error responses

### Frontend
- **Framework:** React 19 (TypeScript)
- **Bundler:** Vite
- **Styling:** Tailwind CSS v4 & Lucide React
- **Features:** Client-side routing, modular UI components, bilingual internationalization (PT/EN)

---

## ✨ Key Features
- **Multi-Warehouse Stock Control:** Track real-time physical stock per warehouse and receive alerts for items below minimum thresholds.
- **Movement Auditing:** Record all inbound stock (`ENTRY`) and outbound usage (`WITHDRAWAL`) with immutable operation codes to preserve audit trails.
- **Requisition Workflow:** Dedicated pipeline for department requests (`PENDING` → `PREPARING` → `FULFILLED`).
- **Data Integrity:** ACID-compliant transactions ensure that stock adjustments and document records are committed safely and accurately.

---

## 📁 Project Structure

```text
stock-manager/
├── backend/               # Spring Boot REST API
│   ├── src/main/java/     # Domain entities, DTOs, controllers, and services
│   └── pom.xml
├── frontend/              # React + Vite application
│   ├── src/
│   │   ├── components/    # Reusable UI widgets (Navbar, Status badges)
│   │   ├── context/       # State providers (e.g., Language Context for i18n)
│   │   ├── pages/         # Core application views (Dashboard, Stock, etc.)
│   └── package.json
└── README.md
