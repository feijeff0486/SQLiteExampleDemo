package com.jeff.sqliteexampledemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 小太阳jeff on 2017/6/7.
 */

public class BookAdapter extends BaseAdapter {

    private List<BookBean> mDatas;
    private LayoutInflater mInflater;
    private static final String TAG = "BookAdapter";

    public BookAdapter(Context context, List<BookBean> datas) {
        mDatas = datas;
        Log.d(TAG, "BookAdapter: mDatas= "+mDatas);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 数据变动，刷新listView
     * @param datas 变动后的数据
     */
    public void update(List<BookBean> datas){
        this.mDatas=datas;
        Log.d(TAG, "update: mDatas= "+mDatas);
        notifyDataSetChanged();
        Log.d(TAG, "update: adapter刷新了");
    }

    /**
     * 刷新单条数据
     * @param lv
     * @param position
     * @param itemData
     */
    public void updateItem(ListView lv,int position,String[] itemData){
        int visiblePos = lv.getFirstVisiblePosition();
        View v = lv.getChildAt(position - visiblePos);
        ViewHolder viewHolder =(ViewHolder)v.getTag();
        if(viewHolder!= null){
            viewHolder.book_name_author.setText(itemData[0]);
            viewHolder.book_pages_price.setText(itemData[1]);
        }
    }

    @Override

    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_book, null);
            holder.book_id = (TextView)convertView.findViewById(R.id.tv_book_id);
            holder.book_name_author = (TextView)convertView.findViewById(R.id.tv_book_name_author);
            holder.book_pages_price = (TextView)convertView.findViewById(R.id.tv_book_pages_price);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.book_id.setText("id:"+mDatas.get(position).getId());
        holder.book_name_author.setText("《"+mDatas.get(position).getName()+"》 -"+
                mDatas.get(position).getAuthor());
        holder.book_pages_price.setText(mDatas.get(position).getPages()+"页   价格："+
                mDatas.get(position).getPrice()+" RMB");
        return convertView;
    }

     final class ViewHolder{
         TextView book_id;
         TextView book_name_author;
         TextView book_pages_price;
    }
}
