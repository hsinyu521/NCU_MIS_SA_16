package ncu.im3069.Group16.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;

import ncu.im3069.Group16.util.DBMgr;

public class LoginHelper {
	
	/**
	 * * 實例化（Instantiates）一個新的（new）ParentHelper物件<br>
	 * * 採用Singleton不需要透過new
	 * */
	private LoginHelper() {
		
	}
	
	/** 靜態變數，儲存ParentHelper物件 */
	private static LoginHelper lh;
	
	/** 儲存JDBC資料庫連線 */
	private Connection conn = null;
	
	/** 儲存JDBC預準備之SQL指令 */
	private PreparedStatement pres = null;
	
	/***靜態方法
	 * 實作Singleton（單例模式），僅允許建立一個LoginHelper物件
	 * 
	 * @return 回傳LoginHelper物件
	 * */
	public static LoginHelper getHelper(){	//1228 12pm
		/** Singleton檢查是否已經有LoginHelper物件，若無則new一個，若有則直接回傳 */
		if(lh == null) lh = new LoginHelper();
		return lh;
	}
	
	//看登入是否成功，失敗會回傳0，非0的則代表id
	public int loginSuccess(Login login){	//1228 12pm
	      /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
	      //int row = -1;
	      /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	      ResultSet rs = null;
	      //
	      //String member = "";	//teachers或parents，代表DB裡的table
	      int loginID = 0;
	      
	      try {
	          /** 取得資料庫之連線 */
	          conn = DBMgr.getConnection();
	          String sql;
	          String type = login.getMemberType();
	          System.out.printf("in LH, type: %s\n", type);
	          
	          if(type.equals("teacher")) {
	        	  sql = "SELECT `id` FROM `sa16`.`teachers` WHERE `email` = ? AND `password` = ?";
	          }
	          else if(type.equals("parent")) {
	        	  sql = "SELECT `id` FROM `sa16`.`parents` WHERE `email` = ? AND `password` = ?";
	          }
	          else {
	        	  sql = "SELECT `id` FROM `sa16`.`managers` WHERE `email` = ? AND `password` = ?";
	          }
	        
	          /** 取得所需之參數 */
	          /** 將參數回填至SQL指令當中 */
	          pres = conn.prepareStatement(sql);
	          pres.setString(1, login.getEmail());
	          pres.setString(2, login.getPwd());
	          /** 執行查詢之SQL指令並記錄其回傳之資料 */
	          rs = pres.executeQuery();
	          //確認用
	          System.out.printf("in LH, sql: "+pres+"\n");
	          
	          /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
	          rs.next();
	          loginID = rs.getInt("id");
	          System.out.printf("in LH, id: %d\n", loginID);

	      } catch (SQLException e) {
	          /** 印出JDBC SQL指令錯誤 **/
	          System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
	      } catch (Exception e) {
	          /** 若錯誤則印出錯誤訊息 */
	          e.printStackTrace();
	      } finally {
	          /** 關閉連線並釋放所有資料庫相關之資源 **/
	          DBMgr.close(rs, pres, conn);
	      }
	      
	      /*
	       * *判斷是否已經有一筆該電子郵件信箱之資料
	       * *若無一筆則回傳False，否則回傳True 
	       */
	      return loginID;
	  }
	
	public String getNameByLogin(Login login) {
		//回傳找到的id
		String name="";
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;
		
		/** SQL指令 */
		String sql; 
		
		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			
			if(login.getMemberType().equals("teacher")) {
				sql = "SELECT `name` FROM `sa16`.`teachers` WHERE `id` = ? LIMIT 1";
			}
			else if(login.getMemberType().equals("parent")){
				sql = "SELECT `name` FROM `sa16`.`parents` WHERE `id` = ? LIMIT 1";
			}
			else {
				sql = "SELECT `name` FROM `sa16`.`managers` WHERE `id` = ? LIMIT 1";
			}
			
			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setInt(1, login.getId());
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();
			
			//要有rs.next()在getString()才會成功!!!!!!
			rs.next();
			name = rs.getString("name");
			
			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			
		} catch (SQLException e) {
			/** 印出JDBC SQL指令錯誤 **/
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/** 若錯誤則印出錯誤訊息 */
			e.printStackTrace();
		} finally {
			/** 關閉連線並釋放所有資料庫相關之資源 **/
			DBMgr.close(rs, pres, conn);
		}
		
		/** 紀錄程式結束執行時間 */
		long end_time = System.nanoTime();
		/** 紀錄程式執行時間 */
		long duration = (end_time - start_time);
		
		/** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("time", duration);
		
		return name;
	}
}
