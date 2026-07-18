public class Action {
    public enum ActionType { ADD_MEMBER, REMOVE_MEMBER, ENROLL, CANCEL }

    private ActionType type;
    private Object data1;
    private Object data2;

    public Action(ActionType type, Object data1, Object data2) {
        this.type = type;
        this.data1 = data1;
        this.data2 = data2;
    }

    public ActionType getType() { return type; }
    public Object getData1() { return data1; }
    public Object getData2() { return data2; }
}