package com.jacobniebloom.ourapplication;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private Button btn_ring;
	private Button shareLocation;
	private TextView txtLat;
	private LocationManager locationManager;


//	@Override
//    public void onLocationChanged(Location location) {
//
//    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		//set up dah buttonz
		btn_ring = (Button) findViewById(R.id.ringButton);

		btn_ring.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("OurApp", "Button Clicked");
				sendSMSManager("text");
			}
		});


		shareLocation = (Button) findViewById(R.id.shareLocation);
		final LocationListener locationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.v("LocationListener", "onStatusChanged");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.v("LocationListener", "onProviderEnabled");
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.v("LocationListener", "onProviderDisabled");
			}

			@Override
			public void onLocationChanged(Location location) {
				Log.v("LocationListener", "onLocationChanged");
				txtLat = (TextView) findViewById(R.id.textview);
				String locationText = "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude();
				txtLat.setText(locationText);
				sendSMSManager("GPS");
			}
		};

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		final String dahProvider = locationManager.getBestProvider(criteria, true);

		Log.v("Provider", dahProvider);

		shareLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				locationManager.requestSingleUpdate(dahProvider, locationListener, Looper.myLooper());
			}
		});
	}


	public void sendSMSManager(String feature) {
		if (feature.equals("text")) {
			Toast.makeText(getApplicationContext(), "Starting text", Toast.LENGTH_SHORT).show();
			try {
				SmsManager.getDefault().sendTextMessage("3124205033", null, "Sar, I am outside", null, null);
				Toast.makeText(getApplicationContext(), "Text Sent", Toast.LENGTH_LONG).show();
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		} else if (feature.equals("GPS")) {
			try {
				SmsManager.getDefault().sendTextMessage("3124205033", null, txtLat.getText().toString(), null, null);
			} catch (Exception e) {
				Log.e("OurApp", "ERROR GPS");
				e.printStackTrace();

			}

		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
				.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.title_section1);
				break;
			case 2:
				mTitle = getString(R.string.title_section2);
				break;
			case 3:
				mTitle = getString(R.string.title_section3);
				break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		public PlaceholderFragment() {
		}

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(
					getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

}