package jp.techacademy.takaomi.okabe.mainactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_web_view.*
import android.util.Log
import androidx.core.text.isDigitsOnly
import java.util.*

class WebViewActivity: AppCompatActivity(){

    // 一覧画面から登録するときのコールバック（FavoriteFragmentへ通知するメソッド)
    var onClickAddFavorite: ((Shop) -> Unit)? = null
    // 一覧画面から削除するときのコールバック（ApiFragmentへ通知するメソッド)
    var onClickDeleteFavorite: ((Shop) -> Unit)? = null

    var id: String = ""
    var imageUrl: String = ""
    var address: String = ""
    var name: String = ""
    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val intent = intent
        val shop = intent.getSerializableExtra(KEY_SHOP)

        if(shop is Shop){
            id = shop.id
//        Log.d("test", id.toString())
            address = shop.address
            name = shop.name
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        }
        webView.loadUrl(url)
//        webView.loadUrl(intent.getStringExtra(KEY_SHOP).toString())


        // お気に入り状態を取得
        val isFavorite = FavoriteShop.findBy(id) != null
        if (isFavorite) {
            Button1.text = "お気に入りから削除"
        } else {
            Button1.text = "お気に入りに登録"
        }

        // UI部品の設定
        Button1.setOnClickListener{
            val isFavorite = FavoriteShop.findBy(id) != null
            if (isFavorite) {
                FavoriteShop.delete(id)
                Button1.text = "お気に入りに登録"
            } else {
                if(shop is Shop) {
                    FavoriteShop.insert(FavoriteShop().apply {
                        id = shop.id
                        name = shop.name
                        address = shop.address
                        imageUrl = shop.logoImage
                        url =
                            if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
                    })
                }
                Button1.text = "お気に入りから削除"
            }
        }
    }


    companion object {
        private const val KEY_SHOP = "key_shop"
        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_SHOP, shop))
        }
    }

//    private fun updateWebViewItem(holder: FavoriteItemViewHolder, position: Int) {
//    }


}