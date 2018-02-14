package cn.sdt.etalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.sdt.libniocommon.util.DateUtil;

/**
 * Created by SDT13411 on 2018/2/9.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    public final static int ITEM_TYPE_FROM = 0;
    public final static int ITEM_TYPE_TO = 1;

    private Context mContext;
    private List<ChatMsg> msgList;

    public MsgAdapter(Context mContext, List<ChatMsg> msgList) {
        this.mContext = mContext;
        this.msgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = null;
        if (viewType == ITEM_TYPE_FROM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_from, parent, false);
            viewHolder = new FromViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_to, parent, false);
            viewHolder = new ToViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMsg msg = msgList.get(position);
        if (msg.isMe()) {
            ToViewHolder viewHolder = (ToViewHolder) holder;
            viewHolder.tvInfo.setText(DateUtil.timestampToDateString(System.currentTimeMillis()));
            viewHolder.tvName.setText(msg.getUserName());
            viewHolder.tvMsg.setText(msg.getMsgBody());
        } else {
            FromViewHolder viewHolder = (FromViewHolder) holder;
            viewHolder.tvInfo.setText(DateUtil.timestampToDateString(System.currentTimeMillis()));
            viewHolder.tvName.setText(msg.getUserName());
            viewHolder.tvMsg.setText(msg.getMsgBody());
        }

    }

    @Override
    public int getItemViewType(int position) {
        ChatMsg msg = msgList.get(position);
        if (msg.isMe()) {
            return ITEM_TYPE_TO;
        } else {
            return ITEM_TYPE_FROM;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return msgList != null ? msgList.size() : 0;
    }

    public class ToViewHolder extends ViewHolder {

        private TextView tvInfo;
        private TextView tvName;
        private TextView tvMsg;

        public ToViewHolder(View itemView) {
            super(itemView);
            tvInfo = (TextView) itemView.findViewById(R.id.info);
            tvName = (TextView) itemView.findViewById(R.id.to_name);
            tvMsg = (TextView) itemView.findViewById(R.id.msg);
        }
    }

    public class FromViewHolder extends ViewHolder {

        private TextView tvInfo;
        private TextView tvName;
        private TextView tvMsg;

        public FromViewHolder(View itemView) {
            super(itemView);
            tvInfo = (TextView) itemView.findViewById(R.id.info);
            tvName = (TextView) itemView.findViewById(R.id.from_name);
            tvMsg = (TextView) itemView.findViewById(R.id.msg);
        }
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
