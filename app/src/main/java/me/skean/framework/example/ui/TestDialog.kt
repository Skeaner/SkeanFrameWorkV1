package me.skean.framework.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.hi.dhl.binding.viewbind
import me.skean.framework.example.R
import me.skean.framework.example.databinding.ActivityTestBinding
import me.skean.framework.example.databinding.DialogTestBinding
import me.skean.skeanframework.component.BaseDialogFragment
import me.skean.skeanframework.component.FullDialogFragment

/**
 * Created by Skean on 19/7/3.
 */
class TestDialog : FullDialogFragment() {
    private val vb: DialogTestBinding by viewbind()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  vb.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}