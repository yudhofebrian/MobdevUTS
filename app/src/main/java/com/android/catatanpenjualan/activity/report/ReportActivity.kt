package com.android.catatanpenjualan.activity.report

import android.os.Bundle
import android.util.Log.d
import com.android.catatanpenjualan.R
import com.android.catatanpenjualan.activity.report.adapter.ReportAdapter
import com.android.catatanpenjualan.activity.report.detail.DetailReportFragment
import com.android.catatanpenjualan.activity.report.presenter.ReportPresenter
import com.android.catatanpenjualan.activity.report.presenter.ReportView
import com.android.catatanpenjualan.base.BaseActivity
import com.android.catatanpenjualan.model.Keranjang
import com.android.catatanpenjualan.model.KeranjangStatus
import kotlinx.android.synthetic.main.activity_report.*
import org.jetbrains.anko.toast

class ReportActivity : BaseActivity(), ReportView {
//    override fun onListFragmentInteraction(item: Penjualan?) {
//
//    }

    private lateinit var presenter: ReportPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        cekSesi(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        presenter = ReportPresenter(this)
        presenter.getReportAll(user?.idUser)
    }

    override fun onSuccessReport(keranjang: List<Keranjang?>?) {
        rvReport.adapter = ReportAdapter(keranjang, object : ReportAdapter.OnClick {
            override fun click(keranjang: Keranjang?, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("penjualan", keranjang)

                val detailReportFragment = DetailReportFragment()
                detailReportFragment.arguments = bundle

                detailReportFragment.show(supportFragmentManager, "Report")
            }

            override fun restore(keranjang: Keranjang?) {
                presenter.restoreStatus(user?.idUser, Integer.parseInt(keranjang?.idKeranjang),
                    KeranjangStatus.PENDING.status, Integer.parseInt(keranjang?.qty), keranjang?.totalHarga)
            }
        })
    }

    override fun onFailedReport(localizedMessage: String?) {
        d("ReportActivity", localizedMessage)
    }

    override fun onSuccessRestore(msg: String?) {
        toast("Berhasil membatalkan penjualan").show()
        presenter.getReportAll(user?.idUser)
    }

    override fun onFailedRestore(msg: String?) {
        d("ReportActivity", msg)
    }

}
