package ncu.im3069.Group16.app;

import org.json.JSONObject;

public class Interview {

    private int case_id;
    private int teacher_id;
    private int state;
    private int parent_id;
    private String Tphone;
    private String subject;
    private String teachCounty;
    private String teachRegion;
    
    
    private InterviewHelper ih = InterviewHelper.getHelper();
    
    public Interview(int case_id,int teacher_id,int state) {
    	this.case_id = case_id;
    	this.teacher_id = teacher_id;
    	this.state = state;
    }
    public Interview(int case_id,int teacher_id) {
    	this.case_id = case_id;
    	this.teacher_id = teacher_id;
    }
    
    //老師面試管理的constructor
    public Interview(int case_id, int parent_id, String cellphone, String subject, String teachCounty, String teachRegion, int state) {
    	this.case_id = case_id;
    	this.parent_id = parent_id;
    	this.Tphone = cellphone;
    	this.subject = subject;
    	this.teachCounty = teachCounty;
    	this.teachRegion = teachRegion;
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
    
    public int getParentId() {
    	return this.parent_id;
    }
    
    public String getTPhone() {
    	return this.Tphone;
    }
    
    public String getSubject() {
    	return this.subject;
    }
    
    public String getCounty() {
    	return this.teachCounty;
    }
    
    public String getRegion() {
    	return this.teachRegion;
    }
    
    public JSONObject getInterviewData() {
        JSONObject jso = new JSONObject();
        jso.put("case_id", getCaseId());
        jso.put("teacher_id", getTeacherId());
        jso.put("state", getState());

        return jso;
    }
    
    public JSONObject getTInterviewData() {
    	JSONObject jso = new JSONObject();
        jso.put("case_id", getCaseId());
        jso.put("parent_id", getParentId());
        jso.put("Tphone", getTPhone());
        jso.put("subject", getSubject());
        jso.put("teachCounty", getCounty());
        jso.put("teachRegion", getRegion());
        jso.put("state", getState());

        return jso;
    }
    
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        /** 檢查該名會員是否已經在資料庫 */
        if(this.case_id != 0) {
        	/** 若有則將目前更新後之資料更新至資料庫中 */
        	//ch.updateLoginTimes(this);
        	
        	/** 透過CaseHelper物件，更新目前之老師會員資料置資料庫中
        	 * *傳入此Case物件並回傳一個JSONObject物件
        	 */
        	data = ih.update(this);
        }
        
        return data;
    }

}