# Vue 3 / Nuxt UI Extraction Patterns

This project is a **Vue 3 application**, not a Nuxt application.

The expected frontend stack is:

* Vue 3
* TypeScript
* Composition API
* Vite
* Tailwind CSS
* Nuxt UI

**Nuxt UI is the component library. Nuxt itself is not being used.**

When analyzing or implementing UI, inspect the actual project structure and existing implementation before making assumptions.

The existing application architecture and design system are the source of truth.

---

# Core Principles

## Vue 3

Use Vue 3 Composition API.

Prefer:

```vue
<script setup lang="ts">
```

over the Options API unless the existing project explicitly uses another pattern.

Use:

* typed props
* typed emits
* composables
* computed values
* reactive state
* reusable components

Avoid putting large amounts of business logic directly inside templates.

---

# Nuxt UI

Nuxt UI is the project's primary UI component library.

Use Nuxt UI components whenever they provide the required functionality.

Examples include:

```vue
<UButton />
<UInput />
<UTextarea />
<USelect />
<USelectMenu />
<UCheckbox />
<URadioGroup />
<USwitch />
<UCard />
<UBadge />
<UAvatar />
<UModal />
<USlideover />
<UDropdownMenu />
<UTooltip />
<UTabs />
<UTable />
<UPagination />
<UNavigationMenu />
<UBreadcrumb />
<USkeleton />
<UAlert />
```

The exact available components depend on the installed Nuxt UI version.

**Always inspect the installed version and existing project usage before assuming a component or API exists.**

---

# Important: Vue, Not Nuxt

This application is a Vue/Vite application.

Do NOT introduce Nuxt-specific architecture.

Do not assume the existence of:

```text
nuxt.config.ts
app.config.ts
pages/
layouts/
server/
middleware/
Nuxt modules
Nuxt routing
Nuxt plugins
```

unless those files or concepts actually exist in the project.

Nuxt UI does not mean the project is a Nuxt application.

---

# File Discovery Order

Before extracting design patterns or implementing UI, inspect the project in this order.

## 1. `package.json`

Determine the actual technology stack.

Look for:

```text
vue
vite
typescript
tailwindcss
@nuxt/ui
vue-router
pinia
vueuse
```

Also inspect the versions.

Do not assume a dependency is available merely because it is commonly used with Vue.

---

## 2. `vite.config.ts`

Inspect:

* Vite plugins
* Vue plugin configuration
* path aliases
* CSS configuration
* environment-specific configuration

Determine how the application is actually built.

---

## 3. `src/main.ts`

Inspect:

* Vue application initialization
* router registration
* Pinia
* Nuxt UI installation
* global CSS imports
* plugins

This file reveals how the application is bootstrapped.

---

## 4. `src/App.vue`

Inspect the root application structure.

Determine:

* global layout
* application shell
* navigation
* global containers
* page rendering
* global UI providers

Do not assume that `App.vue` has a particular structure.

Follow the existing implementation.

---

## 5. `src/assets/`

Inspect global styles and assets.

Common files include:

```text
src/assets/css/main.css
src/assets/css/tailwind.css
src/assets/css/global.css
```

Look for:

* Tailwind configuration
* CSS variables
* fonts
* global typography
* body styles
* background colors
* custom utilities
* component overrides
* design tokens

---

## 6. `src/components/`

Inspect existing components before creating new ones.

Look for reusable:

* buttons
* cards
* tables
* forms
* filters
* headers
* navigation
* modals
* dialogs
* badges
* empty states
* loading states
* error states

Reuse existing components whenever possible.

---

## 7. `src/views/`

If Vue Router is used, inspect the existing views.

Determine how screens are currently structured.

Do not create a new routing architecture.

Follow the existing project's conventions.

---

## 8. `src/router/`

If present, inspect:

* routes
* layouts
* route metadata
* navigation structure
* authentication guards

Do not assume file-based routing.

This is a Vue/Vite application unless the actual project proves otherwise.

---

# Vue Single-File Components

Vue components should generally follow this structure:

```vue
<script setup lang="ts">
interface Props {
  title: string
}

defineProps<Props>()
</script>

<template>
  <section>
    <h2>
      {{ title }}
    </h2>
  </section>
</template>
```

Use `<script setup lang="ts">` as the preferred style.

Keep components focused.

Avoid giant components containing unrelated:

* UI
* state
* business logic
* API requests
* formatting
* validation

Extract reusable logic into composables when appropriate.

---

# Styling Strategy

## Tailwind CSS First

Tailwind CSS is the primary styling mechanism.

Prefer:

```vue
<div class="flex items-center gap-4 rounded-xl border p-4">
```

over creating custom CSS for simple styling.

Use Tailwind for:

* layout
* flexbox
* grid
* spacing
* sizing
* typography
* colors
* borders
* radius
* shadows
* responsive behavior
* hover states
* focus states
* transitions

---

# Avoid Unnecessary CSS

Do not create:

```vue
<style scoped>
.card {
  ...
}
</style>
```

when the styling can reasonably be expressed using Tailwind.

Use `<style scoped>` only when it is genuinely useful, such as:

* complex animations
* third-party library overrides
* CSS that is difficult or inappropriate to express with utilities
* pseudo-elements requiring substantial custom CSS
* existing project conventions

Do not introduce a second styling architecture.

---

# Nuxt UI First

When implementing a UI element, follow this order:

```text
Existing project component
        ↓
Nuxt UI component
        ↓
Compose Nuxt UI components
        ↓
Custom Vue component
        ↓
Custom CSS
```

For example, prefer:

```vue
<UButton>
  Add Book
</UButton>
```

over:

```vue
<button class="...">
  Add Book
</button>
```

when the Nuxt UI component can satisfy the requirement.

---

# Do Not Recreate Nuxt UI

Before creating a custom implementation, determine whether Nuxt UI already provides the required component.

Prefer configuring or composing Nuxt UI through:

* props
* variants
* sizes
* slots
* classes
* component configuration
* theme configuration supported by the installed version

Do not duplicate a Nuxt UI component just to avoid learning its API.

However, if the Stitch design genuinely requires behavior or structure that Nuxt UI cannot provide, create a clean custom Vue component.

---

# Existing Component Discovery

Before creating a component, search the project.

For example, before creating:

```text
BookCard.vue
```

search for existing:

```text
BookCard
Card
EntityCard
ListItem
Book
```

If an existing component already solves the problem, reuse or extend it.

Do not create duplicate visual patterns.

---

# Design System Extraction

Identify repeated design values across the application.

## Colors

Look for:

```text
primary
secondary
background
surface
foreground
muted
border
success
warning
error
info
```

These may exist as:

* Tailwind theme values
* CSS variables
* Nuxt UI theme configuration
* component classes
* shared constants

Trace values back to their actual source.

Do not invent replacement colors.

---

# Typography

Identify:

* font family
* heading sizes
* body sizes
* font weights
* line heights
* muted text
* labels
* captions

Look for repeated patterns across components.

For example:

```text
page title
section title
card title
body
secondary text
caption
```

Treat repeated typography patterns as part of the design system.

---

# Spacing

Identify repeated spacing patterns.

For example:

```text
gap-2
gap-3
gap-4
gap-6
gap-8
```

Do not introduce arbitrary spacing values when the existing project has an established spacing system.

Maintain consistency across screens.

---

# Border Radius

Identify the project's established radius patterns.

For example:

```text
rounded-md
rounded-lg
rounded-xl
```

Do not randomly mix different radius values.

If Stitch specifies a particular radius, reproduce it using the closest existing project token or Nuxt UI configuration.

Only introduce a new value when necessary for visual fidelity.

---

# Shadows and Elevation

Identify existing elevation patterns.

Do not add shadows automatically to every card.

Use shadows only where:

* the existing design system uses them
* Stitch specifies them
* elevation communicates hierarchy

---

# Repeated Patterns = Design System

If the same pattern appears across multiple screens, treat it as a design-system convention.

Examples:

* same button height
* same input height
* same card radius
* same page padding
* same heading hierarchy
* same table header
* same badge treatment
* same empty state
* same spacing
* same icon sizing

Reuse these patterns instead of creating new visual variations.

---

# Google Stitch Integration

When a screen already exists in Google Stitch, **Stitch is the visual source of truth**.

Do not redesign the screen.

Before implementing:

1. Inspect the actual Stitch screen.
2. Inspect related Stitch screens.
3. Inspect the Stitch design system if available.
4. Inspect the existing Vue application.
5. Identify reusable project components.
6. Identify appropriate Nuxt UI components.
7. Determine how the Stitch design maps to the existing architecture.

The goal is:

```text
Google Stitch
     ↓
Visual specification
     ↓
Vue architecture
     ↓
Nuxt UI components
     ↓
Tailwind styling
```

Do not blindly copy generated HTML from Stitch.

Translate the design into clean, maintainable Vue code.

---

# Stitch Visual Fidelity

When implementing an existing Stitch design, preserve:

* layout
* content hierarchy
* spacing
* typography
* colors
* borders
* radius
* shadows
* component dimensions
* icon sizes
* icon placement
* alignment
* visual density
* responsive behavior

Do not:

* redesign the layout
* replace the color palette
* change typography arbitrarily
* add unnecessary gradients
* add unnecessary animations
* introduce glassmorphism
* turn the design into a generic dashboard
* "modernize" the design
* simplify important visual details

**Implementation freedom means choosing the best Vue/Nuxt UI architecture — not changing the design.**

---

# Component Mapping From Stitch

When translating Stitch into Vue/Nuxt UI, map visual concepts to appropriate components.

Examples:

```text
Stitch Button
    ↓
<UButton>

Stitch Input
    ↓
<UInput>

Stitch Select
    ↓
<USelect> / <USelectMenu>

Stitch Card
    ↓
<UCard>

Stitch Badge
    ↓
<UBadge>

Stitch Modal
    ↓
<UModal>

Stitch Dropdown
    ↓
<UDropdownMenu>

Stitch Tooltip
    ↓
<UTooltip>

Stitch Tabs
    ↓
<UTabs>

Stitch Table
    ↓
<UTable>

Stitch Alert
    ↓
<UAlert>

Stitch Loading State
    ↓
<USkeleton>
```

The exact mapping depends on the installed Nuxt UI version.

Inspect the project's existing usage before selecting a component.

---

# Responsive Design

Do not treat responsive design as simply shrinking desktop content.

Determine how the design behaves at:

* mobile
* tablet
* desktop
* large desktop

Use Tailwind responsive utilities.

Example:

```vue
<div class="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
```

Consider:

* navigation collapse
* sidebar behavior
* table overflow
* card stacking
* button wrapping
* typography scaling
* content padding
* modal behavior
* slideover behavior

If Stitch provides mobile designs, follow them directly.

If only a desktop design exists, preserve the design language while creating a sensible responsive adaptation.

Do not invent a completely different mobile UI.

---

# UI States

Important UI should account for:

```text
loading
success
empty
error
disabled
hover
focus
active
```

Use Nuxt UI where appropriate:

```vue
<USkeleton />
<UAlert />
<UBadge />
```

Do not leave loading or empty states as an afterthought.

---

# Page Structure

Follow the existing Vue project's architecture.

A typical structure might look like:

```text
src/
├── components/
│   ├── books/
│   │   ├── BooksHeader.vue
│   │   ├── BooksFilters.vue
│   │   ├── BooksTable.vue
│   │   ├── BookRow.vue
│   │   └── BookEmptyState.vue
│   │
│   └── shared/
│
├── views/
│   └── BooksView.vue
│
├── router/
│
├── composables/
│
├── stores/
│
├── assets/
│
├── App.vue
└── main.ts
```

This is only an example.

**Always inspect the actual repository and follow its existing structure.**

Do not impose this structure if the project uses something different.

---

# Clean Component Architecture

Prefer components that have one clear responsibility.

For example:

```text
BooksView
    ↓
BooksHeader
BooksFilters
BooksTable
    ↓
BookRow
BookStatusBadge
BookActions
```

Do not create extremely large components containing the entire screen unless the project architecture intentionally follows that pattern.

At the same time, avoid creating tiny components that have no meaningful reuse or responsibility.

Optimize for clarity and maintainability.

---

# Visual Verification

A UI implementation is NOT complete simply because:

* TypeScript compiles
* the application runs
* the route works
* the API works
* the data loads
* the components render

After implementing a Stitch screen:

1. Run the application.
2. Open the implemented screen.
3. Inspect the rendered result.
4. Compare it with the Stitch design.
5. Identify visual discrepancies.
6. Fix them.
7. Repeat the comparison.

Pay particular attention to:

```text
Layout
Spacing
Typography
Colors
Sizing
Alignment
Component proportions
Icon sizing
Borders
Radius
Shadows
Visual hierarchy
Responsive behavior
```

The implementation should be as visually faithful to Stitch as possible while remaining clean and idiomatic Vue code.

---

# Final Implementation Rules

Before considering a UI task complete, verify:

* [ ] Stitch design was inspected.
* [ ] Existing project components were inspected.
* [ ] Nuxt UI components were used where appropriate.
* [ ] Tailwind CSS is used for normal styling.
* [ ] No unnecessary custom CSS was introduced.
* [ ] No duplicate components were created.
* [ ] Existing design tokens were reused.
* [ ] Typography matches the design.
* [ ] Colors match the design.
* [ ] Spacing matches the design.
* [ ] Component dimensions match the design.
* [ ] Responsive behavior was considered.
* [ ] Loading state was considered.
* [ ] Empty state was considered.
* [ ] Error state was considered.
* [ ] Interactive states were considered.
* [ ] The final implementation was visually compared against Stitch.

---

# Forbidden Technology Assumptions

Do not introduce or assume:

* Nuxt framework
* Vuetify
* Quasar
* PrimeVue
* Element Plus
* Bootstrap
* another component library

unless the existing project explicitly uses them.

Nuxt UI is the approved component library.

Vue 3 + Vite is the application framework/runtime architecture.

---

# Final Principle

The goal is:

**Exact Stitch visual fidelity + clean Vue 3 architecture + Nuxt UI components + Tailwind CSS + maintainable code.**

When there is a conflict:

1. Preserve the Stitch visual design.
2. Follow the existing project's architecture.
3. Prefer existing components.
4. Prefer Nuxt UI.
5. Use Tailwind for styling.
6. Create custom Vue components only when necessary.

Never change the visual design merely because another implementation is easier.
