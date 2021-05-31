package top.bitleo.ijkdemo.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import top.bitleo.ijkdemo.R
import top.bitleo.ijkdemo.widget.media.IMediaController
import top.bitleo.ijkdemo.widget.media.IjkVideoView


class VideoActivity : AppCompatActivity(), IMediaController  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        ButterKnife.bind(this)

        // handle arguments

        // handle arguments
        mVideoPath = intent.getStringExtra("videoPath")

       mVideoPath?.let {


               mVideoView?.setMediaController(this)

               mVideoView?.setVideoPath(it)

       }

    }

    override fun onPause() {
        super.onPause()
        mVideoView?.stopPlayback()
    }

    override fun onResume() {
        super.onResume()
        mVideoView?.start()
    }


    private var mVideoPath: String? = null


    @JvmField @BindView(R.id.video_view)
    internal var mVideoView: IjkVideoView? = null
    override fun hide() {

    }

    override fun isShowing(): Boolean {

        return false
    }

    override fun setAnchorView(view: View?) {

    }

    override fun setEnabled(enabled: Boolean) {

    }

    override fun setMediaPlayer(player: MediaController.MediaPlayerControl?) {

    }

    override fun show(timeout: Int) {

    }

    override fun show() {

    }

    override fun showOnce(view: View?) {

    }
}