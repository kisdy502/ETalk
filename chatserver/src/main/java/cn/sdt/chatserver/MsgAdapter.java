package cn.sdt.chatserver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SDT13411 on 2018/2/9.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private Context mContext;
    private List<String> msgList;

    public MsgAdapter(Context mContext, List<String> msgList) {
        this.mContext = mContext;
        this.msgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tvMsg = new TextView(mContext);
        ViewHolder viewHolder = new ViewHolder(tvMsg);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMsg.setText(msgList.get(position));
    }

    @Override
    public int getItemCount() {
        return msgList != null ? msgList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMsg = (TextView) itemView;
        }
    }
}
