# 🏋️ Fitness Gym Management System

A console-based Java application for managing gym members, class enrollments, waitlists, and supporting undo operations.

---

## 📖 Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Data Structures](#data-structures)
- [Key Algorithms](#key-algorithms)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## ✨ Features

- **Member Management** – Add, remove, update, and search members by ID.
- **Sorted Listing** – Display all members sorted alphabetically by name.
- **Class Management** – Create gym classes with a specified capacity.
- **Smart Enrollment** – Enroll members directly if space is available; otherwise, add them to a FIFO waitlist.
- **Automatic Promotion** – When a member cancels, the first member on the waitlist is automatically promoted to the enrolled list.
- **Undo Support** – Reverse the last action (add member, remove member, enroll, or cancel enrollment).
- **In-Memory Storage** – Uses Java Collections for fast, temporary data handling.

---

## 🛠️ Technologies

- **Java** (JDK 8 or higher)
- **Java Collections Framework** (`ArrayList`, `TreeMap`, `Queue`, `Stack`)
- No external libraries or databases required.

---

## 📁 Project Structure

| File | Description |
| :--- | :--- |
| `Member.java` | POJO representing a gym member (ID, name, phone, membership type). |
| `GymClass.java` | Represents a class with enrolled members, a waitlist queue, and enrollment/cancellation logic. |
| `Action.java` | Encapsulates an action type and data for undo operations. |
| `GymManager.java` | Main controller. Handles the CLI menu, business logic, and the undo stack. |

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher installed on your system.
