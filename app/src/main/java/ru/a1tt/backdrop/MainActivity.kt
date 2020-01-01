package ru.a1tt.backdrop

import android.content.pm.PackageInfo
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.a1tt.backdrop.MainApplication.Companion.appsIcons
import ru.a1tt.backdrop.MainApplication.Companion.getBitmapFromVectorDrawable
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = AppsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val data = AppsViewModel().data
        data.observe(this, Observer {
            it?.let {list ->
                val diff = DiffUtil.calculateDiff(
                    DiffCalculator(adapter.list, list)
                )
                adapter.list = list
                adapter.appsIcons = appsIcons
                diff.dispatchUpdatesTo(adapter)
                progress_bar.visibility = GONE
            }
        })

        Thread(Runnable {
            val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
            val apps: ArrayList<TargetApplication> = ArrayList(packs.size)
            for (pack in packs) {
                apps.add(TargetApplication(
                    pack.applicationInfo.loadLabel(packageManager) as String,
                    pack.packageName))
                appsIcons[pack.packageName] = getBitmapFromVectorDrawable(pack.applicationInfo.loadIcon(packageManager))
            }
            data.postValue(apps)
        }).start()

        with(toolbar) {
            setTitle(R.string.app_name)
            val params = layoutParams
            params.height = 200
            layoutParams = params
        }

        val rectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top

        val mBackDrop = BackDrop(
            backDrop,
            frontLayout,
            Color.WHITE,
            resources.getDimensionPixelSize(R.dimen.foreground_radius).toFloat(),
            backLayout,
            toolbar,
            resources,
            statusBarHeight)

        mBackDrop.setRecyclerView(recyclerView)

        navigationView.setNavigationItemSelectedListener {
            mBackDrop.close()
            true
        }
    }
}
