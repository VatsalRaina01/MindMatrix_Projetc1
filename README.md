<p align="center">
  <h1 align="center">🍼 ShishuSneh</h1>
  <p align="center">
    <b>A comprehensive baby health & development tracker designed for Indian mothers</b>
    <br/>
    <i>Powered by Gemini AI · Built with Jetpack Compose · Bilingual (English & Hindi)</i>
  </p>
  <p align="center">
    <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the-badge&logo=android" alt="Platform"/>
    <img src="https://img.shields.io/badge/Min%20SDK-26-blue?style=for-the-badge" alt="Min SDK"/>
    <img src="https://img.shields.io/badge/Kotlin-2.0-purple?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
    <img src="https://img.shields.io/badge/Jetpack%20Compose-Material3-teal?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
  </p>
</p>

---

## 📖 About

**ShishuSneh** (शिशु स्नेह — *"Affection for the Child"*) is an offline-first Android application that helps Indian mothers track their baby's health, growth, vaccinations, milestones, and feeding patterns — all in one place.

The app integrates **Google's Gemini AI** to provide context-aware parenting guidance, follows India's **National Immunization Schedule (NIS)**, and uses **WHO growth standards** for percentile-based growth monitoring. Everything is stored locally with **encrypted Room database** (SQLCipher), ensuring complete data privacy.

---

## ✨ Key Features

### 🏠 Dashboard
- At-a-glance baby health summary — age, upcoming vaccines, recent growth, milestone progress
- Quick-action cards for logging feeds, growth, and accessing AI chat
- Real-time age calculation (years, months, weeks, days)

### 📈 Growth Tracking
- Log weight, height, and head circumference over time
- **WHO growth percentile** calculation with z-score analysis
- Interactive growth charts powered by **Vico**
- Visual growth zone indicators (healthy / underweight / overweight)

### 💉 Vaccination Manager
- Full **National Immunization Schedule (NIS)** pre-loaded
- Automatic due-date calculation from date of birth
- Status tracking: Pending → Done / Overdue
- **Push notification reminders** at 7 days, 3 days, and day-of (via WorkManager)
- Bilingual vaccine & disease names (English + Hindi)

### 🎯 Milestone Tracker
- Developmental milestones organized by age (weeks) across 4 domains:
  - **Motor** · **Language** · **Social** · **Cognitive**
- Mark milestones as Achieved / Not Yet / Skipped
- Photo capture for milestone memories
- Bilingual descriptions

### 🍽️ Feeding Log
- Log breastfeeding (with side tracking), formula, and solid food entries
- Duration (minutes) and quantity (ml) tracking
- Daily feeding summary and history
- Contextual feeding tips based on baby's age

### 🤖 AI Parenting Assistant
- Powered by **Gemini 2.5 Flash Lite** via Retrofit
- Context-aware: knows baby's age, recent growth data, and feeding history
- Conversational chat interface with message history
- Response caching for offline access
- Bilingual support (English / Hindi)

### 🌐 Bilingual Support
- Full Hindi (`values-hi`) and English localization
- Language selection during onboarding
- All medical terms, milestones, and tips available in both languages

### 🔒 Privacy & Security
- **Offline-first** — all data stored locally on device
- **SQLCipher** encrypted database
- No user accounts, no cloud sync, no data collection
- Gemini API calls are the only network requests (and responses are cached)

---

## 🏗️ Architecture

The app follows **Clean Architecture** with **MVVM** pattern:

```
com.shishusneh/
├── data/
│   ├── constants/          # WHO growth data, NIS schedule, milestones, feeding tips
│   ├── local/
│   │   ├── dao/            # Room DAOs (BabyProfile, Growth, Vaccination, Milestone, Feeding, GenAI)
│   │   ├── entity/         # Room entities
│   │   ├── converter/      # Type converters
│   │   └── ShishuSnehDatabase.kt
│   ├── remote/
│   │   ├── GeminiApiService.kt    # Retrofit interface for Gemini API
│   │   └── dto/                   # Response DTOs
│   └── repository/         # Repository implementations
│       ├── BabyProfileRepository
│       ├── GrowthRepository
│       ├── VaccinationRepository
│       ├── MilestoneRepository
│       ├── FeedingRepository
│       ├── GenAIRepository
│       └── UserPreferencesRepository
├── di/                     # Hilt dependency injection modules
│   ├── AppModule
│   ├── DatabaseModule
│   └── NetworkModule
├── domain/
│   └── model/              # Domain models (BabyProfile, GrowthEntry, Vaccination, etc.)
├── navigation/             # Compose Navigation (NavGraph, BottomNavItems)
├── ui/
│   ├── theme/              # Material3 theming (Color, Type, Theme)
│   ├── home/               # Dashboard screen
│   ├── growth/             # Growth tracking & charts
│   ├── vaccination/        # Vaccination manager
│   ├── milestone/          # Milestone tracker
│   ├── feeding/            # Feeding log & AI chat
│   ├── onboarding/         # Language selection & profile setup
│   ├── profile/            # Baby profile management
│   └── settings/           # App settings
├── util/                   # Utilities (AgeCalculator, InputValidator, NotificationHelper)
├── worker/                 # WorkManager (VaccinationReminderWorker)
├── MainActivity.kt         # Single Activity host
└── ShishuSnehApp.kt        # Hilt Application class
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **UI** | Jetpack Compose + Material 3 |
| **Navigation** | Compose Navigation |
| **State Management** | ViewModel + StateFlow |
| **DI** | Hilt (Dagger) |
| **Database** | Room + SQLCipher (encrypted) |
| **Preferences** | DataStore |
| **Networking** | Retrofit + OkHttp + Moshi |
| **AI** | Google Gemini 2.5 Flash Lite |
| **Charts** | Vico Compose |
| **Images** | Coil |
| **Animations** | Lottie Compose |
| **Background Work** | WorkManager |
| **Build** | Gradle KTS + KSP |
| **Language** | Kotlin 2.0, Java 17 target |

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Ladybug (2024.2.1) or newer
- **JDK 17**
- **Android SDK 34**
- A **Gemini API Key** from [Google AI Studio](https://aistudio.google.com/apikey)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/VatsalRaina01/MindMatrix_Projetc1.git
   cd MindMatrix_Projetc1
   ```

2. **Add your Gemini API key**
   
   Create or edit `local.properties` in the project root:
   ```properties
   GEMINI_API_KEY=your_gemini_api_key_here
   ```

3. **Open in Android Studio** and let Gradle sync

4. **Run** on an emulator or physical device (API 26+)

> **Note:** The app works fully offline except for the AI chat feature, which requires an internet connection and a valid Gemini API key.

---

## 📊 Data Sources

| Data | Source |
|------|--------|
| Growth Percentiles | **WHO Child Growth Standards** (weight-for-age, 0–5 years) |
| Vaccination Schedule | **India's National Immunization Schedule (NIS)** |
| Developmental Milestones | **CDC / WHO** developmental milestone guidelines |
| Feeding Tips | Curated from pediatric nutrition guidelines, age-stratified |

---

## 📁 Project Structure

```
ShishuSneh/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/shishusneh/    # Application source code
│   │   │   └── res/
│   │   │       ├── values/             # English strings & themes
│   │   │       ├── values-hi/          # Hindi strings
│   │   │       ├── drawable/           # Icons & drawables
│   │   │       └── xml/               # Backup & network config
│   │   └── test/                       # Unit tests
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml              # Version catalog
├── build.gradle.kts                    # Root build file
├── settings.gradle.kts
└── README.md
```

---

## 🤝 Contributing

Contributions are welcome! Here's how to get started:

1. **Fork** the repository
2. Create a **feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. Open a **Pull Request**

---

## 📄 License

This project is part of the **MindMatrix** initiative. All rights reserved.

---

<p align="center">
  <b>Built with ❤️ for every Indian mother and her शिशु</b>
  <br/>
  <sub>ShishuSneh — Because every milestone matters.</sub>
</p>
