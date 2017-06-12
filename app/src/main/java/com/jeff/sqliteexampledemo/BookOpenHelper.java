package com.jeff.sqliteexampledemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 小太阳jeff on 2017/6/7.
 */

public class BookOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, "
            + "name text,"
            + "author text, "
            + "pages integer, "
            + "price real)";//1.0
    public static final String CREATE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text)";//1.1
    public static final String UPDATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, "
            + "name text,"
            + "author text, "
            + "pages integer, "
            + "price real,"
            + "category_id integer)";//2.0
    private Context mContext;

    /**
     * 构造方法创建指定name数据库
     *
     * @param context 上下文
     * @param name    数据库名称
     * @param factory 一般为空
     * @param version 数据库版本
     */
    public BookOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    /**
     * 构造方法直接构造一个数据库名BookStore.db的数据库
     *
     * @param context 上下文
     */
    public BookOpenHelper(Context context) {
        super(context, "BookStore.db", null, 1);
        mContext=context;
    }

    /***
     * 创建数据库时调用，如果数据库已存在不调用
     * @param db SQLiteDatabase对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //App版本1.1时加入Category表。若用户首次建库两张表同时建立，若软件升级会在onUpgrade建立Category表
        db.execSQL(CREATE_BOOK);//数据库1.0
        db.execSQL(CREATE_CATEGORY);//数据库1.1
        Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 升级数据库
     *
     * @param db         SQLiteDatabase对象
     * @param oldVersion 数据库旧版本号
     * @param newVersion 数据库新版号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //针对数据库1.1版本，数据库一旦存在onCreate方法不再调用，Category表无法在onCreate方法中创建
        //根据数据库版本执行Category表的创建语句
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);
                break;
            case 2:
                //数据库2.0 创建Book表，相对1.0版本增加了category_id列
                db.execSQL("alert table Book add column category_id integer");
                break;
            default:
        }

        //过于暴力不建议
//        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
//        onCreate(db);
    }
}
