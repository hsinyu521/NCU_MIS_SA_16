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
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            pres.setInt(1, case_id);
	            pres.setInt(2, teacher_id);

	            /** 執行新增之SQL指令 */
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

	        /** 將SQL指令封裝成JSONObject回傳 */
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
	            String sql = "Update `sa16`.`interview` SET `state` = ? WHERE `cases_id` = ? AND `teachers_id` = ?";
	            /** 取得所需之參數 */
	            int case_id = in.getCaseId();            		
	            int state = in.getState();
	            int teacher_id = in.getTeacherId();
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setInt(1, state);
	            pres.setInt(2, case_id);
	            pres.setInt(3, teacher_id);
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

	        
	        /** 將SQL指令和資料，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("data", jsa);

	        return response;
	    }
	    public JSONObject getByParentId(String p_id) {
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
	            String sql = "SELECT parents.id, cases.parent_id, cases.id, cases.wage, interview.cases_id, interview.state, interview.teachers_id,teachers.name, teachers.id"+
	            		" FROM sa16.parents INNER JOIN cases ON parents.id = cases.parent_id"+
	            		" INNER JOIN interview ON cases.id = interview.cases_id"+
	            		" INNER JOIN teachers ON interview.teachers_id = teachers.id"+
	            		" WHERE parents.id = ?;";
	            
	            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, p_id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            while(rs.next()) {
	                
	                /** 將 ResultSet 之資料取出 */ 
	            	int caseID = rs.getInt("cases_id"); 
	                int parentID = rs.getInt("parent_id");	
	                int teacherID = rs.getInt("teachers_id");	
	                String tname = rs.getString("name");
	                String wage = rs.getString("wage");
	                int state = rs.getInt("state");

	                
	                /** 將每一筆面試資料產生一名新Interview物件 */
	                i = new Interview(caseID, parentID, teacherID, tname, wage, state);
	                /** 取出該項面試之資料並封裝至 JSONsonArray 內 */
	                jsa.put( i.getPInterviewData());
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
	        
	        /** 將SQL指令與面試資料之JSONArray，封裝成JSONObject回傳 */
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
	            String sql = "SELECT cases.id, cases.parent_id, parents.cellphone, cases.subject, cases.teachCounty, cases.teachRegion, cases.wage, interview.state, cases.grade " + 
	            		"FROM sa16.cases " + 
	            		"INNER JOIN interview ON cases.id = interview.cases_id " + 
	            		"INNER JOIN parents ON cases.parent_id = parents.id WHERE interview.teachers_id = ?;";
	            
	            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, t_id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            System.out.printf("in IH\n");
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            while(rs.next()) {
	                /** 將 ResultSet 之資料取出 */ 
	            	int caseID = rs.getInt("id"); 
	                int parentID = rs.getInt("parent_id");	              
	                String cellphone = rs.getString("cellphone");
	                String subject = rs.getString("subject");
	                String grade = rs.getString("grade");
	                String teachCounty = rs.getString("teachCounty");
	                String teachRegion = rs.getString("teachRegion");
	                String wage = rs.getString("wage");
	                int state = rs.getInt("state");
	                
	                /** 將每一筆面試資料產生一名新Interview物件 */
	                i = new Interview(caseID, parentID, cellphone, subject, grade, teachCounty, teachRegion, wage, state);
	                /** 取出該項面試之資料並封裝至 JSONsonArray 內 */
	                jsa.put( i.getTInterviewData());
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
	        
	        /** 將SQL指令與面試資料之JSONArray，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("data", jsa);
	        return response;
	    }
	    
	    public JSONObject getByCaseId(String c_id) {
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
	            String sql = "SELECT cases.id, interview.cases_id, interview.state, interview.teachers_id,teachers.name,teachers.cellphone, teachers.id"+
	            		" FROM sa16.cases "+
	            		" INNER JOIN interview ON cases.id = interview.cases_id"+
	            		" INNER JOIN teachers ON interview.teachers_id = teachers.id"+
	            		" WHERE cases.id = ?;";
	            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, c_id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            System.out.printf("in IH\n");
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            while(rs.next()) {
	                /** 將 ResultSet 之資料取出 */ 
	            	int caseID = rs.getInt("cases_id");	
	                int teacherID = rs.getInt("teachers_id");
	                int state = rs.getInt("state");
	                String tname = rs.getString("name");
	                String tphone = rs.getString("cellphone");
	                
	                
	                /** 將每一筆面試資料產生一名新Interview物件 */
	                i = new Interview(caseID, teacherID, state, tname, tphone);
	                /** 取出該項面試之資料並封裝至 JSONsonArray 內 */
	                jsa.put( i.getCInterviewData());
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
	        
	        /** 將SQL指令與面試資料之JSONArray，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("data", jsa);
	        return response;
	    }
	    public boolean checkDuplicate(Interview i){
	        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
	        int row = -1;
	        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	        ResultSet rs = null;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT count(*) FROM `sa16`.`Interview` WHERE `teachers_id` = ? AND `cases_id` = ?";
	            
	            /** 取得所需之參數 */
	            int teacher_id = i.getTeacherId();
	            int case_id = i.getCaseId();
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setInt(1, teacher_id);
	            pres.setInt(2, case_id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
	            rs.next();
	            row = rs.getInt("count(*)");
	            System.out.print(row);

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
	         * *判斷是否已經有一筆該面試之資料
	         * *若無一筆則回傳False，否則回傳True 
	         */
	        return (row == 0) ? false : true;
	    }
}
