# Course1 — Android Application Development Project (NIT3213)

An Android app with **Login → Dashboard → Details** integrating the **vu-nit3213-api**.

## Tech Stack
- Kotlin, AndroidX (Activity, RecyclerView, ViewBinding)
- **Retrofit + OkHttp + Gson** (API)
- **Hilt** for dependency injection
- **MVVM** (Repository + ViewModels + Activities)
- Unit tests: **JUnit4**, **MockK**, **kotlinx-coroutines-test**

## Screenshots
| Login | Dashboard | Details |
|------|-----------|---------|
| ![Login](docs/login.png) | ![Dashboard](docs/dashboard.png) | ![Details](docs/details.png) |

> Add your images to `docs/` and adjust the paths if needed.

## API
Base: `https://nit3213api.onrender.com/`

### Auth (POST `/{campus}/auth`)
- `campus` ∈ `br` | `sydney` | `footscray`
- Body:
```json
{ "username": "YourFirstName", "password": "YourStudentID" }
