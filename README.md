# Course1 — Android Application Development Project (NIT3213)

An Android app with **Login → Dashboard → Details** integrating the **vu-nit3213-api**.

## Tech Stack
- Kotlin, AndroidX (Activity, RecyclerView, ViewBinding)
- **Retrofit + OkHttp + Gson** (API)
- **Hilt** for dependency injection
- **MVVM** (Repository + ViewModels + Activities)
- Unit tests: **JUnit4**, **MockK**, **kotlinx-coroutines-test**

## Screenshots
| Login                    | Dashboard                        | Details                      |
|--------------------------|----------------------------------|------------------------------|
| ![Login](docs/login.png) | ![Dashboard](docs/dashboard.png) | ![Details](docs/details.png) |

## API
Base: `https://nit3213api.onrender.com/`

### Auth (POST `/{campus}/auth`)
- `campus` ∈ `br` | `sydney` | `footscray`
- Body:
```json
{ "username": "YourFirstName", "password": "YourStudentID" }

## Build & Run
1. Android Studio (Giraffe/Hedgehog+) + JDK 17
2. Clone → Open in Android Studio → let Gradle sync
3. Run the **app** on an emulator/device

## How to Use
1. **Login**
   - Username: your **first name** (e.g., `Alex`)
   - Password: your **Student ID** (without `s`, e.g., `12345678`)
   - Campus: choose **br / sydney / footscray** from the dropdown
2. **Dashboard**
   - Shows the list of entities from `/dashboard/{keypass}`
   - Tap an item to open **Details**
3. **Details**
   - Displays all fields including `description`
   - Use the **Back** button (top left) to return

## Notes / Troubleshooting
- The API host can “cold start,” so the **first request may take ~20–40s**.  
  If you see a timeout, try again—subsequent calls are fast.
- Internet permission is already declared in `AndroidManifest.xml`.
- If build fails due to JDK, set **Gradle JDK = 17**  
  *(Settings → Build, Execution, Deployment → Gradle)*.

## Assessment Mapping
- Project completion: Login → Dashboard → Details with API 
- DI: Hilt module + `@HiltAndroidApp` 
- Testing: ViewModel tests (coroutines-test/MockK)
- README + Git history