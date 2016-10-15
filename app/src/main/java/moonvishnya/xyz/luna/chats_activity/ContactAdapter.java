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

//protected TextView vUsername;
//protected TextView vMsg;
//protected TextView vTime;
//
//public ContactViewHolder(View v) {
//        super(v);
//        vUsername =  (TextView) v.findViewById(R.id.chat_username);
//        vMsg = (TextView)  v.findViewById(R.id.chat_msg_content);
//        vTime = (TextView)  v.findViewById(R.id.chat_time);
//        }
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;

    public ContactAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.username);
        contactViewHolder.vMsg.setText(ci.msg);
        contactViewHolder.vTime.setText(ci.time);

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.chats_card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vMsg;
        protected TextView vTime;


        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.chat_username);
            vTime = (TextView)  v.findViewById(R.id.chat_time);
            vMsg = (TextView)  v.findViewById(R.id.chat_msg_content);
        }
    }
}