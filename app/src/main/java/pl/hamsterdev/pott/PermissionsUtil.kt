package pl.hamsterdev.pott

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

object PermissionsUtil {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    fun grandPermissions(activity: MainActivity) {
        requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                Snackbar.make(
                    activity.findViewById<View>(android.R.id.content).rootView,
                    R.string.no_permissions_message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        val permissionStatusCode = ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)

        if (permissionStatusCode != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}