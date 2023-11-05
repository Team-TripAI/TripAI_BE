package com.milkcow.tripai.plan.embedded;

public enum DayOfWeek {
    SUN(0, "Sunday"),
    MON(1, "Monday"),
    TUE(2, "Tuesday"),
    WED(3, "Wednesday"),
    THU(4, "Thursday"),
    FRI(5, "Friday"),
    SAT(6, "Saturday");

    private final int dayNum;
    private final String dayName;

    DayOfWeek(int dayNum, String dayName) {
        this.dayNum = dayNum;
        this.dayName = dayName;
    }

    public static DayOfWeek of(int dayNum) {
        switch (dayNum) {
            case 0:
                return SUN;
            case 1:
                return MON;
            case 2:
                return DayOfWeek.TUE;
            case 3:
                return DayOfWeek.WED;
            case 4:
                return DayOfWeek.THU;
            case 5:
                return DayOfWeek.FRI;
            case 6:
                return DayOfWeek.SAT;
            default:
                throw new IllegalArgumentException("유효하지 않은 요일 값");
        }
    }

    @Override
    public String toString() {
        return dayName + '\'';
    }
}