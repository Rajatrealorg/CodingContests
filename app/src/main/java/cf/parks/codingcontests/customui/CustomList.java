package cf.parks.codingcontests.customui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cf.parks.codingcontests.R;

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> comp_name, site_link, start_time, end_time, duration, plateform;

    public CustomList(Activity context, List<String> comp_name, List<String> site_link, List<String> start_time, List<String> end_time, List<String> duration, List<String> plateform) {
        super(context, R.layout.event_data_view, comp_name);
        this.context = context;
        this.comp_name = comp_name;
        this.site_link = site_link;
        this.start_time = start_time;
        this.end_time = end_time;
        this.duration = duration;
        this.plateform = plateform;
    }

    private class ViewHolder {
        TextView code_comp_name, code_start_time, code_end_time, code_duration;
        ImageView code_site_logo;
        ImageButton code_go_to_site, code_link_share;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.event_data_view, parent,false);
            holder = new ViewHolder();
            holder.code_comp_name = (TextView) view.findViewById(R.id.code_comp_name);
            holder.code_start_time = (TextView) view.findViewById(R.id.code_start_time);
            holder.code_end_time = (TextView) view.findViewById(R.id.code_end_time);
            holder.code_duration = (TextView) view.findViewById(R.id.code_duration);
            holder.code_site_logo = (ImageView) view.findViewById(R.id.code_site_logo);
            holder.code_go_to_site = (ImageButton) view.findViewById(R.id.code_go_to_site);
            holder.code_link_share = (ImageButton) view.findViewById(R.id.code_link_share);
            view.setTag(holder);
        } else holder = (ViewHolder) view.getTag();

        if (comp_name.get(position) == null) holder.code_comp_name.setText("");
        else holder.code_comp_name.setText(comp_name.get(position));

        if (start_time == null || start_time.size()<1 || start_time.get(position) == null) holder.code_start_time.setText("");
        else holder.code_start_time.setText("Starts : " + start_time.get(position));

        if (end_time.get(position) == null) holder.code_end_time.setText("");
        else holder.code_end_time.setText("Ends : " + end_time.get(position));

        if (duration == null || duration.size()<1 || duration.get(position) == null || duration.get(position).length()<1) holder.code_duration.setText("");
        else holder.code_duration.setText("Duration : " + duration.get(position));

        if (plateform.get(position) == null) holder.code_site_logo.setImageURI(Uri.parse("drawable/other32.png"));
        else holder.code_site_logo.setImageResource(imageResourceAccordingToPlatform(plateform.get(position)));

        if (site_link.get(position) != null) holder.code_go_to_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(String.valueOf(site_link.get(position))));
                context.startActivity(i);
            }
        });

        holder.code_link_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, site_link.get(position));
                context.startActivity(i);
            }
        });

        AnimationSet set = new AnimationSet(true);
        TranslateAnimation slide = new TranslateAnimation(-100, 0, -100, 0);
        slide.setInterpolator(new DecelerateInterpolator(5.0f));
        slide.setDuration(300);
        Animation fade = new AlphaAnimation(0, 1.0f);
        fade.setInterpolator(new DecelerateInterpolator(5.0f));
        fade.setDuration(300);
        set.addAnimation(slide);
        set.addAnimation(fade);
        view.startAnimation(set);
        return view;
    }

    private int imageResourceAccordingToPlatform(String s) {
        switch (s) {
            case "HACKERRANK":
                return R.drawable.hr36;
            case "HACKEREARTH":
                return R.drawable.he32;
            case "CODECHEF":
                return R.drawable.cc32;
            case "CODEFORCES":
                return R.drawable.cf32;
            case "TOPCODER":
                return R.drawable.tc32;
            default:
                return R.drawable.other32;
        }
    }

}
