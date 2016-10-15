package moonvishnya.xyz.luna.chats_activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moonvishnya.xyz.luna.R;

public class MessagesAdapter extends BaseAdapter {

    Context context;
    List<ContactInfo> items;


    public MessagesAdapter(Context context, List<ContactInfo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    private class ViewHolder {
        ImageView friendPhoto;
        TextView txtName;
        TextView txtMessage;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.chats_card_layout, null);
            holder = new ViewHolder();
            holder.txtName= (TextView) convertView.findViewById(R.id.chat_username);
            holder.txtMessage = (TextView) convertView.findViewById(R.id.chat_msg_content);
            holder.time = (TextView) convertView.findViewById(R.id.chat_time);
            holder.friendPhoto = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactInfo rowItem = (ContactInfo) getItem(position);
        holder.txtName.setText(rowItem.getUsername());
        holder.txtMessage.setText(rowItem.getMsg());
        holder.time.setText(rowItem.getTime());
        holder.friendPhoto.setImageResource(rowItem.getImageId());

        return convertView;
    }

}

