package Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ResidenciaAdapter extends ArrayAdapter<Residencia> {
    public ResidenciaAdapter(Context context, List<Residencia> residencias) {
        super(context, android.R.layout.simple_spinner_item, residencias);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.i("LOGRADOURO", residencias.get(0).getLogradouro() + "");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getLogradouro());
        return view;
    }
}
