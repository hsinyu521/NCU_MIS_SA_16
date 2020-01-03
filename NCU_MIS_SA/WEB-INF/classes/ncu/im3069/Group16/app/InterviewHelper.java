package ncu.im3069.Group16.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import ncu.im3069.Group16.util.DBMgr;

public class InterviewHelper {
	   private static InterviewHelper ih;
	    private Connection conn = null;
	    private PreparedStatement pres = null;
	    
	    private InterviewHelper() {}
	    
	    public static InterviewHelper getHelper() {
	        if(ih == null) ih = new InterviewHelper();
	        
	        return ih;
	    }
	    
	    public JSONObject create(Interview in) {
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "INSERT INTO `sa16`.`interview`(`cases_id`, `teachers_id`)"
	                    + " VALUES(?, ?)";
	            
	            /** 取得所需之參數 */
	            int case_id = in.getCaseId();
	            int teacher_id = in.getTeacherId();
	            //int state = in.getState();

	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            pres.setInt(1, case_id);
	            pres.setInt(2, teacher_id);
	           // pres.setInt(3, state);

	            /** 執行新增之SQL指令並記錄影響之行數 */
	            pres.executeUpdate();
	            
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
	            DBMgr.close(pres, conn);
	        }

	        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);

	        return response;

	    }
	    public JSONObject update(Interview in) {
	        /** 紀錄回傳之資料 */
	        JSONArray jsa = new JSONArray();
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "Update `sa16`.`interview` SET `state` = ? WHERE `cases_id` = ?";
	            /** 取得所需之參數 */
	            int case_id = in.getCaseId();            		
	            int state = in.getState();
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setInt(1, state);
	            pres.setInt(2, case_id);

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
	            DBMgr.close(pres, conn);
	        }

	        
	        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("data", jsa);

	        return response;
	    }
	    public JSONObject getByCaseId(String ca_id) {
	        JSONArray jsa = new JSONArray();
	        Interview i = null;
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";;
	        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	        ResultSet rs = null;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT * FROM `sa16`.`interview` WHERE `interview`.`cases_id` = ?";
	            
	            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, ca_id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            while(rs.next()) {
	                
	                /** 將 ResultSet 之資料取出 */ 
	            	int case_id = rs.getInt("case_id"); 
	                int teacher_id = rs.getInt("teacher_id");	              
	                int state = rs.getInt("state");

	                
	                /** 將每一筆商品資料產生一名新Product物件 */
	                i = new Interview(case_id, teacher_id, state);
	                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
	                jsa.put( i.getInterviewData());
	            }

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
	        
	        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
	        
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("data", jsa);
	        return response;
	    }
	    public JSONObject getByTeacherId(String t_id) {
	        JSONArray jsa = new JSONArray();
	        Interview i = null;
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";;
	        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	        ResultSet rs = null;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT * FROM `sa16`.`interview` WHERE `interview`.`teachers_id` = ?";
	            
	            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, t_id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            while(rs.next()) {
	                
	                /** 將 ResultSet 之資料取出 */ 
	            	int case_id = rs.getInt("cases_id"); 
	                int teacher_id = rs.getInt("teachers_id");	              
	                int state = rs.getInt("state");

	                
	                /** 將每一筆商品資料產生一名新Product物件 */
	                i = new Interview(case_id, teacher_id, state);
	                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
	                jsa.put( i.getInterviewData());
	            }

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
	        
	        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
	        
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("data", jsa);
	        return response;
	    }
}
