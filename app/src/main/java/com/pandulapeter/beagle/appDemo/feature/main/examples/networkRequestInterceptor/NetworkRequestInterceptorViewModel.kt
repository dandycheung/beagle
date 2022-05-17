package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.SongRepository
import com.pandulapeter.beagle.appDemo.data.model.Song
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.*
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class NetworkRequestInterceptorViewModel(
    private val songRepository: SongRepository
) : ListViewModel<NetworkRequestInterceptorListItem>() {

    private var selectedSong by Delegates.observable(SongTitle.SONG_1) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            loadSong()
            refreshItems()
        }
    }
    private var loadedSong: Song? = null
    private var isLoading: Boolean = false
    private var job: Job? = null
    private val _items = MutableLiveData(listOf<NetworkRequestInterceptorListItem>())
    override val items: LiveData<List<NetworkRequestInterceptorListItem>> = _items

    init {
        loadSong()
        refreshItems()
    }

    fun onRadioButtonSelected(selectedItem: RadioButtonViewHolder.UiModel) {
        SongTitle.fromResourceId(selectedItem.titleResourceId)?.let { selectedSong = it }
    }

    fun loadSong() {
        if (selectedSong.id != loadedSong?.id) {
            job?.cancel()
            isLoading = true
            refreshItems()
            job = viewModelScope.launch {
                loadedSong = songRepository.getSong(selectedSong.id)
                songRepository.getLibrary() //TODO: Remove this
                isLoading = false
                refreshItems()
            }
        }
    }

    private fun refreshItems() {
        _items.value = mutableListOf<NetworkRequestInterceptorListItem>().apply {
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_1))
            addAll(SongTitle.values().map { songTitle -> RadioButtonViewHolder.UiModel(songTitle.titleResourceId, selectedSong == songTitle) })
            if (isLoading) {
                add(LoadingIndicatorViewHolder.UiModel())
            } else {
                if (selectedSong.id == loadedSong?.id) {
                    add(SongLyricsViewHolder.UiModel(loadedSong?.text?.formatSongLyrics() ?: ""))
                } else {
                    add(ErrorViewHolder.UiModel())
                }
            }
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_3))
            add(CodeSnippetViewHolder.UiModel("NetworkLogListModule()"))
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_4))
            add(
                CodeSnippetViewHolder.UiModel(
                    "dependencies {\n" +
                            "    …\n" +
                            "    api \"io.github.pandulapeter.beagle:log-okhttp:\$beagleVersion\"\n" +
                            "    \n" +
                            "    // Alternative for Android modules:\n" +
                            "    // debugApi \"io.github.pandulapeter.beagle:log-okhttp:\$beagleVersion\"\n" +
                            "    // releaseApi \"io.github.pandulapeter.beagle:log-okhttp-noop:\$beagleVersion\"\n" +
                            "}"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_5))
            add(
                CodeSnippetViewHolder.UiModel(
                    "Beagle.initialize(\n" +
                            "    …\n" +
                            "    behavior = Behavior(\n" +
                            "        …\n" +
                            "        networkLogBehavior = Behavior.NetworkLogBehavior(\n" +
                            "            networkLoggers = listOf(BeagleOkHttpLogger),\n" +
                            "            …\n" +
                            "        )\n" +
                            "    )\n" +
                            ")"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_6))
            add(
                CodeSnippetViewHolder.UiModel(
                    "val client = OkHttpClient.Builder()\n" +
                            "    …\n" +
                            "    .apply { (BeagleOkHttpLogger.logger as? Interceptor?)?.let { addInterceptor(it) } }\n" +
                            "    .build()"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_7))
            add(ClearButtonViewHolder.UiModel())
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_8))
        }
    }

    private fun String.formatSongLyrics() =
        replace(Regex("\\[(.*?)[]]"), "") // Remove chords
            .replace(Regex("\\{(.*?)[}]"), "") // Remove sections
            .replace(Regex("[ ][ ]+"), "") // Remove consecutive whitespaces
            .lines().filterNot { it.isEmpty() }.take(4) // Take the first four lines
            .joinToString("\n") + "\n…"

    private enum class SongTitle(@StringRes val titleResourceId: Int, val id: String) {
        SONG_1(titleResourceId = R.string.case_study_network_request_interceptor_song_1, id = "eagles-hotel_california"),
        SONG_2(titleResourceId = R.string.case_study_network_request_interceptor_song_2, id = "the_rembrandts-ill_be_there_for_you"),
        SONG_3(titleResourceId = R.string.case_study_network_request_interceptor_song_3, id = "the_proclaimers-im_gonna_be");

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}