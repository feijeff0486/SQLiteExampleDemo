package com.jeff.sqliteexampledemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小太阳jeff on 2017/6/7.
 */

public class BookDao {
    private final String TAG = "BookDao";
    private BookOpenHelper mHelper; //BookOpenHelper实例对象
    private Context mContent;

    /**
     * 构造方法，创建BookOpenHelper实例
     *
     * @param context 上下文
     */
    public BookDao(Context context) {
        this.mHelper = new BookOpenHelper(context);
        mContent=context;
    }

    /**
     * 添加数据
     * @param name
     * @param author
     * @param pages
     * @param price
     */
    public void add(String name,String author,int pages,float price) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 开始组装第一条数据
        values.put("name", name);
        values.put("author", author);
        values.put("pages", pages);
        values.put("price", price);
        db.insert("Book", null, values);
        Toast.makeText(mContent, "数据添加成功", Toast.LENGTH_SHORT).show();
//        values.clear();
//        // 开始组装第二条数据
//        values.put("name", "The Da Vinci Code");
//        values.put("author", "Dan Brown");
//        values.put("pages", 454);
//        values.put("price", 16.96);
//        db.insert("Book", null, values);
        db.close();
    }

    /**
     * 修改数据
     *
     * @param id     数据id
     * @param name   书名
     * @param author 作者
     * @param pages  页数
     * @param price  价格
     */
    public void update(int id, String name, String author, int pages, float price) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("author", author);
        values.put("pages", pages);
        values.put("price", price);
        db.update("Book", values, "id=?", new String[]{String.valueOf(id)});
        Toast.makeText(mContent, "数据修改成功", Toast.LENGTH_SHORT).show();
        db.close();
    }

    /**
     * 删除数据
     * db.delete()
     * @param table       表名
     * @param whereClause 约束条件
     * @param whereArgs   约束值
     */
    public void delete(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("Book", "id=?", new String[]{String.valueOf(id)});
        Toast.makeText(mContent, "删除成功", Toast.LENGTH_SHORT).show();
        db.close();
    }

    /**
     * 查询数据库中全部数据
     * db.query（）
     * @param table         表名
     * @param columns       指定列名
     * @param selection     约束条件
     * @param selectionArgs 约束值
     * @param groupBy       groupBy
     * @param having        having
     * @param orderBy       orderBy
     */
    public List<BookBean> searchAll() {

        //查询数据库内容，返回只读类型的SQLiteDatabase对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //打开指定路径的数据库如： path = "/data/data/包名/files/antivirus.db"
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        List<BookBean> bookList = new ArrayList<>();
        Cursor cursor = db.query("Book", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                Log.d(TAG, "searchAll: " + id + "  " + name + "  " + author + "  " + pages + "  " + price);
                BookBean book = new BookBean();
                book.setId(id);
                book.setName(name);
                book.setAuthor(author);
                book.setPages(pages);
                book.setPrice(price);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    //**************************************************************
    //使用SQL操作数据库

    /**
     * 添加数据，更新数据，删除数据
     *
     * @param sql      SQL语句
     * @param bindArgs 一般为空
     */
    public void execSQL(String sql, String bindArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{bindArgs});
        db.close();
    }

    /**
     * 重载方法执行SQL语句
     * @param sql
     */
    public void execSQL(String sql) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }


    /**
     * 使用SQL语句查询数据
     *
     * @param sql           SQL语句
     * @param selectionArgs 约束值
     */
    public void rawQuery(String sql, String selectionArgs) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql, new String[]{selectionArgs});
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                Log.d(TAG, "rawQuery: " + id + "  " + name + "  " + author + "  " + pages + "  " + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /**
     * 重载方法，使用SQL语句查询
     * @param sql
     */
    public void rawQuery(String sql) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                Log.d(TAG, "rawQuery: " + id + "  " + name + "  " + author + "  " + pages + "  " + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

}
