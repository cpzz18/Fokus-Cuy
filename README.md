# FokusCuy (Android App)

Aplikasi pencatatan fokus dan manajemen tugas harian yang dibangun menggunakan **Kotlin**, **Jetpack Compose (Material 3)**, dan **SQLite (via Android Jetpack Room)** dengan arsitektur **Clean Architecture & MVI/MVVM**.

---

## 🛠️ Tech Stack & Spesifikasi

- **Bahasa**: Kotlin 2.2.x
- **UI Toolkit**: Jetpack Compose dengan Material 3 (BOM `2026.02.01`)
- **Database Lokal**: SQLite via Android Jetpack Room `2.7.2` (dengan KSP code generation)
- **Asynchronous**: Kotlin Coroutines & Reactive `Flow`
- **State Management**: Android ViewModel + `StateFlow` (Unidirectional Data Flow)
- **Build System**: Gradle 9.x + Android Gradle Plugin (AGP) 9.4.x
- **SDK Target**: Min SDK 24 (Android 7.0) | Target SDK 37

---

## 📋 Prasyarat Sistem

Sebelum menjalankan proyek, pastikan komputermu sudah memiliki:
1. **Android Studio** versi terbaru (disarankan Ladybug / Meerkat atau yang lebih baru).
2. **JDK**: Java 17 atau Java 21+ terkonfigurasi pada Android Studio (buka `Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK`).
3. **Android SDK Platform 34/35+** dan Android Build Tools terpasang via SDK Manager.

---

## 🚀 Langkah Setup Proyek

### 1. Clone Repositori
```bash
git clone <URL_REPOSITORY>
cd FokusCuy
git checkout dev
```

### 2. Konfigurasi `local.properties`
Buka proyek di Android Studio, atau buat file `local.properties` di root direktori jika belum terbuat otomatis:
```properties
## Ganti path berikut sesuai lokasi Android SDK di komputermu
# Linux / macOS:
sdk.dir=/home/<username>/Android/Sdk
# Windows:
# sdk.dir=C\:\\Users\\<username>\\AppData\\Local\\Android\\Sdk
```

### 3. Sync Gradle
- Di Android Studio: Klik tombol **Sync Project with Gradle Files** (ikon gajah).
- Tunggu hingga Gradle mengunduh dependencies dan KSP menginisialisasi symbols.

---

## 💻 Menjalankan Aplikasi

### Lewat Android Studio
1. Pilih konfigurasi run **`app`**.
2. Pilih target emulator atau perangkat fisik (USB Debugging aktif).
3. Klik tombol **Run** (`Shift + F10` / ikon play hijau).

### Lewat Terminal (CLI)
- **Compile & Generate KSP (Room DAO & Database)**:
  ```bash
  ./gradlew compileDebugKotlin
  ```
- **Jalankan Unit Test**:
  ```bash
  ./gradlew testDebugUnitTest
  ```
- **Build file APK Debug**:
  ```bash
  ./gradlew assembleDebug
  ```
  File APK hasil build berada di:
  `app/build/outputs/apk/debug/app-debug.apk`

---

## 🏛️ Struktur Arsitektur (Clean Architecture)

Proyek ini memisahkan layer secara terisolasi agar mudah dites dan di-maintain:

```
app/src/main/java/com/example/fokus_cuy/
├── MainActivity.kt                  # Entry point activity, inisialisasi Room & DI
├── domain/                          # Business logic murni (zero Android framework dependency)
│   ├── model/
│   │   └── TaskItem.kt              # Domain model
│   └── repository/
│       └── TaskRepository.kt        # Interface kontrak data reaktif (Flow)
├── data/                            # Implementasi data source lokal
│   ├── local/
│   │   ├── AppDatabase.kt           # Instance Singleton SQLite Room Database
│   │   ├── dao/TaskDao.kt           # Query SQL SQLite (@Query, @Insert, @Update, @Delete)
│   │   └── entity/TaskEntity.kt     # Tabel SQLite `tasks` + mapper domain
│   └── repository/
│       └── TaskRepositoryImpl.kt   # Implementasi repository (Dispatchers.IO)
└── ui/                              # Presentation Layer (Jetpack Compose)
    ├── tasks/
    │   ├── TaskScreen.kt            # Layar Compose Material 3 (TopAppBar, Form Input, List)
    │   ├── TaskUiState.kt           # Immutable UI State
    │   └── TaskViewModel.kt         # ViewModel pengelola StateFlow & event
    └── theme/                       # Konfigurasi Color, Type, dan Material3 Theme
```

---

## ⚠️ Catatan Troubleshooting

1. **Error KSP SourceSets pada AGP 9**:
   AGP 9 menggunakan fitur *built-in Kotlin*. Konfigurasi berikut sudah disematkan di [gradle.properties](gradle.properties) untuk memastikan kompatibilitas:
   ```properties
   android.disallowKotlinSourceSets=false
   android.sync.suppressAgpWarnings=UNSUPPORTED_PROJECT_OPTION_USE
   ```
2. **Room Schema / Migration**:
   Database lokal menggunakan nama `fokus_cuy.db`. Jika mengubah skema tabel di `TaskEntity`, naikkan versi di `AppDatabase.kt` (saat ini `version = 1`). Mode destructive migration aktif untuk mempermudah development.
