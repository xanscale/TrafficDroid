package it.localhost.trafficdroid.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.MainActivity;
import it.localhost.trafficdroid.common.AdManager;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.PatenteDTO;
import it.localhost.trafficdroid.service.PatenteService;
import localhost.toolkit.app.MessageDialogFragment;

public class PatenteFragment extends Fragment {
	private static final String BLANK = "";
	private TextView patenteSaldo, patenteNumero, patenteScadenza;
	private View progress, result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.patente, container, false);
		final EditText usrEdit = (EditText) v.findViewById(R.id.patenteUsr);
		final EditText pwdEdit = (EditText) v.findViewById(R.id.patentePwd);
		patenteSaldo = (TextView) v.findViewById(R.id.patenteSaldo);
		patenteNumero = (TextView) v.findViewById(R.id.patenteNumero);
		patenteScadenza = (TextView) v.findViewById(R.id.patenteScadenza);
		progress = v.findViewById(R.id.progress);
		result = v.findViewById(R.id.result);
		usrEdit.setText(Utility.getPatenteUsr(getActivity()));
		pwdEdit.setText(Utility.getPatentePwd(getActivity()));
		v.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String usr = usrEdit.getText().toString();
				String pwd = pwdEdit.getText().toString();
				Utility.setPatenteUsr(getActivity(), usr);
				Utility.setPatentePwd(getActivity(), pwd);
				if (!usr.equals(BLANK) && !pwd.equals(BLANK))
					new PatenteAsyncTask().execute(usr, pwd);
				else
					new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), getString(R.string.wrongData), false);
			}
		});
		((MainActivity) getActivity()).setScreenName(4);
		new AdManager().load(getActivity(), ((AdView) v.findViewById(R.id.adView)), true);
		return v;
	}

	private class PatenteAsyncTask extends PatenteService {
		@Override
		protected void onPreExecute() {
			progress.setVisibility(View.VISIBLE);
			result.setVisibility(View.GONE);
			((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

		@Override
		protected void onPostExecute(BaseDTO abstractResult) {
			progress.setVisibility(View.GONE);
			result.setVisibility(View.VISIBLE);
			if (abstractResult.isSuccess()) {
				PatenteDTO patente = (PatenteDTO) abstractResult;
				patenteSaldo.setText(patente.getSaldo());
				patenteNumero.setText(patente.getNumeoPatente());
				patenteScadenza.setText(patente.getScadenzaPatente());
			} else
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), abstractResult.getMessage(), false);
		}
	}
}
