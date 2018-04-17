package com.yis.file.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yis on 2018/4/17.
 */
public class FileInfo implements Parcelable {

    private String fileName;
    private String filePath;
    private long fileSize;
    private String time;

    public String getFilePath() {
        return filePath == null ? "" : filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeLong(this.fileSize);
        dest.writeString(this.time);
    }

    public FileInfo() {
    }

    protected FileInfo(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.fileSize = in.readLong();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<FileInfo> CREATOR = new Parcelable.Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel source) {
            return new FileInfo(source);
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };
}
