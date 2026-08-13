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
- Controllers inject concrete service implementations; follow the existing pattern.
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
- Use 2-space indentation.
- Generated Nuxt UI/type files are created by dev/build and should not be manually edited.
- If Nuxt UI types are missing, run `pnpm dev` or `pnpm build` before `pnpm typecheck`.

## UI

- Nuxt UI is the primary UI component library. Prefer existing project components first, then Nuxt UI, then AG Grid for data-heavy tables/CRUD grids. Only create a custom component when none of those can reasonably satisfy the requirement — do not recreate functionality Nuxt UI or AG Grid already provides.
- **Overriding Nuxt UI classes:** the `ui` prop appends to the theme's classes rather than replacing them, so any conflicting utility needs Tailwind's `!` prefix (e.g. `ui="{ base: '!px-4 !py-3' }"`). Don't reason about merge order — just add `!` to what you're overriding and move on.

## Design References

When implementing from a Stitch, Figma, screenshot, or other design reference:

- Treat the reference as the visual target; match its layout, spacing, hierarchy, typography, and visual appearance using the existing project UI system.
- Do not sacrifice the reference design just because a default component looks different.
- When a named reference is provided, use it directly — do not enumerate unrelated Stitch projects/screens, fetch large project listings, or research unrelated component APIs unless necessary.
- If exact colors/spacing aren't available as tokens, use your best-guess values once rather than iterating on them by inspection.

## Verification

After implementation:

1. Run the most relevant lint/build/typecheck command.
2. For UI changes, perform one focused browser verification when practical.
3. Verify the changed flow rather than unrelated features.
4. Fix errors caused by the change, then stop — don't repeatedly re-test working, unrelated functionality.

## Documentation

- Trust the actual code and configuration over aspirational documentation.
- `README.md` and `backend/Tasks.md` may describe planned features that are not implemented.
- Do not assume a feature exists because it is mentioned in documentation — verify from source code and configuration.