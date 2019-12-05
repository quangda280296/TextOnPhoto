package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.vm.TextOnPhoto.PhotoEditor.R;
import com.vmb.chinh_sua_anh.adapter.holder.StickerTabHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.Image;
import com.vmb.chinh_sua_anh.utils.IOnTouchClickListener;

import java.util.ArrayList;
import java.util.List;

public class StickerTabAdapter extends BaseAdapter {

    private List<Image.data.item> list;
    private int marked = 0;

    public int getMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }

    public StickerTabAdapter(Context context, List list) {
        super(context, list);
        this.list = list;
    }

    @Override
    protected int getResLayout() {
        return R.layout.item_tab;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new StickerTabHolder(inflater.inflate(getResLayout(), viewGroup, false));
    }

    @Override
    protected void bindView(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof StickerTabHolder) {
            final StickerTabHolder holder = (StickerTabHolder) viewHolder;

            int marked = getMarked();
            if (position != marked)
                holder.layout_tab.setBackgroundColor(context.getResources().getColor(R.color.gray_lv_3));
            else
                holder.layout_tab.setBackgroundColor(context.getResources().getColor(R.color.white_lv_2));

            holder.lbl_name_tab.setText(list.get(position).getName());
            Glide.with(context)
                    .load(Image.getInstance().getData().getStickers().get(position).getUrl_icon())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_icon);

            holder.layout_tab.setOnTouchListener(new IOnTouchClickListener(context, 3, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMarked(position);
                    notifyDataSetChanged();

                    if(context instanceof Activity) {
                        Activity activity = (Activity) context;
                        List ls = new ArrayList();
                        for (int i = 0; i < list.get(position).getCount(); i++)
                            ls.add("");

                        StickerImageAdapter contentAdapter = new StickerImageAdapter(activity, ls,
                                list.get(position).getUrl(), list.get(position).isChangeVersion());
                        RecyclerView recycler_content = activity.findViewById(R.id.recycler_content);
                        recycler_content.setAdapter(contentAdapter);
                    }
                }
            }));
        }
    }

    /*protected String getUriThumb(String img) {
        return Config.UrlData.URL_STICKER + "Sticker_Icon/" + img;
    }*/
}