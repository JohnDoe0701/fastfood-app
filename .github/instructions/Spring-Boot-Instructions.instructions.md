# GitHub Copilot Instructions for Spring Boot Projects

These instructions are for GitHub Copilot to **enforce industry standards and clean code** when generating Spring Boot code.

---

## General Principles

- Always write **clean, readable, and maintainable code**.
- Follow **Java naming conventions**: `PascalCase` for classes, `camelCase` for methods and variables.
- Use **consistent indentation and spacing**.
- Keep lines **≤ 120 characters**.
- Write **self-documenting code**; avoid unnecessary comments.

---

## Architecture & Layering

- **Controllers**: Handle HTTP requests and responses only. Call services for business logic.
- **Services**: Contain business logic. Avoid database or HTTP code in services.
- **Repositories**: Handle all database access. Use JPA or Spring Data repositories.
- **Entities**: Represent database tables. Use proper annotations and validation.
- **DTOs**: Use for API input/output. Do **not expose entities directly**.

---

## Code Practices

- Prefer **constructor injection** (`@RequiredArgsConstructor`) over field injection.
- Use **Builder pattern** for creating objects with multiple fields.
- Validate input with **Spring Validation annotations**.
- Return **ResponseEntity** in controllers to control HTTP status.
- Use **Optional** for potentially null returns.
- Avoid hardcoding configuration values; use **application.yml** or environment variables.

---

## Testing

- Write **unit tests** for services and repositories.
- Write **integration tests** for controllers.
- Use **JUnit 5** and **Mockito** for mocks.
- Keep test data minimal and realistic.

---

## Security & Production Readiness

- Do **not expose generated passwords or secrets**.
- Always validate user input to prevent SQL injection.
- Use **Spring Security** for authentication and authorization.
- Disable development-only endpoints (Swagger/OpenAPI) in production.

---

## Copilot Usage

- Only generate code that follows these rules.
- Suggest **DTOs, services, repositories, and controllers** only with proper layering.
- Avoid generating unsafe, hardcoded, or tightly coupled code.
- Keep prompts focused on **clean code, readability, and maintainability**.

---

## Summary

When generating Spring Boot code:

1. Follow **layered architecture** (Controller → Service → Repository → Entity/DTO).
2. Enforce **Java conventions** and readability.
3. Keep **business logic out of controllers**.
4. Always **validate inputs** and use safe defaults.
5. Ensure all generated code is **testable and maintainable**.