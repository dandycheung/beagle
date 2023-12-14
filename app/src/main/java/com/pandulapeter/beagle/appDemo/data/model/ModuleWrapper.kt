package com.pandulapeter.beagle.appDemo.data.model

import android.widget.Toast
import androidx.annotation.StringRes
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.*
import java.util.*

sealed class ModuleWrapper(
    @StringRes val titleResourceId: Int,
    @StringRes val descriptionResourceId: Int,
    val module: Module<*>,
    val codeSnippet: String
) {
    val id get() = module.id

    class CheckBoxWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_check_box,
        descriptionResourceId = R.string.add_module_check_box_description,
        module = CheckBoxModule(text = "CheckBox", initialValue = false, onValueChanged = {}),
        codeSnippet = "CheckBoxModule(\n" +
                "    text = \"CheckBox\",\n" +
                "    initialValue = false,\n" +
                "    onValueChanged = { … }\n" +
                ")"
    )

    class DividerWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_divider,
        descriptionResourceId = R.string.add_module_divider_description,
        module = DividerModule(),
        codeSnippet = "DividerModule()"
    )

    class ItemListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_item_list,
        descriptionResourceId = R.string.add_module_item_list_description,
        module = ItemListModule(
            title = "ItemList",
            items = listOf(
                BeagleListItemContractImplementation("Item 1"),
                BeagleListItemContractImplementation("Item 2"),
                BeagleListItemContractImplementation("Item 3")
            ),
            onItemSelected = {}
        ),
        codeSnippet = "ItemListModule(\n" +
                "    title = \"ItemList\",\n" +
                "    items = listOf(\n" +
                "        BeagleListItemContractImplementation(\"Item 1\"),\n" +
                "        BeagleListItemContractImplementation(\"Item 2\"),\n" +
                "        BeagleListItemContractImplementation(\"Item 3\")\n" +
                "    ),\n" +
                "    onItemSelected = { … }\n" +
                ")"
    )

    class KeyValueListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_key_value_list,
        descriptionResourceId = R.string.add_module_key_value_list_description,
        module = KeyValueListModule(
            title = "KeyValueList",
            pairs = listOf(
                "Key 1" to "Value 1",
                "Key 2" to "Value 2",
                "Key 3" to "Value 3"
            )
        ),
        codeSnippet = "KeyValueListModule(\n" +
                "    title = \"KeyValueList\",\n" +
                "    pairs = listOf(\n" +
                "        \"Key 1\" to \"Value 1\",\n" +
                "        \"Key 2\" to \"Value 2\",\n" +
                "        \"Key 3\" to \"Value 3\"\n" +
                "    )\n" +
                ")"
    )

    class LoadingIndicatorWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_loading_indicator,
        descriptionResourceId = R.string.add_module_loading_indicator_description,
        module = LoadingIndicatorModule(),
        codeSnippet = "LoadingIndicatorModule()"
    )

    class LogListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_log_list,
        descriptionResourceId = R.string.add_module_log_list_description,
        module = UUID.randomUUID().toString().let { label ->
            LogListModule(
                label = label
            ).also {
                Beagle.log(message = "Log message 3", label = label, payload = "Log payload")
                Beagle.log(message = "Log message 2", label = label)
                Beagle.log(message = "Log message 1", label = label)
            }
        },
        codeSnippet = "LogListModule(tag = …)"
    )

    class LongTextWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_long_text,
        descriptionResourceId = R.string.add_module_long_text_description,
        module = LongTextModule(
            title = "LongText",
            text = "This is a longer piece of text that only becomes visible when the user expands the header."
        ),
        codeSnippet = "LongTextModule(\n" +
                "    title = \"LongText\",\n" +
                "    text = \"This is a longer piece of text that only becomes visible when the user expands the header.\"\n" +
                ")"
    )

    class MultipleSelectionListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_multiple_selection_list,
        descriptionResourceId = R.string.add_module_multiple_selection_list_description,
        module =
        MultipleSelectionListModule(
            title = "MultipleSelectionList",
            items = listOf(
                BeagleListItemContractImplementation("Checkbox 1"),
                BeagleListItemContractImplementation("Checkbox 2"),
                BeagleListItemContractImplementation("Checkbox 3")
            ),
            initiallySelectedItemIds = emptySet(),
            onSelectionChanged = {}
        ),
        codeSnippet = "MultipleSelectionListModule(\n" +
                "    title = \"MultipleSelectionList\",\n" +
                "    items = listOf(\n" +
                "        BeagleListItemContractImplementation(\"Checkbox 1\"),\n" +
                "        BeagleListItemContractImplementation(\"Checkbox 2\"),\n" +
                "        BeagleListItemContractImplementation(\"Checkbox 3\")\n" +
                "    ),\n" +
                "    initiallySelectedItemIds = emptySet(),\n" +
                "    onSelectionChanged = { … }\n" +
                ")"
    )

    class PaddingWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_padding,
        descriptionResourceId = R.string.add_module_padding_description,
        module = PaddingModule(),
        codeSnippet = "PaddingModule()"
    )

    class SingleSelectionListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_single_selection_list,
        descriptionResourceId = R.string.add_module_single_selection_list_description,
        module =
        SingleSelectionListModule(
            title = "SingleSelectionList",
            items = listOf(
                BeagleListItemContractImplementation("Radio button 1"),
                BeagleListItemContractImplementation("Radio button 2"),
                BeagleListItemContractImplementation("Radio button 3")
            ),
            initiallySelectedItemId = null,
            onSelectionChanged = {}
        ),
        codeSnippet = "SingleSelectionListModule(\n" +
                "    title = \"SingleSelectionList\",\n" +
                "    items = listOf(\n" +
                "        BeagleListItemContractImplementation(\"Radio button 1\"),\n" +
                "        BeagleListItemContractImplementation(\"Radio button 2\"),\n" +
                "        BeagleListItemContractImplementation(\"Radio button 3\")\n" +
                "    ),\n" +
                "    initiallySelectedItemId = null,\n" +
                "    onSelectionChanged = { … }\n" +
                ")"
    )

    class SliderWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_slider,
        descriptionResourceId = R.string.add_module_slider_description,
        module = SliderModule(text = { "Slider ($it)".toText() }, onValueChanged = {}),
        codeSnippet = "SliderModule(\n" +
                "    text = { \"Slider (\$it)\".toText() },\n" +
                "    onValueChanged = { … }\n" +
                ")"
    )

    class SwitchWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_switch,
        descriptionResourceId = R.string.add_module_switch_description,
        module = SwitchModule(text = "Switch", onValueChanged = {}),
        codeSnippet = "SwitchModule(\n" +
                "    text = \"Switch\",\n" +
                "    onValueChanged = { … }\n" +
                ")"
    )

    class TextNormalWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_text,
        descriptionResourceId = R.string.add_module_text_description,
        module = TextModule(text = "Text"),
        codeSnippet = "TextModule(\"Text\")"
    )

    class TextSectionHeaderWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_text_section_header,
        descriptionResourceId = R.string.add_module_text_section_header_description,
        module = TextModule(text = "Section header", type = TextModule.Type.SECTION_HEADER),
        codeSnippet = "TextModule(\n" +
                "    text = \"Section header\",\n" +
                "    type = TextModule.Type.SECTION_HEADER\n" +
                ")"
    )

    class TextButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_text_button,
        descriptionResourceId = R.string.add_module_text_button_description,
        module = TextModule(text = "Button", type = TextModule.Type.BUTTON, onItemSelected = {}),
        codeSnippet = "TextModule(\n" +
                "    text = \"Button\",\n" +
                "    type = TextModule.Type.BUTTON,\n" +
                "    onItemSelected = { … }\n" +
                ")"
    )

    class TextInputWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_text_input,
        descriptionResourceId = R.string.add_module_text_input_description,
        module = TextInputModule(text = { "Text input ($it)".toText() }, initialValue = "Hello!", onValueChanged = {}),
        codeSnippet = "TextInputModule(\n" +
                "    text = { \"Text input (\$it)\".toText() },\n" +
                "    initialValue = \"Hello!\",\n" +
                "    onValueChanged = { … }\n" +
                ")"
    )

    data object AnimationDurationSwitchWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_animation_duration_switch,
        descriptionResourceId = R.string.add_module_animation_duration_switch_description,
        module = AnimationDurationSwitchModule(),
        codeSnippet = "AnimationDurationSwitchModule()"
    )

    data object AppInfoButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_app_info_button,
        descriptionResourceId = R.string.add_module_app_info_button_description,
        module = AppInfoButtonModule(),
        codeSnippet = "AppInfoButtonModule()"
    )

    data object BugReportButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_bug_report_button,
        descriptionResourceId = R.string.add_module_bug_report_button_description,
        module = BugReportButtonModule(),
        codeSnippet = "BugReportButtonModule()"
    )

    data object DeveloperOptionsButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_developer_options_button,
        descriptionResourceId = R.string.add_module_developer_options_button_description,
        module = DeveloperOptionsButtonModule(),
        codeSnippet = "DeveloperOptionsButtonModule()"
    )

    data object DeviceInfoWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_device_info,
        descriptionResourceId = R.string.add_module_device_info_description,
        module = DeviceInfoModule(),
        codeSnippet = "DeviceInfoModule()"
    )

    data object ForceCrashButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_force_crash_button,
        descriptionResourceId = R.string.add_module_force_crash_button_description,
        module = ForceCrashButtonModule(),
        codeSnippet = "ForceCrashButtonModule()"
    )

    data object GalleryButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_gallery_button,
        descriptionResourceId = R.string.add_module_gallery_button_description,
        module = GalleryButtonModule(),
        codeSnippet = "GalleryButtonModule()"
    )

    data object HeaderWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_header,
        descriptionResourceId = R.string.add_module_header_description,
        module = HeaderModule(
            title = "Header title",
            subtitle = "Header subtitle"
        ),
        codeSnippet = "HeaderModule(\n" +
                "    title = \"Header title\",\n" +
                "    subtitle = \"Header subtitle\"\n" +
                ")"
    )

    data object KeylineOverlaySwitchWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_keyline_overlay_switch,
        descriptionResourceId = R.string.add_module_keyline_overlay_switch_description,
        module = KeylineOverlaySwitchModule(),
        codeSnippet = "KeylineOverlaySwitchModule()"
    )

    data object LifecycleLogListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_lifecycle_log_list,
        descriptionResourceId = R.string.add_module_lifecycle_log_list_description,
        module = LifecycleLogListModule(),
        codeSnippet = "LifecycleLogListModule()"
    )

    class LoremIpsumGeneratorButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_lorem_ipsum_generator_button,
        descriptionResourceId = R.string.add_module_lorem_ipsum_generator_button_description,
        module = LoremIpsumGeneratorButtonModule { Beagle.currentActivity?.run { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() } },
        codeSnippet = "LoremIpsumGeneratorButtonModule { generatedText -> … }"
    )

    data object NetworkLogListWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_network_log_list,
        descriptionResourceId = R.string.add_module_network_log_list_description,
        module = NetworkLogListModule(),
        codeSnippet = "NetworkLogListModule()"
    )

    data object ScreenCaptureToolboxWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_network_screen_capture_toolbox,
        descriptionResourceId = R.string.add_module_network_screen_capture_toolbox_description,
        module = ScreenCaptureToolboxModule(),
        codeSnippet = "ScreenCaptureToolboxModule()"
    )

    data object ScreenRecordingButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_network_screen_recording_button,
        descriptionResourceId = R.string.add_module_network_screen_recording_button_description,
        module = ScreenRecordingButtonModule(),
        codeSnippet = "ScreenRecordingButtonModule()"
    )

    data object ScreenshotButtonWrapper : ModuleWrapper(
        titleResourceId = R.string.add_module_network_screenshot_button,
        descriptionResourceId = R.string.add_module_network_screenshot_button_description,
        module = ScreenshotButtonModule(),
        codeSnippet = "ScreenshotButtonModule()"
    )
}