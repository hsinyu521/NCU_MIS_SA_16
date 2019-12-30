package ncu.im3069.Group16.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.Group16.app.Manager;
import ncu.im3069.Group16.app.ManagerHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class ManagerController
 */
@WebServlet("/api/manager.do")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/** mh，TeacherHelper之物件與Teacher相關之資料庫方法（Sigleton） */
    private ManagerHelper mh =  ManagerHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagerController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("in MC, doGet\n");
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");
        
        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        if (id.isEmpty()) {	//沒傳id進來，代表要取得所有會員資料，呼叫getAll()
        	/** 透過ManageHelper物件之getAll()方法取回所有管理者之資料，回傳之資料為JSONObject物件 */
        	JSONObject query = mh.getAll();
        	
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "所有管理者資料取得成功");
        	resp.put("response", query);
   
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
        else {
        	/** 透過TeacherHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
        	JSONObject query = mh.getByID(id);
            
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "管理者資料取得成功");
        	resp.put("response", query);
    
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.print("in MC, doPost\n");
    	
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到JSONObject之Request參數 */
        String name = jso.getString("name");
        String email = jso.getString("email");
        String password = jso.getString("password");
        	
        System.out.printf("----------manager註冊---------\n");
        //建一個新的老師會員物件 (建構子1)
        Manager m = new Manager(name, email, password);
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(email.isEmpty() || password.isEmpty() || name.isEmpty()) {	//
        		/** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
        }
        /** 透過TeacherHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
        else if (!mh.checkDuplicate(m)) {
        	/** 透過TeacherHelper物件的create()方法新建一個會員至資料庫 */
        	JSONObject data = mh.create(m);
            
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
        	resp.put("status", "200");
        	resp.put("message", "新增管理者資料成功!");
        	resp.put("response", data);
        	
        	/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        	jsr.response(resp, response);
        }
        else {
        	/** 以字串組出JSON格式之資料 */
        	String resp = "{\"status\": \'400\', \"message\": \'新增管理者帳號失敗，此email帳號重複！\', \'response\': \'\'}";
        	/** 透過JsonReader物件回傳到前端（以字串方式） */
        	jsr.response(resp, response);
        }
	}
		

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        String name = jso.getString("name");
       	String password = jso.getString("password");
        
        /** 透過傳入之參數，新建一個以這些參數之會員Teacher物件 建構子3*/
        Manager m = new Manager(id, name, password);

        /** 透過Teacher物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
        JSONObject data = m.update();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新管理者資料");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        
        /** 透過TeacherHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = mh.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "管理者移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}
