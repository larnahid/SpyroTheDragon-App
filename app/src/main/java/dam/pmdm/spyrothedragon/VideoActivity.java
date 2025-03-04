package dam.pmdm.spyrothedragon;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.easter_egg_video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Cerrar la actividad cuando termine el video
        videoView.setOnCompletionListener(mp -> finish());

        videoView.start();
    }
}
