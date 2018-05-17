package astix.nanaji_so;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SummaryActivityOld extends Activity
{
    public ProgressDialog pDialogVersionCheck;
    AlertDialog  alertFSO;
    TextView selectFsoSpinner;
    Spinner spinner_fsoList;
    LinearLayout ll_ParentFso;
    Object tag_val=null;
    String Spinner_item="";
    ImageView img_bckBtn;

    DBAdapterLtFoods dbengine=new DBAdapterLtFoods(this);
    LinkedHashMap<String, String> hmapFSOListForReport = new LinkedHashMap<String, String>();
    LinkedHashMap<String,LinkedHashMap<String,ArrayList<String>>> lknhmap_SOReportFSO=new LinkedHashMap<String,LinkedHashMap<String,ArrayList<String>>>();
    LinkedHashMap<String, ArrayList<String>> hmapFsoFlagAndSummary= new LinkedHashMap<>();
    ArrayList<String> FsoNameLsit=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        GetSummary get=new GetSummary(SummaryActivityOld.this);
        get.execute();

        img_bckBtn= (ImageView) findViewById(R.id.img_bckBtn);
        img_bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SummaryActivityOld.this,StorelistActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class GetSummary extends AsyncTask<Void, Void, Void>
    {
        public GetSummary(SummaryActivityOld activity)
        {
            pDialogVersionCheck = new ProgressDialog(SummaryActivityOld.this,R.style.MyDialogTheme);
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialogVersionCheck.show();
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialogVersionCheck.setContentView(R.layout.view_custom_progress_dialog);
            pDialogVersionCheck.setIndeterminate(false);
            pDialogVersionCheck.setCancelable(false);
            pDialogVersionCheck.setCanceledOnTouchOutside(false);

            TextView text_title=(TextView) pDialogVersionCheck.findViewById(R.id.text_title);
            text_title.setText("Please wait generating reports..");

            TextView text_msg=(TextView) pDialogVersionCheck.findViewById(R.id.text_msg);
            //text_msg.setText(getText(R.string.genTermValidDataNew));
            text_msg.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if(pDialogVersionCheck.isShowing())
            {
                pDialogVersionCheck.dismiss();
            }
            LayoutCreation();
        }
    }

    void getDataFromDatabase()
    {
        ArrayList<Object> arrSOReportDetails=new ArrayList<Object>();
        arrSOReportDetails=dbengine.fnGetSOReportForAllFSO();

        hmapFSOListForReport=( LinkedHashMap<String, String>) arrSOReportDetails.get(0);
        lknhmap_SOReportFSO=(LinkedHashMap<String,LinkedHashMap<String,ArrayList<String>>>)arrSOReportDetails.get(1);
    }

    void LayoutCreation()
    {
        //spinner_fsoList=(Spinner) findViewById(R.id.spinner_fsoList);
        ll_ParentFso=(LinearLayout) findViewById(R.id.ll_ParentFso);
        selectFsoSpinner= (TextView) findViewById(R.id.selectFsoSpinner);

        for(Map.Entry<String, String> entry:hmapFSOListForReport.entrySet())
        {
            String FsoNames=entry.getKey().toString().trim();
            String FSOid=entry.getValue().toString().trim();

            FsoNameLsit.add(FsoNames);
        }

        /*ArrayAdapter adapterCategory=new ArrayAdapter (SummaryActivityOld.this,android.R.layout.simple_spinner_item,FsoNameLsit);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fsoList.setAdapter(adapterCategory);*/

        final ArrayAdapter adapterCategory = new ArrayAdapter<String>(SummaryActivityOld.this, R.layout.list_item, R.id.fso_name, FsoNameLsit);
        selectFsoSpinner.setText(FsoNameLsit.get(0).toString().trim());
      /*  spinner_fsoList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Spinner_item=spinner_fsoList.getSelectedItem().toString().trim();
                SpinnerFilter(Spinner_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }

        });*/

        selectFsoSpinner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final Dialog alertDialog = new Dialog(SummaryActivityOld.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.fso_list_dialog, null);

                final ListView listFSO = (ListView)convertView. findViewById(R.id.list_view);
                listFSO.setAdapter(adapterCategory);

                alertDialog.setContentView(convertView);

                listFSO.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String fsonameinspinner=listFSO.getItemAtPosition(position).toString().trim();
                        selectFsoSpinner.setText(fsonameinspinner);

                        alertDialog.dismiss();
                        SpinnerFilter(fsonameinspinner);
                    }
                });

                alertDialog.show();
            }
        });

        SectionsCreationLoop();
    }

    void SectionsCreationLoop()
    {
        for(Map.Entry<String, LinkedHashMap<String, ArrayList<String>>> entry:lknhmap_SOReportFSO.entrySet())
        {
            String key=entry.getKey().toString().trim(); //FsoId+Name
            String FsoID=key.split(Pattern.quote("^"))[0];
            String FsoName=key.split(Pattern.quote("^"))[1];

            hmapFsoFlagAndSummary=entry.getValue(); //key-FsoApprovalFlags+Color, Value- Arraylist(Report summary)

            View ll_main= createLinearLayoutVertical(); // sections for each fso
            ll_main.setBackgroundResource(R.drawable.border_orngeboundary);
            ll_main.setTag(FsoID);

            View txt_view=createTextView(FsoName);
            txt_view.setBackgroundResource(R.drawable.fso_name_bck);
            txt_view.setPadding(1, 8,1, 8);
            ((TextView) txt_view).setTextColor(Color.WHITE);
            ((TextView) txt_view).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            ((TextView) txt_view).setTextSize(14);

            ((ViewGroup) ll_main).addView(txt_view);

            for(Map.Entry<String, ArrayList<String>> entrry1:hmapFsoFlagAndSummary.entrySet())
            {
                String FlagName= entrry1.getKey().toString().trim().split(Pattern.quote("^"))[0];
                String LayoutColor= entrry1.getKey().toString().trim().split(Pattern.quote("^"))[1];

                View ll_ForFlgsAndData= createLinearLayoutVertical();

                //LAYOUT HEADING FOR FLAG NAME AND IMG
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.fso_store_heading, null);

                LinearLayout ll_heading=(LinearLayout) view.findViewById(R.id.ll_heading);
                ll_heading.setTag(FsoID+"_"+FlagName+"_"+"false");
                ll_heading.setBackgroundColor(Color.parseColor(LayoutColor));

                TextView txt_heading=(TextView) view.findViewById(R.id.txt_heading);
                txt_heading.setText(FlagName);

                final ImageView img_drpdown=(ImageView) view.findViewById(R.id.img_drpdown);

                ((ViewGroup) ll_ForFlgsAndData).addView(view);
                //ENDS HERE

                ll_heading.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        String fsoID=v.getTag().toString().trim().split(Pattern.quote("_"))[0];
                        String flgName=v.getTag().toString().trim().split(Pattern.quote("_"))[1];
                        String tag_val=v.getTag().toString().trim().split(Pattern.quote("_"))[2];
                        Boolean isExpand=Boolean.valueOf(tag_val);

                        if(!isExpand)
                        {
                            v.setTag(fsoID+"_"+flgName+"_"+"true");

                            LinearLayout ll_main=(LinearLayout) ll_ParentFso.findViewWithTag(fsoID+"_"+flgName);

                            ll_main.setVisibility(View.VISIBLE);
                            img_drpdown.setImageResource(R.drawable.collapse_arrow);
                        }
                        else
                        {
                            v.setTag(fsoID+"_"+flgName+"_"+"false");

                            LinearLayout ll_main=(LinearLayout) ll_ParentFso.findViewWithTag(fsoID+"_"+flgName);

                            ll_main.setVisibility(View.GONE);
                            img_drpdown.setImageResource(R.drawable.expand_arrow);
                        }
                    }
                });

                //LAYOUT FOR VISIBILTY DEPENDING ON EXPAND-COLLAPSE
                View ll_FlgReports= createLinearLayoutVertical();
                ll_FlgReports.setVisibility(View.GONE);
                ll_FlgReports.setTag(FsoID+"_"+FlagName);

                View heading_name=createTextView("Store Name");
                LinearLayout.LayoutParams lp_name=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_name.weight=1;
                heading_name.setPadding(15, 2, 0, 2);
                ((TextView) heading_name).setGravity(Gravity.LEFT);
                heading_name.setLayoutParams(lp_name);

                View heading_address=createTextView("Store Address");
                LinearLayout.LayoutParams lp_addrss=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp_addrss.weight=1;
                heading_address.setPadding(0, 2, 15, 2);
                ((TextView) heading_address).setGravity(Gravity.RIGHT);
                heading_address.setLayoutParams(lp_addrss);

                View ll_Heading= createLinearLayoutHorizontal();
                ll_Heading.setBackgroundResource(R.drawable.summ_row_bck);

                ((ViewGroup) ll_Heading).addView(heading_name);
                ((ViewGroup) ll_Heading).addView(heading_address);

                ((ViewGroup) ll_FlgReports).addView(ll_Heading);


                ArrayList<String> FsoReportData=entrry1.getValue();

                for(int i=0;i<FsoReportData.size();i++)
                {
                    String StoreId=FsoReportData.get(i).toString().trim().split(Pattern.quote("^"))[0];
                    String StoreName=FsoReportData.get(i).toString().trim().split(Pattern.quote("^"))[1];
                    String StoreAddress=FsoReportData.get(i).toString().trim().split(Pattern.quote("^"))[2];

                    View txt_strName=createTextView(StoreName);
                    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
                    ((TextView) txt_strName).setGravity(Gravity.LEFT);
                    txt_strName.setPadding(15, 2, 0, 2);
                    lp.weight=1;

                    txt_strName.setLayoutParams(lp);

                    View txt_strAddrss=createTextView(StoreAddress);
                    LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
                    txt_strAddrss.setPadding(0, 2, 15, 2);
                    ((TextView) txt_strAddrss).setGravity(Gravity.RIGHT);
                    lp1.weight=1;
                    txt_strAddrss.setLayoutParams(lp1);

                    View ll_StoreParent= createLinearLayoutHorizontal();

                    ((ViewGroup) ll_StoreParent).addView(txt_strName);
                    ((ViewGroup) ll_StoreParent).addView(txt_strAddrss);

                    ((ViewGroup) ll_FlgReports).addView(ll_StoreParent);

                    View v1=new View(SummaryActivityOld.this);
                    v1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3));
                    ((ViewGroup) ll_FlgReports).addView(v1);
                }

                ((ViewGroup) ll_ForFlgsAndData).addView(ll_FlgReports);

                ((ViewGroup) ll_main).addView(ll_ForFlgsAndData);

            }
            ll_ParentFso.addView(ll_main);
            createViewForSpace();
        }
    }

    void SpinnerFilter(String SpinnerItem)
    {
        String HmapFsoId=hmapFSOListForReport.get(SpinnerItem);

        for(Map.Entry<String, String> entry:hmapFSOListForReport.entrySet())
        {
            String FsoId=entry.getValue().toString().trim();

            if(SpinnerItem.equals(FsoNameLsit.get(0).toString().trim()))
            {
                LinearLayout ll_filter=(LinearLayout) ll_ParentFso.findViewWithTag(FsoId);
                if(ll_filter != null)
                    ll_filter.setVisibility(View.VISIBLE);
            }
            else
            {
                if(FsoId.equals(HmapFsoId))
                {
                    LinearLayout ll_filter=(LinearLayout) ll_ParentFso.findViewWithTag(FsoId);
                    if(ll_filter != null)
                        ll_filter.setVisibility(View.VISIBLE);
                }
                else
                {
                    LinearLayout ll_filter=(LinearLayout) ll_ParentFso.findViewWithTag(FsoId);
                    if(ll_filter != null)
                        ll_filter.setVisibility(View.GONE);
                }
            }
        }
    }

    View createLinearLayoutVertical()
    {
        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(0, 3, 0, 3);
        return ll;
    }

    View createLinearLayoutHorizontal()
    {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(0, 3, 0, 3);
        return ll;
    }

    View createTextView(String text)
    {
        TextView companyTV = new TextView(this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        companyTV.setGravity(Gravity.CENTER);
        companyTV.setLayoutParams(lp);
        companyTV.setTextSize(12);
        companyTV.setPadding(1, 5, 1, 5);
        companyTV.setText(text);
        companyTV.setTextColor(Color.BLACK);
        companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return companyTV;
    }

    void createViewForSpace()
    {
        View v=new View(SummaryActivityOld.this);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10));
        ll_ParentFso.addView(v);
    }


}
