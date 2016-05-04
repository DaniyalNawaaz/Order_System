package Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.technologyminds.ferozproductsorderbookingsystem.R;

import java.util.List;

import Utils.Constant;
import Utils.Item;
import Utils.Notification;

/**
 * Created by user on 4/30/2016.
 */
public class DiscountAdapter extends BaseAdapter {
    private Context context;
    private List<Item> listOfItems;

    public DiscountAdapter(Context context, List<Item> listOfItems){
        this.context = context;
        this.listOfItems = listOfItems;
        Constant.itemDiscountList = listOfItems;
        Log.i("List Size",Constant.itemDiscountList.size()+"");

        for(int i=0;i<Constant.itemDiscountList.size();i++){
            Log.i("Product Name",Constant.itemDiscountList.get(i).getItemName());
            Log.i("Product Name",Constant.itemDiscountList.get(i).getDiscount_per_product()+"-");
        }
        //Toast.makeText(context,itemList.size()+" Grid",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return listOfItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {

        protected TextView txtProductName;
        protected EditText edDiscount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            //Toast.makeText(context,"If",Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.txtProductName = (TextView) convertView.findViewById(R.id.product_name);
            viewHolder.edDiscount = (EditText) convertView.findViewById(R.id.discount);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtProductName.setText(listOfItems.get(position).getItemName());

        if(String.valueOf(listOfItems.get(position).getDiscount_per_product()).contentEquals("0.0")){

        }
        else{
            viewHolder.edDiscount.setText(String.valueOf(listOfItems.get(position).getDiscount_per_product()));
        }

        viewHolder.edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Position",position+"-");
                Item selectedItem = listOfItems.get(position);
                if(!String.valueOf(selectedItem.getDiscount_per_product()).isEmpty()){
                    Constant.itemDiscountList.remove(position);
                    Constant.itemDiscountList.add(position, selectedItem);
                    //Log.i("Product Name", Constant.itemDiscountList.get(position).getItemName());
                    //Log.i("Product Flavour", Constant.itemDiscountList.get(position).getFlavour());
                    if(!viewHolder.edDiscount.getText().toString().isEmpty()){
                        Constant.itemDiscountList.get(position).setDiscount_per_product(Double.parseDouble(viewHolder.edDiscount.getText().toString()));
                    }
                    //Log.i("Product Quantity", String.valueOf(Constant.itemDiscountList.get(position).getQuantity()));
                    //Log.i("Product Price", String.valueOf(Constant.itemDiscountList.get(position).getPrice()));
                    //Log.i("Product Discount", String.valueOf(Constant.itemDiscountList.get(position).getDiscount_per_product()));
//                    Log.i("Total",String.valueOf(Constant.itemDiscountList.get(position).getQuantity()
//                            * Constant.itemDiscountList.get(position).getPrice()));
                }
            }
        });

        return convertView;
    }
}

