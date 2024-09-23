## Test Video App

#### How to Set Up the App

   **Requirements:** Android Studio, Android SDK, Google API key for Maps, Google web api id.
   
   **Steps:**
   1. Clone the project repository.
   2. Open the project in Android Studio.
   3. In **local.defaults.properties**, provide your Google API key for Maps and Web API client id for get Google Id option.
   4. Sync the Gradle files and install the required dependencies.
   5. Build and run the app on a physical or virtual Android device.
____

#### Architecture Used

   **The app follows MVVM architecture:**
   
+ Model: Handles data sources using Repository patterns.
+ ViewModel: Manages UI-related data and communicates with the repository.
+ Fragments(View): Observes LiveData from the ViewModel for state changes.
+ LiveData: Keeps the UI updated with changes in data.
+ Repository: Abstracts data handling logic and manages database operations.
+ Saving Data: Used Shared Preferences.
____ 

#### Key Features

**1. Google Sign-In:** Google credentials are handled by GoogleSignInClient and passed through the ViewModel for user authentication. User data is stored in Shared Preferences. Logout and deletion of all user data is included.

**2. Video Playback:** Handled using ExoPlayer for efficient media playback. The ViewModel tracks playback state, including saving/restoring the video position. 

**3. Maps:** Integrated Google Maps API to show locations related to videos. The ViewModel fetches video locations from the repository and updates the map markers in the view.

#### Additional options

+ Once logged in, the user will have access to a **Profile** page with user details such as username and photo.
+ Profile page have Favorites videos.
____ 
