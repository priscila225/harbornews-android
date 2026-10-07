# Harbor News Android

Local-news Android app: headline feed, article pages and a weather strip, scoped to one station.

## Architecture

Four layers per feature module, dependencies point inward:

`ui` (Activity, adapters, Compose) -> `presentation` (ViewModel, ViewState, Event) -> `domain` (use cases, repository interfaces, models) -> `data` (repository impl, remote data source, DTOs, mappers).

- Feature modules (`feature-*`) never depend on each other. Shared code lives in `common`; cross-feature UI goes through an interface in `common` (see `WeatherStripFactory`).
- `app` only aggregates Koin modules and hosts the Application class.
- Each feature owns one `di/<Feature>Module.kt`.

## Tech stack

Kotlin 2.1, Gradle Kotlin DSL with a version catalog, Koin 4, Retrofit + OkHttp + Gson, LiveData (not StateFlow) for UI state, Room, WorkManager, Compose for new UI only, Timber.
Tests: JUnit 5, MockK, Kotest assertions, kotlinx-coroutines-test, Robolectric (via the vintage engine).

## Conventions

- **Coroutines.** ViewModels extend `BaseViewModel` and launch on `this` with `launch(dispatchers.ui)`. Never use `viewModelScope`. Why: the injected `DispatcherProvider` keeps tests deterministic, and the base class cancels its `SupervisorJob` in `onCleared`.
- **Dispatchers.** Never reference `Dispatchers.*` outside `DispatcherProviderImpl`. Use `dispatchers.bg` for I/O and `dispatchers.ui` for UI work. Why: tests swap the provider.
- **Koin.** ViewModels are bound with `viewModel { }`. Repositories are `single`, use cases and mappers are `factory`. Definitions live in the owning feature's `di` package. Why: scope correctness, and `KoinModulesTest` verifies the graph.
- **Logging.** Timber only, never `android.util.Log`. Why: the release tree strips logs.
- **Gson DTOs.** Gson ignores Kotlin null-safety, so treat every DTO field as nullable at the mapper boundary and never use `!!`. Why: a missing JSON field becomes a runtime crash.
- **Results.** Repositories return `common.Result<T>`; use cases are `suspend fun execute(...)`.
- **One-shot UI events.** Use `Event<T>` over LiveData, never a bare LiveData of a command.
- Style: max line length 130, methods under 60 lines, complexity under 15 (enforced by detekt and ktlint).

## Anti-patterns

- Calling an API or repository from an Activity, Fragment or composable.
- Holding an `Activity` or `Context` in a singleton.
- Launching a coroutine in a scope that nothing cancels (`GlobalScope`, ad-hoc `CoroutineScope(...)`).
- Side effects in a composable body; use `LaunchedEffect`.
- Collecting a Flow in a lifecycle owner without `repeatOnLifecycle`.

## Commands

```bash
./gradlew test                 # unit tests
./gradlew detekt ktlintCheck   # static analysis
./gradlew ktlintFormat         # auto-fix style
./gradlew :app:assembleDebug   # build the app
```
