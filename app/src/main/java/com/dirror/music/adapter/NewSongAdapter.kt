package com.dirror.music.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dirror.music.MyApplication
import com.dirror.music.R
import com.dirror.music.music.standard.data.StandardSongData
import com.dirror.music.ui.activity.PlayerActivity
import com.dirror.music.util.GlideUtil
import com.dirror.music.util.parseArtist

/**
 * 歌单适配器
 */
class NewSongAdapter(private val songDataList: ArrayList<StandardSongData>): RecyclerView.Adapter<NewSongAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val clTrack: ConstraintLayout = view.findViewById(R.id.clTrack)
        val ivCover: ImageView = view.findViewById(R.id.ivCover)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTrackCount: TextView = view.findViewById(R.id.tvTrackCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_playlist, parent, false).apply {
            return ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songData = songDataList[position]
        holder.apply {
            songData.imageUrl?.let { GlideUtil.load(it, ivCover) }
            tvName.text = songData.name
            tvTrackCount.text = songData.artists?.let { parseArtist(it) }
            clTrack.setOnClickListener {
                playMusic(songData, it)
            }
        }
    }

    override fun getItemCount(): Int {
        return songDataList.size
    }

    /**
     * 播放音乐
     */
    private fun playMusic(songData: StandardSongData, view: View?) {
        // 歌单相同
        if (MyApplication.musicController.value?.getPlaylist() == songDataList) {
            // position 相同
            if (songData == MyApplication.musicController.value?.getPlayingSongData()?.value) {
                if (view != null) {
                    view.context.startActivity(Intent(view.context, PlayerActivity::class.java))
                    (view.context as Activity).overridePendingTransition(
                        R.anim.anim_slide_enter_bottom,
                        R.anim.anim_no_anim
                    )
                }
            } else {
                MyApplication.musicController.value?.playMusic(songData)
            }
        } else {
            // 设置歌单
            MyApplication.musicController.value?.setPlaylist(songDataList)
            // 播放歌单
            MyApplication.musicController.value?.playMusic(songData)
        }
    }

}