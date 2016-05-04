package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import com.technologyminds.ferozproductsorderbookingsystem.R;

import java.util.ArrayList;
import java.util.List;

import Utils.Constant;
import Utils.Item;
import Utils.Order;

/**
 * Created by user on 3/18/2016.
 */
public class PastOrderStatisticAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private List<Order> itemList;
    private List<Order> filterItemList;
    ValueFilter valueFilter;

    public PastOrderStatisticAdapter(Context context, List<Order> itemList){
        this.context = context;
        this.itemList = itemList;
        this.filterItemList = itemList;
        Constant.filterList = itemList;
        //Toast.makeText(context,itemList.size()+" Grid",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        protected TextView txtStoreName;
        protected TextView txtOrderId;
        protected TextView txtOrderDate;
        protected TextView txtAmount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            //Toast.makeText(context,"If",Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.past_order_statistics,null);

            viewHolder = new ViewHolder();

            viewHolder.txtStoreName = (TextView) convertView.findViewById(R.id.txt_store_name);
            viewHolder.txtOrderId = (TextView) convertView.findViewById(R.id.txt_order_id);
            viewHolder.txtOrderDate = (TextView) convertView.findViewById(R.id.txt_date);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //viewHolder.img.setImageResource(itemList.get(position).getImage_id());
        viewHolder.txtStoreName.setText(itemList.get(position).getStoreName());
        viewHolder.txtOrderId.setText(String.valueOf(itemList.get(position).getOrderId()));
        viewHolder.txtOrderDate.setText(String.valueOf(itemList.get(position).getDeliveryDate()));
        double total =itemList.get(position).getOrderAmount();
//        Constant.totalAmmount = Constant.totalAmmount + total;
//        Log.i("Total",Constant.totalAmmount+"");
        viewHolder.txtAmount.setText(String.valueOf(total));

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

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Order> filterList = new ArrayList<Order>();
                for (int i = 0; i < filterItemList.size(); i++) {
                    if ( (filterItemList.get(i).getOrderId() )
                            .contains(constraint.toString())) {

                        Order order = new Order();
                        order.setStoreName(filterItemList.get(i).getStoreName());
                        order.setOrderId(filterItemList.get(i).getOrderId());
                        order.setDeliveryDate(filterItemList.get(i).getDeliveryDate());
                        order.setOrderAmount(filterItemList.get(i).getOrderAmount());
                        order.setOrderDate(filterItemList.get(i).getOrderDate());
                        filterList.add(order);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
                Constant.filterList = filterList;
            } else {
                results.count = filterItemList.size();
                results.values = filterItemList;

            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            itemList = (ArrayList<Order>) results.values;
            notifyDataSetChanged();
        }

    }

}

