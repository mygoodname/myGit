package com.tangbing.admin.calendercheckdemo.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangbing.admin.calendercheckdemo.R;
import com.tangbing.admin.calendercheckdemo.model.SignDateModel;

import java.util.ArrayList;

/**
 * Created by tangbing on 2019/3/20.
 * Describe :
 */

public class DateAdapter  extends RecyclerView.Adapter<DateAdapter.MyViewHolder>{

    private ArrayList<SignDateModel> data;
    private Context context;

    public DateAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_data_layout,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        SignDateModel signDateModel=data.get(i);
        if(!"".equals(signDateModel.getDay())){
            if(signDateModel.getStatus()==SignDateModel.STATUS_CURRENT_DAY){
                myViewHolder.imageView.setImageResource(R.mipmap.signin);
                myViewHolder.dataText.setTextColor(context.getResources().getColor(R.color.white));
            }else
            if(signDateModel.getStatus()==SignDateModel.STATUS_HAVE_SIGN){
                myViewHolder.imageView.setImageResource(R.mipmap.alreadysignedin);
                myViewHolder.dataText.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }else{
                myViewHolder.imageView.setImageResource(R.color.white);
                myViewHolder.dataText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }

            myViewHolder.dataText.setText(signDateModel.getDay());
        }

    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }
    public void setData(ArrayList dataList){
        if(data==null)
        data = new ArrayList<>();
        data.clear();
        data.addAll(dataList);
        notifyDataSetChanged();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dataText;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
          dataText=itemView.findViewById(R.id.data);
          imageView=itemView.findViewById(R.id.image);
        }
    }
    public void setSignStatus(String[] status){
        for(int j=0;j<data.size();j++){
            SignDateModel signDateModel=data.get(j);
            for(int i=0;i<status.length;i++){
                String data=status[i];
                if(data.equals(signDateModel.getContent())){
                    signDateModel.setStatus(SignDateModel.STATUS_HAVE_SIGN);
                }
            }
        }
        notifyDataSetChanged();
    }
}
