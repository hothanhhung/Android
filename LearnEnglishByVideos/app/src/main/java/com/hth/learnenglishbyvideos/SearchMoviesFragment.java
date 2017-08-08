package com.hth.learnenglishbyvideos;

import java.util.ArrayList;
import java.util.Calendar;

import com.hth.data.YouTubeParserJSObject;
import com.hth.data.YouTubeService;
import com.hth.data.YouTubeVideo;
import com.hth.learnenglishbyvideos.R;
import com.hth.utils.UIUtils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

public class SearchMoviesFragment extends Fragment {

	private ArrayList<YouTubeVideo> lstAdvantageYouTubeVideo = null;
	private View rootView;
	private ProgressDialog progressDialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_searchmovies,
				container, false);

		rootView.findViewById(R.id.layoutResultOfSearch).setVisibility(View.GONE);
		
		Spinner spinner = (Spinner) rootView.findViewById(R.id.spnKindOfSort);
		ArrayAdapter<CharSequence> spnadapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.sort_kind_array,
						android.R.layout.simple_spinner_item);
		spnadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spnadapter);

		lstAdvantageYouTubeVideo = new ArrayList<YouTubeVideo>();

		EditText txtFromDate = (EditText) rootView
				.findViewById(R.id.txtFromDate);
		txtFromDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showStartDateDialog(v);
			}
		});
		EditText txtToDate = (EditText) rootView.findViewById(R.id.txtToDate);
		txtToDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showStartDateDialog(v);
			}
		});

		Button btSearchMovies = (Button) rootView
				.findViewById(R.id.btSearchMovies);
		btSearchMovies.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchMovies();
			}
		});
		return rootView;
	}
	

	private String txtQuerySearch = "";
	private String txtFromDateSearch = "";
	private String txtToDateSearch = "";
	private String txtOrderSearch = "";
	private String txtPageSearch = "";
	private int pageNumber = 1;
	private void searchMovies()
	{
		pageNumber = 1;
		txtPageSearch = "";
		txtQuerySearch = ((EditText) rootView.findViewById(R.id.txtQuery)).getText().toString();
		txtFromDateSearch = ((EditText) rootView.findViewById(R.id.txtFromDate)).getText().toString();
		txtToDateSearch = ((EditText) rootView.findViewById(R.id.txtToDate)).getText().toString();
		/*
		 * <item>�?ộ chính xác</item>
	        <item>Ngày</item>
	        <item>Lượt xem</item>
	        <item>Lượt thích</item>
	        <item>Tiêu đ�?</item>
		 */
		Spinner spnKindOfSort = (Spinner) rootView.findViewById(R.id.spnKindOfSort);
		switch (spnKindOfSort.getSelectedItemPosition()) {
		case 0:
			txtOrderSearch = "relevance";
			break;
		case 1:
			txtOrderSearch = "date";
			break;
		case 2:
			txtOrderSearch = "viewCount";
			break;
		case 3:
			txtOrderSearch = "rating";
			break;
		case 4:
			txtOrderSearch = "title";
			break;
		default:
			break;
		} ;
		loadAdvantageYouTubeVideo();
	}

	private YouTubeParserJSObject youTubeParserJSObject = null;
	private void loadAdvantageYouTubeVideo() {
		/*if(progressDialog == null)
		{
			progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait for a moment...", true);;
		}else{
			progressDialog.show();
		}*/

		youTubeParserJSObject = YouTubeService
				.searchPublishedVideos(txtQuerySearch, txtFromDateSearch, txtToDateSearch, txtOrderSearch, txtPageSearch);

		rootView.findViewById(R.id.layoutResultOfSearch).setVisibility(View.VISIBLE);

		if(youTubeParserJSObject==null) lstAdvantageYouTubeVideo = new ArrayList<YouTubeVideo>();
		else lstAdvantageYouTubeVideo = youTubeParserJSObject.getYouTubeVideos();
		
		
		if(youTubeParserJSObject==null || youTubeParserJSObject.getTotalResults()<1 || lstAdvantageYouTubeVideo.size() < 1)
		{
			rootView.findViewById(R.id.layoutBannerSearch).setVisibility(View.VISIBLE);
			rootView.findViewById(R.id.cbDisplayBannerSearch).setVisibility(View.GONE);
			((TextView) rootView.findViewById(R.id.txtInfoResultSearch)).setText("Found no video");
		}else 
		{
			int totalNumber = lstAdvantageYouTubeVideo.size() < youTubeParserJSObject.getResultsPerPage()? lstAdvantageYouTubeVideo.size() : youTubeParserJSObject.getTotalResults();
			if(youTubeParserJSObject.getPrevPageToken()!="") totalNumber = youTubeParserJSObject.getTotalResults();
			int totalPage = Math.round((totalNumber + youTubeParserJSObject.getResultsPerPage() -  1)/youTubeParserJSObject.getResultsPerPage());
			((TextView) rootView.findViewById(R.id.txtInfoResultSearch)).setText("Found "+totalNumber+" videos. Display page "+pageNumber+"/"+totalPage);
			
			rootView.findViewById(R.id.layoutBannerSearch).setVisibility(View.GONE);
			
			CheckBox cbDisplayBannerSearch = (CheckBox)rootView.findViewById(R.id.cbDisplayBannerSearch);
			cbDisplayBannerSearch.setVisibility(View.VISIBLE);			
			cbDisplayBannerSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener ()
			{
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			    {
			    	LinearLayout layoutBannerSearch = (LinearLayout)rootView.findViewById(R.id.layoutBannerSearch);
					if(isChecked) layoutBannerSearch.setVisibility(View.VISIBLE);
					else layoutBannerSearch.setVisibility(View.GONE);

			    }
			});
			cbDisplayBannerSearch.setChecked(false);
		}
		
		// list of results
		ListView listView = (ListView) rootView.findViewById(R.id.lvMovies);
		MovieYoutubeItemAdapter adapter = new MovieYoutubeItemAdapter(
				getActivity(), lstAdvantageYouTubeVideo, getResources());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// check internet
				if (!UIUtils.isOnline(getActivity())) {
					UIUtils.showAlertErrorNoInternet(getActivity(), false);
					return;
				}

				Intent playerYoutubeMovieIntent = new Intent(getActivity(),
						PlayerYoutubeMovieActivity.class);
				playerYoutubeMovieIntent.putExtra(
						com.hth.data.DeveloperConst.EXTAR_MOVIE_ID, view
								.findViewById(R.id.title).getTag().toString());
				startActivity(playerYoutubeMovieIntent);
			}
		});

		// go back results of search
		ImageButton btGoBackPage = (ImageButton) rootView
				.findViewById(R.id.btGoBackPage);
		if (youTubeParserJSObject == null || youTubeParserJSObject.getPrevPageToken() == "")
			btGoBackPage.setVisibility(View.GONE);
		else {
			btGoBackPage.setVisibility(View.VISIBLE);
			btGoBackPage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(youTubeParserJSObject!= null)
					{
						pageNumber--;
						txtPageSearch = youTubeParserJSObject.getPrevPageToken();
						loadAdvantageYouTubeVideo();
					}
					
				}
			});
		}
		
		// goto next results of search
		ImageButton btGoNextPage = (ImageButton) rootView
				.findViewById(R.id.btGoNextPage);
		if (youTubeParserJSObject == null || youTubeParserJSObject.getNextPageToken() == "" || lstAdvantageYouTubeVideo.size() < youTubeParserJSObject.getResultsPerPage())
			btGoNextPage.setVisibility(View.GONE);
		else {
			btGoNextPage.setVisibility(View.VISIBLE);
			btGoNextPage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(youTubeParserJSObject!= null)
					{
						pageNumber++;
						txtPageSearch = youTubeParserJSObject.getNextPageToken();
						loadAdvantageYouTubeVideo();
					}
				}
			});
		}
		/*if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}*/
	}

	View selectedDate = null;

	public void showStartDateDialog(View v) {
		selectedDate = v;
		DialogFragment dialogFragment = new StartDatePicker();
		dialogFragment.show(getActivity().getFragmentManager(),
				"start_date_picker");
	}

	Calendar c = Calendar.getInstance();
	int startYear = c.get(Calendar.YEAR);
	int startMonth = c.get(Calendar.MONTH);
	int startDay = c.get(Calendar.DAY_OF_MONTH);

	@SuppressLint("ValidFragment")
	private class StartDatePicker extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@SuppressWarnings("deprecation")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			// Use the current date as the default date in the picker
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
					startYear, startMonth, startDay);
			dialog.setButton2(getResources().getString(R.string.strBoQua), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedDate = null;
					dialog.cancel();
				}
			});
			dialog.setButton(getResources().getString(R.string.strXoaNgay),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (selectedDate != null)
								((EditText) selectedDate).setText("");
							selectedDate = null;
							dialog.cancel();
						}
					});
			dialog.setButton3(getResources().getString(R.string.strChonNgay),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			return dialog;
		}

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			// Do something with the date chosen by the user
			startYear = year;
			startMonth = monthOfYear;
			startDay = dayOfMonth;

			if (selectedDate != null)
				((EditText) selectedDate).setText(""						
						+ (startMonth < 9 ? "0"
								+ (startMonth + 1)
								: startMonth + 1) + "-"
						+ (startDay < 10 ? "0" + startDay
								: startDay)
						+ "-"
						+ startYear);
			selectedDate = null;
		}
	}
}
