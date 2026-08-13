# AGENTS.md

Library Management System. Two fully independent apps at the repo root; there is no root build tool:

- `backend/` — Spring Boot 4.0.6 / Java 21 / Maven, package `org.library`
- `frontend/` — Vue 3 + Vite + Nuxt UI v4 + Tailwind 4, managed by pnpm

## Documentation caveat

`README.md` and `backend/Tasks.md` are an aspirational learning plan, not current reality. They mention MySQL/PostgreSQL, role-based auth, a borrow system, notifications, recycle bin, and audit log — most of that is **not implemented**. Trust the code and `application.properties`, not these docs.

## Backend (`backend/`)

Run from `backend/` using the wrapper: `.\mvnw spring-boot:run`, `.\mvnw test` (or `./mvnw` on non-Windows).

- Server runs on **port 5050** (`server.port=5050`), not 8080.
- Database is **SQL Server** (`jdbc:sqlserver://localhost:1433;databaseName=library`), user `test` / `123456` — committed in `application.properties` as dev-only creds, along with the JWT secret. Do not treat them as real secrets. `spring.jpa.hibernate.ddl-auto=update` auto-generates the schema from entities; there are no migrations.
- Spring Boot 4 uses modular starters: the web starter is `spring-boot-starter-webmvc` (not `spring-boot-starter-web`, which the README/Tasks.md reference).
- All routes except `/api/auth/**` and `/error` require a JWT bearer token (stateless). **Role-based authorization is not implemented** (no `@PreAuthorize`/`@EnableMethodSecurity`).
- Controllers inject the concrete impl classes directly, not the service interfaces (e.g. `BookController` takes `BookServiceImpl`). Follow that pattern. Mind the typo: the category impl class is `CategoryServiceImp`.
- All responses are wrapped in `payload.ApiResponse` via `ApiResponse.okResponse(...)`; errors return `payload.ApiError` from `GlobalExceptionHandler` (`@RestControllerAdvice`, handles `ResourceNotFoundException` + validation only; no generic 500 handler).
- Lombok is used throughout (`@RequiredArgsConstructor` constructor injection, `@Getter`/`@Setter`/`@Builder` entities). Lombok annotation-processor paths are preconfigured in the Maven compiler plugin.
- The only test is one `@SpringBootTest` context-loads test; `mvn test` boots the full context and needs SQL Server reachable. No MockMvc/unit tests exist yet.

## Frontend (`frontend/`)

Use **pnpm only** (pnpm@11 via `packageManager`, `pnpm-lock.yaml`); never npm. Run from `frontend/`.

- Scripts: `pnpm dev`, `pnpm build`, `pnpm preview`, `pnpm lint` (eslint `src`), `pnpm typecheck` (`vue-tsc -p ./tsconfig.app.json`). CI runs install → lint → build → typecheck.
- File-based routing: routes are auto-generated from `src/pages/` by the `vue-router/vite` plugin (`routes` imported from `vue-router/auto-routes`); the typed route map is generated to `src/route-map.d.ts`.
- `auto-imports.d.ts`, `components.d.ts`, and `node_modules/.nuxt-ui/ui/*` (the `#build/ui/*` tsconfig alias) are **generated at dev/build time and gitignored**. Run `pnpm dev` or `pnpm build` once before `pnpm typecheck`, or Nuxt UI component types will be missing.
- Nuxt UI components (`UButton`, `UApp`, `UColorModeButton`, …) are auto-imported and globally registered — do not add imports for them.
- Style: 2-space indent (`.editorconfig`); ESLint flat config with `vue/multi-word-component-names` disabled.

## Frontend UI / Design System

**Nuxt UI is the primary UI component library for this project.**

Before designing or implementing UI, **ALWAYS check the installed `nuxt-ui`
skill and Nuxt UI documentation first** before creating a custom UI solution.

When component/API details are needed, consult:

https://ui.nuxt.com/llms.txt

Nuxt UI documentation:
https://ui.nuxt.com/

### Nuxt UI documentation workflow

When deciding how to implement a UI pattern:

1. Check the installed `nuxt-ui` skill and its references.
2. Check Nuxt UI's documentation / `llms.txt` for the available component.
3. Prefer an existing Nuxt UI component over a custom implementation.
4. Use the component's documented props, slots, variants, and customization
   options to match the required design.
5. Only create a custom component when Nuxt UI does not provide a suitable
   solution.

Do not load or use `llms-full.txt` by default. Use it only when the normal
Nuxt UI documentation is insufficient and the task requires deeper
implementation details.

### Component-first rule

If Nuxt UI provides an existing component that can satisfy the requirement, **use that component instead of implementing an equivalent component yourself.**

This applies especially to:

* `Sidebar`
* Navigation
* Buttons
* Inputs
* Selects
* Dropdowns
* Modals
* Dialogs
* Tabs
* Cards
* Alerts
* Toasts
* Tooltips
* Breadcrumbs
* Command palettes
* Menus
* Pagination
* Forms
* Overlays

For example:

**If Nuxt UI has a Sidebar component, use the Nuxt UI Sidebar. Do NOT build a custom sidebar using `<aside>`, `<div>`, Tailwind classes, and custom state management.**

### Before creating a custom component

Before implementing a UI component from scratch:

1. Check whether Nuxt UI provides an equivalent component.
2. Check the Nuxt UI documentation for its variants, props, slots, and customization options.
3. Use and customize the Nuxt UI component if it is suitable.
4. Only create a custom component when Nuxt UI does not provide a suitable solution.

### Do not reinvent Nuxt UI

Do NOT:

* Recreate a Nuxt UI component manually.
* Build a custom version of an existing Nuxt UI component.
* Use plain HTML + Tailwind when a suitable Nuxt UI component exists.
* Ignore an existing Nuxt UI component because a custom implementation appears easier.
* Create custom navigation/sidebar/modal/form components when Nuxt UI already provides them.

Customization is encouraged. Replacement is not.

Use Nuxt UI's documented props, slots, variants, and Tailwind customization to make the component visually match the required design.

### Design implementation priority

When implementing a UI design, follow this priority:

1. **Existing project components** — reuse them when appropriate.
2. **Nuxt UI components** — preferred for general UI components.
3. **AG Grid** — preferred for data grids and data-heavy tables.
4. **Custom Vue components** — only when neither of the above is suitable.
5. **Raw HTML/Tailwind implementation** — only when no suitable component exists.

**For data-heavy tables, AG Grid takes priority over Nuxt UI or custom table implementations.**

### Stitch / Reference Designs

When implementing a screen based on a design reference, screenshot, or Google Stitch design:

* Treat the reference as the **visual target**.
* Treat Nuxt UI as the **general UI component system used to implement that target**.
* Treat AG Grid as the **data-grid component system** when the reference contains a data-heavy table.
* Do not manually recreate components that already exist in Nuxt UI.
* Use the closest Nuxt UI component and customize it until it matches the reference.
* Preserve the reference's layout, spacing, hierarchy, visual appearance, and behavior.

  If the reference contains a data-heavy table:
> Use AG Grid as the foundation and customize it to match the reference.

For example, if the reference contains a sidebar:

> Do not draw a custom sidebar.

Instead:

> Identify the appropriate Nuxt UI Sidebar component, use it as the foundation, and customize it to match the reference.

### Important distinction

The goal is **not**:

> "Create a custom design and use Nuxt UI components where convenient."

The goal is:

> **"Build the design using Nuxt UI components wherever Nuxt UI provides a suitable component."**

Nuxt UI should be the default implementation choice, not an optional styling library.

### When a custom component is justified

A custom implementation is acceptable when:

* Nuxt UI has no equivalent component.
* The required behavior is fundamentally different from the available Nuxt UI component.
* The existing Nuxt UI component cannot reasonably be customized to satisfy the requirement.

When choosing a custom implementation despite an existing Nuxt UI component, briefly explain why the Nuxt UI component was not suitable.

## Data Grids / Tables

**AG Grid is the primary data-grid and table solution for this project.**

For data-heavy tables and CRUD/data-management screens, **ALWAYS use AG Grid
as the table implementation**. Do not build these tables manually with
HTML `<table>`, Nuxt UI Table, or custom Vue/Tailwind implementations.

Before implementing a data grid:

1. Check the installed `ag-grid` skill and its references.
2. Check AG Grid documentation when API or feature details are needed.
3. Use AG Grid as the foundation of the table.
4. Configure and customize AG Grid to match the required design.
5. Only use another table implementation when AG Grid is genuinely unsuitable.

### AG Grid should be used for

- Books
- Authors
- Categories
- Borrowings
- Users
- Search results
- CRUD screens
- Administrative data
- Sortable/filterable datasets
- Pagination
- Row selection
- Column resizing/reordering
- Inline editing
- Large or potentially growing datasets
- Tables with advanced interactions

### Do not reinvent data-grid functionality

Do NOT:

- Create a custom `<table>` for a data-heavy screen.
- Use Nuxt UI Table when the screen is an application/data-management table.
- Implement sorting manually when AG Grid provides it.
- Implement filtering manually when AG Grid provides it.
- Implement pagination manually when AG Grid provides it.
- Implement row selection manually when AG Grid provides it.
- Implement column resizing/reordering manually when AG Grid provides it.

Use AG Grid's built-in capabilities instead.

### Nuxt UI + AG Grid

Nuxt UI remains the primary UI component library for the rest of the
application.

Use:

- **Nuxt UI** → layout, sidebar, navigation, buttons, inputs, forms,
  dropdowns, dialogs, modals, filters, toolbars, alerts, etc.
- **AG Grid** → data grids and data-heavy tables.

AG Grid should visually integrate with the Nuxt UI design system.

Customize AG Grid's theme, spacing, typography, borders, headers, rows,
cells, and surrounding layout to match the application's design.

### Design references / Stitch

When a Stitch, screenshot, Figma, or other design reference contains a
data table:

- Treat the reference as the visual target.
- Use AG Grid as the table implementation.
- Customize AG Grid to match the reference.
- Do NOT recreate the table manually just to reproduce its appearance.

The goal is:

> **Match the design using AG Grid, not recreate the data grid from scratch.**

