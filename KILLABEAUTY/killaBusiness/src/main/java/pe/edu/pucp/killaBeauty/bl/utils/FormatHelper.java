package pe.edu.pucp.killaBeauty.bl.utils;

public class FormatHelper {
    public static String capitalizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        StringBuilder resultado = new StringBuilder();
        boolean siguienteMayuscula = true;

        // Convertimos todo a minúsculas primero para limpiar
        for (char c : texto.toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(c)) {
                siguienteMayuscula = true;
            } else if (siguienteMayuscula) {
                c = Character.toUpperCase(c);
                siguienteMayuscula = false;
            }
            resultado.append(c);
        }

        return resultado.toString();
    }
}
