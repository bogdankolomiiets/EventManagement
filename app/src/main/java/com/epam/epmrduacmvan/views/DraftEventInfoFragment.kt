package com.epam.epmrduacmvan.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.Constants.Companion.CITY
import com.epam.epmrduacmvan.IntentRequestCodes
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.LanguageRecycleViewAdapter
import com.epam.epmrduacmvan.model.*
import com.epam.epmrduacmvan.utils.LanguageItemClickListener
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel
import kotlinx.android.synthetic.main.fragment_draft_event_info.view.*
import java.text.SimpleDateFormat
import java.util.*

class DraftEventInfoFragment : Fragment(), View.OnClickListener, LanguageItemClickListener {
    private lateinit var eventTemplate: Event.Builder
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private lateinit var category: EditText
    private lateinit var city: EditText
    private lateinit var isPrivateEvent: CheckBox
    private lateinit var eventDescription: EditText
    private lateinit var eventAddress: EditText
    private lateinit var languageRecyclerView: RecyclerView
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_draft_event_info, container, false)

        eventTemplate = (activity as NewEventActivity).eventTemplate

        category = root.findViewById(R.id.select_category)
        category.setOnClickListener(this)

        city = root.findViewById(R.id.select_city)
        city.setOnClickListener(this)

        isPrivateEvent = root.findViewById(R.id.is_private_event)
        isPrivateEvent.setOnClickListener(this)

        eventDescription = root.findViewById(R.id.event_description)
        eventDescription.doAfterTextChanged { eventTemplate.description = it.toString() }

        eventAddress = root.findViewById(R.id.event_address)
        eventAddress.doAfterTextChanged { eventTemplate.address = it.toString() }

        languageRecyclerView = root.findViewById(R.id.language_recyclerview)
        languageRecyclerView.setHasFixedSize(true)
        languageRecyclerView.layoutManager = LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)

        val languageAdapter = LanguageRecycleViewAdapter(this)
        languageRecyclerView.adapter = languageAdapter

        ViewModelProviders.of(this).get(AdditionalDataViewModel::class.java).getLanguages()
            .observe(this, androidx.lifecycle.Observer {
                languageAdapter.setData(it)
            })

        val calendarStartDate = Calendar.getInstance()
        val startYear = calendarStartDate.get(Calendar.YEAR)
        val startMonth = calendarStartDate.get(Calendar.MONTH).plus(1)
        val startDay = calendarStartDate.get(Calendar.DAY_OF_MONTH)

        val calendarEndDate = Calendar.getInstance()
        val endYear = calendarEndDate.get(Calendar.YEAR)
        val endMonth = calendarEndDate.get(Calendar.MONTH).plus(1)
        val endDay = calendarEndDate.get(Calendar.DAY_OF_MONTH)

        root.start_date_text.text = getString(R.string.date_concatenation, startDay, startMonth, startYear)
        root.text_start_time.text = timeFormat.format(calendarStartDate.time)

        root.end_date_text.text = getString(R.string.date_concatenation, endDay, endMonth, endYear)
        root.text_end_time.text = timeFormat.format(calendarEndDate.time)

        root.relativeLayout_start.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendarStartDate.set(Calendar.HOUR_OF_DAY, hour)
                calendarStartDate.set(Calendar.MINUTE, minute)
                root.text_start_time.text = timeFormat.format(calendarStartDate.time)

                if (calendarEndDate.timeInMillis < calendarStartDate.timeInMillis) {
                    root.text_end_time.text = timeFormat.format(calendarStartDate.time)
                    calendarEndDate.timeInMillis = calendarStartDate.timeInMillis
                    eventTemplate.finishDateTime = calendarStartDate.timeInMillis
                }

                //set start time for event
                eventTemplate.startDateTime = calendarStartDate.timeInMillis
            }

            TimePickerDialog(root.context, R.style.DateTimePickerStyle, timeSetListener, calendarStartDate.get(Calendar.HOUR_OF_DAY),
                calendarStartDate.get(Calendar.MINUTE), true).show()

            val dpd = DatePickerDialog(root.context, R.style.DateTimePickerStyle, DatePickerDialog.OnDateSetListener { _, yearOf, monthOfYear, dayOfMonth ->
                //set start date for event
                calendarStartDate.set(yearOf, monthOfYear, dayOfMonth)

                // Display Selected date in TextView
                root.start_date_text.text = getString(R.string.date_concatenation, dayOfMonth, monthOfYear.inc(), yearOf)

                if (calendarEndDate.timeInMillis < calendarStartDate.timeInMillis) {
                    root.end_date_text.text = getString(R.string.date_concatenation, dayOfMonth, monthOfYear.inc(), yearOf)
                    calendarEndDate.timeInMillis = calendarStartDate.timeInMillis
                    eventTemplate.finishDateTime = calendarStartDate.timeInMillis
                }

                eventTemplate.startDateTime = calendarStartDate.timeInMillis
            }, startYear, startMonth.dec(), startDay)

            dpd.show()
        }

        root.relativeLayout_finish.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendarEndDate.set(Calendar.HOUR_OF_DAY, hour)
                calendarEndDate.set(Calendar.MINUTE, minute)
                root.text_end_time.text = timeFormat.format(calendarEndDate.time)

                if (calendarEndDate.timeInMillis < calendarStartDate.timeInMillis) {
                    root.text_start_time.text = timeFormat.format(calendarEndDate.time)
                    calendarStartDate.timeInMillis = calendarEndDate.timeInMillis
                    eventTemplate.startDateTime = calendarStartDate.timeInMillis
                }

                //set end time for event
                eventTemplate.finishDateTime = calendarEndDate.timeInMillis
            }

            TimePickerDialog(root.context, R.style.DateTimePickerStyle, timeSetListener, calendarEndDate.get(Calendar.HOUR_OF_DAY),
                calendarEndDate.get(Calendar.MINUTE), true).show()

            val dpd = DatePickerDialog(root.context, R.style.DateTimePickerStyle, DatePickerDialog.OnDateSetListener { _, yearOf, monthOfYear, dayOfMonth ->
                //set end date for event
                calendarEndDate.set(yearOf, monthOfYear, dayOfMonth)

                // Display Selected date in TextView
                root.end_date_text.text = getString(R.string.date_concatenation, dayOfMonth, monthOfYear.inc(), yearOf)

                if (calendarEndDate.timeInMillis < calendarStartDate.timeInMillis) {
                    root.start_date_text.text = getString(R.string.date_concatenation, dayOfMonth, monthOfYear.inc(), yearOf)
                    calendarStartDate.timeInMillis = calendarEndDate.timeInMillis
                    eventTemplate.startDateTime = calendarStartDate.timeInMillis
                }

                eventTemplate.finishDateTime = calendarEndDate.timeInMillis
            }, endYear, endMonth.dec(), endDay)

            dpd.show()
        }

        return root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.select_city -> startActivityForResult(
                Intent(root.context, CitySearchActivity::class.java),
                IntentRequestCodes.REQUEST_CITY)
            R.id.select_category -> startActivityForResult(
                Intent(root.context, CategorySearchActivity::class.java),
                IntentRequestCodes.REQUEST_CATEGORY)
            R.id.is_private_event -> {
                eventTemplate.type = if ((view as CheckBox).isChecked) Event.TYPE_PRIVATE else Event.TYPE_PUBLIC
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                IntentRequestCodes.REQUEST_CITY -> {
                    val parcelableExtra = data?.getParcelableExtra<City>(CITY)

                    if (parcelableExtra != null) {
                        city.setText(parcelableExtra.name)
                        eventTemplate.city = parcelableExtra
                        eventTemplate.country = parcelableExtra.country
                    }
                }
                IntentRequestCodes.REQUEST_CATEGORY -> {
                    val parcelableExtra = data?.getParcelableExtra<Category>(Constants.CATEGORY)

                    if (parcelableExtra != null) {
                        category.setText(parcelableExtra.name)
                        eventTemplate.category = parcelableExtra
                    }
                }
            }
        }
    }

    override fun onItemClick(language: Language) {
        eventTemplate.language = language
    }
}