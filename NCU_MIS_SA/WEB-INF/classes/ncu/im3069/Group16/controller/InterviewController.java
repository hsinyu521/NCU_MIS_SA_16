package ncu.im3069.Group16.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		int state = jso.getInt("state");

	     
	     /** 建立一個新的案件物件 */
	     Interview i =new Interview(case_id,teacher_id,state);

	     
	     /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
	     if(2>3) {
	         /** 以字串組出JSON格式之資料 */
	         String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
	         /** 透過JsonReader物件回傳到前端（以字串方式） */
	         jsr.response(resp, response);
	     }
	     else {
	         /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
	         JSONObject data = ih.create(i);
	         
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
