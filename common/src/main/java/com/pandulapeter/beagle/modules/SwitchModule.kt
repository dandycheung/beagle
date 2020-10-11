package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.modules.SwitchModule.Companion.DEFAULT_INITIAL_VALUE
import com.pandulapeter.beagle.modules.SwitchModule.Companion.DEFAULT_IS_ENABLED
import com.pandulapeter.beagle.modules.SwitchModule.Companion.DEFAULT_IS_VALUE_PERSISTED
import com.pandulapeter.beagle.modules.SwitchModule.Companion.DEFAULT_ON_VALUE_CHANGED
import com.pandulapeter.beagle.modules.SwitchModule.Companion.DEFAULT_SHOULD_REQUIRE_CONFIRMATION

/**
 * Displays a simple switch.
 *
 * @param text - The text to display on the switch.
 * @param initialValue - Whether or not the switch is checked initially. If [isValuePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched. [DEFAULT_INITIAL_VALUE] by default.
 * @param isEnabled - Can be used to enable or disable all user interaction with the module. [DEFAULT_IS_ENABLED] by default.
 * @param isValuePersisted - Can be used to enable or disable persisting the value on the local storage. This will only work if the module has a unique, constant ID. [DEFAULT_IS_VALUE_PERSISTED] by default.
 * @param shouldRequireConfirmation - Can be used to enable or disable bulk apply. When enabled, changes made to the module by the user only take effect after a confirmation step. [DEFAULT_SHOULD_REQUIRE_CONFIRMATION] by default.
 * @param id - A unique identifier for the module. Must be a unique constant for the save / load feature to work (see [isValuePersisted]]). [Module.randomId] by default.
 * @param onValueChanged - Callback triggered when the user toggles the switch. In case of persisted values, this will also get called the first time the module is added. [DEFAULT_ON_VALUE_CHANGED] by default.
 */
data class SwitchModule(
    override val text: (Boolean) -> Text,
    override val initialValue: Boolean = DEFAULT_INITIAL_VALUE,
    override val isEnabled: Boolean = DEFAULT_IS_ENABLED,
    override val isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
    override val shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
    override val id: String = Module.randomId,
    override val onValueChanged: (Boolean) -> Unit = DEFAULT_ON_VALUE_CHANGED
) : ValueWrapperModule<Boolean, SwitchModule> {

    constructor(
        text: CharSequence,
        initialValue: Boolean = DEFAULT_INITIAL_VALUE,
        isEnabled: Boolean = DEFAULT_IS_ENABLED,
        isValuePersisted: Boolean = DEFAULT_IS_VALUE_PERSISTED,
        shouldRequireConfirmation: Boolean = DEFAULT_SHOULD_REQUIRE_CONFIRMATION,
        id: String = Module.randomId,
        onValueChanged: (Boolean) -> Unit = DEFAULT_ON_VALUE_CHANGED
    ) : this(
        text = { Text.CharSequence(text) },
        initialValue = initialValue,
        isEnabled = isEnabled,
        isValuePersisted = isValuePersisted,
        shouldRequireConfirmation = shouldRequireConfirmation,
        id = id,
        onValueChanged = onValueChanged
    )

    companion object {
        private const val DEFAULT_INITIAL_VALUE = false
        private const val DEFAULT_IS_ENABLED = true
        private const val DEFAULT_IS_VALUE_PERSISTED = false
        private const val DEFAULT_SHOULD_REQUIRE_CONFIRMATION = false
        private val DEFAULT_ON_VALUE_CHANGED: (Boolean) -> Unit = {}
    }
}