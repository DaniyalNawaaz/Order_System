package Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technologyminds.ferozproductsorderbookingsystem.R;

import java.util.List;

import Utils.Constant;
import Utils.Item;

/**
 * Created by user on 3/17/2016.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Item> itemList;

    public GridViewAdapter(Context context, List<Item> itemList){
        this.context = context;
        this.itemList = itemList;
        Constant.itemList = itemList;
        Log.i("List Size",Constant.itemList.size()+"");
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

        protected TextView txtFlavour;
        protected EditText edQuantity;
        protected ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            //Toast.makeText(context,"If",Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_view_item,null);

            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.txtFlavour = (TextView) convertView.findViewById(R.id.tx_flavour);
            viewHolder.edQuantity = (EditText) convertView.findViewById(R.id.ed_quantity);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load("http://ferozproducts.com/order-system/"+itemList.get(position).getImage_path())
                        .noFade().resize(120, 180)
                .placeholder(R.drawable.loading)
                .into(viewHolder.img);
        //viewHolder.img.setImageResource(itemList.get(position).getImage_id());
        viewHolder.txtFlavour.setText(itemList.get(position).getFlavour());
        if(!String.valueOf(itemList.get(position).getQuantity()).contentEquals("0")){
            viewHolder.edQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));
        }

        //viewHolder.edQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));
        //viewHolder.txtItemId.setText(String.valueOf(itemList.get(position).getItemId()));
        //viewHolder.txtItemName.setText(itemList.get(position).getItemName());
        //viewHolder.txtPrice.setText(String.valueOf(itemList.get(position).getPrice()));
        //viewHolder.edQuantity.setText(String.valueOf(itemList.get(position).getQuantity()));
        viewHolder.edQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Position",position+"-");
                Item selectedItem = itemList.get(position);
                if(!String.valueOf(selectedItem.getQuantity()).isEmpty()){
                    Constant.itemList.remove(position);
                    Constant.itemList.add(position, selectedItem);
                    //Log.i("Product Name", Constant.itemList.get(position).getItemName());
                    //Log.i("Product Flavour", Constant.itemList.get(position).getFlavour());
                    if(!viewHolder.edQuantity.getText().toString().isEmpty()){
                        Constant.itemList.get(position).setQuantity(Integer.parseInt(viewHolder.edQuantity.getText().toString()));
                    }
                    //Log.i("Product Quantity", String.valueOf(Constant.itemList.get(position).getQuantity()));
                    //Log.i("Product Price", String.valueOf(Constant.itemList.get(position).getPrice()));
                    //Log.i("Total",String.valueOf(Constant.itemList.get(position).getQuantity()
                            //* Constant.itemList.get(position).getPrice()));
                }
            }
        });
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
