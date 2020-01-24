package com.epam.epmrduacmvan.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.epam.epmrduacmvan.Constants.Companion.EVENT
import com.epam.epmrduacmvan.IntentRequestCodes.Companion.REQUEST_PICK_IMAGE
import com.epam.epmrduacmvan.PermissionsRequestCodes.Companion.REQUEST_READ_EXTERNAL_STORAGE_FOR_PICK_IMAGE
import com.epam.epmrduacmvan.PermissionsRequestCodes.Companion.REQUEST_READ_EXTERNAL_STORAGE_FOR_YOUTUBE_LINK
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FullEventInfoFilesFragment : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var fabMenuContainer: LinearLayout
    private lateinit var addResourcesFab: FloatingActionButton
    private lateinit var addImageFab: FloatingActionButton
    private lateinit var addYoutubeLinkFab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_full_event_info_files, container, false)

        //init UI components
        fabMenuContainer = rootView.findViewById(R.id.fab_menu_container)
        addResourcesFab = rootView.findViewById(R.id.add_resources_fab)
        addImageFab = rootView.findViewById(R.id.add_image)
        addYoutubeLinkFab = rootView.findViewById(R.id.add_youtube_link)

        //set listeners
        addResourcesFab.setOnClickListener(this)
        addImageFab.setOnClickListener(this)
        addYoutubeLinkFab.setOnClickListener(this)

        addResourcesFab.isVisible = (activity as FullEventInfoActivity).eventForDisplay.attendeeType == Event.ORGANIZER

        return rootView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_resources_fab -> showFabMenu()
            R.id.add_youtube_link -> startActivityEnterYoutubeLink()
            R.id.add_image -> if (checkReadExternalStoragePermission()) pickImage() else requestReadExternalStoragePermission(REQUEST_READ_EXTERNAL_STORAGE_FOR_PICK_IMAGE)
        }
    }

    private fun startActivityEnterYoutubeLink() {
        val intent = Intent(this.context, ProvideYoutubeLinkActivity::class.java)
        intent.putExtra(EVENT, (activity as FullEventInfoActivity).eventForDisplay.id)
        startActivity(intent)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("file/*")
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    private fun makePhoto() {
    }

    private fun showFabMenu() {
        TransitionManager.beginDelayedTransition(fabMenuContainer)
        fabMenuContainer.isVisible = !fabMenuContainer.isVisible
    }

    private fun checkReadExternalStoragePermission(): Boolean {
        return PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(rootView.context, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun requestReadExternalStoragePermission(requestCode: Int) {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if ((grantResults.isNotEmpty()) and (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            when (requestCode) {
                REQUEST_READ_EXTERNAL_STORAGE_FOR_PICK_IMAGE -> pickImage()
                REQUEST_READ_EXTERNAL_STORAGE_FOR_YOUTUBE_LINK -> startActivityEnterYoutubeLink()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}