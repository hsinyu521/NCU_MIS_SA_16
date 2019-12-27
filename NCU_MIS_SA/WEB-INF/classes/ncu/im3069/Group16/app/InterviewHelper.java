package ncu.im3069.Group16.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
	            String sql = "INSERT INTO `sa16`.`interview`(`cases_id`, `teachers_id`, `state`)"
	                    + " VALUES(?, ?, ?)";
	            
	            /** 取得所需之參數 */
	            int case_id = in.getCaseId();
	            int teacher_id = in.getTeacherId();
	            int state = in.getState();

	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            pres.setInt(1, case_id);
	            pres.setInt(2, teacher_id);
	            pres.setInt(3, state);

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
}
