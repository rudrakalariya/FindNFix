package com.hackaneers.findnfix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private List<ServiceItem> serviceList;

    public ServiceAdapter(Context context, List<ServiceItem> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_service, parent, false);
        }

        ImageView serviceImage = convertView.findViewById(R.id.serviceIcon);
        TextView serviceName = convertView.findViewById(R.id.serviceName);

        ServiceItem item = serviceList.get(position);
        serviceImage.setImageResource(item.getImageResId());
        serviceName.setText(item.getName());

        return convertView;
    }
}
