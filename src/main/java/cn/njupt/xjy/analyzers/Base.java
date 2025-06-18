package cn.njupt.xjy.analyzers;

public class Base {
    protected static Boolean isPointerType(String type) {
        return type.equals("ptr") || type.endsWith("*");
    }

    protected static Boolean isIdentity(String identity) {
        return !identity.contains("%");
    }

    protected static String getIdentity(String caller, String identity) {
        if (identity.startsWith("%")) {
            return String.format("%s:%s", caller, identity);
        } else {
            return identity;
        }
    }

    protected static String getPercent(Float content) {
        if (content.isNaN() || content.isInfinite()) {
            return "NaN";
        } else {
            return String.format("%.2f%%", content*100);
        }
    }

    protected static String getTimeString(Long millisecond) {
        if (millisecond < 1000) {
            return String.format("%d(ms)", millisecond);
        }
        Double second = (double) millisecond / 1000;
        if (second < 60) {
            return String.format("%.2f(s)", second);
        }
        Double minute = (double) second / 60;
        if (minute < 60) {
            return String.format("%.2f(min)", minute);
        }
        Double hour = (double) minute / 60;
        return String.format("%.2f(hour)", hour);
    }

    protected static void showTitle(String title, Integer size) {
        if (title == null) {
            System.out.println("=".repeat(size));
            return;
        }
        Integer margin = (size - title.length()) / 2;
        if (margin <= 0) {
            System.out.println(title);
            return;
        }
        String space = "=".repeat(margin);
        System.out.printf("%s%s%s\n", space, title, space);
    }
}