Group 15 MSD Project

Easy Access App
# Introduction

The Easy Access App is a digital student access card application designed for students of Technological University Dublin (TUD). It enables students to access campus facilities, such as the library and printers, using a convenient student ID card interface. The app features facial recognition, ID/password login, and incorporates TUD’s theme colors (Blue, White, Green) to enhance user familiarity and experience. Primary functionalities include digital ID presentation, library access, and printing access.

## Objectives
Digital Student ID: Develop a digital student ID card for TUD students with multiple login options (Facial ID, Student ID/Password, Passkey).
Core Functionalities: Include essential features such as library access, printer usage, credit management, and notifications.
User-Friendly Interface: Design an accessible interface with high contrast for visibility and support for Dark Mode.
##App Design
### Colour Scheme and Layout
**Colours**: Utilizes TUD theme colours: Blue, White, Green.
**Backgrounds**: White (Light Mode) and Black (Dark Mode) for enhanced contrast and accessibility.
## User Interface Design
Header Section: Displays the student’s name, ID number, profile picture, and additional details like course and faculty.
Digital Card Section: Offers a visual representation of the student card with swipe functionality to reveal additional information.
Action Cards Section: Quick-access icons for Library, Printer, and Credit Management.
Bottom Navigation: Navigation options for Home, Profile, Library, and Printing.
User Information Display
# Header Section:

Profile Picture: A clear, high-resolution image resembling a physical ID photo.
Student Details: Displays the student’s name, ID number, course details, academic year, and faculty (e.g., Computer Science, 3rd year).
Graduation/Expiry Date: Sets graduation year as the ID expiration.
Digital Card Display:

Expandable Section: Displays additional card details like a barcode for scanner compatibility at campus facilities.
Swipe Interaction: Smooth expand/collapse gesture to reveal or hide card details.
Action Cards
Provides quick access to essential services:

Library Access Icon: Recognizable book icon.
Printer Access Icon: Instant access via a printer symbol.
Credits/Top-Up: Euro sign for managing printer credits.
Notifications
Real-time notifications for:

Library Alerts: Such as available books or return due dates.
Printer Alerts: For notifications like “Document ready for printing.”
Visual Indicators: Icon badges and subtle alert sounds for user attention.
Key Functionalities and Requirements
Multi-Activity Design: Incorporates three or more activities (excluding login) with data sharing using intents.
Database Integration: Stores student details and usage history.
Sensor Usage: Implements biometric (Facial ID) and sensors for enhanced login security.
Custom Views & Gestures: Features an expandable card view with swipe gestures for additional details.
GitHub Version Control: Maintains consistent commits to a shared GitHub repository.
Dark Mode & Accessibility: Supports Dark Mode and high-contrast UI for visibility.
State Management & Optimization: Ensures efficient memory usage with optimized loading speeds.
Notification Integration: Implements push notifications for library and printing updates.
User Interaction Design and UX
The design adheres to Android UX guidelines for intuitive navigation:

Login Screen: Entry via Facial ID, Passkey, or Student ID/Password, with accessibility features like keyboard shortcuts and auto-focus.
Home Screen: Displays an overview of the digital student card, access icons, and profile information.
Notification System: Alerts presented through the notification tray with relevant icons (e.g., library book icon for library notifications).
Technical Implementation
Data Handling: Uses a database to store student data and transaction history.
Sensors and Biometrics: Utilizes facial recognition for secure access.
Custom Views & Swipe Gestures: Implements Android’s View animations for a smooth, expandable card interface.
Continuous Git Integration: Regularly updates and commits with documented changes.
Accessibility and User-Centered Design
The app is designed with accessibility in mind:

Contrast Settings: Ensures visibility in both Light and Dark Modes.
Font Scaling: Supports Android’s font scaling for improved readability.
Icons and Labels: Features clearly labeled icons for navigation with tooltips and haptic feedback.
Future Enhancements
Potential improvements include:

NFC Integration: Facilitate contactless ID access in future iterations.
Advanced Notification Settings: Customize alerts for library or printer usage.
