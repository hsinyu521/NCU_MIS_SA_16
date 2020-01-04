package ncu.im3069.Group16.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.Group16.app.Case;
import ncu.im3069.Group16.app.CaseHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class CaseController
 */
@WebServlet("/api/case.do")
public class CaseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /** ch，CaseHelper之物件與Case相關之資料庫方法（Sigleton） */
    private CaseHelper ch =  CaseHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CaseController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String id = jsr.getParameter("id");
        String parent_id = jsr.getParameter("parent_id");
        String subject = jsr.getParameter("subject");
        String teachExperience = jsr.getParameter("teachExperience");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別案件之資料，否則代表要取回全部資料庫內案件之資料 */
        if (!id.isEmpty()) {
          /** 透過 CaseHelper 物件的 getByID() 方法自資料庫取回資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = ch.getById(id);
          resp.put("status", "200");
          resp.put("message", "單筆案件資料取得成功");
          resp.put("response", query);
        }
        else if(!parent_id.isEmpty()) {
        	/** 透過 CaseHelper 物件之 getByParentId() 方法取回資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = ch.getByParentId(parent_id);
            resp.put("status", "200");
            resp.put("message", "家長會員編號，案件資料取得成功");
            resp.put("response", query);
        }
        else if(!subject.isEmpty()) {
            /** 透過 CaseHelper 物件之 getBySubject() 方法取回資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = ch.getBySubject(subject);
            resp.put("status", "200");
            resp.put("message", "科目，案件資料取得成功");
            resp.put("response", query);
        }
        else if(!teachExperience.isEmpty()) {
            /** 透過 CaseHelper 物件之 getByExperience() 方法取回資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = ch.getByExperience(teachExperience);
            resp.put("status", "200");
            resp.put("message", "教學經驗，案件資料取得成功");
            resp.put("response", query);
        }
        else {
            /** 透過 CaseHelper 物件之 getAll() 方法取回所有案件之資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = ch.getAll();
            resp.put("status", "200");
            resp.put("message", "所有案件資料取得成功");
            resp.put("response", query);
          }

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        int parent_id = jso.getInt("parent_id");
        String grade = jso.getString("grade");
        String subject = jso.getString("subject");
        String teach_county = jso.getString("teachCounty");
        String teach_region = jso.getString("teachRegion");
        String wage  = jso.getString("wage");
        String teachtime = jso.getString("teachTime");
        String teachExperience = jso.getString("teachExperience");
        
        /** 建立一個新的案件物件 */
        Case c =new Case(parent_id,grade,subject,teach_county,teach_region
        		,wage,teachtime,teachExperience);

        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(grade.isEmpty()||subject.isEmpty()||teach_county.isEmpty()||teach_region.isEmpty()
        		||wage.isEmpty()||teachtime.isEmpty()||teachExperience.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }else {
            /** 透過CaseHelper物件的create()方法新建一個案件至資料庫 */
            JSONObject data = ch.create(c);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        int parent_id = jso.getInt("parent_id");
        String grade = jso.getString("grade");
        String subject = jso.getString("subject");
        String teach_county = jso.getString("teachCounty");
        String teach_region = jso.getString("teachRegion");
        String wage  = jso.getString("wage");
        String teachtime = jso.getString("teachTime");
        String teachExperience = jso.getString("teachExperience");
        int state = jso.getInt("state");
        
        /** 透過傳入之參數，新建一個以這些參數之會員Case物件 */
        Case c =new Case(id,parent_id,grade,subject,teach_county,teach_region
        		,wage,teachtime,teachExperience,state);
        
        /** 透過Case物件的update()方法至資料庫更新該案件資料，回傳之資料為JSONObject物件 */
        JSONObject data = c.update();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新案件資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}



}
