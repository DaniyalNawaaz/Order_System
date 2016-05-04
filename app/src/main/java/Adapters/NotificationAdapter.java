package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technologyminds.ferozproductsorderbookingsystem.R;

import java.util.List;
import Utils.Notification;

/**
 * Created by user on 3/18/2016.
 */
public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private List<Notification> notificationListList;

    public NotificationAdapter(Context context, List<Notification> notificationListList){
        this.context = context;
        this.notificationListList = notificationListList;
        //Toast.makeText(context,itemList.size()+" Grid",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return notificationListList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationListList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {

        protected TextView txtMessage;
        protected TextView txtDate;
        protected TextView txtTime;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            //Toast.makeText(context,"If",Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notification_list_view,null);

            viewHolder = new ViewHolder();

            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.notif_mesg);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txt_notif_date);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_notif_time);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtMessage.setText(notificationListList.get(position).getMessage());
        viewHolder.txtDate.setText(notificationListList.get(position).getDate());
        viewHolder.txtTime.setText(notificationListList.get(position).getTime());
        //viewHolder.img.setImageResource(itemList.get(position).getImage_id());
        //viewHolder.txtFlavour.setText(itemList.get(position).getFlavour());
        //viewHolder.edQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));
        //viewHolder.txtItemId.setText(String.valueOf(itemList.get(position).getItemId()));
        //viewHolder.txtItemName.setText(itemList.get(position).getItemName());
        //viewHolder.txtPrice.setText(String.valueOf(itemList.get(position).getPrice()));
        //viewHolder.edQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));

//        viewHolder.edQuantity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Item selectedItem = itemList.get(position);
//                selectedItem.setQuantity(Integer.parseInt(viewHolder.edQuantity.getText().toString()));
//                Constant.itemList.add(selectedItem);
//                Toast.makeText(context, selectedItem.getFlavour() + "\n" + viewHolder.edQuantity.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });


        return convertView;
    }
}
