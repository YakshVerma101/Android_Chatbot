Writter — AI Chatbot 

Writter is an Android chatbot app built with Kotlin and Jetpack Compose, powered by Google AI’s Gemini 2.5 Pro model.
It enables real-time intelligent conversations with natural, context-aware responses.
Firebase Authentication is integrated for secure user login and session management, and the app can be extended to store user-specific chat histories.

Features
AI conversations with Gemini 2.5 Pro for natural and contextual replies
Firebase Authentication for secure login and signup
Session-based chat with individual user sessions
Modern Material 3 design using Jetpack Compose
Optional Firebase Realtime Database for message storage
Optimized for Android 13+ (API 34)

Tech Stack
Layer	Technology
Language	Kotlin
UI Framework	Jetpack Compose, Material 3
AI Model	Google AI Gemini 2.5 Pro
Authentication	Firebase Auth
Database	Firebase Realtime DB (optional)
Networking & AI	com.google.ai.client.generativeai
Build System	Gradle 8 + Kotlin DSL

Setup Instructions
Clone the repository
git clone https://github.com/<your-username>/Writter.git
cd Writter
Add your Gemini API key
Visit Google AI Studio
Create a new API key
In local.properties, add:
GEMINI_API_KEY=your_api_key_here
Add your Android package name (for example, com.example.writter)
Download google-services.json and place it in your app module
Sync Gradle
Android Studio → Sync Project with Gradle Files
Run the App
Launch on an emulator or real device

Dependencies
implementation("com.google.ai.client.generativeai:generativeai:1.1.0")
implementation("androidx.compose.material3:material3:<latest>")
implementation("androidx.activity:activity-compose:<latest>")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:<latest>")
implementation("com.google.firebase:firebase-auth:<latest>")
implementation("com.google.firebase:firebase-database:<latest>")
Example Usage
val model = GenerativeModel(
    modelName = "gemini-2.5-pro",
    apiKey = BuildConfig.GEMINI_API_KEY
)
val chat = model.startChat()
val response = chat.sendMessage("Hello! Tell me about black holes.")
Log.d("GeminiChat", response.text ?: "No response")

Future Enhancements
Multilingual support
Voice input and output
Cloud chat history sync
Custom themes and avatars

License
This project is licensed under the MIT License — you may use, modify, and distribute it with attribution.
