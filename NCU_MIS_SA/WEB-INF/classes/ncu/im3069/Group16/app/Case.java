package ncu.im3069.Group16.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.json.*;


public class Case{

    /** id，案件編號 */
    private int id;

    /** parent_id，外鍵：家長id */
    private int parent_id;

    /** grade，教學對象 */
    private String grade;
    
    /** subject，教學科目 */
    private String subject;

    /** teach_county，上課縣市 */
    private String teach_county;

    /** teach_region，上課地區 */
    private String teach_region;

    /** wage，時薪 */
    private int wage;

    /** teachTime，上課時間 */
    private String teachtime;
    
    /** teachExperience，教學經驗要求 */
    private String teachExperience;
    
    /** state，案件狀態 */
    private int state;
    
    /** create，案件創建時間 */
    private Timestamp created;

    /** modify，案件修改時間 */
    private Timestamp modified;

    /** ch，CaseHelper 之物件與 Case 相關之資料庫方法（Sigleton） */
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
    		,int wage,String teachtime,String teachExperience) {
    	//1218 min 我覺得不需要state, create, modify，少了teachExperience,多了state
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
    		,int wage,String teachtime,String teachExperience,int state,Timestamp modified,Timestamp created) {
    	//1218 min 這感覺不會有最後兩個時間的參數，在helper中直接把當下時間塞進sql語法就好(可以參考更新老師會員資料)
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
		this.modified=modified;
		this.created=created;
    }

    /**
     * *設定案件id和編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * *取得案件id編號
     *
     * @return int 回傳訂單編號
     */
    public int getId() {
        return this.id;
    }

    /**
     * *取得案件家長聯絡人
     *
     * @return int 回傳家長id
     */
    public int getParent_id() {
        return this.parent_id;
    }

    /**
     * *取得案件教學對象
     *
     * @return String 回傳教學對象
     */
    public String getGrade() {
        return this.grade;
    }
    
    /**
     * *取得案件科目
     *
     * @return int 回傳案件科目
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * *取得案件教學縣市
     *
     * @return int 回傳案件教學地點
     */
    public String getCounty() {
        return this.teach_county;
    }
    
    /**
     * *取得案件教學地點
     *
     * @return int 回傳案件教學地點
     */
    public String getRegion() {
        return this.teach_region;
    }
    
    /**
     ** 取得案件時薪
     *
     * @return int 回傳時薪
     */
    public int getWage() {
        return this.wage;
    }
    
    /**
     * *取得案件教學時間
     *
     * @return String 回傳案件教學時間
     */
    public String getTeachTime() {
        return this.teachtime;
    }
    
    /**
     * *取得案件教學經驗要求
     *
     * @return int 回傳教學經驗要求
     */
    public String getTeachExperience() {
        return this.teachExperience;
    }
    
    /**
     * *取得案件狀態
     *
     * @return int 回傳案件狀態
     */
    public int getState() {
        return this.state;
    }

    /**
     ** 取得案件創建時間
     *
     * @return Timestamp 回傳案件創建時間
     */
    public Timestamp getCreated() {
        return this.created;
    }

    /**
     * 取得案件修改時間
     *
     * @return Timestamp 回傳案件修改時間
     */
    public Timestamp getModifyTime() {
        return this.modified;
    }

    /**
     * *更新案件資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {	//1218 min
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        /** 檢查該名會員是否已經在資料庫 */
        if(this.id != 0) {
        	/** 若有則將目前更新後之資料更新至資料庫中 */
        	//ch.updateLoginTimes(this);
        	
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
