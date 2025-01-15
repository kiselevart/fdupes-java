package kiselevart;

public class ValueFormatter {
    public static String formattedValue(long number) { 
        String numberString = String.valueOf(number);
        StringBuilder formattedNumber = new StringBuilder();

        int length = numberString.length();
        int commas = (length - 1) / 3;

        for (int i = 0; i < length; i++) {
            formattedNumber.append(numberString.charAt(i));
            if ((length - i - 1) % 3 == 0 && commas > 0) {
                formattedNumber.append(',');
                commas--;
            }
        }

        return formattedNumber.toString();
    }
}
