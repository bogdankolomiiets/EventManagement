package com.epam.epmrduacmvan.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.epam.epmrduacmvan.Constants.Companion.EVENT_ID
import com.epam.epmrduacmvan.IntentRequestCodes.Companion.REQUEST_IMAGE_FROM_CAMERA
import com.epam.epmrduacmvan.IntentRequestCodes.Companion.REQUEST_PICK_FILE
import com.epam.epmrduacmvan.IntentRequestCodes.Companion.REQUEST_PICK_IMAGE
import com.epam.epmrduacmvan.PermissionsRequestCodes.Companion.REQUEST_CAMERA
import com.epam.epmrduacmvan.PermissionsRequestCodes.Companion.REQUEST_READ_EXTERNAL_STORAGE_FOR_PICK_IMAGE
import com.epam.epmrduacmvan.PermissionsRequestCodes.Companion.REQUEST_READ_EXTERNAL_STORAGE_FOR_YOUTUBE_LINK
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.interfaces.EventShareable
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class FullEventInfoFilesFragment : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var fabMenuContainer: LinearLayout
    private lateinit var addResourcesFab: FloatingActionButton
    private lateinit var addImageFab: FloatingActionButton
    private lateinit var addYoutubeLinkFab: FloatingActionButton
    private lateinit var makePhoto: FloatingActionButton
    private lateinit var addFile: FloatingActionButton
    private lateinit var additionalDataViewModel: AdditionalDataViewModel
    private var event: Event? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_full_event_info_files, container, false)

        //init UI components
        fabMenuContainer = rootView.findViewById(R.id.fab_menu_container)
        addResourcesFab = rootView.findViewById(R.id.add_resources_fab)
        addImageFab = rootView.findViewById(R.id.add_image)
        addYoutubeLinkFab = rootView.findViewById(R.id.add_youtube_link)
        addFile = rootView.findViewById(R.id.add_file)
        makePhoto = rootView.findViewById(R.id.make_photo)

        //set listeners
        addResourcesFab.setOnClickListener(this)
        addImageFab.setOnClickListener(this)
        addYoutubeLinkFab.setOnClickListener(this)
        addFile.setOnClickListener(this)
        makePhoto.setOnClickListener(this)

        event = (activity as? EventShareable)?.getEvent()

        addResourcesFab.isVisible = Event.ORGANIZER == event?.attendeeType

        additionalDataViewModel = ViewModelProviders.of(this).get(AdditionalDataViewModel::class.java)

        return rootView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_resources_fab -> showFabMenu()
            R.id.add_youtube_link -> startActivityEnterYoutubeLink()
            R.id.add_file -> intentPickFile()
            R.id.add_image -> if (checkReadExternalStoragePermission()) intentPickImage() else requestReadExternalStoragePermission(REQUEST_READ_EXTERNAL_STORAGE_FOR_PICK_IMAGE)
            R.id.make_photo -> intentMakePhoto()
        }
    }

    private fun startActivityEnterYoutubeLink() {
        val intent = Intent(this.context, ProvideYoutubeLinkActivity::class.java)
        intent.putExtra(EVENT_ID, event?.id)
        startActivity(intent)
    }

    private fun intentPickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_PICK_IMAGE)
    }

    private fun intentPickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), REQUEST_PICK_FILE)
    }

    private fun intentMakePhoto() {
        if (PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(rootView.context, Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity?.packageManager?.let {
                if (intent.resolveActivity(it) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_FROM_CAMERA)
                }
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        }
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
                REQUEST_READ_EXTERNAL_STORAGE_FOR_PICK_IMAGE -> intentPickImage()
                REQUEST_READ_EXTERNAL_STORAGE_FOR_YOUTUBE_LINK -> startActivityEnterYoutubeLink()
                REQUEST_CAMERA -> intentMakePhoto()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_FILE,
                REQUEST_PICK_IMAGE,
                REQUEST_IMAGE_FROM_CAMERA -> {
                    data?.data?.let { uri ->
                        activity?.contentResolver?.let { contentResolver ->
                            val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
                            cursor?.let {
                                it.moveToFirst()
                                additionalDataViewModel.uploadArtifact(event?.id ?: 0, File(it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))))
                                it.close()
                            }
                        }
                    }
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

}