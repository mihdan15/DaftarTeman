package com.mihdan.daftarteman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_data.*

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHp: String? = null


    private fun getJkel(): String {
        var gender = ""
        if (lakilaki.isChecked) {
            gender = "Laki-Laki"
        } else if (perempuan.isChecked) {
            gender = "Perempuan"
        }
        return gender
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data
        update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cekNama = new_nama.getText().toString()
                cekAlamat = new_alamat.getText().toString()
                cekNoHp = new_nohp.getText().toString()
                val getJkel: String = getJkel()

                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHp!!) || isEmpty(getJkel!!)) {
                    Toast.makeText(this@UpdateData, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val setdata_teman = data_teman()
                    setdata_teman.nama = new_nama.getText().toString()
                    setdata_teman.alamat = new_alamat.getText().toString()
                    setdata_teman.no_hp = new_nohp.getText().toString()
                    setdata_teman.jkel = getJkel()

                    updateTeman(setdata_teman)
                }
            }
        })
    }
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }
    private val data: Unit
        private get() {
            val getNama = intent.extras!!.getString("dataNama")
            val getAlamat = intent.extras!!.getString("dataAlamat")
            val getNoHp = intent.extras!!.getString("dataNoHp")
            val getJkel = intent.extras!!.getString("dataJkel")

            new_nama!!.setText(getNama)
            new_alamat!!.setText(getAlamat)
            new_nohp!!.setText(getNoHp)

            if (getJkel == "Laki-Laki") {
                lakilaki.isChecked = true
            } else if (getJkel == "Perempuan") {
                perempuan.isChecked = true
            }
        }

    private fun updateTeman(setdataTeman: data_teman) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("DataTeman")
            .child(getKey!!)
            .setValue(setdataTeman)
            .addOnSuccessListener {
                new_nama!!.setText("")
                new_alamat!!.setText("")
                new_nohp!!.setText("")
                lakilaki.isChecked = false
                perempuan.isChecked = false

                Toast.makeText(this@UpdateData, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this@UpdateData, "Data gagal diubah", Toast.LENGTH_SHORT).show()
            }
    }
}