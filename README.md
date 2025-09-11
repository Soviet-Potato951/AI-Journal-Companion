# AI Journal Companion 📓🤖

The **AI Journal Companion** is an Android app built with **Kotlin** and **Jetpack Compose** that helps users record their daily thoughts, detect emotions, and receive supportive advice. It also provides tools to visualize emotional trends through interactive charts.

---

## ✨ Features

- **Journal Entries**  
  Write and save daily reflections with a clean Compose UI.  

- **Emotion Detection**  
  Backend-powered emotion analysis provides categorized moods and tailored advice.  

- **Sorting & Searching**  
  - Sort entries by Bubble Sort, Insertion Sort, or Selection Sort.  
  - Search entries using Binary Search Trees, HashMaps, or Doubly Linked Lists.  

- **Visualization**  
  - Generate PieCharts showing emotional distribution.  
  - Quickly spot trends and changes in mood.  

- **Help & Support**  
  - In-app Help popup with FAQs and step-by-step guides.  
  - Drag-and-drop deletion of entries.  

---

## 📱 Installation

### Prerequisites
- Android 8.0 (API 26) or higher  
- Device with at least **2GB RAM** (or Android Emulator)  
- Internet connection for backend emotion detection  

### Steps
1. Download the `.apk` from the **Releases** section.  
2. Transfer it to your device and install (enable *Install from Unknown Sources* if prompted).  
3. Launch the app and start journaling!  

---

## 🚀 Usage

- **Create Entry:** Tap the `+` button to add new journal text.  
- **Trigger Emotion Detection:** Save an entry and let the backend analyze it.  
- **Sort Entries:** Use the dropdown menu to select sorting algorithms.  
- **Search Entries:** Choose your preferred search method from the dropdown.  
- **View Charts:** Navigate to the PieChart view to see emotion breakdowns.  
- **Open Help:** Access the help popup for FAQs and instructions.  
- **Delete Entry:** Long-press an entry and drag it to remove.  

---

## 🧪 Performance Evaluation

- **Emotion Detection:** Measures backend responsiveness and accuracy.  
- **Resource Usage:** Optimized for smooth performance on devices and emulators.  
- **Backend Delay:** Tested with both local and remote FastAPI servers.  
- **Algorithm Analysis:**  
  - **Sorting:** Bubble, Insertion, Selection – compared by speed and efficiency.  
  - **Searching:** Binary Search Tree, HashMap, Doubly Linked List.  

---

## 🛠️ Tech Stack

- **Frontend:** Kotlin, Jetpack Compose  
- **Backend:** FastAPI (Python) for emotion detection  
- **Data Models:** Kotlin `data class` with enums for sort/search methods  
- **Visualization:** Custom Compose `Canvas` PieChart  

---

## 📂 Project Structure

