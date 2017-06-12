package com.jeff.sqliteexampledemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_input;
    private EditText et_input_arg;
    private Button btn_addData;
    private Button btn_execSql;
    private Button btn_rawQuery;
    private BookDao dao;
    private ListView lvBook;
    private static final String TAG = "MainActivity";
    private BookAdapter adapter;
    private List<BookBean> bookBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        dao = new BookDao(this);
        bookBeanList = dao.searchAll();
        Log.d(TAG, "onCreate: bookBeanList= " + bookBeanList.toString());

        adapter = new BookAdapter(this, bookBeanList);
        lvBook.setAdapter(adapter);
        lvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUpdateDialog(position);
            }
        });

        lvBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;//当返回为false时，表示当前不是OnItemLongClick，同样会触发OnItemClick事件
            }
        });

    }

    /**
     * 弹出删除dialog
     *
     * @param position
     */
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除本条数据");
        builder.setMessage("该动作不可撤回！");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.delete(bookBeanList.get(position).getId());
                dialog.dismiss();
                if (adapter == null) {
                    bookBeanList = dao.searchAll();
                    lvBook.setAdapter(new BookAdapter(getApplicationContext(),bookBeanList));
                }else {
                    bookBeanList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.show();
    }

    private void initView() {
        et_input = (EditText) findViewById(R.id.et_input);
        et_input_arg = (EditText) findViewById(R.id.et_input_arg);
        btn_addData = (Button) findViewById(R.id.btn_add_data);
        btn_execSql = (Button) findViewById(R.id.btn_exec_sql);
        btn_rawQuery = (Button) findViewById(R.id.btn_rawQuery);
        btn_addData.setOnClickListener(this);
        btn_execSql.setOnClickListener(this);
        btn_rawQuery.setOnClickListener(this);
        lvBook = (ListView) findViewById(R.id.lv_book);
    }

    @Override
    public void onClick(View v) {
        String input = et_input.getText().toString();
        String arg = et_input_arg.getText().toString();

        switch (v.getId()) {
            case R.id.btn_add_data:
                showAddDialog();
                break;
            case R.id.btn_exec_sql:
                if (!TextUtils.isEmpty(input)) {
                    if (!TextUtils.isEmpty(arg)) {
                        dao.execSQL(input, arg);
                    } else {
                        dao.execSQL(input);
                    }
                } else {
                    Toast.makeText(this, "输入SQL语句不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_rawQuery:
                if (!TextUtils.isEmpty(input)) {
                    if (!TextUtils.isEmpty(arg)) {
                        dao.rawQuery(input, arg);
                    } else {
                        dao.rawQuery(input);
                    }
                } else {
                    Toast.makeText(this, "输入SQL语句不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        Log.d(TAG, "onClick: 点击回调");

    }

    /**
     * 弹出修改数据dialog
     *
     * @param position
     */
    private void showUpdateDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_update_dialog, null);
        builder.setView(view);
        final EditText et_name = (EditText) view.findViewById(R.id.et_dialog_update_book_name);
        final EditText et_author = (EditText) view.findViewById(R.id.et_dialog_update_book_author);
        final EditText et_pages = (EditText) view.findViewById(R.id.et_dialog_update_book_pages);
        final EditText et_price = (EditText) view.findViewById(R.id.et_dialog_update_book_price);
        Button btn_update_ok = (Button) view.findViewById(R.id.btn_dialog_update_ok);
        Button btn_update_cancel = (Button) view.findViewById(R.id.btn_dialog_update_cancel);

        final int current_id = bookBeanList.get(position).getId();
        final String ex_name = bookBeanList.get(position).getName();
        final String ex_author = bookBeanList.get(position).getAuthor();
        final int ex_pages = bookBeanList.get(position).getPages();
        final float ex_price = bookBeanList.get(position).getPrice();

        et_name.setText(ex_name);
        et_name.setSelection(ex_name.length());
        et_author.setText(ex_author);
        et_pages.setText(String.valueOf(ex_pages));
        et_price.setText(String.valueOf(ex_price));

        final AlertDialog dialog = builder.show();
        btn_update_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String author = et_author.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(author) &&
                        !TextUtils.isEmpty(et_pages.getText()) && !TextUtils.isEmpty(et_price.getText())) {
                    int pages = Integer.parseInt(et_pages.getText().toString());
                    float price = Float.parseFloat(et_price.getText().toString());
                    Log.d(TAG, "onClick: " + name + " " + author + " " + pages + " " + price);
                    if (!name.equals(ex_name) || !author.equals(ex_author) || pages != ex_pages ||
                            price != ex_price) {
                        dao.update(current_id, name, author, pages, price);
                        dialog.dismiss();
                        bookBeanList = dao.searchAll();
                        if (adapter == null) {
                            lvBook.setAdapter(new BookAdapter(getApplicationContext(),bookBeanList));
                        }else {
//                            adapter.updateItem(lvBook,position,new String[]{"《"+name+"》 -"+
//                                    author,pages+"页   价格："+
//                                    String.valueOf(price)+" RMB"});
                            adapter.update(bookBeanList);
//                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "数据未变动", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示添加Dialog
     */
    private void showAddDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_add_dialog, null);
        builder.setView(view);
        final EditText et_name = (EditText) view.findViewById(R.id.et_dialog_add_book_name);
        final EditText et_author = (EditText) view.findViewById(R.id.et_dialog_add_book_author);
        final EditText et_pages = (EditText) view.findViewById(R.id.et_dialog_add_book_pages);
        final EditText et_price = (EditText) view.findViewById(R.id.et_dialog_add_book_price);
        Button btn_ok = (Button) view.findViewById(R.id.btn_dialog_add_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_dialog_add_cancel);

        final AlertDialog dialog = builder.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String author = et_author.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(author) &&
                        !TextUtils.isEmpty(et_pages.getText()) && !TextUtils.isEmpty(et_price.getText())) {
                    int pages = Integer.parseInt(et_pages.getText().toString());
                    float price = Float.parseFloat(et_price.getText().toString());
                    dao.add(name, author, pages, price);
                    dialog.dismiss();

                    bookBeanList = dao.searchAll();
                    if (adapter == null) {
                        lvBook.setAdapter(new BookAdapter(getApplicationContext(), bookBeanList));
                    } else {
                        adapter.update(bookBeanList);
//                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}
