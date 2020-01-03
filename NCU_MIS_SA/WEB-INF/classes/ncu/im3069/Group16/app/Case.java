package ncu.im3069.Group16.app;

import java.sql.*;
import org.json.*;


public class Case{

    private int id;
    private int parent_id;
    private String grade;
    private String subject;
    private String teach_county;
    private String teach_region;
    private String wage;
    private String teachtime;
    private String teachExperience;
    private int state;
    private Timestamp created;
    private Timestamp modified;
    private CaseHelper ch = CaseHelper.getHelper();
    /**
     * *實例化（Instantiates）一個新的（new）Order 物件<br>
     ** 採用多載（overload）方法進行，此建構子用於建立案件資料時，產生一個新的案件
     *
     * @param parent_id 外鍵：家長id
     * @param grade 教學對象 
     * @param subject 教學科目
     * @param teach_county 上課縣市
     * @param teach_regin 上課地區
     * @param wage時薪
     * @param teachtime 上課時間
     * @param teachExperience 教學經驗要求 
     * @param state 案件狀態
     * @param create 案件創建時間
     * @param modify 案件修改時間
     */
    public Case(int parent_id,String grade,String subject,String teach_county, String teach_region
    		,String wage,String teachtime,String teachExperience) {
    	//1223 hsin 應該還是要state(不用放在參數)，然後每次新增都是0
    			this.parent_id=parent_id;
    			this.grade=grade;
    			this.subject=subject;
    			this.teach_county=teach_county;
    			this.teach_region=teach_region;
    			this.wage=wage;
    			this.teachtime=teachtime;
    			this.teachExperience=teachExperience;
    			this.state=0;
    }

    /**
     * *實例化（Instantiates）一個新的（new）Order 物件<br>
     * *採用多載（overload）方法進行，此建構子用於修改案件資料時，新改資料庫已存在的案件
     *
     * @param parent_id 外鍵：家長id
     * @param grade 教學對象 
     * @param subject 教學科目
     * @param teach_county 上課縣市
     * @param teach_regin 上課地區
     * @param wage時薪
     * @param teachtime 上課時間
     * @param teachExperience 教學經驗要求 
     * @param state 案件狀態
     * @param create 案件創建時間
     * @param modify 案件修改時間
     */
    public Case(int id,int parent_id,String grade,String subject,String teach_county, String teach_region
    		,String wage,String teachtime,String teachExperience,int state) {
    	//1224 hsin老師更改和顯示都是用這個建構子，所以所有的資料都要有(外鍵、狀態、時間)
        this.id = id;
        this.parent_id = parent_id;
		this.grade=grade;
		this.subject=subject;
		this.teach_county=teach_county;
		this.teach_region=teach_region;
		this.wage=wage;
		this.teachtime=teachtime;
		this.teachExperience=teachExperience;
		this.state=state;
		//this.modified=modified;
		//this.created=created;
    }

    /**
     * *設定案件id和編號
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getParent_id() {
        return this.parent_id;
    }

    public String getGrade() {
        return this.grade;
    }
    
    public String getSubject() {
        return this.subject;
    }

    public String getCounty() {
        return this.teach_county;
    }

    public String getRegion() {
        return this.teach_region;
    }

    public String getWage() {
        return this.wage;
    }

    public String getTeachTime() {
        return this.teachtime;
    }

    public String getTeachExperience() {
        return this.teachExperience;
    }

    public int getState() {
        return this.state;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public Timestamp getModifyTime() {
        return this.modified;
    }

    public JSONObject update() {	//1218 min
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        if(this.id != 0) {
        	/** 透過CaseHelper物件，更新目前之老師會員資料置資料庫中
        	 * *傳入此Case物件並回傳一個JSONObject物件
        	 */
        	data = ch.update(this);
        }
        
        return data;
    }

    /**
     * *取得案件基本資料
     *
     * @return JSONObject 取得案件基本資料
     */
    public JSONObject getCaseData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("parent_id", getParent_id());
        jso.put("grade", getGrade());
        jso.put("subject", getSubject());
        jso.put("teach_county", getCounty());
        jso.put("teach_region", getRegion());
        jso.put("wage", getWage());
        jso.put("teachtime", getTeachTime());
        jso.put("teach_experience", getTeachExperience());
        jso.put("state", getState());
        jso.put("modified", getModifyTime());
        jso.put("created", getCreated());

        return jso;
    }




}
