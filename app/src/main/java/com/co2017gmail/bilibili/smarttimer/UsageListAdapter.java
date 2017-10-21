package com.co2017gmail.bilibili.smarttimer;

        import android.app.Application;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ResolveInfo;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

        import android.content.pm.ApplicationInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Provide views to RecyclerView with the directory entries.
 */
public class UsageListAdapter extends RecyclerView.Adapter<UsageListAdapter.ViewHolder> {

    private List<CustomUsageStats> mCustomUsageStatsList = new ArrayList<>();
    ApplicationDB applicationDB;
    static UsageDB usageDB;
    EventsDB eventsDB;
    static Usage usage ;
    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat df3 = new SimpleDateFormat("dd/MM/yy/HH/mm", Locale.ENGLISH);
    String dateTime = df2.format(new Date());
    ArrayList<working_time> working_time_list = new ArrayList<>();
    ArrayList<String> disturbing_app_list = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mPackageName;
        private final TextView mLastTimeUsed;
        private final ImageView mAppIcon;


        public ViewHolder(View v) {
            super(v);
            mPackageName = (TextView) v.findViewById(R.id.textview_package_name);
            mLastTimeUsed = (TextView) v.findViewById(R.id.textview_last_time_used);
            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
        }

        public TextView getLastTimeUsed() {
            return mLastTimeUsed;
        }

        public TextView getPackageName() {
            return mPackageName;
        }

        public ImageView getAppIcon() {
            return mAppIcon;
        }
    }

    public UsageListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usage_row_home_page, viewGroup, false);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateTime = df2.format(new Date());
        usage = usageDB.find(viewGroup.getContext(),dateTime);

        ArrayList<Events> events_today = new ArrayList<>();
        ArrayList<Events> events_today_filter = new ArrayList<>();
        working_time_list = new ArrayList<>();
        Context context = viewGroup.getContext();
        if(eventsDB.findAll(context)!=null)
            events_today = (ArrayList<Events>) eventsDB.findAll(context);
        for(Events events: events_today){

            if(events.eventStatusTime.equals("ON")) {
                events_today_filter.add(events);
//                Log.i("Event_NAME:", events.eventName);
//                Log.i("Event_TIME:", events.eventStartTime + " TO " + events.eventFinishTime);
                try {
                    Date date_start = df3.parse(events.eventStartTime);
                    Date date_end = df3.parse(events.eventFinishTime);
                    working_time_list.add(new working_time(date_start,date_end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Context context= viewHolder.getPackageName().getContext();
        String appname = getAppNameFromPackage(mCustomUsageStatsList.get(position).usageStats.getPackageName(), context);
        viewHolder.getPackageName().setText(appname);
        App app = applicationDB.find(context,appname);
        app.usage = mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground();
        applicationDB.update(context, app);

        usage.totalUsage = usage.totalUsage + mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground();
        usageDB.update(context, usage);

        viewHolder.getLastTimeUsed().setText(toUsageTime(mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground()));
        viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).appIcon);
    }

    @Override
    public int getItemCount() {
        return mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<CustomUsageStats> customUsageStats) {
        mCustomUsageStatsList = customUsageStats;
    }

    private String getAppNameFromPackage(String packageName, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);
        for (ResolveInfo app : pkgAppsList) {
            if (app.activityInfo.packageName.equals(packageName)) {
                return app.activityInfo.loadLabel(context.getPackageManager()).toString();
            }
        }
        return null;
    }

    protected  String toUsageTime(Long time) {
        long TimeInforground = time;
        int minutes = 500, seconds = 500, hours = 500;
        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
        seconds = (int) (TimeInforground / 1000) % 60;
        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        if (hours == 0 && minutes == 0) {
            return seconds + "s";
        } else if (hours == 0) {
            return minutes + "m" + seconds + "s";
        } else
            return hours + "h" + minutes + "m" + seconds + "s";
    }

    public class working_time{
        private Date start;
        private Date end;

        private working_time(Date start, Date end){
            this.start = start;
            this.end = end;
        }

        private Date get_from(){
            return start;
        }

        private Date get_end(){
            return end;
        }
    }

    private boolean CheckisPeriod(ArrayList<working_time> working_time_list){
        Date now = new Date();
        for(working_time time_range: working_time_list){
            Date start = time_range.get_from();
            Date end = time_range.get_end();
//            Log.i("Time_now", df3.format(now));
//            Log.i("Time_start", df3.format(start));
//            Log.i("Time_end", df3.format(end));
            if(now.before(end)&&now.after(start))
                Log.i("Time_end", "True!");
            return true;
        }
        return false;
    }

}

