package jack.com.servicekeep.model;

public class TimeRequest {
    public int id;
    public long time;
    public int active;

    public TimeRequest(int id, long currentTimeMillis, int active) {
        this.id = id;
        this.time = currentTimeMillis;
        this.active = active;
    }

    public TimeRequest() {

    }
}
