package ncu.im3069.Group16.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.Group16.app.Case;
import ncu.im3069.Group16.app.Interview;
import ncu.im3069.Group16.app.InterviewHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class InterviewController
 */
@WebServlet("/api/interview.do")
public class InterviewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private InterviewHelper ih =  InterviewHelper.getHelper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InterviewController() {
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
     String parent_id = jsr.getParameter("parent_id");
     String teacher_id = jsr.getParameter("teacher_id");
     String case_id = jsr.getParameter("case_id");

     /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
     JSONObject resp = new JSONObject();

     /** 判斷該字串是否存在，若存在代表要取回個別相關資料 */
     if (!parent_id.isEmpty()) {
       /** 透過 InterviewHelper 物件的 getByParentId() 方法自資料庫取回資料，回傳之資料為 JSONObject 物件 */
       JSONObject query = ih.getByParentId(parent_id);
       resp.put("status", "200");
       resp.put("message", "家長會員編號面試資料取得成功");
       resp.put("response", query);
     }
     else if(!teacher_id.isEmpty()) {
     	/** 透過 InterviewHelper 物件之 getByTeacherId() 方法取回資料，回傳之資料為 JSONObject 物件 */
         JSONObject query = ih.getByTeacherId(teacher_id);
         resp.put("status", "200");
         resp.put("message", "老師會員編號面試資料取得成功");
         resp.put("response", query);
     }
     else if(!case_id.isEmpty()) {
      	/** 透過 InterviewHelper 物件之 getByCaseId() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = ih.getByCaseId(case_id);
          resp.put("status", "200");
          resp.put("message", "案件編號取得面試資料成功");
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
		
		int case_id = jso.getInt("case_id");
		int teacher_id = jso.getInt("teacher_id");
	     
	     /** 建立一個新的面試物件 */
	     Interview i =new Interview(case_id,teacher_id);

	     
	     /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
	     if(case_id == 0 || teacher_id == 0) {
	         /** 以字串組出JSON格式之資料 */
	         String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
	         /** 透過JsonReader物件回傳到前端（以字串方式） */
	         jsr.response(resp, response);
	     }
	     else {
	    	 if(!ih.checkDuplicate(i)) {
	    		 /** 透過Interview物件的create()方法新建一個面試至資料庫 */
		         JSONObject data = ih.create(i);
		         
		         /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		         JSONObject resp = new JSONObject();
		         resp.put("status", "200");
		         resp.put("response", data);
		         
		         /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		         jsr.response(resp, response);
	    	 }
	    	 else {	//已應徵過
	    		 /** 以字串組出JSON格式之資料 */
		         String resp = "{\"status\": \'400\', \"message\": \'您已應徵過\', \'response\': \'\'}";
		         /** 透過JsonReader物件回傳到前端（以字串方式） */
		         jsr.response(resp, response);
	    	 }
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
        int case_id = jso.getInt("case_id");
        int teacher_id = jso.getInt("teacher_id");
        int state = jso.getInt("state");
        int casestate = jso.getInt("casestate");

        /** 透過傳入之參數，新建一個以這些參數之會員Interview物件和Case物件 */
        Interview i =new Interview(case_id,teacher_id,state);
        Case c = new Case(case_id,casestate);
        /** 透過Interview物件的update()方法至資料庫更新該案件資料，回傳之資料為JSONObject物件 */
        JSONObject data1 = c.update1();
        JSONObject data = i.update();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新面試資料...");
        resp.put("response", data1);
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}
	
}

