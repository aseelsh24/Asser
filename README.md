# Asser - Age Calculator

Asser is a modern Android application built with Jetpack Compose that allows you to calculate your age with precision. You can calculate your age based on today's date or any custom date you choose. The app keeps a history of your last 10 calculations and offers a clean, customizable user experience with multiple themes.

## ✨ Features

- **Age Calculation**: Calculate age in years, months, days, and hours.
- **Flexible Dates**: Choose to calculate age until today or a specific custom date.
- **Calculation History**: Automatically saves the last 10 calculations for quick reference.
- **Share Results**: Easily share your calculated age with friends and family through any social app.
- **Birthday Countdown**: See how many days are left until your next birthday.
- **Material Design 3**: A beautiful, modern UI built entirely with Jetpack Compose and Material 3.
- **Theming**:
    - Light and Dark mode support.
    - 3 accent color themes to choose from (Green, Blue, Purple).
    - Dynamic color support on Android 12+.
- **Splash Screen**: A smooth entry into the app with a native splash screen.
- **MVVM Architecture**: Built using the recommended MVVM architecture pattern for a scalable and maintainable codebase.

## 🛠 Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: Model-View-ViewModel (MVVM)
- **Language**: Kotlin
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Dependency Injection**: Hilt
- **Database**: Room for local storage
- **Navigation**: Jetpack Navigation Compose
- **Theming**: Material Design 3

## 🚀 How to Build and Run

You can build and run the project using Android Studio or from the command line.

### Prerequisites

- Java Development Kit (JDK) 17 or higher.
- Android Studio (latest version recommended).
- An Android device or emulator running API level 26 or higher.

### From Command Line

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd Asser
    ```

2.  **Grant execute permissions to the Gradle wrapper:**
    ```bash
    chmod +x ./gradlew
    ```

3.  **Build the project:**
    This will download all dependencies and compile the app.
    ```bash
    ./gradlew build
    ```

4.  **Install the app on a connected device or emulator:**
    ```bash
    ./gradlew installDebug
    ```

5.  **Run the app:**
    The app will be available on your device/emulator.

## ⚙️ GitHub Actions

This project is configured with a GitHub Actions workflow that automatically builds the project on every `push` and `pull_request` to the `main` branch.

The workflow performs the following steps:
1.  Checks out the code.
2.  Sets up the correct Java version.
3.  Builds the project using Gradle.
4.  Assembles a release-ready (unsigned) APK.
5.  Uploads the generated APK as a build artifact, which can be downloaded from the workflow summary page.
