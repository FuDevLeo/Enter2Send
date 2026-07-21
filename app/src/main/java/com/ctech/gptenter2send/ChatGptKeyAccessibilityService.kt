package com.ctech.gptenter2send

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ChatGptKeyAccessibilityService : AccessibilityService() {
    private var consumedKeyCode: Int? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() {
        consumedKeyCode = null
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (!BridgePreferences.isMasterEnabled(this)) {
            consumedKeyCode = null
            return false
        }

        if (event.keyCode != KeyEvent.KEYCODE_ENTER &&
            event.keyCode != KeyEvent.KEYCODE_NUMPAD_ENTER
        ) {
            return false
        }

        if (event.isShiftPressed) {
            consumedKeyCode = null
            return false
        }

        if (event.action == KeyEvent.ACTION_UP) {
            val consume = consumedKeyCode == event.keyCode
            consumedKeyCode = null
            return consume
        }

        if (event.action != KeyEvent.ACTION_DOWN) return false
        if (event.repeatCount > 0 && consumedKeyCode == event.keyCode) return true

        val root = rootInActiveWindow ?: return false
        if (root.packageName?.toString() != CHATGPT_PACKAGE) return false

        val composer = findUniqueFocusedEditableNode(root) ?: return false
        val sendButton = findUniqueSendButtonNearComposer(composer) ?: return false
        val clicked = sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        if (clicked) consumedKeyCode = event.keyCode
        return clicked
    }

    private fun findUniqueFocusedEditableNode(
        root: AccessibilityNodeInfo
    ): AccessibilityNodeInfo? {
        val matches = mutableListOf<AccessibilityNodeInfo>()

        fun visit(node: AccessibilityNodeInfo) {
            if (matches.size > 1) return
            val isComposerCandidate = node.packageName?.toString() == CHATGPT_PACKAGE &&
                node.isVisibleToUser &&
                node.isEnabled &&
                node.isFocused &&
                node.isEditable
            if (isComposerCandidate) matches += node

            for (index in 0 until node.childCount) {
                node.getChild(index)?.let(::visit)
                if (matches.size > 1) return
            }
        }

        visit(root)
        return matches.singleOrNull()
    }

    private fun findUniqueSendButtonNearComposer(
        composer: AccessibilityNodeInfo
    ): AccessibilityNodeInfo? {
        var ancestor: AccessibilityNodeInfo? = composer
        repeat(MAX_COMPOSER_ANCESTOR_LEVELS) {
            ancestor = ancestor?.parent
            val scope = ancestor ?: return null
            val candidates = mutableListOf<AccessibilityNodeInfo>()
            collectSendTargets(scope, null, candidates)
            when (candidates.size) {
                1 -> return candidates.single()
                in 2..Int.MAX_VALUE -> return null
            }
        }
        return null
    }

    private fun collectSendTargets(
        node: AccessibilityNodeInfo,
        clickableAncestor: AccessibilityNodeInfo?,
        matches: MutableList<AccessibilityNodeInfo>
    ) {
        if (matches.size > 1) return

        val clickableTarget = if (isClickableActionNode(node)) node else clickableAncestor
        if (hasSendIdentity(node) && clickableTarget != null && matches.none { it == clickableTarget }) {
            matches += clickableTarget
        }

        for (index in 0 until node.childCount) {
            val child = node.getChild(index) ?: continue
            collectSendTargets(child, clickableTarget, matches)
            if (matches.size > 1) return
        }
    }

    private fun isClickableActionNode(node: AccessibilityNodeInfo): Boolean =
        node.packageName?.toString() == CHATGPT_PACKAGE &&
            node.isVisibleToUser &&
            node.isEnabled &&
            node.isClickable &&
            node.actionList.any { it.id == AccessibilityNodeInfo.ACTION_CLICK }

    private fun hasSendIdentity(node: AccessibilityNodeInfo): Boolean {
        if (node.packageName?.toString() != CHATGPT_PACKAGE ||
            !node.isVisibleToUser ||
            !node.isEnabled
        ) return false

        val description = node.contentDescription?.toString()?.trim()
        if (description != null && SEND_DESCRIPTIONS.any {
                it.equals(description, ignoreCase = true)
            }
        ) {
            return true
        }

        val viewId = node.viewIdResourceName?.lowercase() ?: return false
        return SEND_VIEW_ID_SUFFIXES.any(viewId::endsWith)
    }

    companion object {
        private const val CHATGPT_PACKAGE = "com.openai.chatgpt"
        private const val MAX_COMPOSER_ANCESTOR_LEVELS = 5

        private val SEND_DESCRIPTIONS = setOf("Send", "Send message")
        private val SEND_VIEW_ID_SUFFIXES = setOf(
            "/send",
            "/send_button",
            "/send_message",
            "/send_message_button"
        )
    }
}
