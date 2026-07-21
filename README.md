# Enter2Send

Enter2Send is an unofficial, narrowly scoped Android accessibility utility for physical-keyboard input in the official ChatGPT app. It targets Samsung DeX and other Android setups where Enter should send the focused message while Shift+Enter inserts a newline.

This project is independent and is not affiliated with, endorsed by, or sponsored by OpenAI. ChatGPT is a trademark of OpenAI.

## Behavior

- Enter and Numpad Enter click ChatGPT's existing Send control only when one focused editable composer and one nearby Send action are exposed unambiguously.
- Shift+Enter is not consumed, allowing ChatGPT to insert a newline normally.
- Other keys, fields, and applications are unaffected.
- Empty or ambiguous composer states pass the key through unchanged.
- F8 dictation is not included.

The service is restricted to `com.openai.chatgpt`. It does not access `AccessibilityNodeInfo.text`, log accessibility content, request network access, collect telemetry, or use fixed screen coordinates.

## Compatibility status

Real-device testing on a Galaxy S23 Ultra running Android 16 / One UI 8.5 with ChatGPT `1.2026.195(12)` confirmed that plain Enter sends exactly once and Shift+Enter inserts a newline in Samsung DeX. The explicit `KEYCODE_NUMPAD_ENTER` path was emulator-tested because the target DeX keyboard has no numpad.

ChatGPT updates may change its accessibility hierarchy; when the composer or Send control cannot be identified uniquely, Enter is deliberately left alone.

## Build and install

Requirements: JDK 17 and Android SDK 35.

```powershell
.\gradlew.bat clean assembleDebug
```

Install `app/build/outputs/apk/debug/app-debug.apk`, open **Enter2Send**, and use **Open accessibility settings** to enable its service. The in-app switch pauses Enter interception without revoking accessibility access.

Test the following before relying on a build:

1. Enter sends one non-empty ChatGPT message exactly once.
2. Shift+Enter inserts a newline and does not send.
3. Enter in ChatGPT search/settings and in another app behaves normally.
4. Disabling the in-app switch or accessibility service stops interception immediately.
5. If available, Numpad Enter sends exactly once.

## Dictation limitation

In ChatGPT Remote, the accessible Stop action ends microphone capture but discards the transcript and hides the composer. The accessible Send action ends capture and submits immediately. No separate stop-and-commit action is exposed, so the requested F8 start/stop-for-review workflow is not included instead of broadening the app into a custom keyboard, speech recognizer, coordinate map, or generalized remapper.

## License

Licensed under the [Apache License 2.0](LICENSE).
