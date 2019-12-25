package ncu.im3069.Group16.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.Group16.app.Teacher;
import ncu.im3069.Group16.app.TeacherHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class TeacherController
 */
@WebServlet("/api/teacher.do")
//@WebServlet(name = "teacher", urlPatterns = { "/api/teacher.do" })
public class TeacherController extends HttpServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** th，TeacherHelper之物件與Teacher相關之資料庫方法（Sigleton） */
    private TeacherHelper th =  TeacherHelper.getHelper();
    
    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {	//1212 6pm by min
    	System.out.print("in teacherController doPost\n");
    	
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到JSONObject之Request參數 */
        String name = jso.getString("name");
        String email = jso.getString("email");
        String password = jso.getString("password");
        String cellphone = jso.getString("cellphone");
        int gender = jso.getInt("gender");
        	
        System.out.printf("----------teacher註冊---------\n");
        //建一個新的老師會員物件 (建構子1)
        Teacher t = new Teacher(name, email, password, cellphone, gender);
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || cellphone.isEmpty() ) {	//
        		/** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
        }
        /** 透過TeacherHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
        else if (!th.checkDuplicate(t)) {
        	/** 透過TeacherHelper物件的create()方法新建一個會員至資料庫 */
        	JSONObject data = th.create(t);
                
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "成功! 註冊老師會員資料");
        	resp.put("response", data);
                
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
        else {
        	/** 以字串組出JSON格式之資料 */
        	String resp = "{\"status\": \'400\', \"message\": \'新增老師會員帳號失敗，此email帳號重複！\', \'response\': \'\'}";
        	/** 透過JsonReader物件回傳到前端（以字串方式） */
        	jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {	//1212 6pm by min
    	System.out.println("in doGet\n");
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");
        String option = jsr.getParameter("option");
        System.out.printf("in tc doGet, option: %s\n", option);
        
        //ajax中的GET有傳入login這個參數且值為true的話，會去找登入的是誰
        if(option.equals("login")) {
        	String loginId = th.whoLogIn();
        	
        	if(loginId.equals("")) {	//若沒人登入
        		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
        		//回傳訊息告訴前端正常顯示
        		resp.put("status", "400");
                resp.put("message", "沒人登入哦");
                
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
        	}
        	else {
        		JSONObject query = th.getByID(loginId);
        		
        		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "老師會員資料取得成功");
                resp.put("response", query);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
        	}
        }
        
        else {
        	/** 取出經解析到JSONObject之Request參數 */
            //String id = jso.getString("id");
        	System.out.printf("tc, option=getMember");
        	/** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        	if (id.isEmpty()) {	//沒傳id進來，代表要取得所有會員資料，呼叫getAll()
        		/** 透過TeacherHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
        		JSONObject query = th.getAll();
            
        		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        		JSONObject resp = new JSONObject();
        		resp.put("status", "200");
        		resp.put("message", "所有老師會員資料取得成功");
        		resp.put("response", query);
    
        		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        		jsr.response(resp, response);
        	}
        	else {
        		/** 透過TeacherHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
        		JSONObject query = th.getByID(id);
            
        		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        		JSONObject resp = new JSONObject();
        		resp.put("status", "200");
        		resp.put("message", "老師會員資料取得成功");
        		resp.put("response", query);
    
        		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        		jsr.response(resp, response);
        	}
        }
        
    }

    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {	//1212 5pm by min
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        
        /** 透過TeacherHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = th.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "老師會員移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {	//1223 8pm by min
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        //看要登入還註冊
        String option = jso.getString("option");
        
        if(option.equals("update")) {
        	/** 取出經解析到JSONObject之Request參數 */
        	int id = jso.getInt("id");
        	String password = jso.getString("password");
        	String cellphone = jso.getString("cellphone");

        	/** 透過傳入之參數，新建一個以這些參數之會員Teacher物件 建構子3*/
        	Teacher t = new Teacher(id, password, cellphone);

        	/** 透過Teacher物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
        	JSONObject data = t.update();
        
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "成功! 更新會員資料...");
        	resp.put("response", data);
        
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
 
        //若為登入
        else if(option.equals("login")) {	
        	System.out.printf("----------teacher登入---------\n");
        	String email = jso.getString("email");
        	String password = jso.getString("password");
        	//建一個新的老師會員物件 (建構子4)
        	//Teacher t = new Teacher(email, password);
        	String realPwd = th.getPwdByEmail(email);
        	System.out.printf("real: %s, your: %s\n", realPwd, password);
        	
        	//要先確認email存在、找密碼
        	if(realPwd.isEmpty()) {	//此email不存在，故realPwd為空
        		System.out.printf("email not exist\n");
        		
        		/** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'登入失敗，無此老師會員帳號！\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
        	}
        	else if(password.equals(realPwd)) {	//密碼正確
        		System.out.printf("login success.\n");
        		/** 透過TeacherHelper物件的create()方法新建一個會員至資料庫 */
                //JSONObject data = th.create(t);
        		th.updateLogin(email, true);
        		
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "登入成功!");
                //resp.put("response", data);
                
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
        	}
        	else {	//密碼錯誤
        		System.out.printf("pwd error.\n");
        		/** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'登入失敗，密碼錯誤！\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
        	}
        }
    }
}
