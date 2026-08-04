# Changelog

All notable Enter2Send changes are documented here.

## [0.3.1] - 2026-08-04

### Fixed

- Enter now acts only in the supported app window that currently owns keyboard input focus.
- Switching to another DeX window no longer brings the previous supported app forward or sends its unfinished draft.
- Switching away during post-send focus restoration now cancels the pending refocus instead of pulling the old app back to the foreground.

### Verified

- Manually accepted on a Galaxy S23 Ultra running Android 16 / One UI 8.5 in Samsung DeX.
- Covered supported-to-unsupported and supported-to-supported window switching, immediate switch-away after sending, consecutive sends, and Shift+Enter.

## [0.3.0] - 2026-07-28

### Added

- Added opt-in Messenger and Claude Remote Control support alongside ChatGPT.
- Added per-app switches, a refreshed status screen, updated project artwork, and structured app-support requests.

### Changed

- Generalized the semantic-only, fail-open send lifecycle across supported app profiles without changing the package, preferences, or accessibility-service identity.

## [0.2.0] - 2026-07-23

### Fixed

- Stabilized consecutive sends by waiting for the Send control to clear before restoring composer focus.
- Preserved Shift+Enter newlines and removed unsafe automatic resend behavior.

## [0.1.0] - 2026-07-21

### Added

- First public Enter2Send release for physical-keyboard Enter-to-send behavior in ChatGPT on Android and Samsung DeX.
- Added Shift+Enter newline behavior and explicit Numpad Enter handling with no network access, telemetry, coordinate taps, or custom keyboard.

[0.3.1]: https://github.com/ctech1313/Enter2Send/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/ctech1313/Enter2Send/releases/tag/v0.3.0
[0.2.0]: https://github.com/ctech1313/Enter2Send/releases/tag/v0.2.0
[0.1.0]: https://github.com/ctech1313/Enter2Send/releases/tag/v0.1.0
