package com.vmb.chinh_sua_anh.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.activity.ChooseImageActivity;
import com.vmb.chinh_sua_anh.activity.FontActivity;
import com.vmb.chinh_sua_anh.adapter.FontAdapter;
import com.vmb.chinh_sua_anh.base.fragment.BaseFragment;
import com.vmb.chinh_sua_anh.handler.Data;

public class Main_Detail_Font_Text_Fragment extends BaseFragment {

    private View layout_download_more_fonts;
    private RecyclerView recycler_font_text;

    @Override
    protected int getResLayout() {
        return R.layout.layout_font_text;
    }

    @Override
    protected void initView(View view) {
        recycler_font_text = view.findViewById(R.id.recycler_font_text);
        layout_download_more_fonts = view.findViewById(R.id.layout_download_more_fonts);
    }

    @Override
    protected void initData() {
        layout_download_more_fonts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), FontActivity.class),
                        Config.RequestCode.REQUEST_CODE_FONT);
            }
        });

        /*list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/svn_segoe_ui.ttf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/svn_redressed.ttf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/UVF Centeria.ttf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/SVN-Fresh Script.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/Amerika Sans.ttf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/Geogrotesque-Light.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/SVN-Aslang Barry.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/SVN-Maphylla.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/SVN-Steady.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/UVF Channel.ttf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/VL_Atlantika.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/VL_Delicatta.otf"));
        list.add(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Original Fonts/Fonts/VL_Selfie_Regular.otf"));*/

        // If the size of views will change as the data changes.
        recycler_font_text.setHasFixedSize(false);
        // Setting the LayoutManager.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_font_text.setLayoutManager(layoutManager);
        recycler_font_text.setNestedScrollingEnabled(true);

        FontAdapter adapter = new FontAdapter(getActivity(), Data.getInstance().getListFonts(getActivity()));
        recycler_font_text.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    public void updateFontList() {
        recycler_font_text.getAdapter().notifyDataSetChanged();
        recycler_font_text.smoothScrollToPosition(recycler_font_text.getAdapter().getItemCount() - 1);
    }
}