# Harbor News Android

A small local-news app: a headline feed, article pages and a current-conditions weather strip for one station.

```bash
./gradlew test detekt ktlintCheck
./gradlew :app:assembleDebug
```

Modules: `app`, `common`, `testUtils`, `feature-news`, `feature-weather`. Architecture and conventions are described in [CLAUDE.md](CLAUDE.md).

All names, endpoints and content are fictional.
