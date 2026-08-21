---
version: alpha
name: Athenaeum
description: Warm scholarly library-management UI built on Nuxt UI v4 and Tailwind CSS 4 — terracotta brand accent on warm stone neutrals, full light/dark support.
colors:
  primary: "#9F3C11"
  primary-hover: "#8A340D"
  primary-active: "#7A2A00"
  primary-wash: "#FCEEE8"
  primary-tint: "#F7D7C9"
  primary-deep: "#5C1F00"
  primary-wash-dark: "#3A2118"

  surface: "#FFFFFF"
  canvas: "#FBF9F8"
  surface-raised: "#EFEDEC"
  surface-emphasis: "#E5E1E0"
  border-muted: "#EAE6E4"
  border: "#E5E1E0"
  border-strong: "#D6D0CD"
  ink: "#1B1C1B"
  ink-highlighted: "#1B1C1B"
  ink-muted: "#6E6863"
  ink-faint: "#8A847E"
  on-primary: "#FFFFFF"

  dark-surface: "#292524"
  dark-canvas: "#1C1917"
  dark-surface-emphasis: "#44403C"
  dark-border-muted: "#292524"
  dark-border: "#3A3532"
  dark-border-strong: "#4A4440"
  dark-ink: "#E7E5E4"
  dark-ink-highlighted: "#FAF9F7"
  dark-ink-muted: "#A8A29E"
  dark-ink-faint: "#78716C"
  dark-on-primary: "#1C1917"

typography:
  display-lg:
    fontFamily: Geist Variable
    fontSize: 28px
    fontWeight: 600
    lineHeight: 36px
    letterSpacing: -0.02em
  heading-sm:
    fontFamily: Inter Variable
    fontSize: 14px
    fontWeight: 600
    lineHeight: 20px
  body-md:
    fontFamily: Inter Variable
    fontSize: 14px
    fontWeight: 400
    lineHeight: 20px
  label-sm:
    fontFamily: Inter Variable
    fontSize: 12px
    fontWeight: 500
    lineHeight: 16px
  table-header:
    fontFamily: Inter Variable
    fontSize: 11px
    fontWeight: 600
    letterSpacing: 0.04em
  brand-serif:
    fontFamily: Lora
    fontSize: 23px
    fontWeight: 700
    letterSpacing: -0.01em

rounded:
  sm: 6px
  md: 8px
  lg: 12px
  full: 9999px

spacing:
  base: 4px
  xs: 8px
  sm: 12px
  md: 16px
  lg: 24px
  xl: 32px
  header-height: 56px
  row-height: 40px

components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
  button-secondary:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.ink}"
    rounded: "{rounded.md}"
  input:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.ink}"
    rounded: "{rounded.md}"
    padding: 12px
  card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.lg}"
  modal:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.lg}"
---

# Athenaeum Design System

## Overview

Athenaeum is a warm, bookish design system for a library management app. It pairs a burnt-terracotta brand accent (`#9F3C11`) with warm stone neutrals, favoring hairline borders and tonal surfaces over heavy decoration. Inter carries all UI text and data, Geist handles display headings, and Lora serif is reserved for the brand wordmark. Light mode is a soft off-white canvas; dark mode is built on Tailwind's stone scale. Both are first-class themes.

## Colors

- **Primary (#9F3C11):** Burnt terracotta used for primary actions, active states, focus rings, and highlights. Full ramp from `#FCEEE8` (50) to `#330F00` (950).
- **Canvas (#FBF9F8):** Warm page background in light mode; cards sit on white `#FFFFFF` surfaces.
- **Borders (#E5E1E0 / #EAE6E4 / #D6D0CD):** Warm gray hairlines defining structure instead of shadows.
- **Ink (#1B1C1B / #6E6863 / #8A847E):** Near-black text with muted and dimmed tiers for secondary content.
- **Dark canvas (#1C1917):** Stone-based dark background; cards on `#292524`, borders `#3A3532`, text `#E7E5E4`.
- **Active nav wash (#FCEEE8 light / #3A2118 dark):** Soft brand tint marking the current sidebar item.

## Typography

- **Display:** Geist Variable, semibold 28px with tight tracking for page titles.
- **Body/UI:** Inter Variable at 14px default; 12px for small labels and metadata.
- **Table headers:** Inter 11px semibold, uppercase with +0.04em letter-spacing.
- **Brand wordmark:** Lora serif bold ~23px, used only for logos.

## Layout & Spacing

- Fixed left sidebar (240px expanded, 72px icon rail) plus a 56px header bar.
- Pages stack vertically: breadcrumb → page title → toolbar → data grid filling remaining space.
- Strict 4px spacing base; cards use 16–24px padding, form fields stack with 8px gaps.

## Elevation & Depth

Depth comes from tonal layering and hairline borders, not shadows. Controls use only subtle `shadow-sm`. Modals get one soft diffuse shadow `0 8px 32px rgba(0,0,0,0.04)` over a blurred `rgba(0,0,0,0.2)` overlay.

## Shapes

Consistent rounding ladder: 8px radius for inputs, buttons, toolbars; 12px for modals, cards, and grid wrappers; fully rounded pills for badges and avatars.

## Components

- **Buttons:** Primary filled in brand terracotta; secondary on white/card surfaces; 8px radius.
- **Inputs & selects:** Card-colored background, 1px warm border ring, focus ring in primary, muted placeholder text.
- **Modals:** Max-width ~512px, 12px radius, thin border, soft diffuse shadow, blurred overlay.
- **Data grids:** AG Grid Quartz theme matched to tokens — white header background, uppercase 11px headers, 40px rows, hover wash `#F7F5F4` (light) / `#35302C` (dark), pagination panel 48px.
- **Status badges:** Subtle tinted pills using semantic colors (success/warning/error/neutral).
- **Icons:** Lucide icon set throughout.
- **Notifications:** Compact bottom-right toasts, 18rem wide, 8px radius, theme-synced to color mode.

## Do's and Don'ts

- Do use the terracotta accent sparingly — one dominant action per view.
- Don't introduce pure black/white backgrounds; always use the warm stone tints.
- Do keep depth flat — prefer borders over shadows.
- Don't mix radii levels within one component group.
- Do support both light and dark themes with token swaps, not hard-coded colors.
