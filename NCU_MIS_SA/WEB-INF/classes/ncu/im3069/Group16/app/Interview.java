package ncu.im3069.Group16.app;

import org.json.JSONObject;

public class Interview {

    private int case_id;
    private int teacher_id;
    private int state;
    //join table
    private int parent_id;
    private String Pphone;
    private String Tname;
    private String Tphone;
    private String subject;
    private String grade;
    private String teachCounty;
    private String teachRegion;
    private String wage;
    
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
    public Interview(int case_id, int parent_id, String cellphone, String subject, String grade, String teachCounty, String teachRegion, String wage, int state) {
    	this.case_id = case_id;
    	this.parent_id = parent_id;
    	this.Pphone = cellphone;
    	this.subject = subject;
    	this.grade = grade;
    	this.teachCounty = teachCounty;
    	this.teachRegion = teachRegion;
    	this.wage = wage;
    	this.state = state;
    }
    
    //家長面試管理的constructor
    public Interview(int case_id,int parent_id, int teacher_id, String teacher_name, String wage, int state){
    	this.case_id = case_id;
    	this.parent_id = parent_id;
    	this.teacher_id = teacher_id;
    	this.Tname = teacher_name;
    	this.wage = wage;
    	this.state = state;
    }
    
    //案件編號面試管理的constructor
    public Interview(int case_id, int teacher_id, int state, String teacher_name, String teacher_cellphone){
    	this.case_id = case_id;
    	this.teacher_id = teacher_id;
    	this.state = state;
    	this.Tname = teacher_name;
    	this.Tphone = teacher_cellphone;
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
    
    public String getPPhone() {
    	return this.Pphone;
    }
    
    public String getTName() {
    	return this.Tname;
    }
    
    public String getTPhone() {
    	return this.Tphone;
    }
    
    public String getSubject() {
    	return this.subject;
    }
    
    public String getGrade() {
    	return this.grade;
    }
    
    public String getCounty() {
    	return this.teachCounty;
    }
    
    public String getRegion() {
    	return this.teachRegion;
    }
    
    public String getWage() {
    	return this.wage;
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
        jso.put("Pphone", getPPhone());
        jso.put("subject", getSubject());
        jso.put("grade", getGrade());
        jso.put("teachCounty", getCounty());
        jso.put("teachRegion", getRegion());
        jso.put("wage", getWage());
        jso.put("state", getState());

        return jso;
    }
    
    public JSONObject getPInterviewData() {
    	JSONObject jso = new JSONObject();
        jso.put("case_id", getCaseId());
        jso.put("parent_id", getParentId());
        jso.put("teacher_id", getTeacherId());
        jso.put("Tname", getTName());
        jso.put("wage", getWage());
        jso.put("state", getState());

        return jso;
    }
    public JSONObject getCInterviewData() {
    	JSONObject jso = new JSONObject();
        jso.put("case_id", getCaseId());
        jso.put("teacher_id", getTeacherId());
        jso.put("state", getState());
        jso.put("Tname", getTName());
        jso.put("Tphone", getTPhone());

        return jso;
    }
    
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        /** 檢查該面試是否已經在資料庫 */
        if(this.case_id != 0) {
        	/** 若有則將目前更新後之資料更新至資料庫中 */
        	/** 透過InterviewHelper物件，更新目前資料置資料庫中
        	 * *傳入此Interview物件並回傳一個JSONObject物件
        	 */
        	data = ih.update(this);
        }
        return data;
    }

}