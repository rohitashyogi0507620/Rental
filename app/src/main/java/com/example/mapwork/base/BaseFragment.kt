package com.example.mapwork.base

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapwork.utils.SessionConstants
import com.example.mapwork.utils.SessionManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.File


abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!
    protected val viewModel by lazy { buildViewModel() }


    lateinit var progessLayout: ProgressBar
    lateinit var sessionManager: SessionManager
    val gson by lazy { Gson() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progessLayout = setProgressDialog()
        sessionManager = SessionManager()
        sessionManager.setContext(SessionConstants.APPLICATION_STATE, requireContext())

        getBundle()
        initViews()
        initLiveDataObservers()
        onClickListener()


    }

    override fun onDestroyView() {
        LogD("STATE", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun buildViewModel(): V
    protected abstract fun setProgressDialog(): ProgressBar

    open fun getBundle() {

    }

    open fun initViews() {

    }

    open fun initLiveDataObservers() {

    }

    open fun onClickListener() {

    }

    fun ToastShort(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
    }

    fun ToastLong(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
    }

    fun SnackbarLong(v: View, message: String) {
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show()
    }

    fun SnackbarShort(v: View, message: String) {
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }

    fun LogD(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun LogE(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun progressDialog(isShowing: Boolean) {

        if (progessLayout != null) {
            if (isShowing){
                progessLayout.visibility=View.VISIBLE
            }else{
                progessLayout.visibility=View.GONE
            }
        }
    }

   fun showErrorDialog(errormessage:String){
       ToastShort(errormessage)
   }


}