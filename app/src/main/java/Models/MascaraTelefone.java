package Models;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MascaraTelefone implements TextWatcher {
    private EditText editText;
    private int limiteCaracter;
    private boolean isRemovingText = false;

    public MascaraTelefone(EditText editText, int limiteCaracter) {
        this.editText = editText;
        this.limiteCaracter = limiteCaracter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nada a fazer aqui
        isRemovingText = count > after;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Nada a fazer aqui
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (isRemovingText) {
            // Se o usuário está removendo o texto, não aplica a máscara
            isRemovingText = false;
            return;
        }
        String phone = s.toString();

        // Remove todos os caracteres não numéricos
        phone = phone.replaceAll("[^\\d]", "");

        if (phone.length() > limiteCaracter) {
            phone = phone.substring(0, limiteCaracter);
        }


        // Verifica o tamanho do número de telefone para aplicar a máscara correta

        if (phone.length() <= 10) {
            // Aplica a máscara para telefones com até 10 dígitos (## ####-####)
            if (phone.length() >= 2) {
                phone = "(" + phone.substring(0, 2) + ") " + phone.substring(2);
            }
            if (phone.length() >= 9) {
                phone = phone.substring(0, 9) + "-" + phone.substring(9);
            }
        } else {
            // Aplica a máscara para telefones com mais de 10 dígitos (## #####-####)
            if (phone.length() >= 2) {
                phone = "(" + phone.substring(0, 2) + ") " + phone.substring(2);
            }
            if (phone.length() >= 10) {
                phone = phone.substring(0, 10) + "-" + phone.substring(10);
            }

        }

        // Define o texto formatado no EditText
        editText.removeTextChangedListener(this);
        editText.setText(phone);
        editText.setSelection(phone.length());
        editText.addTextChangedListener(this);

    }

}
