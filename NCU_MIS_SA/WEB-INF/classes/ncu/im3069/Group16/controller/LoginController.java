package ncu.im3069.Group16.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import ncu.im3069.Group16.app.Login;
import ncu.im3069.Group16.app.LoginHelper;
import ncu.im3069.Group16.app.TeacherHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/api/login.do")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/** th，TeacherHelper之物件與Teacher相關之資料庫方法（Sigleton） */
    private LoginHelper lh =  LoginHelper.getHelper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("in LC, doGet\n");
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
		
		HttpSession session = request.getSession();	//登入用
		
		String type = (String)session.getAttribute("memberType");
		
		//System.out.printf("in LC, type:%s , id: %d\n", type, id);
		
		if(type==null) {	//沒人登入
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
    		//回傳訊息告訴前端正常顯示
    		resp.put("status", "400");
            resp.put("message", "沒人登入哦");
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
		}
		else {	//有人登入
			int id = (int)session.getAttribute("id");
			Login login =  new Login(type, id);
			String name = lh.getNameByLogin(login);
			
			JSONObject data = new JSONObject();
			data.put("memberType", type);
			data.put("id", id);
			data.put("name", name);
			
			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "有人登入");
            resp.put("response", data);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
		}
		
	}

	/**
	 * 用在登入
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.printf("in LoginController, doPost\n");
		
		HttpSession session = request.getSession();	//登入用
		session.removeAttribute("memberType");
		session.removeAttribute("id");
		
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到JSONObject之Request參數 */
        String memberType = jso.getString("memberType");
        String email = jso.getString("email");
        String password = jso.getString("password");
        System.out.printf("in LC, type: %s\n", memberType);
        
        Login login = new Login(memberType, email, password);
        
        int loginID = lh.loginSuccess(login);
        
        if(loginID!=0) {	//登入成功
        	System.out.printf("in LC, logID!=0, sucsess\n");
        	//set session
            session.setAttribute("memberType", memberType);
            session.setAttribute("id",loginID);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "登入成功!");
            //resp.put("response", data);	//1227
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
        	System.out.printf("login error.\n");
    		/** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'登入失敗，請重新輸入！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
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
		JsonReader jsr = new JsonReader(request);
		HttpSession session = request.getSession();	//登入用
		
		session.removeAttribute("memberType");
		session.removeAttribute("id");
		session.invalidate();
		
		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "登出成功！");

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}
