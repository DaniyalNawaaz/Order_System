package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.technologyminds.ferozproductsorderbookingsystem.R;

import java.util.List;

import Utils.Item;


/**
 * Created by user on 3/18/2016.
 */
public class OrderStatisticAdapter extends BaseAdapter {
    private Context context;
    private List<Item> itemList;


    public OrderStatisticAdapter(Context context, List<Item> itemList){
        this.context = context;
        this.itemList = itemList;
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
        protected TextView txtProductName;
        protected TextView txtQuantity;
        protected TextView txtPrice;
        protected TextView txtDiscount;
        protected TextView txtTotal;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            //Toast.makeText(context,"If",Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_statistic_view,null);

            viewHolder = new ViewHolder();

            viewHolder.txtProductName = (TextView) convertView.findViewById(R.id.txt_product_name);
            viewHolder.txtQuantity = (TextView) convertView.findViewById(R.id.txt_product_quantity);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
            viewHolder.txtDiscount = (TextView) convertView.findViewById(R.id.txt_discount);
            viewHolder.txtTotal = (TextView) convertView.findViewById(R.id.txt_total);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //viewHolder.img.setImageResource(itemList.get(position).getImage_id());
        viewHolder.txtProductName.setText(itemList.get(position).getItemName());
        viewHolder.txtQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));
        viewHolder.txtPrice.setText(String.valueOf(itemList.get(position).getPrice()));
        viewHolder.txtDiscount.setText(String.valueOf(itemList.get(position).getDiscount_per_product()));
        double total =itemList.get(position).getQuantity() * (itemList.get(position).getPrice()-itemList.get(position).getDiscount_per_product());
//        Constant.totalAmmount = Constant.totalAmmount + total;
//        Log.i("Total",Constant.totalAmmount+"");
        viewHolder.txtTotal.setText(String.valueOf(total));

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
