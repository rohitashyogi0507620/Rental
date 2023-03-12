package com.example.mapwork.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity() {

    private var _binding: T? = null
    protected val binding get() = _binding!!
    protected val viewModel by lazy { buildViewModel() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }

        _binding = DataBindingUtil.setContentView(this, getLayoutId())

        getBundle()
        initViews()
        initLiveDataObservers()
        onClickListener()

    }


    protected abstract fun getLayoutId(): Int
    protected abstract fun buildViewModel(): V

    open fun getBundle() {

    }

    open fun initViews() {

    }

    open fun initLiveDataObservers() {

    }

    open fun onClickListener() {

    }
}