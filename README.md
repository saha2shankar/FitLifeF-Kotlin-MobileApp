# FitLife - Your Personal Fitness Companion

FitLife is a comprehensive mobile application designed to help users track their fitness journey, manage workouts, monitor expenses related to fitness, and discover fitness locations. Built with **Kotlin** and **Jetpack Compose**, it leverages **Firebase** for backend services, providing a seamless and modern user experience.

## 📱 Project Overview

FitLife aims to be a one-stop solution for fitness enthusiasts. Whether you are planning your weekly workouts, tracking your gym expenses, or looking for the nearest gym or park, FitLife has you covered.

## ✨ Key Features

-   **User Authentication**: Secure Login, Sign-up, and Forgot Password functionality using Firebase Auth.
-   **Dashboard**: A personalized home screen displaying user details and quick access to key features.
-   **Exercise Management**:
    -   Create, read, update, and delete (CRUD) personal exercises.
    -   Add details like routine name, instructions, equipment needed, and images.
    -   Search exercises by name.
-   **Weekly Workout Plan**:
    -   Organize exercises by day of the week (Sunday - Saturday).
    -   Mark workouts as completed.
    -   Track sets, reps, and notes for each planned exercise.
-   **Expense Tracking**:
    -   Monitor fitness-related expenses (e.g., supplements, gear, memberships).
    -   View total expenditure.
    -   Add and delete expense records.
-   **Fitness Maps**:
    -   Interactive Google Map showing user location.
    -   Discover nearby gyms, parks, swimming pools, and yoga centers in Kathmandu.
    -   Get directions to fitness locations.
-   **Accessories Guide**:
    -   A visual catalog of essential gym accessories with descriptions.

## 📱 Screens & Navigation

1.  **Splash Screen**: Welcome screen.
2.  **Auth Screens**:
    -   **Login**: Email/Password login.
    -   **Signup**: Create a new account.
    -   **Forgot Password**: Reset password via email.
3.  **Dashboard (Home)**: Central hub with navigation to other modules.
4.  **Exercise Screen**: Lists all custom exercises. Supports searching and swipe-to-delete/edit.
    -   **Create/Edit Exercise**: Form to input exercise details.
5.  **Week Plan Screen**: Displays your weekly schedule. Swipe right to complete, swipe left to delete.
6.  **Expense Screen**: Tracks your spending. Shows a summary card and a list of transactions.
7.  **Map Screen**: Google Maps integration displaying fitness points of interest.
8.  **Accessories Screen**: Grid view of fitness equipment.

## 🛠 Tech Stack

-   **Language**: Kotlin
-   **UI Framework**: Jetpack Compose (Material3)
-   **Architecture**: MVVM (Model-View-ViewModel)
-   **Backend**: Firebase
    -   **Authentication**: User management.
    -   **Firestore**: NoSQL database for storing user, exercises, plans, and expenses.
-   **Maps**: Google Maps SDK for Android (Maps Compose).
-   **Image Loading**: Coil.
-   **Asynchronous Programming**: Coroutines & Flow.
-   **Navigation**: Jetpack Compose Navigation.

## 🗄 Database Structure (Firestore)

The app uses Cloud Firestore with the following collections:

### 1. `users`
Stores user profile information.
-   `uid` (Document ID)
-   `name`: String
-   `email`: String
-   `mobile`: String

### 2. `exercises`
Stores custom exercises created by the user.
-   `id`: String
-   `userId`: String (Owner)
-   `routineName`: String
-   `instructions`: String
-   `exercises`: List<String>
-   `equipment`: List<String>
-   `imageUrl`: String
-   `latitude`: Double (Optional)
-   `longitude`: Double (Optional)
-   `createdAt`: Timestamp

### 3. `expenses`
Stores expense records.
-   `id`: String
-   `userId`: String
-   `category`: String
-   `amount`: Double
-   `description`: String
-   `date`: Timestamp
-   `createdAt`: Timestamp

### 4. `weekly_plans` (Inferred)
Stores the weekly workout schedule.
-   `id`: String
-   `userId`: String
-   `exerciseId`: String
-   `exerciseName`: String
-   `day`: String (e.g., "Monday")
-   `reps`: Int
-   `sets`: Int
-   `notes`: String
-   `completed`: Boolean

## 🚀 Getting Started

### Prerequisites
-   Android Studio (latest version recommended).
-   JDK 11 or higher.
-   A Firebase project.
-   Google Maps API Key.

### Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/saha2shankar/FitLifeF-Kotlin-MobileApp.git
    ```
2.  **Open in Android Studio**:
    Open the project directory in Android Studio.

3.  **Firebase Setup**:
    -   Create a project in the [Firebase Console](https://console.firebase.google.com/).
    -   Add an Android app with the package name `np.com.harishankarsah.fitlife`.
    -   Download `google-services.json` and place it in the `app/` directory.
    -   Enable **Authentication** (Email/Password).
    -   Enable **Cloud Firestore**.

4.  **Google Maps Setup**:
    -   Get an API Key from the [Google Cloud Console](https://console.cloud.google.com/).
    -   Enable "Maps SDK for Android".
    -   Add the key to your `local.properties` or `AndroidManifest.xml` (ensure it's securely managed).
    *Note: The project might expect the key in `AndroidManifest.xml` meta-data.*

5.  **Build and Run**:
    -   Sync Gradle files.
    -   Run the app on an emulator or physical device.

## 📖 User Manual (For Non-Technical Users)

1.  **Sign Up**: Open the app and click "Sign Up". Enter your details to create an account.
2.  **Dashboard**: You will see your home screen. From here, you can access all features.
3.  **Adding an Exercise**:
    -   Go to **Exercises**.
    -   Tap the **+** button.
    -   Fill in the details (Name, Instructions, etc.) and save.
4.  **Planning Your Week**:
    -   Go to **Weekly Plan**.
    -   You can view your schedule. (Adding plans is likely done via the Exercise details or a specific "Add to Plan" button).
    -   Swipe **Right** on a task to mark it as DONE.
    -   Swipe **Left** to remove it.
5.  **Tracking Expenses**:
    -   Go to **Expense Tracking**.
    -   Tap **+** to add a new expense.
    -   Enter the amount and description.
    -   View your total spending at the top.
6.  **Finding Gyms**:
    -   Go to **Maps**.
    -   Allow location permissions.
    -   View nearby gyms and parks on the map.

## 🤝 Contribution

Contributions are welcome!
1.  Fork the project.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

## 🔮 Future Improvements

-   **Dark Mode Support**: Full system-wide dark mode.
-   **Notifications**: Reminders for workouts.
-   **Offline Mode**: Room database caching for offline access.
-   **Social Features**: Share workouts with friends.
-   **Advanced Analytics**: Charts for weight progress and expense categories.

## 📞 Support

For support, email [sahharishankar11@gmail.com](mailto:sahharishankar11@gmail.com) or open an issue in the repository.

---
*Made with ❤️ and lots of love by Hari Shankar Sah*
