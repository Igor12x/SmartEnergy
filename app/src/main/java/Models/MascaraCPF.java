package Models;

import android.text.TextWatcher;
import android.widget.EditText;
import android.text.Editable;


public class MascaraCPF implements TextWatcher {
        private EditText editText;
        private int limiteCaracter;
        private boolean isRemovingText = false;

        public MascaraCPF(EditText editText, int limiteCaracter) {
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
            String cpf = s.toString();

            cpf = cpf.replaceAll("[^\\d]", "");

            // Verifica se o CPF excede o limite de caracteres
            if (cpf.length() > limiteCaracter) {
                cpf = cpf.substring(0, limiteCaracter);
            }

            // Aplica a máscara do CPF (###.###.###-##)
            if (cpf.length() >= 3) {
                cpf = cpf.substring(0, 3) + "." + cpf.substring(3);
            }
            if (cpf.length() >= 7) {
                cpf = cpf.substring(0, 7) + "." + cpf.substring(7);
            }
            if (cpf.length() >= 11) {
                cpf = cpf.substring(0, 11) + "-" + cpf.substring(11);
            }

            // Define o texto formatado no EditText
            editText.removeTextChangedListener(this);
            editText.setText(cpf);
            editText.setSelection(cpf.length());
            editText.addTextChangedListener(this);
        }
    }


