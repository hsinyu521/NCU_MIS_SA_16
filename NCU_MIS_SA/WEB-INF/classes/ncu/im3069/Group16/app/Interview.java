package ncu.im3069.Group16.app;

import org.json.JSONObject;

public class Interview {

    private int case_id;

    private int teacher_id;

    private int state;
    
    public Interview(int case_id,int teacher_id,int state) {
    	this.case_id = case_id;
    	this.teacher_id = teacher_id;
    	this.state = state;
    }
    public int getCaseId() {
    	return this.case_id;
    }
    public int getTeacherId() {
    	return this.teacher_id;
    }
    public int getState() {
    	return this.state;
    }
    
    public JSONObject getInterviewData() {
        JSONObject jso = new JSONObject();
        jso.put("case_id", getCaseId());
        jso.put("teacher_id", getTeacherId());
        jso.put("state", getState());

        return jso;
    }

}
