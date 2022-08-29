package com.nongboo.flowery

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.View.inflate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nongboo.flowery.databinding.ActivityLoginBinding.inflate
import com.nongboo.flowery.databinding.ActivityMainBinding
import com.nongboo.flowery.viewmodel.MyViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    private lateinit var viewModel: MyViewModel
    private lateinit var adapter: MyAdapter

    //오늘 날짜
    private val now = System.currentTimeMillis() //현재 시간 가져오기
    private val D_now = Date(now) //현재 시간을 date 타입으로 변환
    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko","kr"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        observerViewModel()

        auth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser

        //오늘 날짜 출력
        binding.dateText.text = dateFormat.format(D_now) //현재 시간을 dateFormat에 선언한 형태의 string으로 변환

        //달력 날짜
        binding.calendarView.setOnDateChangeListener{ calendarView, year, month, day ->
            // 날짜 선택시 그 날의 정보 할당
            binding.dateText.text ="${year}년 ${month+1}월 ${day}일"
        }

        //사용자 프로필 사진 클릭했을 때
        binding.userImage.setOnClickListener{
            moveUserInformationPage()
        }

        //+버튼 눌렀을 때
        binding.floatingActionButton.setOnClickListener {
            //dialog 띄어서 아이템 추가
            ItemDialog().show(supportFragmentManager, "ItemDialog")
        }

        adapter = MyAdapter(viewModel)
        binding.recyclerView.adapter = adapter //RecyclerView와 Adapter 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)//아이템마다 각 높이를 일정하게

        registerForContextMenu(binding.recyclerView) //컨텍스트 메뉴 등록하는법

    }

    private fun observerViewModel() {
        viewModel.apply {
            itemsData.observe(this@MainActivity) {

                val percentData= "${this.getItemsPercent()}% 완료"
                val builder = SpannableStringBuilder(percentData)
                builder.setSpan(ForegroundColorSpan(Color.parseColor("#19A763")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.todoPercent.text = builder
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.ctx_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit -> ItemDialog(viewModel.longClickItem).show(
                supportFragmentManager,
                "ItemDialog",
            )
            R.id.delete -> {
                viewModel.deleteItem(viewModel.longClickItem)

            }
            else -> return super.onContextItemSelected(item)
        }
        return true

    }

    //사용자 정보 액티비티 호출
    private fun moveUserInformationPage(){
        startActivity(Intent(this,UserInformationActivity::class.java))
        finish()
    }
}


