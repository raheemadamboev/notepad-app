package xyz.teamgravity.notepad.presentation.screen.note.list

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.destinations.NoteAddScreenDestination
import com.ramcosta.composedestinations.generated.destinations.NoteEditScreenDestination
import com.ramcosta.composedestinations.generated.destinations.PinLockScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SupportScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.teamgravity.coresdkandroid.android.BuildUtil
import xyz.teamgravity.coresdkandroid.connect.ConnectUtil
import xyz.teamgravity.coresdkandroid.settings.navigateAppLocaleSettings
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.coresdkcompose.paging.shouldShowEmptyState
import xyz.teamgravity.coresdkcompose.review.DialogReview
import xyz.teamgravity.coresdkcompose.update.DialogUpdateAvailable
import xyz.teamgravity.coresdkcompose.update.DialogUpdateDownloaded
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.util.Helper
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.button.NoteFloatingActionButton
import xyz.teamgravity.notepad.presentation.component.card.CardNote
import xyz.teamgravity.notepad.presentation.component.dialog.NoteAlertDialog
import xyz.teamgravity.notepad.presentation.component.drawer.DrawerNoteList
import xyz.teamgravity.notepad.presentation.component.text.TextInfo
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteList
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>(start = true)
@Composable
fun NoteListScreen(
    drawer: DrawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    ),
    scope: CoroutineScope = rememberCoroutineScope(),
    navigator: DestinationsNavigator,
    viewmodel: NoteListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val notes = viewmodel.notes.collectAsLazyPagingItems()
    val shouldShowEmptyState by notes.shouldShowEmptyState()
    val autoSave by viewmodel.autoSave.collectAsStateWithLifecycle()
    val updateLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {}
    )

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                NoteListViewModel.NoteListEvent.Review -> {
                    viewmodel.onReview(activity)
                }

                NoteListViewModel.NoteListEvent.DownloadAppUpdate -> {
                    viewmodel.onUpdateDownload(updateLauncher)
                }
            }
        }
    )

    LifecycleEventEffect(
        event = Lifecycle.Event.ON_RESUME,
        onEvent = viewmodel::onUpdateCheck
    )

    ModalNavigationDrawer(
        drawerState = drawer,
        drawerContent = {
            DrawerNoteList(
                drawer = drawer,
                scope = scope,
                onPinLock = {
                    navigator.navigate(PinLockScreenDestination)
                },
                onLanguage = {
                    if (BuildUtil.atLeastTiramisu()) context.navigateAppLocaleSettings()
                },
                onSupport = {
                    navigator.navigate(SupportScreenDestination)
                },
                onShare = {
                    Helper.shareApp(context)
                },
                onRate = {
                    ConnectUtil.viewAppPlayStorePage(context)
                },
                onSourceCode = {
                    Helper.viewSourceCode(context)
                },
                onAbout = {
                    navigator.navigate(AboutScreenDestination)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = {
                        TextPlain(
                            id = R.string.app_name
                        )
                    },
                    navigationIcon = {
                        IconButtonPlain(
                            onClick = {
                                scope.launch {
                                    drawer.open()
                                }
                            },
                            icon = Icons.Rounded.Menu,
                            contentDescription = R.string.cd_open_drawer
                        )
                    },
                    actions = {
                        TopBarMoreMenuNoteList(
                            expanded = viewmodel.menuExpanded,
                            onExpand = viewmodel::onMenuExpand,
                            onDismiss = viewmodel::onMenuCollapse,
                            autoSave = autoSave,
                            onAutoSave = viewmodel::onAutoSaveChange,
                            onDeleteAll = viewmodel::onDeleteAllShow,
                            onPinLock = {
                                navigator.navigate(PinLockScreenDestination)
                                viewmodel.onMenuCollapse()
                            },
                            onLanguage = {
                                if (BuildUtil.atLeastTiramisu()) context.navigateAppLocaleSettings()
                                viewmodel.onMenuCollapse()
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                NoteFloatingActionButton(
                    onClick = {
                        navigator.navigate(NoteAddScreenDestination)
                    },
                    icon = Icons.Rounded.Add,
                    contentDescription = R.string.cd_add_note
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { padding ->
            if (notes.itemCount > 0) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    contentPadding = padding,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalItemSpacing = 10.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            end = 10.dp
                        )
                ) {
                    items(
                        count = notes.itemCount,
                        key = notes.itemKey(
                            key = { note ->
                                note.id!!
                            }
                        ),
                        contentType = notes.itemContentType()
                    ) { index ->
                        val note = notes[index]
                        if (note != null) {
                            CardNote(
                                note = note,
                                onClick = {
                                    navigator.navigate(NoteEditScreenDestination(id = it.id!!))
                                }
                            )
                        }
                    }
                }
            } else {
                TextInfo(
                    icon = R.drawable.ic_bulb,
                    message = R.string.empty_notes_message
                )
            }
            if (shouldShowEmptyState) {
                TextInfo(
                    icon = R.drawable.ic_bulb,
                    message = R.string.empty_notes_message
                )
            }
            if (viewmodel.deleteAllShown) {
                NoteAlertDialog(
                    title = R.string.confirm_deletion,
                    message = R.string.wanna_delete_all,
                    onDismiss = viewmodel::onDeleteAllDismiss,
                    onConfirm = viewmodel::onDeleteAll
                )
            }
            DialogReview(
                visible = viewmodel.reviewShown,
                onDismiss = viewmodel::onReviewDismiss,
                onDeny = viewmodel::onReviewDeny,
                onRemindLater = viewmodel::onReviewLater,
                onReview = viewmodel::onReviewConfirm
            )
            DialogUpdateAvailable(
                type = viewmodel.updateAvailableType,
                onDismiss = viewmodel::onUpdateAvailableDismiss,
                onConfirm = viewmodel::onUpdateAvailableConfirm
            )
            DialogUpdateDownloaded(
                visible = viewmodel.updateDownloadedShown,
                onDismiss = viewmodel::onUpdateDownloadedDismiss,
                onConfirm = viewmodel::onUpdateInstall
            )
        }
    }
}