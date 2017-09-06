package mafken.redditapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.SubredditViewHolder> {

    private int itemCount;
    private List<Subreddit> subjects;
    private SubredditsFragment.SubredditOnclickListener listener;

    public SubredditAdapter(List<Subreddit> subjects, SubredditsFragment.SubredditOnclickListener listener) {
        this.subjects = subjects;
        this.itemCount = subjects.size();
        this.listener = listener;
    }

    @Override
    public SubredditAdapter.SubredditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subject, parent, false);
        v.setOnClickListener(listener);
        return new SubredditAdapter.SubredditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubredditAdapter.SubredditViewHolder holder, final int position) {
        TextView subjectAuthor = holder.subjectAuthor;
        TextView subjectCreatedOn = holder.subjectCreatedOn;
        TextView subjectTitle = holder.subjectTitle;
        TextView subjectContent = holder.subjectContent;
        TextView subjectUpvotes = holder.subjectUpvotes;
        TextView subjectAmountOfComments = holder.subjectAmountOfComments;
        ImageView subjectImage = holder.subjectImage;
        Context context = holder.subjectImage.getContext();
        final int holderPosition = holder.getAdapterPosition();

        //setting the texts
        subjectAuthor.setText(subjects.get(holderPosition).getAuthor());
        subjectCreatedOn.setText(String.valueOf(subjects.get(holderPosition).getReadableTimestamp()));
        subjectTitle.setText(subjects.get(holderPosition).getTitle());
        subjectContent.setText(subjects.get(holderPosition).getSelftext());
        subjectUpvotes.setText(String.valueOf(subjects.get(holderPosition).getUps()));
        subjectAmountOfComments.setText(String.valueOf(subjects.get(holderPosition).getNum_comments()));

        Picasso.with(context).load(subjects.get(holderPosition).getThumbnail())
                .error(R.drawable.reddit_placeholder)
                .into(subjectImage);

    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public static class SubredditViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        public TextView subjectAuthor;

        @BindView(R.id.createdOn)
        public TextView subjectCreatedOn;

        @BindView(R.id.title)
        public TextView subjectTitle;

        @BindView(R.id.content)
        public TextView subjectContent;


        @BindView(R.id.image)
        public ImageView subjectImage;


        @BindView(R.id.upvotes)
        public TextView subjectUpvotes;


        @BindView(R.id.comments)
        public TextView subjectAmountOfComments;


        public SubredditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
