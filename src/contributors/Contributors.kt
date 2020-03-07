package contributors

import contributors.Contributors.LoadingStatus.*
import contributors.Variant.*
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import tasks.*
import java.awt.event.ActionListener
import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext

enum class Variant {
//    BLOCKING,         // Request1Blocking
//    BACKGROUND,       // Request2Background
//    CALLBACKS,        // Request3Callbacks
//    SUSPEND,          // Request4Coroutine
//    CONCURRENT,       // Request5Concurrent
//    NOT_CANCELLABLE,  // Request6NotCancellable
//    PROGRESS,         // Request6Progress
    CHANNELS,          // Request7Channels
    FLOWS
}

interface Contributors: CoroutineScope {

    val job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun init() {
        // Start a new loading on 'load' click
        addLoadListener {
            saveParams()
            loadContributors()
        }

        // Save preferences and exit on closing the window
        addOnWindowClosingListener {
            job.cancel()
            saveParams()
            System.exit(0)
        }

        // Load stored params (user & password values)
        loadInitialParams()
    }

    fun loadContributors() {
        val (username, password, org, _) = getParams()
        val req = RequestData(username, password, org)

        clearResults()
        val service = createGithubService(req.username, req.password)

        val startTime = System.currentTimeMillis()
        when (getSelectedVariant()) {
            CHANNELS -> {  // Performing requests concurrently and showing progress
                launch(Dispatchers.Default) {
                    loadContributorsChannels(service, req) { users, completed ->
                        withContext(Dispatchers.Main) {
                            updateResults(users, startTime, completed)
                        }
                    }
                }.setUpCancellation()
            }
            FLOWS -> {
                launch(Dispatchers.Default) {
                    loadContributorsFlows(service, req) { users, completed ->
                        withContext(Dispatchers.Main) {
                            updateResults(users, startTime, completed)
                        }
                    }
                }.setUpCancellation()
            }
        }
    }

    private enum class LoadingStatus { COMPLETED, CANCELED, IN_PROGRESS }

    private fun clearResults() {
        updateContributors(listOf())
        updateLoadingStatus(IN_PROGRESS)
        setActionsStatus(newLoadingEnabled = false)
    }

    private fun updateResults(
        users: List<User>,
        startTime: Long,
        completed: Boolean = true
    ) {
        updateContributors(users)
        updateLoadingStatus(if (completed) COMPLETED else IN_PROGRESS, startTime)
        if (completed) {
            setActionsStatus(newLoadingEnabled = true)
        }
    }

    private fun updateLoadingStatus(
        status: LoadingStatus,
        startTime: Long? = null
    ) {
        val time = if (startTime != null) {
            val time = System.currentTimeMillis() - startTime
            "${(time / 1000)}.${time % 1000 / 100} sec"
        } else ""

        val text = "Loading status: " +
                when (status) {
                    COMPLETED -> "completed in $time"
                    IN_PROGRESS -> "in progress $time"
                    CANCELED -> "canceled"
                }
        setLoadingStatus(text, status == IN_PROGRESS)
    }

    private fun Job.setUpCancellation() {
        // make active the 'cancel' button
        setActionsStatus(newLoadingEnabled = false, cancellationEnabled = true)

        val loadingJob = this

        // cancel the loading job if the 'cancel' button was clicked
        val listener = ActionListener {
            loadingJob.cancel()
            updateLoadingStatus(CANCELED)
        }
        addCancelListener(listener)

        // update the status and remove the listener after the loading job is completed
        launch {
            loadingJob.join()
            setActionsStatus(newLoadingEnabled = true)
            removeCancelListener(listener)
        }
    }

    fun loadInitialParams() {
        setParams(loadStoredParams())
    }

    fun saveParams() {
        val params = getParams()
        if (params.username.isEmpty() && params.password.isEmpty()) {
            removeStoredParams()
        }
        else {
            saveParams(params)
        }
    }

    fun getSelectedVariant(): Variant

    fun updateContributors(users: List<User>)

    fun setLoadingStatus(text: String, iconRunning: Boolean)

    fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false)

    fun addCancelListener(listener: ActionListener)

    fun removeCancelListener(listener: ActionListener)

    fun addLoadListener(listener: () -> Unit)

    fun addOnWindowClosingListener(listener: () -> Unit)

    fun setParams(params: Params)

    fun getParams(): Params
}
