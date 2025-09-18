
# Course1 — NIT3213 Android App

An Android app with **Login → Dashboard → Details** that integrates the `vu-nit3213-api`.

---

## Build & Run

### Requirements
- **Android Studio** 
- **JDK 17**
- Internet connection

### Steps (Android Studio)
1. **Clone** this repo and **Open** it in Android Studio.
2. Let **Gradle sync** finish (accept prompts).
3. Set **Gradle JDK = 17**  
   *Settings/Preferences → Build, Execution, Deployment → Gradle → Gradle JDK = 17*
4. Click **Run ▶** and choose an emulator or connected device.

### Optional: Gradle CLI
```bash
./gradlew assembleDebug     # builds the APK
./gradlew installDebug      # installs to a connected device/emulator
````

---

## How to Use

1. **Login**

   * **Username:** your first name (e.g., `Alex`)
   * **Password:** your Student ID **without** the leading `s` (e.g., `12345678`)
   * **Campus:** choose one — `br`, `sydney`, or `footscray`
2. **Dashboard** uses the `keypass` from login to call `/dashboard/{keypass}` and lists entities.
3. **Details** shows all fields (including `description`). Use **Back** to return.

---

## Dependencies & Setup

### Gradle / Android

* Kotlin + AndroidX (Activity, RecyclerView, ViewBinding)
* **Material 3** for UI
* **Coroutines** for async work

### Networking

* **Retrofit** (REST client)
* **OkHttp** (+ **HttpLoggingInterceptor**)
* **Gson** (JSON)

### Dependency Injection

* **Hilt** (Dagger Hilt)

### Unit Testing

* **JUnit4**
* **MockK**
* **kotlinx-coroutines-test**

> These are declared in the module `build.gradle(.kts)` and provided via a `NetworkModule` (Hilt) that creates `OkHttpClient`, `Retrofit`, `Gson`, and `ApiService`.

#### Required Android permission

```xml
<!-- app/src/main/AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET"/>
```

#### Hilt setup

* `App.kt` annotated with `@HiltAndroidApp`
* Activities annotated with `@AndroidEntryPoint`
* `NetworkModule.kt` annotated with `@Module` + `@InstallIn(SingletonComponent::class)`

#### API base URL

```
https://nit3213api.onrender.com/
```

* **Auth:** `POST /{campus}/auth` → `{ "keypass": "..." }`
* **Dashboard:** `GET /dashboard/{keypass}`

---

## Notes / Troubleshooting

* The API host can **cold start**; the **first request may take \~20–40s**. If you hit a timeout, try again.
* If builds fail due to JDK version, set **Gradle JDK = 17** (see steps above).
* If you see network errors on an emulator, confirm the emulator has internet (open a webpage).

---

## Tests (optional)

Run unit tests:

```bash
./gradlew test
```

Uses **kotlinx-coroutines-test** (with `Dispatchers.setMain`) and **MockK** for ViewModel logic.

```

