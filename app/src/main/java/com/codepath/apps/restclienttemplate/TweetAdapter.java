package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
        private List<Tweet> tweets;
        Context context;

        public TweetAdapter(List<Tweet> tweets){
            this.tweets = tweets;
        }


        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View tweetView = inflater.inflate(R.layout.item_tweet,parent,false);
            ViewHolder viewHolder = new ViewHolder(tweetView);
            return viewHolder;
        }

         public void onBindViewHolder(ViewHolder holder, int position){
            Tweet tweet = tweets.get(position);
            holder.tvName.setText(tweet.user.name);
            holder.tvUserName.setText("@"+tweet.user.username);
            holder.tvUserTweet.setText(tweet.body);
            holder.time.setText(getRelativeTimeAgo(tweet.createdAt));

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivProfileImage);


        }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

        public int getItemCount(){
            return tweets.size();
        }

        public void clear() {
            tweets.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Tweet> list) {
             tweets.addAll(list);
             notifyDataSetChanged();
         }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView ivProfileImage;
            public TextView tvName;
            public TextView tvUserName;
            public TextView tvUserTweet;
            public TextView time;
            ImageButton btnReply;

            public ViewHolder(View itemView) {
                super(itemView);

                ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvUserTweet = (TextView) itemView.findViewById(R.id.tvUserTweet);
                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
                time = (TextView) itemView.findViewById(R.id.time);
                btnReply = (ImageButton) itemView.findViewById(R.id.btnReply);

                btnReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            Tweet tweet = tweets.get(position);
                            Intent intent = new Intent(context, ReplyActivity.class);

                            intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                            intent.putExtra("username", tweet.user.getUsername());
                            intent.putExtra("uid", tweet.uid);

                            context.startActivity(intent);
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Tweet tweet = tweets.get(position);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("username",tweet.user.getUsername());
                        intent.putExtra("name", tweet.user.getName());
                        intent.putExtra("body", tweet.getBody());
                        intent.putExtra("imageUrl", tweet.user.getProfileImageUrl());
                        intent.putExtra("time",getRelativeTimeAgo(tweet.createdAt));
                        context.startActivity(intent);
                    }
                });



            }

         }
}
