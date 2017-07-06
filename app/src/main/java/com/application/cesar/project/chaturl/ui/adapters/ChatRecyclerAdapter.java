package com.application.cesar.project.chaturl.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.cesar.project.chaturl.R;
import com.application.cesar.project.chaturl.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;



public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;
    private Context context;

    public ChatRecyclerAdapter(List<Chat> chats, Context context)
    {
        mChats = chats;
        this.context = context;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 1);

        String message = chat.message;

        if(message.startsWith("http")) {
            myChatViewHolder.txtChatMessage.setText(chat.message);
            myChatViewHolder.urllinear.setVisibility(View.VISIBLE);
            //myChatViewHolder.title.setText("Titulo");
            myChatViewHolder.urltext.setText("Url");
            myChatViewHolder.description.setText("Description");

            myChatViewHolder.txtChatMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = ((TextView)v).getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(str));
                    context.startActivity(intent);
                }

            });

            myChatViewHolder.urllinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = ((TextView) v.findViewById(R.id.url)).getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(str));
                    context.startActivity(intent);
                }
            });


            YourDataTask yourDataTask = new YourDataTask(myChatViewHolder);
            yourDataTask.execute("https://noembed.com/embed?url="+chat.message);

        }else{
            myChatViewHolder.txtChatMessage.setText(chat.message);

        }
        myChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 1);
        String message = chat.message;


        if(message.startsWith("http")) {
        otherChatViewHolder.txtChatMessage.setText(chat.message);
            otherChatViewHolder.urllinear.setVisibility(View.VISIBLE);
            otherChatViewHolder.title.setText("Titulo");
            otherChatViewHolder.urltext.setText("Url ");
            otherChatViewHolder.description.setText("Description");

            otherChatViewHolder.txtChatMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = ((TextView)v).getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(str));
                    context.startActivity(intent);
                }

            });

            otherChatViewHolder.urllinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = ((TextView) v.findViewById(R.id.url)).getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(str));
                    context.startActivity(intent);
                }
            });



            YourDataTask yourDataTask = new YourDataTask(otherChatViewHolder);
            yourDataTask.execute("https://noembed.com/embed?url="+chat.message);

        }else{
            otherChatViewHolder.txtChatMessage.setText(chat.message);

        }
        otherChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet, title, urltext, description;
        private LinearLayout urllinear;
        private ImageView loadimagemine;


        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            urllinear = (LinearLayout) itemView.findViewById(R.id.linearurl);
            title =(TextView) itemView.findViewById(R.id.title);
            urltext =(TextView) itemView.findViewById(R.id.url);
            description =(TextView) itemView.findViewById(R.id.description);
            loadimagemine = (ImageView) itemView.findViewById(R.id.image_post_set);

        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet, title, urltext, description;
        private LinearLayout urllinear;
        private ImageView loadimage;


        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            urllinear = (LinearLayout) itemView.findViewById(R.id.linearurl);
            title =(TextView) itemView.findViewById(R.id.title);
            urltext =(TextView) itemView.findViewById(R.id.url);
            description =(TextView) itemView.findViewById(R.id.description);
            loadimage = (ImageView) itemView.findViewById(R.id.image_post_set);


        }
    }



    protected class YourDataTask extends AsyncTask<String, Void, JSONObject>
    {
        RecyclerView.ViewHolder otherChatViewHolder;

        public YourDataTask(RecyclerView.ViewHolder otherChatViewHolder)
        {
            this.otherChatViewHolder = otherChatViewHolder;
        }

        @Override
        protected JSONObject doInBackground(String... params)
        {

            String str= params[0];
            //String str="https://noembed.com/embed?url=https://www.youtube.com/watch?v=Mt9FG9YjBMM";

            //Log.e("App", "LA URL ES " + Urlembed);


            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONObject(stringBuffer.toString());


            }
            catch(Exception ex)
            {
                Log.e("App", "errores de conexion", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {


                //aqui buenas noches se me acbaron las pilas
                try {

                    Log.e("App", "Success: " + response.getString("title"));
                    Log.e("Imagentelevision", "hola "+response.getString("thumbnail_url"));


                    if(otherChatViewHolder instanceof OtherChatViewHolder)
                    {


                                Picasso.with(context).load(response.getString("thumbnail_url")).placeholder(R.drawable.bg_title_tile).error(R.drawable.bg_title_tile).resize(110, 110).centerCrop().into(((OtherChatViewHolder)this.otherChatViewHolder).loadimage);

                        ((OtherChatViewHolder)this.otherChatViewHolder).title.setText(response.getString("title"));
                        ((OtherChatViewHolder)this.otherChatViewHolder).urltext.setText(response.getString("url"));
                        if(response.getString("description").equals("")){
                            ((OtherChatViewHolder) this.otherChatViewHolder).description.setText("");

                        }else {
                            ((OtherChatViewHolder) this.otherChatViewHolder).description.setText(response.getString("description"));
                        }

                    }
                    else if(otherChatViewHolder instanceof MyChatViewHolder)
                    {

                        Picasso.with(context).load(response.getString("thumbnail_url")).placeholder(R.drawable.bg_title_tile).error(R.drawable.bg_title_tile).resize(110, 110).centerCrop().into(((MyChatViewHolder)this.otherChatViewHolder).loadimagemine);

                        ((MyChatViewHolder)this.otherChatViewHolder).title.setText(response.getString("title"));
                        ((MyChatViewHolder)this.otherChatViewHolder).urltext.setText(response.getString("url"));
                        if(response.getString("description").equals("")){
                            ((MyChatViewHolder) this.otherChatViewHolder).description.setText("");

                        }else {
                            ((MyChatViewHolder) this.otherChatViewHolder).description.setText(response.getString("description"));
                        }



                    }
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
                Log.e("App", "Successo: " + response.toString());

            }
        }
    }
}
