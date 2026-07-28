<div align="center">
  <h1>&#9000; Enter2Send</h1>
  <p><strong>Desktop-style physical-keyboard controls for ChatGPT, Messenger, and Claude Remote Control on Android and Samsung DeX.</strong></p>
  <p><code>Enter &rarr; Send</code> &nbsp;&middot;&nbsp; <code>Shift+Enter &rarr; Newline</code> &nbsp;&middot;&nbsp; ChatGPT DeX verified</p>
</div>

Enter2Send is a small Android accessibility utility for people who use the official ChatGPT, Messenger, or Claude Remote Control experience with a physical keyboard. It makes explicitly enabled message composers behave like desktop chat boxes without replacing those apps, installing a custom keyboard, or reading message contents.

> [!IMPORTANT]
> Enter2Send is an independent, unofficial project. It is not affiliated with, endorsed by, or sponsored by OpenAI, Meta, or Anthropic. ChatGPT is a trademark of OpenAI. Messenger is a trademark of Meta. Claude is a trademark of Anthropic.

[Overview](#overview) &middot; [Install](#install) &middot; [Compatibility](#compatibility) &middot; [Roadmap](#roadmap) &middot; [Privacy](#privacy-and-safety)

## Overview

| Key or context | Result |
| --- | --- |
| **Enter** in an enabled, supported composer | Sends the current message exactly once |
| **Numpad Enter** in the focused composer | Sends through the explicit Android numpad key path |
| **Shift+Enter** | Passes through to the active app and inserts a newline |
| After a successful send | Waits for the active app to clear the Send control, then restores composer focus |
| Empty or ambiguous composer | Leaves Enter to the active app's normal behavior |
| Search, settings, login fields, disabled apps, or another app | Completely unaffected |
| Enter2Send switch disabled | All keyboard input passes through unchanged |

The service acts only when Android exposes all of the following unambiguously:

1. The active package is an enabled supported app: `com.openai.chatgpt`, `com.facebook.orca`, or the Remote Control surface in `com.anthropic.claude`.
2. There is one visible, enabled, focused editable composer.
3. There is one nearby visible, enabled Send action.

Claude support adds one more requirement: the active window must expose the exact visible Remote Control semantic marker `Change mode`. Normal Claude chats and Dispatch do not meet that requirement and remain entirely native.

If any requirement is missing or ambiguous, Enter2Send does nothing and the key continues normally.

## Why this exists

Android chat apps can treat physical Enter as a newline, which interrupts keyboard-first workflows in DeX. General-purpose remappers can approximate Enter-to-send with macros or screen taps, but those approaches may require broad configuration or depend on a fixed screen layout.

Enter2Send is deliberately narrower: it recognizes the focused composer and the app's existing Send control through accessibility semantics, then activates that control without using screen coordinates.

## Compatibility

Real-device Samsung DeX testing passed with:

- **Device:** Galaxy S23 Ultra
- **OS:** Android 16 / One UI 8.5
- **ChatGPT:** `1.2026.195(12)`
- **Verified:** Enter sends exactly once, focus returns for an immediate second send, and Shift+Enter inserts a newline
- **Numpad Enter:** Explicit keycode path emulator-tested; physical verification is pending because the target keyboard has no numpad

Messenger support is verified in the official `com.facebook.orca` package on the target Galaxy S23 Ultra in Samsung DeX and is off by default.

Claude Remote Control support targets the official `com.anthropic.claude` package and is off by default. Validation targets Claude `1.260716.20` on the same Galaxy/DeX environment; ordinary Claude chats and Dispatch are intentionally excluded.

Supported-app updates may change their accessibility hierarchies. When an app no longer exposes a unique composer or Send control, Enter2Send is designed to fail open and leave the key untouched.

## Install

Download the signed APK from the [latest GitHub release](https://github.com/ctech1313/Enter2Send/releases/latest):

1. Download the `Enter2Send` APK and its matching `.sha256` checksum file.
2. Confirm the APK's SHA-256 matches the published checksum.
3. Allow your browser or file manager to install unknown apps when Android prompts you.
4. Install and open **Enter2Send**.
5. Select **Open accessibility settings**.
6. Enable **Enter2Send** under installed accessibility apps.
7. Return to the app and confirm **Accessibility service: ON**.
8. Leave **Enter-to-send enabled** switched on.
9. Enable the individual apps you want Enter2Send to handle. ChatGPT defaults on; Messenger and Claude Remote Control default off until explicitly enabled.

Verify the download in PowerShell with:

```powershell
(Get-FileHash .\Enter2Send-v*.apk -Algorithm SHA256).Hash
```

The release notes also publish the signing-certificate SHA-256 fingerprint. Every official update will use the same signing identity.

### Build from source

Build the debug APK with JDK 17 and Android SDK 35:

```powershell
git clone https://github.com/ctech1313/Enter2Send.git
cd Enter2Send
.\gradlew.bat clean assembleDebug
```

The debug APK is written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

The in-app switch pauses interception without revoking accessibility access. Disabling the Android accessibility service stops Enter2Send completely.

<details>
<summary><strong>Quick verification checklist</strong></summary>

1. In each enabled app, type a non-empty message and press Enter. It should send once.
2. Type two lines with Shift+Enter. Nothing should send until plain Enter is pressed.
3. Press Enter in app search/settings and in another application. Behavior should remain normal.
4. Disable the in-app switch and confirm Enter2Send stops intercepting immediately.
5. If your keyboard has a numpad, confirm Numpad Enter sends once.
6. Without clicking the composer again, type and send a second message with one Enter press.
7. Disable one app switch and confirm Enter passes through normally there while the other enabled app still works.

</details>

## Privacy and safety

Enter2Send intentionally has a small trust boundary:

- No network permission
- No analytics, telemetry, advertising, or crash reporting
- No backend, account, API integration, or database
- No access to `AccessibilityNodeInfo.text`
- No message-content logging, storage, or transmission
- No fixed-coordinate taps or gesture injection
- No generalized key-remapping interface
- No custom keyboard or input method

The accessibility service is package-restricted to the official ChatGPT, Messenger, and Claude Android apps, with separate in-app enablement switches. Claude handling is further restricted to Remote Control windows carrying the exact semantic marker described above. Only handled Enter and Numpad Enter events are consumed; all other key events return immediately.

## Troubleshooting

**Enter still inserts a newline**

- Confirm the Android accessibility service and the in-app switch are both enabled.
- Confirm the individual ChatGPT, Messenger, or Claude Remote Control switch is enabled.
- Confirm the actual message composer is focused and a Send button is available.
- If the supported app was recently updated, its accessibility hierarchy may have changed. Open an issue with the Android, One UI, and app versions&mdash;never include message contents.

**Enter behaves unexpectedly elsewhere**

- Disable the in-app switch or accessibility service immediately.
- Report the affected screen and application version. The service should leave every non-composer field and every other app untouched.

## Roadmap

- [x] Enter sends from the focused ChatGPT composer
- [x] Shift+Enter inserts a newline
- [x] Restore composer focus only after the supported app confirms the send transition
- [x] Samsung DeX verification on the target Galaxy device
- [x] Opt-in Messenger package and semantic Send-control support
- [x] Messenger verification across available chat surfaces on the target Galaxy device and DeX
- [x] Opt-in Claude Remote Control package and semantic surface support
- [ ] Claude Remote Control verification on the target Galaxy device and DeX
- [ ] Physical Numpad Enter verification
- [ ] **Optional dictation hotkey support** if ChatGPT exposes uniquely identifiable controls

The current ChatGPT Remote hierarchy does not give its dictation start control a unique accessible description or view ID. The only matcher that activated it relied on an unnamed structural wrapper, so it was removed rather than shipping an unsafe F8 action. Dictation remains on the roadmap until ChatGPT exposes a semantic control; Enter2Send will not substitute coordinates, gestures, a custom keyboard, or a separate speech-recognition stack.

### Emulator preflight (2026-07-21)

- `Pixel_9_Pro_XL_API_35` booted successfully on Android 15 / API 35 without wiping AVD data.
- The experimental debug APK installed, the accessibility service bound, and the status screen reported ON.
- The F8 switch was confirmed off on first launch, could be changed, persisted normally, and was returned to off.
- Injecting F8 outside ChatGPT left the service bound with no crash.
- The official ChatGPT package was not installed. Its Play Store page opened in the unauthenticated Play Store activity, so normal Chat, Remote, microphone pass-through, and ChatGPT key-flow testing could not be attempted without user credentials.

These results are preflight evidence only. Android 15 emulation cannot replace Android 16 / One UI / Samsung DeX acceptance on the target Galaxy device.

## Project boundaries

Enter2Send is not a replacement chat client, browser wrapper, API client, backend service, custom keyboard, or general-purpose remapper. Its purpose is one focused improvement: make a physical keyboard feel natural in explicitly supported Android message composers.

## License

Licensed under the [Apache License 2.0](LICENSE).
