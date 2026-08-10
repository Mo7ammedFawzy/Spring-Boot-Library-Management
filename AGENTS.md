# AGENTS.md

Library Management System. Two fully independent apps at the repo root; there is no root build tool:

- `backend/` — Spring Boot 4.0.6 / Java 21 / Maven, package `org.library`
- `frontend/` — Vue 3 + Vite + Nuxt UI v4 + Tailwind 4, managed by pnpm

The frontend is a stock Nuxt UI template and is **not wired to the backend** (no API calls, no proxy). Don't assume they talk to each other.

## Documentation caveat

`README.md` and `backend/Tasks.md` are an aspirational learning plan, not current reality. They mention MySQL/PostgreSQL, role-based auth, a borrow system, notifications, recycle bin, and audit log — most of that is **not implemented**. Trust the code and `application.properties`, not these docs.

## Backend (`backend/`)

Run from `backend/` using the wrapper: `.\mvnw spring-boot:run`, `.\mvnw test` (or `./mvnw` on non-Windows).

- Server runs on **port 5050** (`server.port=5050`), not 8080.
- Database is **SQL Server** (`jdbc:sqlserver://localhost:1433;databaseName=library`), user `test` / `123456` — committed in `application.properties` as dev-only creds, along with the JWT secret. `spring.jpa.hibernate.ddl-auto=update`, so the schema is auto-generated from entities; there are no migrations.
- All routes except `/api/auth/**` and `/error` require a JWT bearer token (stateless). **Role-based authorization is not implemented** (no `@PreAuthorize`/`@EnableMethodSecurity`).
- Controllers inject the concrete impl classes directly, not the service interfaces (e.g. `BookController` takes `BookServiceImpl`). Follow that pattern. Mind the typo: the category impl class is `CategoryServiceImp`.
- All responses are wrapped in `payload.ApiResponse` via `ApiResponse.okResponse(...)`; errors return `payload.ApiError` from `GlobalExceptionHandler` (handles `ResourceNotFoundException` + validation only; no generic 500 handler).
- Lombok is used throughout (`@RequiredArgsConstructor` constructor injection, `@Getter`/`@Setter`/`@Builder` entities). Maven compiler annotation-processing paths for Lombok are already configured.
- The only test is one `@SpringBootTest` context-loads test; `mvn test` boots the full context and needs SQL Server reachable. No MockMvc/unit tests exist yet.

## Frontend (`frontend/`)

Use **pnpm only** (pnpm@11 via `packageManager`, `pnpm-lock.yaml`); never npm. Run from `frontend/`.

- Scripts: `pnpm dev`, `pnpm build`, `pnpm preview`, `pnpm lint` (eslint `src`), `pnpm typecheck` (`vue-tsc -p ./tsconfig.app.json`). CI runs install → lint → build → typecheck.
- File-based routing: routes are auto-generated from `src/pages/` by the `vue-router/vite` plugin (`routes` imported from `vue-router/auto-routes`); the typed route map is generated to `src/route-map.d.ts`.
- `auto-imports.d.ts`, `components.d.ts`, and `node_modules/.nuxt-ui/ui/*` (the `#build/ui/*` tsconfig alias) are **generated at dev/build time and gitignored**. Run `pnpm dev` or `pnpm build` once before `pnpm typecheck`, or Nuxt UI component types will be missing.
- Nuxt UI components (`UButton`, `UApp`, `UColorModeButton`, …) are auto-imported and globally registered — do not add imports for them.
- Style: 2-space indent (`.editorconfig`); ESLint flat config with `vue/multi-word-component-names` disabled.
