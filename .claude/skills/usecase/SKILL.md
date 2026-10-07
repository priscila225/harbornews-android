---
name: usecase
description: Pattern for adding a domain use case in this project
---

A use case is a small class with constructor-injected repository interfaces and one `suspend fun execute(...)` that returns `common.Result<T>`.
Bind it with `factory { }` in the feature's Koin module and add a test that mocks the repository with MockK.
It never touches Android classes or dispatchers; the repository implementation owns `withContext(dispatchers.bg)`.
