package mafken.redditapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditDetailFragment extends Fragment{
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

    private Subreddit SRData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subreddit_detail, container, false);
        ButterKnife.bind(this, v);

        SRData = SubredditsFragment.currentSubreddit;
        setTextFields();
        return v;
    }

    public void setTextFields(){
        subjectAuthor.setText(SRData.getAuthor());
        subjectCreatedOn.setText(String.valueOf(SRData.getReadableTimestamp()));
        subjectTitle.setText(SRData.getTitle());
        subjectContent.setText(SRData.getSelftext());
        subjectUpvotes.setText(String.valueOf(SRData.getUps()));
        subjectAmountOfComments.setText(String.valueOf(SRData.getNum_comments()));

        Picasso.with(getContext()).load(SRData.getThumbnail())
                .error(R.drawable.reddit_placeholder).into(subjectImage);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
