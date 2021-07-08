package ru.butterbean.easyrent.screens.archive_reserve

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentArchiveReserveBinding
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.ext_files.ExtFilesExtensionFragment
import ru.butterbean.easyrent.utils.*

class ArchiveReserveFragment : ExtFilesExtensionFragment() {

    private var _binding: FragmentArchiveReserveBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: ArchiveReserveViewModel
    private lateinit var mCurrentReserve: ReserveArchiveData
    private lateinit var mCurrentRoom: RoomData
    private var mCurrentDateCheckIn = "" // date in format yyyy-MM-dd
    private var mCurrentDateCheckOut = "" // date in format yyyy-MM-dd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveReserveBinding.inflate(layoutInflater, container, false)
        mCurrentReserve = arguments?.getSerializable("reserve") as ReserveArchiveData
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.restore_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            R.id.restore -> {
                mViewModel.replaceReserveFromArchive(mCurrentReserve) { reserve ->
                    APP_ACTIVITY.navController.navigate(
                        R.id.action_archiveReserveFragment_to_editReserveFragment,
                        createArgsBundle("reserve", reserve)
                    )
                }
                true
            }
            R.id.delete -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage(getString(R.string.finally_reserve_delete_message))
            .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                dialog.cancel()
                mViewModel.deleteReserveArchive(mCurrentReserve) { goToArchiveReservesFragment() }
            }
            .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun goToArchiveReservesFragment() {
        APP_ACTIVITY.navController.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun changeExtFilesButtonsVisibility() {
        mViewModel.getExtFilesCount(mCurrentReserve.id) { setExtFilesButtonsVisibility(it) }
    }

    private fun setExtFilesButtonsVisibility(filesCount: Int) {
        when (filesCount) {
            0 -> {
                mBinding.editReserveBtnShowFiles.visibility = View.GONE
                mBinding.editReserveBtnShowSingleFile.visibility = View.GONE
            }
            1 -> {
                mBinding.editReserveBtnShowFiles.visibility = View.GONE
                mBinding.editReserveBtnShowSingleFile.visibility = View.VISIBLE
                mBinding.editReserveBtnShowSingleFile.setSingleExtFileImage(this)
            }
            else -> {
                mBinding.editReserveBtnShowFiles.visibility = View.VISIBLE
                mBinding.editReserveBtnShowSingleFile.visibility = View.GONE
                mBinding.editReserveBtnShowFilesText.text = filesCount.toString()
            }
        }
    }

    private fun initialize() {
        APP_ACTIVITY.title = "${getString(R.string.archive)}. ${getString(R.string.reserve)}"
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(ArchiveReserveViewModel::class.java)

        APP_ACTIVITY.title = "${getString(R.string.archive)}. ${getString(R.string.reserve)}"

        // получим модель помещения и его название из БД
        mViewModel.getRoomById(mCurrentReserve.roomId) { room ->
            mCurrentRoom = room
            mBinding.editReserveRoomName.text = room.name
        }

        changeExtFilesButtonsVisibility()

        mBinding.editReserveGuest.text = mCurrentReserve.guestName
        mBinding.editReservePhoneNumber.hint = "+${getCountryZipCode()} XXX XXX-XX-XX"
        mBinding.editReservePhoneNumber.text = mCurrentReserve.phoneNumber
        mBinding.editReserveSum.text = mCurrentReserve.sum.toString()
        mBinding.editReservePayment.text = mCurrentReserve.payment.toString()
        mBinding.editReserveWasCheckIn.isChecked = mCurrentReserve.wasCheckIn
        mBinding.editReserveWasCheckOut.isChecked = mCurrentReserve.wasCheckOut
        if (mCurrentReserve.dateCheckIn.isNotEmpty()) {
            mCurrentDateCheckIn = mCurrentReserve.dateCheckIn.substring(0, 10)
            mBinding.editReserveDateCheckIn.text = mCurrentReserve.dateCheckIn.toDateFormat(false)
            mBinding.editReserveTimeCheckIn.text = mCurrentReserve.dateCheckIn.toTimeFormat()
        }
        if (mCurrentReserve.dateCheckOut.isNotEmpty()) {
            mCurrentDateCheckOut = mCurrentReserve.dateCheckOut.substring(0, 10)
            mBinding.editReserveDateCheckOut.text = mCurrentReserve.dateCheckOut.toDateFormat(false)
            mBinding.editReserveTimeCheckOut.text = mCurrentReserve.dateCheckOut.toTimeFormat()
        }
        // guest count
        mBinding.editReserveGuestsCount.text = mCurrentReserve.guestsCount.toString()

        mBinding.editReserveBtnPhoneCall.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mBinding.editReservePhoneNumber.text.toString())
                )
            startActivity(intent)
        }

        mBinding.editReserveBtnPhoneWhatsapp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://api.whatsapp.com/send?phone=" + mBinding.editReservePhoneNumber.text.toString())
            startActivity(intent)
        }

        mBinding.editReserveBtnPhoneSms.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + mBinding.editReservePhoneNumber.text.toString())
                )
            startActivity(intent)
        }

        changePhoneButtonsInEnabled()

        // добавляем меню
        setHasOptionsMenu(true)

        mBinding.editReserveBtnShowSingleFile.setOnClickListener {
            getSingleExtFileParams { extFileParams ->
                startAnyApp(extFileParams)
            }
        }

        mBinding.editReserveBtnShowFiles.setOnClickListener {
            APP_ACTIVITY.navController.navigate(
                R.id.action_archiveReserveFragment_to_extFilesArchiveListFragment,
                createArgsBundle("reserve", mCurrentReserve)
            )
        }
    }

    private fun changePhoneButtonsInEnabled() {
        val vis = !phoneNumberIsEmpty(mBinding.editReservePhoneNumber.text.toString())
        mBinding.editReserveBtnPhoneCall.isVisible = vis
        mBinding.editReserveBtnPhoneWhatsapp.isVisible = vis
        mBinding.editReserveBtnPhoneSms.isVisible = vis
    }

    override fun getSingleExtFileParams(f: (Bundle) -> Unit) {
        mViewModel.getSingleExtFileByReserveId(mCurrentReserve.id) { extFile ->
            f(extFile.getParamsBundle())
        }
    }
}