package com.thetripod.synq;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

        public void setCouponNo(String couponNo) {
            TextView field = (TextView) mView.findViewById(R.id.coupon_no);
            field.setText(couponNo);
        }

        public void setSlot(String slot) {
            TextView field = (TextView) mView.findViewById(R.id.slot);
            field.setText(slot);
        }
        public void setActivity(String activity){
            TextView field = (TextView) mView.findViewById(R.id.activity);
            field.setText(activity);

        }
        public void setStatus(String status){
            TextView field = (TextView) mView.findViewById(R.id.status);
            field.setText(status);

        }
        public void setEta(String eta){
            TextView field = (TextView) mView.findViewById(R.id.eta);
            field.setText(eta);
        }
}
