package ncu.im3069.Group16.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.Group16.app.Parent;
import ncu.im3069.Group16.app.ParentHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class ParentController
 */
@WebServlet("/api/parent.do")
//@WebServlet(name = "parent.do", urlPatterns = { "/api/parent.do" })
public class ParentController extends HttpServlet {	
	/** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** ph，ParentHelper之物件與Parent相關之資料庫方法（Sigleton） */
    private ParentHelper ph =  ParentHelper.getHelper();
	
	/**處理Http Method請求GET方法（取得資料）
	 * 
	 * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	//1216 1am by min
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		
		/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料* */
		String id = jsr.getParameter("id");
		
        /**判斷該字串是否存在
         * 若存在代表要取回個別家長會員之資料 否則會取回全部資料庫內家長會員之資料 
         * */
        if (id.isEmpty()) {	//沒傳id進來，代表要取得所有會員資料，呼叫getAll()
        	/** 透過TeacherHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
        	JSONObject query = ph.getAll();
        	
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "所有家長會員資料取得成功");
        	resp.put("response", query);
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
        else {
        	/** 透過TeacherHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
        	JSONObject query = ph.getByID(id);
            
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "家長會員資料取得成功");
        	resp.put("response", query);
            
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
	}

	 /*
	  * *處理Http Method請求POST方法（新增資料）
	  * *
	  * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
	  * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	  * @throws ServletException the servlet exception
	  * @throws IOException Signals that an I/O exception has occurred.
	  * */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	//1216 1am by min
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String name = jso.getString("name");
        String email = jso.getString("email");
        String password = jso.getString("password");
        String cellphone = jso.getString("cellphone");
        int gender = jso.getInt("gender");
        
        /** 建立一個新的家長會員物件 */
        Parent p = new Parent(name, email, password, cellphone, gender);	//建構子2
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || cellphone.isEmpty() ) {	//
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        /** 透過TeacherHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
        else if (!ph.checkDuplicate(p)) {
            /** 透過TeacherHelper物件的create()方法新建一個會員至資料庫 */
            JSONObject data = ph.create(p);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功!註冊家長會員資料");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'新增家長會員帳號失敗，此email帳號重複！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
	}

	/*
     * *處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {	//1216 1am by min
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        

        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
       	//String email = jso.getString("email");
       	String password = jso.getString("password");
       	String cellphone = jso.getString("cellphone");
        	
       	/** 透過傳入之參數，新建一個以這些參數之會員Parent物件 建構子3*/
        Parent p = new Parent(id, password, cellphone);
        
        /** 透過Parent物件的update()方法至資料庫更新該名家長會員資料，回傳之資料為JSONObject物件 */
        JSONObject data = p.update();
        	
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功!更新家長會員資料");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

	 /*
	  * *處理Http Method請求DELETE方法（刪除）
	  * *
	  * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
	  * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	  * @throws ServletException the servlet exception
	  *  @throws IOException Signals that an I/O exception has occurred.
	  *  */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	//1216 1am by min
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        
        /** 透過TeacherHelper物件的deleteByID()方法至資料庫刪除該名家長會員，回傳之資料為JSONObject物件 */
        JSONObject query = ph.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        System.out.printf("query.getInt(\"row\"):"+query.getInt("row")+"\n");
        if(query.getInt("row")==1) {
            resp.put("status", "200");
            resp.put("message", "家長會員移除成功！");
            resp.put("response", query);
        }
        else {
        	resp.put("status", "400");
            resp.put("message", "家長會員移除失敗！該家長還有案件存在！");
            resp.put("response", query);
        }
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}
}