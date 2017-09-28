package ssangyong.dclass.seoulexploration.handler;

/**
 * 이벤트에서 전달 받은 데이터를 넘겨주는 객체
 */
public class CallBackEvent {
    private String event;
    private Object data;

    public CallBackEvent(String event, Object data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
