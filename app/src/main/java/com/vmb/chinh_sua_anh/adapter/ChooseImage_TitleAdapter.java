package com.vmb.chinh_sua_anh.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quangda280296.photoeditor.R;
import com.vmb.chinh_sua_anh.adapter.holder.ChooseImage_TitleViewHolder;
import com.vmb.chinh_sua_anh.base.adapter.BaseAdapter;
import com.vmb.chinh_sua_anh.base.adapter.holder.BaseViewHolder;
import com.vmb.chinh_sua_anh.handler.Image;
import com.vmb.chinh_sua_anh.utils.IOnTouchClickListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseImage_TitleAdapter extends BaseAdapter {

    private List<Image.data.item> list;
    private int marked = 0;

    public int getMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }

    public ChooseImage_TitleAdapter(Context context, List list) {
        super(context, list);
        this.list = list;
    }

    @Override
    protected int getResLayout() {
        return R.layout.item_fragment_choose_image_tab_2_title;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ChooseImage_TitleViewHolder(inflater.inflate(getResLayout(), viewGroup, false));
    }

    @Override
    protected void bindView(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ChooseImage_TitleViewHolder) {
            final ChooseImage_TitleViewHolder holder = (ChooseImage_TitleViewHolder) viewHolder;

            int marked = getMarked();
            if (position != marked) {
                holder.layout_root.setBackgroundColor(context.getResources().getColor(R.color.gray_lv_6));
                holder.view_1.setVisibility(View.GONE);
                holder.view_2.setVisibility(View.GONE);
                holder.view_3.setVisibility(View.GONE);
                holder.view_4.setVisibility(View.GONE);
            } else {
                holder.layout_root.setBackgroundColor(context.getResources().getColor(R.color.purple_lv_4));
                holder.view_1.setVisibility(View.VISIBLE);
                holder.view_2.setVisibility(View.VISIBLE);
                holder.view_3.setVisibility(View.VISIBLE);
                holder.view_4.setVisibility(View.VISIBLE);
            }

            holder.lbl_title.setText(list.get(position).getName());

            holder.layout_root.setOnTouchListener(new IOnTouchClickListener(context, 2, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMarked(position);
                    notifyDataSetChanged();

                    if(context instanceof Activity) {
                        Activity activity = (Activity) context;
                        List ls = new ArrayList();
                        for(int i = 0; i < list.get(position).getCount(); i++)
                            ls.add("");

                        ChooseImage_ContentAdapter contentAdapter = new ChooseImage_ContentAdapter(activity, ls,
                                list.get(position).getUrl(),
                                Image.getInstance().getData().getPhotos().get(position).isChangeVersion());
                        RecyclerView recycler_content = activity.findViewById(R.id.recycler_content);
                        recycler_content.setAdapter(contentAdapter);
                    }
                }
            }));
        }
    }
}