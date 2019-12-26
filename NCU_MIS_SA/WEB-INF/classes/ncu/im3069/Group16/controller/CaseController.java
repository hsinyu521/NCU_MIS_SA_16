package ncu.im3069.Group16.controller;

import java.io.IOException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
       
    /** ch，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
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

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = ch.getById(id);
          resp.put("status", "200");
          resp.put("message", "單筆案件資料取得成功");
          resp.put("response", query);
        }
        else {
          /** 透過 orderHelper 物件之 getAll() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
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
        int wage  = jso.getInt("wage");
        String teachtime = jso.getString("teachTime");
        String teachExperience = jso.getString("teachExperience");
        
        /** 建立一個新的案件物件 */
        Case c =new Case(parent_id,grade,subject,teach_county,teach_region
        		,wage,teachtime,teachExperience);

        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(grade.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }else {
            /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
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
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
