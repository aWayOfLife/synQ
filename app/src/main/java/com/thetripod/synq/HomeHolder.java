package com.thetripod.synq;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class HomeHolder extends RecyclerView.ViewHolder {
        View mView;
        Context mContext;

        public HomeHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mContext = mView.getContext();
        }

        public void setBookingId(String bookingId) {
            TextView field = (TextView) mView.findViewById(R.id.bookingID);
            field.setText(bookingId);
        }
        public void setBankerId(String bankerId) {
            TextView field = (TextView) mView.findViewById(R.id.bankerID);
            field.setText(bankerId);
        }
        public void setServiceId(String serviceId) {
            TextView field = (TextView) mView.findViewById(R.id.serviceID);
            field.setText(serviceId);
        }
        public void setBookingTimestamp(String bookingTimestamp){
            TextView field = (TextView)mView.findViewById(R.id.timeStamp);
        }
        public void setBranch(String branchName){
            TextView field = (TextView)mView.findViewById(R.id.branch);
        }
}
