package com.juaracoding.absensidika.Permission.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.juaracoding.absensidika.Permission.model.approval.PermissionActivity;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;
import com.juaracoding.absensidika.Utility.Tools;
import com.juaracoding.absensidika.Utility.ViewAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterListAttendenceMyApproval extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PermissionActivity> items = new ArrayList<>();


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, PermissionActivity obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListAttendenceMyApproval(Context context, List<PermissionActivity> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name,txtNamaAtas;
        public ImageView bt_expand;
        public View lyt_expand;
        public View lyt_parent;
        public RelativeLayout lycContainer;
        public LinearLayout layoutNamaBawah,layoutHeaderTop;
        private Button btnStatus,btnApprove,btnReject;
        private TextView txtTanggal, txtKeterangan,txtNamaBawah;
        private ImageView imgDetail;


        public OriginalViewHolder(View v) {
            super(v);


            bt_expand = (ImageView) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            lycContainer = (RelativeLayout)v.findViewById(R.id.lytContainer);
            layoutNamaBawah = (LinearLayout)v.findViewById(R.id.layoutNamaBawah);
            layoutHeaderTop = (LinearLayout)v.findViewById(R.id.layoutHeaderTop);
            txtNamaAtas = (TextView)v.findViewById(R.id.txtNamaAtas);
            btnApprove = (Button)v.findViewById(R.id.btnApprove);
            btnApprove.setTag("Approved");
            btnReject = (Button)v.findViewById(R.id.btnReject);
            btnReject.setTag("Not Approved");
            btnStatus = (Button)v.findViewById(R.id.btnStatus);
            txtTanggal = (TextView)v.findViewById(R.id.txtTanggal);
            txtKeterangan = (TextView)v.findViewById(R.id.txtKeterangan);
            txtNamaBawah = (TextView)v.findViewById(R.id.txtNamaBawah);
            imgDetail = (ImageView)v.findViewById(R.id.imageDetail);

            if(AppUtil.getSetting(ctx,"isManager","0").equalsIgnoreCase("0")) {

                    btnApprove.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);

            }else{
                if(AppUtil.getSetting(ctx,"showApproval","0").equalsIgnoreCase("0")){
                    btnApprove.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                }
            }


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_my_approval, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final PermissionActivity p = items.get(position);

            view.txtNamaAtas.setText(p.getUserId());
            view.txtNamaBawah.setText(p.getUserId());
            view.txtTanggal.setText(p.getFromDate() + " s/d "+p.getToDate());
            view.txtKeterangan.setText(p.getPermissionId());

            /*
            "approved_at": null,
                "declined_at": null,
                "created_at": "2019-04-25 03:29:52",
             */


            if(p.getStatus()!=null){
                if(p.getStatus().toString().equalsIgnoreCase("open")) {
                    view.btnStatus.setText("PROCESS");
                    view.btnStatus.setBackground(ContextCompat.getDrawable(ctx, R.drawable.btn_rounded_orange));
                    view.layoutHeaderTop.setBackground(ContextCompat.getDrawable(ctx, R.color.headersiapp));
                }
            }

            if(p.getStatus()!=null){
                if(p.getStatus().toString().equalsIgnoreCase("Not Approved")) {
                    view.btnStatus.setText("REJECT");
                    view.btnStatus.setBackground(ContextCompat.getDrawable(ctx, R.drawable.btn_rounded_red));
                    view.layoutHeaderTop.setBackground(ContextCompat.getDrawable(ctx, R.color.red_500));
                }
            }

            if(p.getStatus()!=null){
                if(p.getStatus().equalsIgnoreCase("Approved")) {
                    view.btnStatus.setText("APPROVED");
                    view.btnStatus.setBackground(ContextCompat.getDrawable(ctx, R.drawable.btn_rounded_green));
                    view.layoutHeaderTop.setBackground(ContextCompat.getDrawable(ctx, R.color.green_500));
                }
            }


            view.btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });

            view.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });


            if(p.getPicture1()!=null){

                Picasso.get()
                        .load(p.getPicture1())
                        .fit()
                        .into(view.bt_expand);

                Picasso.get()
                        .load(p.getPicture1())
                        .fit()
                        .into(view.imgDetail);

            }




            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!p.expanded, v, view.lyt_expand);
                    items.get(position).expanded = show;
                    if(show){
                        final float scale = ctx.getResources().getDisplayMetrics().density;
                        int px = (int) (78 * scale + 0.5f);  // replace 100 with your dimensions
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
                        view.lycContainer.setLayoutParams(params);
                        view.txtNamaAtas.setVisibility(View.VISIBLE);
                        view.layoutNamaBawah.setVisibility(View.GONE);

                    }else{
                        final float scale = ctx.getResources().getDisplayMetrics().density;
                        int px = (int) (200 * scale + 0.5f);  // replace 100 with your dimensions
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
                        view.lycContainer.setLayoutParams(params);
                        view.txtNamaAtas.setVisibility(View.GONE);
                        view.layoutNamaBawah.setVisibility(View.VISIBLE);
                    }
                }
            });


            // void recycling view
            if(p.expanded){
                view.lyt_expand.setVisibility(View.VISIBLE);
                view.txtNamaAtas.setVisibility(View.VISIBLE);
                view.layoutNamaBawah.setVisibility(View.GONE);

            } else {
                view.lyt_expand.setVisibility(View.GONE);
                view.txtNamaAtas.setVisibility(View.GONE);
                view.layoutNamaBawah.setVisibility(View.VISIBLE);



            }


        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}