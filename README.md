# Local Service Finder App

## Overview
The **Local Service Finder App** is a mobile application that connects customers with local service providers based on their location. Customers can browse available service providers, book appointments, and track their bookings, while service providers can manage their service requests.

## Features
- **User Authentication:** Firebase Authentication for secure sign-up and login.
- **User Roles:** Customers and Service Providers have different dashboards.
- **Location-based Service Providers:** Service providers are filtered based on the user's city.
- **Service Provider Selection & Booking:** Customers can book service providers from a list.
- **Booking Management:** Customers can track their bookings, and service providers can view upcoming and completed requests.

## Setup and Execution Instructions

### Prerequisites
Ensure you have the following installed:
- **Android Studio** (Latest version recommended)
- **Java Development Kit (JDK)**
- **Firebase Account** (For authentication and Firestore database setup)

### Installation Steps
1. **Clone the Repository**
   ```sh
   git clone https://github.com/your-username/local-service-finder.git
   cd local-service-finder
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Click on **Open an Existing Project**
   - Select the cloned repository folder

3. **Configure Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project and add an **Android App**
   - Download the **google-services.json** file and place it in `app/` directory
   - Enable **Authentication** and **Firestore Database** in Firebase

4. **Build and Run the App**
   - Connect an Android device or use an emulator
   - Click **Run** ▶️ in Android Studio

### Execution Instructions
1. **Launching the App**
   - Open the app on your Android device or emulator.

2. **User Login/Signup**
   - New users must sign up with an email and password.
   - Existing users can log in using their credentials.

3. **Using the Customer Dashboard**
   - The home screen displays a list of available service providers filtered by city.
   - Tap on a service provider to view details and book a service.
   - The **"My Bookings"** tab shows all past and upcoming bookings.

4. **Using the Service Provider Dashboard**
   - Service providers can view **upcoming and completed service requests**.
   - They can accept or reject service requests as needed.

5. **Profile Management**
   - Users can update their profile details, such as name and city.

## Dependencies & External Libraries
The project uses the following dependencies:

```gradle
dependencies {
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-firestore:24.0.0'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'com.google.android.gms:play-services-location:18.0.0'
}
```

### Installing Dependencies
- Dependencies are managed via **Gradle**.
- Ensure that you have an active internet connection for Gradle sync.
- If Gradle fails, try running:
  ```sh
  ./gradlew build
  ```

## Folder Structure
```
/local-service-finder
│── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/servicefinder/
│   │   │   │   ├── activities/ (All activity files)
│   │   │   │   ├── adapters/ (RecyclerView adapters)
│   │   │   │   ├── models/ (Data models)
│   │   │   │   ├── utils/ (Utility classes)
│   │   │   ├── res/layout/ (XML layout files)
│   │   │   ├── res/drawable/ (App icons and images)
│── google-services.json (Firebase Configuration)
│── build.gradle (Gradle dependencies)
│── README.md (This file)
```

## Contributions
Contributions are welcome! Feel free to submit a pull request or open an issue.

## License
This project is licensed under the **MIT License**.

## Contact
For any inquiries, contact **rudrakalariya1@gmail.com**.

