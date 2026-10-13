# QMDJ theme foundation

The engine supplies a complete `QMDJThemeDefinition` through a CompositionLocal and
bridges its color, typography, and shape roles into Material 3. This follows the
[Compose custom design systems pattern](https://developer.android.com/develop/ui/compose/designsystems/custom).
The provider is independent of any palette; Serenity is merely the default selection.

## Files and contracts

| File | Responsibility |
|---|---|
| `Color.kt` | Semantic colors and explicit Material color-role mapping |
| `Typography.kt` | Semantic text styles and all Material typography slots |
| `Shape.kt` | Semantic geometry and Material shape mapping |
| `Spacing.kt` | Semantic layout dimensions and minimum target sizes |
| `Gradient.kt` | Theme-owned brushes, including optional solid treatments |
| `ThemeDefinition.kt` | Immutable bundle of the five token groups |
| `Theme.kt` | Provider, accessors, Material bridge, legacy entry point |
| `themes/SerenityTheme.kt` | All concrete Serenity values |

Use `QMDJTheme.colors`, `.typography`, `.shapes`, `.spacing`, and `.gradients` in
future screens and shared components. For example, select `colors.card`,
`typography.body`, `shapes.card`, `spacing.cardPadding`, and
`gradients.healthCardGradient` by purpose. Do not construct colors, `TextStyle`,
corner radii, dimensions, or gradients in production feature code. Do not branch on
the selected theme's name inside a component.

All text styles use `sp` and retain system font scaling. Serenity has 20sp body text,
18sp minimum text, and 56dp minimum targets. Component implementation must still
apply these target/spacing roles explicitly: Material controls do not automatically
consume QMDJ spacing, per-component shapes, or custom gradients. Layout and usability
at 200% text scale must be checked when actual components/screens are built.

## Native component integration

Future QMDJ components are semantic wrappers around Material 3, following Section 7
of `docs/UI_DESIGN_SYSTEM.md`. Prefer `Button`/`OutlinedButton`, `Card`, `TextField`,
`AlertDialog`, `NavigationBar`/`NavigationBarItem`, and native progress indicators.
Use `Scaffold` for screen structure. Use Foundation only for necessary flexibility;
custom drawing requires a documented limitation of both native layers.

Standard colors and styles inherit the Material bridge. For specialized wrapper
roles, pass `QMDJTheme` tokens through the native component's styling APIs. Never
introduce literal colors or dimensions in wrappers. Preserve native semantics,
focus, editing, selection, progress, keyboard actions, and dialog dismissal. Forward
the caller's `Modifier`; support meaningful descriptions for unlabeled functional
icons without duplicating visible labels. Validate 56dp targets and 200% text scaling.

Keep wrappers on public native APIs so future Compose/Material updates can be
handled centrally without changing screens. This strategy introduces no components
or screen changes in the theme foundation.

## Selecting a theme

```kotlin
QMDJTheme(theme = SerenityTheme) {
    Screen()
}
```

`WisdomcareTheme` delegates to this provider so existing app and preview calls
continue to compile. It also accepts a theme definition. It is deprecated for new
code. The old `darkTheme`/`dynamicColor` parameters were removed; current call sites
use neither. Dark mode and wallpaper colors no longer silently override a selection.
Only the documented light Serenity palette is implemented. Future dark palettes
must be supplied as approved definitions selected at the app root.

## Adding a future Technology theme

1. Add `themes/TechnologyTheme.kt` using the existing `QMDJThemeDefinition` contract.
2. Supply a complete, reviewed `QMDJThemeColors` and theme-owned `QMDJGradients`.
   Keep foreground/background pairs accessible. Do not partially override brand
   colors while accidentally retaining Serenity pressed/container/gradient colors.
3. Supply typography, shapes, and spacing, or deliberately reuse the accessible
   baseline when those groups are unchanged. Use immutable brush/shape values.
4. Select the definition at the application root; no engine or consumer changes
   are necessary. Verify contrast, text scaling, target sizes, and component states.

The following is an illustrative definition after `technologyColors` and
`technologyGradients` have been approved and defined in that file. It does not
introduce an unreviewed Technology palette into the app:

```kotlin
val TechnologyTheme = QMDJThemeDefinition(
    colors = technologyColors,
    typography = SerenityTheme.typography,
    shapes = SerenityTheme.shapes,
    spacing = SerenityTheme.spacing,
    gradients = technologyGradients,
)

// Only the root selection changes; Screen continues to read semantic tokens.
QMDJTheme(theme = TechnologyTheme) {
    Screen()
}
```

A Minimal Healthcare theme can supply `SolidColor` brushes instead of gradients
without changing gradient consumers. More distinctive typography or geometry can
also be supplied through the existing contracts.

## Compatibility and limits

Replacing the root definition updates semantic consumers and the Material bridge
on recomposition. Nested providers override only their subtree, then the enclosing
definition resumes. Access without a provider fails clearly instead of silently
choosing a style.

Existing Material-based screens receive the mapped palette, typography, and shapes
without editing their code. Existing hardcoded layout dimensions remain fixed until
a separately authorized screen migration; this foundation does not rewrite screens,
business logic, or implement reusable QMDJ components. Theme selection persistence,
settings UI, dynamic palettes, and new product styles are outside this task.
