# Accessible Text Reader

A Java application that reads text files aloud, developed as part of an academic activity on inclusive technologies.

## 📋 About the Project

This project was developed as an alternative activity for the Computer Programming course in the ADS program at IFSP-Caraguatatuba, in the context of the institutional event celebrating the World Day for Cultural Diversity for Dialogue and Development (May 21, 2025).

The central theme of the event was "Connections that Transform: Cultural Diversity and Technological Inclusion" and inspired the creation of this accessible text reader, designed as a tool for people with visual impairments.

## ✨ Features

- Automatic reading aloud of any text file
- Line-by-line navigation controlled by the user
- Three reading speeds (Slow, Normal, Fast)
- Simple and accessible interface
- Intuitive control through keyboard shortcuts
- Visual feedback of reading progress

## 🔧 Requirements

- Windows (uses the system's native voice synthesizer)
- Java Runtime Environment (JRE) 8 or higher
- Functional audio output

## 💻 How to Use

1. Run the program
2. A welcome message will be displayed and automatically read
3. Select any text file for reading
4. Choose your preferred speed (Slow, Normal, or Fast)
5. Use the space bar to advance and hear each line
6. Press ESC when you want to end the reading

## ⌨️ Keyboard Shortcuts

- **Space bar**: Reads the current line and advances to the next
- **ESC**: Ends the reading and closes the program

## 🔍 Technical Details

The application was built with:
- Java Swing for the graphical interface
- Windows System.Speech (via PowerShell) for voice synthesis
- Threads to allow simultaneous reading with dialog display

---

Developed by Vitor de Oliveira, May/2025.
