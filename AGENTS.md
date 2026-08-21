# AGENTS.md

Library Management System with two independent apps:

- `backend/` — Spring Boot 4.0.6, Java 21, Maven
- `frontend/` — Vue 3, Vite, Nuxt UI v4, Tailwind 4, pnpm

## Working Style

- Inspect only the files relevant to the task before changing anything.
- Implement directly when the request is clear — don't plan, ask clarifying questions, or research obvious choices first.
- Keep changes small and localized; reuse existing code, state, services, components, and patterns.
- Do not modify unrelated files, over-engineer, refactor unnecessarily, or introduce new architecture unless asked.
- Avoid unnecessary sub-agents and broad repository exploration when the relevant files are already known.
- Escalate to deeper investigation and planning only when the task is genuinely complex, ambiguous, or blocked.
- Prefer `grep`/`glob` to locate the relevant method, endpoint, or component before reading a full file. Avoid reading entire controllers, services, or Vue pages when only one function or section is relevant.
- Never start, stop, or restart the backend (`spring-boot:run`) or frontend (`pnpm dev`) dev server processes, for any reason — including to "pick up" code changes, to test a fix, or because a running instance looks stale. Assume both are already running and managed by the user. If a running instance appears out of date or needs restarting, tell the user and let them do it.

## Backend

Run commands from `backend/`:

```bash
.\mvnw spring-boot:run
.\mvnw test
```

- Server port: `5050`.
- Database: SQL Server on `localhost:1433`, database `library`.
- Database schema is managed with Hibernate `ddl-auto=update`; there are no migrations.
- All routes except `/api/auth/**` and `/error` require JWT authentication.
- Role-based authorization is not currently implemented.
- Controllers inject service interfaces follow the existing pattern.
- `CategoryServiceImp` is the existing category service implementation name; preserve it.
- Responses use `payload.ApiResponse`.
- Errors are handled by the existing `GlobalExceptionHandler`.
- Lombok is used throughout.
- Follow existing entity, DTO, service, repository, and controller patterns.

## Frontend

Run commands from `frontend/`.

- Use `pnpm`; never npm.
- Main commands: `pnpm dev`, `pnpm build`, `pnpm lint`, `pnpm typecheck`.
- File-based routes are generated from `src/pages/`.
- Nuxt UI components are auto-imported; do not import them manually.
- Prefer existing component usage already present in the codebase. Do not research or inspect Nuxt UI documentation unless the existing implementation cannot satisfy the requested change.
- Use 2-space indentation.
- Generated Nuxt UI/type files are created by dev/build and should not be manually edited.
- If Nuxt UI types are missing, run `pnpm dev` or `pnpm build` before `pnpm typecheck`.

## UI

- Nuxt UI is the primary UI component library. Prefer existing project components first, then Nuxt UI, then AG Grid for data-heavy tables/CRUD grids. Only create a custom component when none of those can reasonably satisfy the requirement — do not recreate functionality Nuxt UI or AG Grid already provides.
- **Pagination defaults to AG Grid:** use AG Grid's built-in pagination (`pagination: true`, `paginationPageSize`, etc.) for data grids. Do not build pagination controls from Nuxt UI components unless the design explicitly requires it.
- **Overriding Nuxt UI classes:** the `ui` prop appends to the theme's classes rather than replacing them, so any conflicting utility needs Tailwind's `!` prefix (e.g. `ui="{ base: '!px-4 !py-3' }"`). Don't reason about merge order — just add `!` to what you're overriding and move on.

## Design References

When implementing from a Stitch, Figma, screenshot, or other design reference:

- Treat the reference as the visual target; match its layout, spacing, hierarchy, typography, and visual appearance using the existing project UI system.
- Do not sacrifice the reference design just because a default component looks different.
- When a named reference is provided, use it directly — do not enumerate unrelated Stitch projects/screens, fetch large project listings, or research unrelated component APIs unless necessary. If locating a named screen requires listing projects/screens first, filter or grep the result for the given title instead of reading the full listing.
- If exact colors/spacing aren't available as tokens, use your best-guess values once rather than iterating on them by inspection.

### Google Stitch

- Project: `Workstation Library Manager`
- Project ID: `6496321036061345431`
- Use this project for all Athenaeum UI/design tasks.
- When a Stitch screen is referenced, access it directly from this project.
- Do not enumerate or search unrelated Stitch projects.
## Stitch Instructions

Get the images and code for the following Stitch project's screens:

## Project
Title: Workstation Library Manager
ID: 6496321036061345431

## Screens:
1. Books - Athenaeum Modern (Final Polish)
   ID: 6b350e2529e14c2781bc93fb4198a922
2. Dashboard - Athenaeum Modern
      ID: 96341750b8ea4155b8db5771d667b16f

Use a utility like `curl -L` to download the hosted URLs.

## Verification

After implementation:

1. Run the most relevant lint/build/typecheck command.
2. For UI changes, perform one focused browser verification when practical.
3. Verify the changed flow rather than unrelated features.
4. Fix errors caused by the change, then stop — don't repeatedly re-test working, unrelated functionality.
5. Verify via lint/build/typecheck/compile only (`mvnw compile`, `pnpm build`, `pnpm typecheck`). Do not start, stop, or restart a dev server to smoke-test an endpoint or page — see the server-process rule under Working Style.
6. If live verification against a running server is genuinely necessary, stop and ask the user to confirm the server is running and up to date, rather than starting or restarting it yourself.

## Documentation
[opencode.jsonc](../../.config/opencode/opencode.jsonc)
- Always use Context7 automatically when you need library/API documentation or when implementing, generating, configuring, or setting up code that depends on a library or API. Do not wait for me to ask.
- Trust the actual code and configuration over aspirational documentation.
- `README.md` and `backend/Tasks.md` may describe planned features that are not implemented.
- Do not assume a feature exists because it is mentioned in documentation — verify from source code and configuration.

## Agent skills

### Issue tracker

GitHub Issues (`gh` CLI). See `docs/agents/issue-tracker.md`.

### Triage labels

Default canonical labels: `needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: `CONTEXT.md` + `docs/adr/` at repo root. See `docs/agents/domain.md`.