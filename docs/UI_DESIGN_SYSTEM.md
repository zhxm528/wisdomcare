# QMDJ UI Design System

**Theme:** Modern Chinese Serenity (现代中式静雅)  
**Version:** 1.1  
**Date:** 2026-10-13  
**Scope:** All future QMDJ Android screens, shared components, and interaction states.

This document is the project-level visual and interaction standard. Modern Chinese Serenity is the current theme definition; future product themes use the same semantic token contracts. The repository uses the `com.wisdomcare.app` package and a Compose theme foundation; retain that package structure when implementing QMDJ. Component names below are proposed contracts, not claims that these components already exist.

“Must” indicates a requirement. “Should” indicates the default, with any justified exception recorded during design review. Accessibility and safety take precedence over decoration.

## 1. Design Philosophy

### Product principles

- **Readable before decorative:** A user should identify the main information and next action without interpreting an illustration, gesture, or color code.
- **Simple and predictable:** Give each screen one clear purpose. Keep navigation, labels, and action placement consistent.
- **Calm with clear urgency:** Ordinary use should feel reassuring. A critical state must remain unmistakable without flashing, alarming decoration, or exaggerated claims.
- **Trust through clarity:** Show data sources, measurement times, save results, and missing information. Never imply that an absent measurement is a normal result.
- **Dignity and independence:** Help older adults complete tasks themselves. Avoid childish language, patronizing imagery, or treating all seniors as having the same abilities.
- **Safe recovery:** Make cancellation, correction, and return paths visible. Preserve input after recoverable errors.

### Emotional and cultural direction

Modern Chinese Serenity should feel calm, warm, elegant, natural, trustworthy, and premium but approachable. Bamboo green suggests growth; cloud blue creates spaciousness; warm paper surfaces soften the screen. Landscape painting (山水画), ink wash atmosphere (水墨意境), rice paper (宣纸), and morning mist inspire restraint, whitespace, and subtle layering.

These references belong in atmosphere and composition, not ornamental interface controls. Keep familiar Android navigation, clear sans-serif text, and recognizable actions. Avoid hospital dashboards, antique decoration, dense enterprise layouts, and highly colorful consumer-app styling.

### Elderly users and accessibility

Design for Chinese users aged 60+, including people aged 80+ with reduced vision, dexterity, hearing, or working memory. Do not infer ability from age alone. Provide large text and targets by default, respect system text settings, and allow users to proceed at their own pace.

QMDJ requires at least **4.5:1 contrast for all informative text**, including large numbers, and **3:1 for essential control boundaries, icons, focus indicators, and chart marks** against adjacent backgrounds. These are QMDJ acceptance thresholds; decorative elements are exempt. Color must always be paired with text, shape, or an icon. Support TalkBack, logical focus order, switch/keyboard access, and feedback that does not depend on sound alone.

## 2. Color System

Use semantic tokens rather than raw HEX values in screen code. All values below are opaque sRGB colors. This palette defines the light theme; do not automatically invert it for dark mode.

### Brand, surfaces, and text

| Token | Chinese name | English name | HEX | Usage |
|---|---|---|---|---|
| `primary` | 竹青 | Bamboo Green | `#2F6654` | Main action, selected navigation text/icon, positive emphasis |
| `primaryPressed` | 深竹青 | Deep Bamboo | `#24503F` | Pressed primary action |
| `primaryContainer` | 竹雾 | Bamboo Mist | `#E2EDE5` | Selected backgrounds and gentle wellness panels |
| `secondary` | 云蓝 | Cloud Blue | `#365F7A` | Informational links, secondary emphasis |
| `secondaryPressed` | 深云蓝 | Deep Cloud Blue | `#294A60` | Pressed filled secondary action |
| `secondaryContainer` | 天青雾 | Sky Mist | `#E3EDF2` | Information panels and selection support |
| `background` | 宣纸白 | Rice Paper | `#F6F3EB` | Screen background; warm, not pure white |
| `surface` | 玉纸白 | Jade Paper | `#FCFAF4` | Cards, dialogs, inputs, navigation surfaces |
| `surfaceVariant` | 暖石 | Warm Stone | `#EAE6DC` | Quiet grouped areas and disabled backgrounds |
| `textPrimary` | 松墨 | Pine Ink | `#24352F` | Titles, body text, values; replaces pure black |
| `textSecondary` | 灰竹 | Bamboo Gray | `#526159` | Supporting text, units, timestamps, placeholders |
| `onFilled` | 玉纸白 | Jade Paper | `#FCFAF4` | Text/icons on dark brand and semantic fills |
| `outline` | 石青灰 | Stone Gray | `#78867D` | Input boundaries, outlined controls, essential separators |
| `divider` | 纸纹灰 | Paper Gray | `#D6DCD2` | Decorative separators only; not an essential boundary |
| `focus` | 云蓝 | Cloud Blue | `#365F7A` | Visible focus ring on light surfaces |
| `scrim` | 松墨遮罩 | Pine Ink Scrim | `#24352F` | Modal overlay at 32% opacity; never used for text |

`onPrimary`, `onSecondary`, `onWarning`, and `onError` resolve to `onFilled`. Text on light containers resolves to `textPrimary` or the matching dark semantic color. Selected controls must also expose their selected state, not only change color.

### Accent and health status colors

| Token | Chinese name | English name | HEX | Usage |
|---|---|---|---|---|
| `warning` | 暮橙 | Sunset Orange | `#96501F` | Attention needed, caution text/icon, warning action |
| `warningContainer` | 晨霞 | Sunrise Paper | `#F4E5D5` | Warning panel with dark warning text |
| `achievement` | 秋金 | Autumn Gold | `#7C6122` | Modest achievement icon or label; not health severity |
| `achievementContainer` | 稻香 | Rice Gold Mist | `#F1EAD4` | Completed goals and milestones |
| `error` | 朱砂 | Muted Cinnabar | `#A13F37` | Failed validation, destructive action, critical status |
| `errorPressed` | 深朱砂 | Deep Cinnabar | `#82322D` | Pressed destructive action |
| `errorContainer` | 淡朱纸 | Cinnabar Paper | `#F5E4DF` | Error and critical information panels |
| `success` | 竹青 | Bamboo Green | `#2F6654` | Confirmed success with explicit text/check icon |
| `info` | 云蓝 | Cloud Blue | `#365F7A` | Important neutral health information |

Important health information is not automatically a warning. Choose status from validated product/domain rules, not from a color preference. Distinguish “needs attention,” “critical,” “unavailable,” and “outdated” with explicit labels. Do not use green to imply a diagnosis or guaranteed health.

### Pairing and state rules

- Default text uses `textPrimary` on `background` or `surface`. Supporting text uses `textSecondary` on those same surfaces.
- Filled primary/secondary/destructive buttons use `onFilled` on their specified dark fill. Never put pale brand colors behind pale button text.
- Warning, error, achievement, and information containers use their matching dark semantic text/icon. Validate any new pairing before use.
- Disabled controls use `surfaceVariant`, `textSecondary`, and an explicit disabled semantic state. Explain unmet prerequisites nearby; do not reduce the opacity of an entire control containing readable text.
- Pressed, focused, selected, loading, and disabled states must remain distinct. On dark filled controls, use a light inner focus ring plus a `focus` outer ring against the light surrounding surface.
- Decorative dividers and illustrations cannot substitute for essential boundaries. Charts require labels and distinguishable shapes/line styles as well as color.

### Prohibited colors and behavior

Do not use pure black (`#000000`), pure white (`#FFFFFF`), neon tones, high saturation rainbow palettes, harsh hospital cyan, or aggressive red as a brand/background color. Controlled cinnabar is reserved for meaningful error or critical states. Do not use random shades, color-only status dots, or wallpaper-driven colors that replace the approved palette.

Any future dark palette must explicitly define and validate corresponding semantic roles before release. Do not treat the light palette as dark-mode compatible or silently inherit default purple theme colors.

## 3. Gradient Guidelines

Gradients are optional atmospheric treatments. Plain surfaces remain the default, especially for forms, dialogs, navigation, and alerts.

| Gradient token | Inspiration | Stops, start to end | Allowed use |
|---|---|---|---|
| `mountainMist` | 山岚 / Mountain mist | `#F6F3EB` to `#E2EDE5` | Top-to-bottom welcome or dashboard background |
| `morningSky` | 晨空 / Morning sky | `#E3EDF2` to `#FCFAF4` | Information or dashboard summary card |
| `bambooWellness` | 竹影 / Bamboo shade | `#E2EDE5` to `#FCFAF4` | Health score/summary card |
| `softSunrise` | 晨霞 / Soft sunrise | `#F4E5D5` to `#FCFAF4` | Achievement or encouraging overview card |

Use a static two-stop linear gradient at 0% and 100%, normally top to bottom. Keep it low contrast and restricted to one highlighted card or background region per viewport. Do not layer gradients with text textures or decorative patterns.

Place `textPrimary` over these light gradients and check contrast across the entire area behind the text, including any illustration overlay. Use a solid surface when contrast cannot be guaranteed. A health score gradient is decoration, never a severity indicator or clinical interpretation. Avoid neon colors, commercial rainbow blends, animated gradients, and gradient-filled functional buttons.

## 4. Typography System

Use Android's system sans-serif family with its Chinese fallback. Noto Sans CJK SC / 思源黑体 is an acceptable bundled alternative only after licensing, package-size, and glyph-coverage review. Do not require downloaded fonts for readability. Keep Latin text, Chinese text, digits, and units visually consistent; use aligned numerals for comparable metrics where supported.

| Token | Size | Line height | Weight | Purpose |
|---|---|---|---|---|
| `screenTitle` | 28sp | 40sp | 600 | Main screen heading |
| `sectionTitle` | 24sp | 34sp | 600 | Section and dialog title |
| `cardTitle` | 22sp | 32sp | 600 | Card heading |
| `body` | 20sp | 30sp | 400 | Main content, instructions, input values |
| `label` | 20sp | 30sp | 500 | Buttons, input labels, common controls |
| `supporting` | 18sp | 28sp | 400 | Units, timestamps, help, navigation labels |
| `metric` | 36sp | 46sp | 600 | A prominent health value, always with an 18sp+ unit |

**The minimum default size for readable UI text is 18sp.** Essential instructions, warnings, and errors use 20sp or larger. Never shrink text to fit. Use normal Chinese character spacing; do not apply all-caps styling, thin weights, calligraphy, or decorative serif fonts to functional content.

Respect system font scaling using `sp` for text and line height. Permit wrapping and component growth at 200% font scale; important text and actions must not be clipped or ellipsized. Android supports nonlinear scaling, so do not substitute a hand-calculated multiplier for platform text sizing. See [Android font scaling guidance](https://developer.android.com/about/versions/14/features#non-linear-font-scaling).

A future “larger text” preference may select larger semantic type tokens but must still respect the system scale; never cap or reset it. Critical values should wrap as a coherent value/unit group or move the unit to the next line. Truncate optional previews only when a labeled route opens the full content.

## 5. Layout and Spacing Rules

Use `dp` for layout and an approved spacing scale of **4, 8, 12, 16, 20, 24, 32, and 48dp**. Use 4dp only for tightly related noninteractive details; do not use it between touch targets.

| Rule/token | Default | Application |
|---|---|---|
| `screenInset` | 20dp | Horizontal content margin; 16dp on narrow screens when needed |
| `cardPadding` | 20dp | Inner card padding; minimum 16dp |
| `cardGap` | 16dp | Gap between cards |
| `sectionGap` | 24dp | Gap between major content sections |
| `relatedGap` | 8dp | Label-to-value or icon-to-label gap |
| `controlGap` | 12dp | Separation between independent controls; minimum 8dp |
| `touchTarget` | 56 x 56dp minimum | Every interactive target, including navigation items |
| `primaryActionHeight` | 60dp minimum | Important or full-width action; grow for text |
| `inputHeight` | 60dp minimum | Input area excluding external label/help |
| `iconSize` | 24dp; 28dp for major navigation | Visible graphic within its larger target |

QMDJ's 56dp target exceeds Android's 48dp accessibility baseline. Keep targets separate and ensure the focusable/hit area matches the intended action. See [Android accessibility guidance](https://developer.android.com/guide/topics/ui/accessibility/apps).

Use a single main reading column on phones. A two-column summary is allowed only when each card retains full labels, units, and adequate targets; stack it at large font sizes or narrow widths. Avoid dense tables, horizontal scrolling for essential content, nested scrolling, and carousels hiding core actions.

Place the main purpose and next action early in the reading order. Use labeled sections or separate task screens for long workflows; allow scrolling when it is needed to preserve large text. Do not force every screen into a single viewport. Keep fixed actions clear of content, system bars, display cutouts, gesture areas, and the keyboard. Prefer a maximum 600dp reading column on large screens; use additional columns only for independent sections.

## 6. Shape System

| Token | Radius | Usage |
|---|---|---|
| `shapeSmall` | 8dp | Status labels and small noninteractive containers |
| `shapeControl` | 12dp | Buttons and inputs |
| `shapeCard` | 20dp | Cards and health metric cards |
| `shapeDialog` | 24dp | Dialogs and modal sheet top corners |

Use soft rounded rectangles with consistent geometry. Avoid ornamental frames, irregular ink edges, and excessive pill shapes for major actions. Round icons and avatars may be circular; required text must never be clipped by shape boundaries.

Cards use `surface`; ordinary cards have 0dp elevation and spacing-based grouping. A subtle 1dp decorative border may supplement grouping. Clickable cards must include a clear labeled affordance. Inputs and outlined buttons use a 1dp `outline` boundary; focused inputs use a 2dp `focus` boundary. Dialogs may use 6dp elevation with the approved scrim. Shadows are secondary cues and must not determine readability.

## 7. Component Design Rules

All shared components must inherit the same color, type, spacing, shape, and accessibility tokens. Use these contracts when components are implemented later.

### Android native component strategy

**Android native components + QMDJ design rules = QMDJ reusable components.**

Build semantic wrappers around Jetpack Compose and Material 3. Keep native behavior and apply QMDJ's design tokens and elderly-friendly rules; do not create a separate UI framework.

Use this implementation priority:

1. Use Material 3 components whenever they support the required interaction.
2. Use Compose Foundation primitives only where Material 3 lacks the needed flexibility; retain native components for the functional controls inside the composition.
3. Use custom drawing only when neither option can satisfy the requirement. Document the concrete limitation and preserve an accessible alternative for meaningful information.

| QMDJ wrapper | Native foundation | Responsibility |
|---|---|---|
| `QButton` | Material 3 `Button`, `OutlinedButton`, or `TextButton` | Apply semantic action variants, typography, shape, and target sizes. Use `Button` for the filled variant. |
| `QCard` | Material 3 `Card` | Apply themed grouping, geometry, padding, and the appropriate static or clickable overload. |
| `QInput` | Material 3 `TextField` | Apply persistent labeling, themed fields, error/help text, units, and native editing behavior. |
| `QDialog` | Material 3 `AlertDialog` | Apply themed content and explicit actions while retaining native dialog behavior. |
| `QBottomNavigation` | Material 3 `NavigationBar` and `NavigationBarItem` | Implement the bottom-navigation form of `QNavigation`, including labeled destinations and selected states. |
| `QLoading` | Material 3 `CircularProgressIndicator` or `LinearProgressIndicator` | Apply themed progress and a readable status label; expose determinate progress when known. |
| Future screen containers | Material 3 `Scaffold` | Arrange native app bars, navigation, content, and actions with appropriate insets. |

Do not recreate native buttons, editable fields, dialogs, navigation interactions, or progress controls using canvas drawing, gesture handlers, or clickable boxes solely to achieve QMDJ styling. Foundation layouts may arrange native controls and supporting content.

### Theme and accessibility integration

- Material 3 roles must come from the QMDJ theme bridge, for example `MaterialTheme.colorScheme.primary`. QMDJ-specific roles such as `warning`, `card`, component shapes, spacing, and gradients must come from `QMDJTheme` accessors. Apply native styling APIs using these tokens, never literal colors or feature-local dimensions.
- Prefer inherited Material styling when its mapped role fits. Use token-based component defaults only where a wrapper needs a more specific role. Components must not inspect theme names or depend on Serenity's concrete values.
- Forward a caller's `Modifier` and expose relevant native options such as enabled state, input configuration, selection, dismissal, and progress. Keep wrapper APIs focused on their semantic purpose rather than reproducing the full Material API.
- Preserve native roles, click/edit actions, checked/selected states, focus, keyboard handling, and progress semantics. Do not clear accessibility semantics merely to restyle a component or add a duplicate click handler around an existing native control.
- Support meaningful `contentDescription` for unlabeled functional icons. Visible labels should retain native announcements; do not duplicate them with redundant descriptions. Decorative icons must not add extra TalkBack stops.
- Retain system text scaling and flexible layouts. Apply QMDJ's minimum 56dp targets through native sizing/modifier APIs; verify that adjacent targets remain separate and essential labels are not clipped at 200% text scale.
- Verify TalkBack and keyboard/switch access where applicable. Custom arrangement must not obscure errors, reorder focus incorrectly, or remove the native dismissal and Back behavior of dialogs.

### Future compatibility

Use public Compose and Material 3 APIs. Avoid copied library internals, private APIs, reflection, and custom interaction engines that duplicate Android capabilities. Keep theme mapping and component adaptations centralized so Android, Material 3, and Compose upgrades do not require rewriting individual screens. After dependency upgrades, verify native interactions, semantics, text scaling, and the theme bridge, especially newly introduced Material roles.

| Component | Purpose | Visual style | Usage rules |
|---|---|---|---|
| `QCard` | Group one coherent topic | `surface`, 20dp corners, 20dp padding; title plus content | Distinguish static from clickable cards. A clickable card has one clear primary destination; do not nest competing click regions. Use separate labeled actions when needed. |
| `QButton` | Perform an explicit action | Primary: bamboo fill/onFilled text. Secondary: surface/outline/primary text. Destructive: error fill. 12dp corners, 20sp label, 60dp primary height | One dominant action per task region. Use verbs such as “保存记录”. Support enabled, pressed, focused, disabled, loading. Loading keeps a readable label, prevents duplicate submission, and ends with explicit feedback. |
| `QInput` | Enter or edit a value | Surface fill, outline border, 12dp corners, persistent external label, 20sp value | Placeholder supplements the label. Show unit, expected format, and field-level error text. Use the appropriate keyboard, allow corrections, preserve input, and never encode an error only through a red border. |
| `QDialog` | Confirm a consequential choice or show a blocking issue | Solid surface, 24dp corners, 24dp padding, section title, concise body | Use specific action labels such as “取消” and “删除记录”. Stack buttons when labels grow. Provide a safe exit, focus management, and a clear consequence; ordinary success belongs inline. |
| `QNavigation` | Move between stable top-level destinations | Solid surface; labeled icons; primary selected text/icon on primaryContainer | Use 3–5 destinations, with at least 56dp target areas and 18sp labels. Show location and selected semantics. At large text sizes switch to an accessible labeled navigation alternative instead of truncating. Preserve context and respect Android Back. |
| `QHealthMetricCard` | Explain one health measurement or summary | QCard geometry; 36sp value, 18sp+ unit, 22sp title; optional approved light gradient | Show metric name, value, unit, timestamp, source, status text, and an understandable explanation. Include a labeled detail route when more context is needed. Missing/stale data must be explicit and never presented as zero or normal. |

### Shared feedback and state contracts

- **Loading:** Keep the screen structure stable; pair progress with “正在加载…” or a task-specific label. Never fabricate a metric while waiting. Announce meaningful state changes without repeated screen-reader interruptions.
- **Empty:** Explain what is missing and offer one relevant action, such as “添加记录”; do not show a blank card or unexplained dash.
- **Error/offline:** Explain the failed action in plain language, retain input or available data, expose its age if relevant, and provide a labeled retry/recovery action. Hide internal exception text from users.
- **Success:** Confirm the specific outcome, for example “记录已保存”. Keep feedback visible inline until the next meaningful action when it is important; do not rely on a short toast.
- **Destructive action:** Explain the affected item and consequence, and provide cancellation. Offer undo when the operation is reversible; do not claim reversibility when it is unavailable.
- **Focus and selection:** Provide visible indicators and correct role, state, and enabled semantics. Avoid duplicate announcements from icons already described by an adjacent label.

## 8. Elderly Friendly UI Rules

1. Combine important numbers with meaning. A value such as “步数 3,200 步” needs a time period and an explanation of what the user can do next; a bare gauge is insufficient.
2. Use familiar Simplified Chinese for the default interface. Expand unfamiliar abbreviations and keep units visible. Use explicit dates and times, especially on older records, rather than ambiguous “recent” labels.
3. Keep essential actions visible and labeled. Do not require long press, swipe, drag, or pinch to access a core task; gestures may supplement a visible control.
4. Use large separate buttons and forgiving input. Avoid adjacent tiny icons, hidden overflow-only actions, and automatic submission while a user is still editing.
5. Reduce memory load: keep instructions near the relevant field, show progress in multi-step tasks, and preserve entered information when navigating back.
6. Limit choice overload. Prefer a short set of relevant actions and clear categories; disclose optional details without hiding safety information.
7. Reduce unnecessary scrolling through prioritization and focused task screens, while keeping scrolling available for large text. Never compress content to satisfy a fixed screen height.
8. Provide immediate interaction feedback and a clear outcome. For longer work, show progress and retain cancellation when supported. Never describe an unconfirmed save as successful.
9. Use neutral, respectful wording. Encourage achievable habits without guilt, streak pressure, exaggerated celebration, or guaranteed health outcomes.
10. Health severity and recommendation logic must come from approved product/domain rules. AI-generated thresholds, diagnoses, and invented interpretations are not design decisions. A critical state must have clear text and an appropriate product-approved next step.
11. Confirm high-impact deletion and sensitive sharing with explicit consequences. Explain permissions at the point of use and avoid making unrelated permissions a prerequisite for basic navigation.
12. Avoid flashing, autoplay, looping decoration, and forced time limits. Optional transitions should be subtle (150–250ms), honor system animation preferences, and convey no information that disappears when animation is disabled.
13. Test with representative seniors from both 60+ and 80+ groups. Observe whether they can find actions, read values, correct mistakes, and recover without coaching; do not use age alone as a usability proxy.

## 9. Image and Illustration Guidelines

Allowed motifs include distant mountains, clouds, bamboo, natural foliage, morning mist, and restrained ink wash shapes. Prefer broad, quiet silhouettes and warm natural photography. People should be depicted respectfully, with realistic diversity and without stereotypes.

Decorative background illustrations use **3–8% opacity, maximum 10%**, and sit in margins, headers, or empty-state areas. Rice paper texture must be barely perceptible and confined to non-text regions. Keep all essential text on an unobstructed solid surface or a validated gradient; remove decoration whenever it competes with reading.

Use one cultural motif per screen region. Functional icons remain simple, consistent Android-style symbols paired with labels; do not turn mountain or bamboo imagery into ambiguous controls. Hide purely decorative images from accessibility services and describe meaningful images by purpose.

Avoid complicated patterns, ornamental seals, calligraphy backgrounds, red-and-gold ceremonial styling, dense landscape compositions, clinical stock imagery, and excessive decoration. Images must never resemble medical evidence, replace a measurement, or imply an unsupported health outcome.

## 10. Android Implementation Guidelines

Use the existing Jetpack Compose/Material 3 foundation when future implementation is authorized. The logical organization is:

```text
app/
  ui/
    theme/         # Semantic colors, typography, spacing, shapes, gradients
    components/    # Shared QMDJ components and their states
```

In this repository, these logical directories belong under `app/src/main/java/com/wisdomcare/app/ui/`. Retain existing package names; do not create a second theme hierarchy or rename the project merely to match the QMDJ product name.

- Centralize design tokens in the theme and map them to Material color/type/shape roles. Explicitly configure roles used by the app; default Material styling must not introduce unapproved colors or undersized text.
- Implement shared QMDJ components once and reuse them across screens. If a component does not yet exist, implement it in the shared layer when authorized instead of copying a local version into each screen.
- Do not hardcode arbitrary colors, radii, typography, or spacing in features. Introduce a new token only through a reviewed update to this specification.
- The implemented theme foundation supplies complete semantic definitions through `QMDJTheme` and maps standard roles into Material 3. Serenity is the default light definition; wallpaper colors and system dark mode do not override an explicit selection. Add approved future themes as data definitions, including validated dark palettes when needed.
- Use `sp` for text/line height, `dp` for spacing, flexible sizing, and minimum heights rather than fixed heights that clip scaled text. Apply window and keyboard insets to content and persistent actions.
- Localize visible strings and accessibility labels. Expose roles, selected/checked state, errors, headings, and meaningful progress through Compose semantics. Prefer built-in accessible controls, but verify their QMDJ target sizes and type tokens. Consult [Compose accessibility defaults](https://developer.android.com/develop/ui/compose/accessibility/api-defaults).
- Choose component states from actual data/action state. Preserve pending input and prevent duplicate operations. Distinguish unavailable, stale, loading, failed, and valid data.
- Treat a future dark theme as a separate reviewed token mapping. Verify semantic consistency, contrast, imagery, and system-bar treatment for both themes before exposing it as supported.

### Acceptance checklist for future UI changes

- Tokens and shared components match this specification; no arbitrary feature-level styling appears.
- QMDJ components wrap the appropriate native Material 3 control. Any Foundation-only or custom-drawing exception has a documented requirement and preserves accessible behavior.
- All informative text passes QMDJ's 4.5:1 threshold; essential non-text indicators pass 3:1 against adjacent surfaces, including pressed/focused states and gradients.
- Text at default and 200% system scale remains readable; controls grow and navigation stays usable on narrow phones and larger screens.
- Every interactive target is at least 56 x 56dp, with separate, nonoverlapping hit regions.
- TalkBack reads a coherent order, values include units/context, decorative assets are skipped, and actionable items have correct labels and state.
- Keyboard/switch navigation, Android Back, keyboard visibility, and safe insets do not block completion.
- Loading, empty, error, offline, stale data, success, and destructive-action states are handled where applicable.
- Core tasks have been reviewed with representative elderly users; record any unresolved accessibility issue before release.

## 11. AI Development Rules

Before generating or modifying any QMDJ UI, an AI coding assistant must:

1. Read `docs/UI_DESIGN_SYSTEM.md` and the applicable repository instructions.
2. Follow all defined color, typography, spacing, shape, accessibility, and component rules.
3. Do not invent new styles, brand colors, gradients, decorative motifs, or health interpretation rules.
4. Inspect and reuse existing QMDJ components and theme tokens. The proposed component catalog is not evidence of existing code. If missing, add shared components only within an authorized implementation task.
5. Ask the user before introducing major design changes, including a new visual direction, palette, type hierarchy, navigation model, or relaxation of accessibility requirements. Present the concrete proposal and its effects first.
6. Apply the native component priority in Section 7: Material 3 first, Foundation for necessary flexibility, and custom drawing only for a documented unmet requirement. Preserve native accessibility and interaction behavior.

For routine implementation within these rules, proceed within the authorized task scope. For approved design changes, update this document and relevant tokens together so the specification remains authoritative. Record the rationale, version, and affected components; do not create undocumented exceptions.

For the initial design-system task, create only this Markdown document. Do not modify Kotlin, XML, build configuration, existing project files, or runtime themes.
