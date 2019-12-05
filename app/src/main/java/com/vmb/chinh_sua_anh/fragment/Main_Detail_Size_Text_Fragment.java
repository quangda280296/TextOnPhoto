package com.vmb.chinh_sua_anh.fragment;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.PhotoHandler;
import com.vmb.chinh_sua_anh.handler.TextOnPhotoHandler;

public class Main_Detail_Size_Text_Fragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekbar_text_size;
    private TextView lbl_text_size;

    @Override
    protected int getResLayout() {
        return R.layout.layout_size_text;
    }

    @Override
    protected void initView(View view) {
        seekbar_text_size = view.findViewById(R.id.seekbar_text_size);
        lbl_text_size = view.findViewById(R.id.lbl_text_size);
    }

    @Override
    protected void initData() {
        float textSize = TextOnPhotoHandler.getInstance().getItem().getTextSize();
        lbl_text_size.setText("" + (int) textSize);

        seekbar_text_size.setMax(100);
        seekbar_text_size.setProgress((int) textSize);
        seekbar_text_size.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        lbl_text_size.setText(progress + "");
        int size = (progress * 90) / 100;
        if (size < 10)
            size = 10;

        PhotoHandler.getInstance().getPhotoEditor().changeTextSize(size);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}