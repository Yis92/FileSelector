package com.yis.file.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yis.file.R;
import com.yis.file.model.FileInfo;
import com.yis.file.utils.FileUtil;

import java.util.List;


/**
 * 使用遍历文件夹的方式
 * Created by yis on 2018/4/17.
 */

public class FolderDataRecycleAdapter extends RecyclerView.Adapter<FolderDataRecycleAdapter.ViewHolder> {

    private Context mContext;
    private List<FileInfo> data;
    private boolean isPhoto = false;

    public FolderDataRecycleAdapter(Context mContext, List<FileInfo> data, boolean isPhoto) {
        this.mContext = mContext;
        this.data = data;
        this.isPhoto = isPhoto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_folder_data_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_content.setText(data.get(position).getFileName());
        holder.tv_size.setText(FileUtil.FormetFileSize(data.get(position).getFileSize()));
        holder.tv_time.setText(data.get(position).getTime());

        //封面图
        if (isPhoto) {
            Glide.with(mContext).load(data.get(position).getFilePath()).into(holder.iv_cover);
        } else {
            Glide.with(mContext).load(FileUtil.getFileTypeImageId(mContext, data.get(position).getFilePath())).fitCenter().into(holder.iv_cover);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMain;
        TextView tv_content;
        TextView tv_size;
        TextView tv_time;
        ImageView iv_cover;

        public ViewHolder(View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rl_main);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_cover = itemView.findViewById(R.id.iv_cover);
        }
    }

}
