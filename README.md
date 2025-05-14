# 🗳️ Voting Machine Simulator

A multi-component **voting machine simulation system** written in **Java**, designed to model real-world voting infrastructure. This project simulates communication between voting hardware components such as card readers, printers, screens, and a central voting manager — all using Java sockets and threading.

---

## 📌 Features

- 🧾 Simulated smart cards with voter info stored on virtual SD cards  
- 🖨️ Printer and screen devices with simulated I/O  
- 🧠 Voting manager logic that processes votes and handles device coordination  
- 🔄 Multi-threaded architecture using Java’s `BlockingQueue` for inter-component messaging  
- 🧪 Terminal-based control panel for inserting cards and triggering events  
- ❌ Failure simulation (e.g., printer failure) to test system resilience  

---

## 🛠️ Technologies Used

- **Language:** Java  
- **Concurrency:** Java Threads, `BlockingQueue`  
- **Communication:** Java Sockets  
- **File I/O:** Serialization of ballot and card objects  
- **IDE:** IntelliJ IDEA  


---

## 🏗️ Architecture

The system follows a modular architecture where each hardware component (screen, printer, SD card readers, etc.) is simulated as a separate thread that communicates with a central controller called `VotingControl`. These components interact using message-passing and socket communication.

### 📊 System Diagram

![System Architecture](![architecture](https://github.com/user-attachments/assets/2d64c6d9-4381-4c62-9b61-0ab84ec6271e))

> ℹ️ Make sure to place the image in your repo (e.g., under a `resources/` folder), and update the path if needed.

### 🧩 Component Overview

- **Main**  
  Launches the system and initializes all components.

- **VotingControl**  
  Acts as the central hub, managing communication and coordination among all subsystems.

- **Monitor**  
  Observes and logs the system's state.

- **AdminManager**  
  Handles admin interactions and system configuration.

- **VotingManager**  
  Validates and processes incoming votes.

- **CardHolder**  
  Interfaces with SD card readers representing voter cards.

---

### 🔌 Drivers and Devices

| Driver            | Connected Component |
|------------------|----------------------|
| Latch Driver      | Latches              |
| SD Card 1 Driver  | SD Card 1            |
| Screen Driver     | Screen               |
| SD Card 2 Driver  | SD Card 2            |
| SD Card 3 Driver  | SD Card 3            |
| Printer Driver    | Printer              |

Each driver operates as an independent thread and communicates with `VotingControl` using Java's `BlockingQueue`, mimicking asynchronous hardware communication in a real voting machine environment.

---
---
