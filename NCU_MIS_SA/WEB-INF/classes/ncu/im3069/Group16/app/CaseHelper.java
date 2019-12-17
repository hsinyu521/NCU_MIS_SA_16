package ncu.im3069.Group16.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.json.*;
import ncu.im3069.Group16.util.DBMgr;

public class CaseHelper {
    
    private static CaseHelper ch;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    private CaseHelper() {}
    
    public static CaseHelper getHelper() {
        if(ch == null) ch = new CaseHelper();
        
        return ch;
    }
    
    public JSONObject create(Case ca) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        int id = -1;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `sa16`.`cases`(`parent_id`, `grade`, `subject`, `teachCounty`, `teachRegion`, `wage`, `teachTime`, `teachExperience`, `modified`, `created`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int parent_id = ca.getParent_id();
            String grade = ca.getGrade();
            int subject = ca.getSubject();
            int teachCounty = ca.getCounty();
            int teachRegion = ca.getRegion();
            int wage = ca.getWage();
            String teachTime = ca.getTeachTime();
            int teachExperience = ca.getTeachExperience();

            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, parent_id);
            pres.setString(2, grade);
            pres.setInt(3, subject);
            pres.setInt(4, teachCounty);
            pres.setInt(5, teachRegion);
            pres.setInt(6, wage);
            pres.setString(7, teachTime);
            pres.setInt(8, teachExperience);
            pres.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            pres.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            
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
    
    /**
     * 更新一案件資料
     *
     * @param c 一名會員之Case物件
     * @return the JSONObject 回傳SQL指令執行結果與執行之資料
     */
    public JSONObject update(Case c) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `sa16`.`members` SET `grade` = ? ,`subject` = ? , `teachCounty` = ? , `teachRegion` = ?,`wage` = ? ,`teachTime` = ? ,`teachExperience` = ?,`modified` = ? WHERE `id` = ?";
            /** 取得所需之參數 */
            int id = c.getId();            		
            String grade = c.getGrade();
            int subject = c.getSubject();
            int teachCounty = c.getCounty();
            int teachRegion = c.getRegion();
            int wage = c.getWage();
            String teachTime = c.getTeachTime();
            int teachExperience = c.getTeachExperience();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, grade);
            pres.setInt(2, subject);
            pres.setInt(3, teachCounty);
            pres.setInt(4, teachRegion);
            pres.setInt(5, wage);
            pres.setString(6, teachTime);
            pres.setInt(7, teachExperience);
            pres.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(9, id);

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
    
    public JSONObject getAll() {
        Case c = null;
        JSONArray jsa = new JSONArray();
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa16`.`cases`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                
                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("id");            		
                String grade = rs.getString("grade");
                int subject = rs.getInt("subject");
                int teachCounty = rs.getInt("teachCounty");
                int teachRegion = rs.getInt("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                int teachExperience = rs.getInt("teachExperience");
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(c.getCaseData());
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
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject getById(String case_id) {
        JSONObject data = new JSONObject();
        Case c = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa16`.`cases` WHERE `cases`.`id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, case_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("id");            		
                String grade = rs.getString("grade");
                int subject = rs.getInt("subject");
                int teachCounty = rs.getInt("teachCounty");
                int teachRegion = rs.getInt("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                int teachExperience = rs.getInt("teachExperience");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                data = c.getCaseData();
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
        response.put("data", data);

        return response;
    }
}
