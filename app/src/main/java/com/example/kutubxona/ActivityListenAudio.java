package com.example.kutubxona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ActivityListenAudio extends AppCompatActivity {

    private Button playBtn, btnFastForward, btnRewind;
    private SeekBar seekBar;
    private TextView txtStart, txtStop;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private String filePath;
    TextView txt;
    ImageView img;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Завершение активности
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("kitobIdListen");
        String bookName = intent.getStringExtra("kitobName");
        String Img = intent.getStringExtra("kitobImage");
        txt=findViewById(R.id.text1);
        txt.setText(bookName);

        // Включаем кнопку "Назад"
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Аудио книга"); // Название заголовка
        }

        // Привязка элементов интерфейса
        playBtn = findViewById(R.id.playbtn);
        btnFastForward = findViewById(R.id.btnNext);
        btnRewind = findViewById(R.id.btnPrev);
        seekBar = findViewById(R.id.seek_bar);
        txtStart = findViewById(R.id.txtStart);
        txtStop = findViewById(R.id.txtStop);
        img = findViewById(R.id.song_icon);

        // Путь к аудиофайлу
        filePath = getFilesDir().getAbsolutePath() + "/myAudios/"+userId;

        // Проверяем существование файла
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            txtStart.setText("Файл не найден!");
            return;
        }

        // Инициализация MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            txtStart.setText("Ошибка загрузки файла!");
            return;
        }

        // Обновление SeekBar
        seekBar.setMax(mediaPlayer.getDuration());
        txtStop.setText(formatTime(mediaPlayer.getDuration()));
        handler.post(updateSeekBar);

        // Установите изображение
        try {
            File directory = new File(this.getFilesDir(), "myPhotos");
            File file = new File(directory, Img);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Кнопка Play/Pause
        playBtn.setOnClickListener(v -> togglePlayPause());

        // Перемотка вперед
        btnFastForward.setOnClickListener(v -> fastForward());

        // Перемотка назад
        btnRewind.setOnClickListener(v -> rewind());

        // Перемотка через SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Завершение трека
        mediaPlayer.setOnCompletionListener(mp -> {
            playBtn.setBackgroundResource(R.drawable.baseline_play_arrow_24);
            seekBar.setProgress(0);
            txtStart.setText(formatTime(0));
        });
    }

    // Форматирование времени
    private String formatTime(int ms) {
        int minutes = ms / 1000 / 60;
        int seconds = (ms / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    // Воспроизведение/пауза
    private void togglePlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.baseline_play_arrow_24);
        } else {
            mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.baseline_pause_24);
        }
    }

    // Перемотка вперед
    private void fastForward() {
        int newPosition = mediaPlayer.getCurrentPosition() + 5000; // Перемотка на 10 секунд вперед
        mediaPlayer.seekTo(Math.min(newPosition, mediaPlayer.getDuration()));
    }

    // Перемотка назад
    private void rewind() {
        int newPosition = mediaPlayer.getCurrentPosition() - 5000; // Перемотка на 10 секунд назад
        mediaPlayer.seekTo(Math.max(newPosition, 0));
    }

    // Обновление SeekBar
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            txtStart.setText(formatTime(mediaPlayer.getCurrentPosition()));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
    }
}