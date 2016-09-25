package it.localhost.trafficdroid.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.AdManager;
import it.localhost.trafficdroid.common.ListExit;
import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.PedaggioDTO;
import it.localhost.trafficdroid.service.PedaggioService;
import localhost.toolkit.app.MessageDialogFragment;

public class PedaggioFragment extends Fragment {
	private TextView result;
	private View progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.pedaggio, container, false);
		result = (TextView) v.findViewById(R.id.result);
		progress = v.findViewById(R.id.progress);
		final AutoCompleteTextView moneyFrom = (AutoCompleteTextView) v.findViewById(R.id.moneyFrom);
		final AutoCompleteTextView moneyTo = (AutoCompleteTextView) v.findViewById(R.id.moneyTo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ListExit.getInstance().getKeys());
		moneyFrom.setAdapter(adapter);
		moneyTo.setAdapter(adapter);
		v.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String from = ListExit.getInstance().get(moneyFrom.getText().toString()).toString();
				String to = ListExit.getInstance().get(moneyTo.getText().toString()).toString();
				if (from != null && to != null)
					new PedaggioAsyncTask().execute(from, to);
				else
					new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), getString(R.string.wrongData), false);
			}
		});
		new AdManager().load(getActivity(), ((AdView) v.findViewById(R.id.adView)), true);
		return v;
	}

	private class PedaggioAsyncTask extends PedaggioService {
		@Override
		protected void onPreExecute() {
			progress.setVisibility(View.VISIBLE);
			result.setVisibility(View.GONE);
			((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

		@Override
		protected void onPostExecute(BaseDTO dto) {
			progress.setVisibility(View.GONE);
			result.setVisibility(View.VISIBLE);
			if (dto.isSuccess())
				result.setText("€ " + ((PedaggioDTO) dto).getPedaggio());
			else
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), dto.getMessage(), false);
		}
	}
}
