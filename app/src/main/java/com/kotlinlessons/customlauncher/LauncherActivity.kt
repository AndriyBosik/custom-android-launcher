package com.kotlinlessons.customlauncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kotlinlessons.customlauncher.fragments.LauncherFragment

class LauncherActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment = LauncherFragment.newInstance()

}