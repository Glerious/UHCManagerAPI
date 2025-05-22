package fr.glerious.uhcmanagerapi.timeline;

public class Timer {

    private static Integer time = 0;

    public Integer getTime()
    {
        return time;
    }

    public void setTime(Integer time) {
        Timer.time = time;
    }

    public void increment() {
        time++;
    }

    public void reset()
    {
        setTime(0);
    }

    public String asClock() {
        int hour = (int) ((double) (time / 3600));
        int minute = (int) ((double) (time / 60) - 60 * hour);
        int secondes = time - 3600 * hour - 60 * minute;

        StringBuilder timer = new StringBuilder();

        appendLeadingZero(timer, hour);
        timer.append(":");
        appendLeadingZero(timer, minute);
        timer.append(":");
        appendLeadingZero(timer, secondes);

        return timer.toString();
    }

    private void appendLeadingZero(StringBuilder sb, int value) {
        if (value < 10) sb.append("0");
        sb.append(value);
    }
}

