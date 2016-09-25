package it.localhost.trafficdroid.ethereogeneousItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.dto.BadNewsDTO;
import localhost.toolkit.widget.HeterogeneousItem;

public class BadNewsDialogItem extends HeterogeneousItem<BadNewsDTO> {
	private static final String bn_acc = "incidente";
	private static final String bn_anh = "animali";
	private static final String bn_bkd = "veicolo fermo";
	private static final String bn_emv = "colonnine SOS";
	private static final String bn_fig = "incendio";
	private static final String bn_fod = "nebbia";
	private static final String bn_fld = "straripamenti";
	private static final String bn_fop = "nebbia a banchi";
	private static final String bn_ibu = "nevischio";
	private static final String bn_los1 = "code";
	private static final String bn_los2 = "traffico";
	private static final String bn_ocm = "perdita di carico";
	private static final String bn_peo = "pedoni";
	private static final String bn_pra1 = "pioggia";
	private static final String bn_pra2 = "temporale";
	private static final String bn_pss = "personale su strada";
	private static final String bn_res = "chius";
	private static final String bn_rsr = "riduzione di carreggiata";
	private static final String bn_sab = "area di servizio";
	private static final String bn_sat = "senso unico alternato";
	private static final String bn_sdc = "scambio di carreggiata";
	private static final String bn_sm = "catene";
	private static final String bn_sn = "mezzi spargisale";
	private static final String bn_sne1 = "neve";
	private static final String bn_sne2 = "grandine";
	private static final String bn_spc = "controllo velocità";
	private static final String bn_vfr = "veicolo in fiamme";
	private static final String bn_win = "vento";
	private static final String bn_cls = "carburante chius";
	private static final String bn_ecr = "manifestazione";
	private static DateFormat sdfBnFormat;

	public BadNewsDialogItem(Context context, BadNewsDTO extra) {
		super(context, extra);
		sdfBnFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
		View view = layoutInflater.inflate(R.layout.dialog_item_badnews, viewGroup, false);
		view.setTag(R.id.text1, view.findViewById(android.R.id.text1));
		view.setTag(R.id.badNewsDate, view.findViewById(R.id.badNewsDate));
		view.setTag(R.id.text2, view.findViewById(android.R.id.text2));
		return view;
	}

	@Override
	public void onResume(View view) {
		((TextView) view.getTag(R.id.text1)).setText(extra.getTitle());
		((TextView) view.getTag(R.id.text2)).setText(extra.getDescription());
		TextView badNewsDate = (TextView) view.getTag(R.id.badNewsDate);
		badNewsDate.setText(sdfBnFormat.format(extra.getDate()));
		int drawable;
		if (extra.getTitle().contains(bn_los1) || extra.getTitle().contains(bn_los2))
			drawable = R.drawable.bn_los;
		else if (extra.getTitle().contains(bn_acc))
			drawable = R.drawable.bn_acc;
		else if (extra.getTitle().contains(bn_anh))
			drawable = R.drawable.bn_anh;
		else if (extra.getTitle().contains(bn_pss))
			drawable = R.drawable.bn_pss;
		else if (extra.getTitle().contains(bn_bkd))
			drawable = R.drawable.bn_bkd;
		else if (extra.getTitle().contains(bn_fop))
			drawable = R.drawable.bn_fop;
		else if (extra.getTitle().contains(bn_ibu))
			drawable = R.drawable.bn_ibu;
		else if (extra.getTitle().contains(bn_fig))
			drawable = R.drawable.bn_fig;
		else if (extra.getTitle().contains(bn_emv))
			drawable = R.drawable.bn_emv;
		else if (extra.getTitle().contains(bn_fld))
			drawable = R.drawable.bn_fld;
		else if (extra.getTitle().contains(bn_fod))
			drawable = R.drawable.bn_fod;
		else if (extra.getTitle().contains(bn_ocm))
			drawable = R.drawable.bn_ocm;
		else if (extra.getTitle().contains(bn_peo))
			drawable = R.drawable.bn_peo;
		else if (extra.getTitle().contains(bn_pra1) || extra.getTitle().contains(bn_pra2))
			drawable = R.drawable.bn_pra;
		else if (extra.getTitle().contains(bn_sn))
			drawable = R.drawable.bn_sn;
		else if (extra.getTitle().contains(bn_sne1) || extra.getTitle().contains(bn_sne2))
			drawable = R.drawable.bn_sne;
		else if (extra.getTitle().contains(bn_sm))
			drawable = R.drawable.bn_sm;
		else if (extra.getTitle().contains(bn_rsr))
			drawable = R.drawable.bn_rsr;
		else if (extra.getTitle().contains(bn_sab))
			drawable = R.drawable.bn_sab;
		else if (extra.getTitle().contains(bn_sat))
			drawable = R.drawable.bn_sat;
		else if (extra.getTitle().contains(bn_sdc))
			drawable = R.drawable.bn_sdc;
		else if (extra.getTitle().contains(bn_spc))
			drawable = R.drawable.bn_spc;
		else if (extra.getTitle().contains(bn_win))
			drawable = R.drawable.bn_win;
		else if (extra.getTitle().contains(bn_vfr))
			drawable = R.drawable.bn_vfr;
		else if (extra.getTitle().contains(bn_cls))
			drawable = R.drawable.bn_cls;
		else if (extra.getTitle().contains(bn_res))
			drawable = R.drawable.bn_res;
		else if (extra.getTitle().contains(bn_ecr))
			drawable = R.drawable.bn_ecr;
		else
			drawable = android.R.drawable.ic_dialog_alert;
		badNewsDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable);
	}
}