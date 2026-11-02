package ch.mcserver.goliathPaperCore.common.service;

public class NumberFormatter {

    public static String format(String input) {
        try {
            long number = Long.parseLong(input);
            String[] suffixes = {"", "K", "M", "B", "T", "Qa", "Qi"};
            double value = number;
            int index = 0;

            while (Math.abs(value) >= 1000 && index < suffixes.length - 1) {
                value /= 1000;
                index++;
            }

            return (value == (long) value ? String.valueOf((long) value) : String.format(java.util.Locale.US, "%.1f", value)) + suffixes[index];

        } catch (NumberFormatException e) {
            return "N/A";
        }
    }
}