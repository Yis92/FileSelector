package com.yis.file.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yis.file.R;
import com.yis.file.adapter.PublicTabViewPagerAdapter;
import com.yis.file.fragment.FolderDataFragment;
import com.yis.file.model.FileInfo;
import com.yis.file.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用 Media Store 多媒体库
 */
public class MediaStoreActivity extends AppCompatActivity {

    private TabLayout tlFile;
    private ViewPager vpFile;

    private List<String> mTabTitle = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();

    private ArrayList<FileInfo> imageData = new ArrayList<>();
    private ArrayList<FileInfo> wordData = new ArrayList<>();
    private ArrayList<FileInfo> xlsData = new ArrayList<>();
    private ArrayList<FileInfo> pptData = new ArrayList<>();
    private ArrayList<FileInfo> pdfData = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        tlFile = findViewById(R.id.tl_file);
        vpFile = findViewById(R.id.vp_file);


        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("正在加载中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread() {
            @Override
            public void run() {
                super.run();
                getFolderData();
            }
        }.start();
    }

    /**
     * 遍历文件夹中资源
     */
    public void getFolderData() {

        getImages();

        getDocumentData(1);
        getDocumentData(2);
        getDocumentData(3);
        getDocumentData(4);

        handler.sendEmptyMessage(1);
    }


    private void initData() {

        mTabTitle = new ArrayList<>();
        mFragment = new ArrayList<>();

        mTabTitle.add("image");
        mTabTitle.add("word");
        mTabTitle.add("xls");
        mTabTitle.add("ppt");
        mTabTitle.add("pdf");

        FolderDataFragment imageFragment = new FolderDataFragment();
        Bundle imageBundle = new Bundle();
        imageBundle.putParcelableArrayList("file_data", imageData);
        imageBundle.putBoolean("is_image", true);
        imageFragment.setArguments(imageBundle);
        mFragment.add(imageFragment);

        FolderDataFragment wordFragment = new FolderDataFragment();
        Bundle wordBundle = new Bundle();
        wordBundle.putParcelableArrayList("file_data", wordData);
        wordBundle.putBoolean("is_image", false);
        wordFragment.setArguments(wordBundle);
        mFragment.add(wordFragment);

        FolderDataFragment xlsFragment = new FolderDataFragment();
        Bundle xlsBundle = new Bundle();
        xlsBundle.putParcelableArrayList("file_data", xlsData);
        xlsBundle.putBoolean("is_image", false);
        xlsFragment.setArguments(xlsBundle);
        mFragment.add(xlsFragment);

        FolderDataFragment pptFragment = new FolderDataFragment();
        Bundle pptBundle = new Bundle();
        pptBundle.putParcelableArrayList("file_data", pptData);
        pptBundle.putBoolean("is_image", false);
        pptFragment.setArguments(pptBundle);
        mFragment.add(pptFragment);

        FolderDataFragment pdfFragment = new FolderDataFragment();
        Bundle pdfBundle = new Bundle();
        pdfBundle.putParcelableArrayList("file_data", pdfData);
        pdfBundle.putBoolean("is_image", false);
        pdfFragment.setArguments(pdfBundle);
        mFragment.add(pdfFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        PublicTabViewPagerAdapter tabViewPagerAdapter = new PublicTabViewPagerAdapter(fragmentManager, mFragment, mTabTitle);
        vpFile.setAdapter(tabViewPagerAdapter);

        tlFile.setupWithViewPager(vpFile);

        tlFile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFile.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        progressDialog.dismiss();
    }

    /**
     * 加载图片
     */
    public void getImages() {

        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME};

        //asc 按升序排列
        //desc 按降序排列
        //projection 是定义返回的数据，selection 通常的sql 语句，例如  selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
        ContentResolver mContentResolver = this.getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");


        String imageId = null;

        String fileName;

        String filePath;

        while (cursor.moveToNext()) {

            imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));

            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));

            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));


            Log.e("photo", imageId + " -- " + fileName + " -- " + filePath);
            FileInfo fileInfo = FileUtil.getFileInfoFromFile(new File(filePath));
            imageData.add(fileInfo);
        }
        cursor.close();

        cursor = null;
    }

    /**
     * 获取手机文档数据
     *
     * @param selectType
     */
    public void getDocumentData(int selectType) {

        String[] columns = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.DATA};

        String select = "";

        switch (selectType) {
            //word
            case 1:
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.doc'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.docx'" + ")";
                break;
            //xls
            case 2:
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.xls'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.xlsx'" + ")";
                break;
            //ppt
            case 3:
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.ppt'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.pptx'" + ")";
                break;
            //pdf
            case 4:
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.pdf'" + ")";
                break;
        }

//        List<FileInfo> dataList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), columns, select, null, null);
//        Cursor cursor = contentResolver.query(Uri.parse(Environment.getExternalStorageDirectory() + "/tencent/QQfile_recv/"), columns, select, null, null);

        int columnIndexOrThrow_DATA = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                String path = cursor.getString(columnIndexOrThrow_DATA);

                FileInfo document = FileUtil.getFileInfoFromFile(new File(path));

//                dataList.add(document);
                switch (selectType) {
                    //word
                    case 1:
                        wordData.add(document);
                        break;
                    //xls
                    case 2:
                        xlsData.add(document);
                        break;
                    //ppt
                    case 3:
                        pptData.add(document);
                        break;
                    //pdf
                    case 4:
                        pdfData.add(document);
                        break;
                }
            }
            cursor.close();
        }
    }


}
