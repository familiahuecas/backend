package com.familiahuecas.backend.util;

public class TableNameConverter {

    // Método para convertir un nombre de tabla a hexadecimal
    public static String encodeToHex(String tableName) {
        StringBuilder hexString = new StringBuilder();
        for (char character : tableName.toCharArray()) {
            hexString.append(Integer.toHexString(character));
        }
        return hexString.toString();
    }

    // Método para convertir un nombre de tabla desde hexadecimal a texto
    public static String decodeFromHex(String hexString) {
        StringBuilder decodedString = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexChar = hexString.substring(i, i + 2);
            decodedString.append((char) Integer.parseInt(hexChar, 16));
        }
        return decodedString.toString();
    }

    public static void main(String[] args) {
        // Ejemplo de uso

        String originalName = "detalleadelanto";
        String encodedName = encodeToHex(originalName);
        String decodedName = decodeFromHex("756269636163696f6e");


        System.out.println("Original: " + originalName);
        System.out.println("Hexadecimal: " + encodedName);
        System.out.println("Decodificado: " + decodedName);
    }
}
