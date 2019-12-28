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
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `sa16`.`cases`(`parent_id`, `grade`, `subject`, `teachCounty`, `teachRegion`, `wage`, `teachTime`, `teachExperience`, `state`, `modified`, `created`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            
            /** 取得所需之參數 */
            int parent_id = ca.getParent_id();
            String grade = ca.getGrade();
            String subject = ca.getSubject();
            String teachCounty = ca.getCounty();
            String teachRegion = ca.getRegion();
            int wage = ca.getWage();
            String teachTime = ca.getTeachTime();
            String teachExperience = ca.getTeachExperience();
            int state = ca.getState();

            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, parent_id);
            pres.setString(2, grade);
            pres.setString(3, subject);
            pres.setString(4, teachCounty);
            pres.setString(5, teachRegion);
            pres.setInt(6, wage);
            pres.setString(7, teachTime);
            pres.setString(8, teachExperience);
            pres.setInt(9, state); 
            pres.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pres.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            
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
    public JSONObject update(Case ca) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `sa16`.`cases` SET `grade` = ? ,`subject` = ? , `teachCounty` = ? , `teachRegion` = ?,`wage` = ? ,`teachTime` = ? ,`teachExperience` = ?,`modified` = ? WHERE `id` = ?";
            /** 取得所需之參數 */
            int id = ca.getId();            		
            String grade = ca.getGrade();
            String subject = ca.getSubject();
            String teachCounty = ca.getCounty();
            String teachRegion = ca.getRegion();
            int wage = ca.getWage();
            String teachTime = ca.getTeachTime();
            String teachExperience = ca.getTeachExperience();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, grade);
            pres.setString(2, subject);
            pres.setString(3, teachCounty);
            pres.setString(4, teachRegion);
            pres.setInt(5, wage);
            pres.setString(6, teachTime);
            pres.setString(7, teachExperience);
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
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
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

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                
                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("id");    
                int parent_id = rs.getInt("parent_id");
                String grade = rs.getString("grade");
                String subject = rs.getString("subject");
                String teachCounty = rs.getString("teachCounty");
                String teachRegion = rs.getString("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                String teachExperience = rs.getString("teachExperience");
                int state = rs.getInt("state");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, parent_id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience, state, modified, created);
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
        response.put("sql", exexcute_sql);
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
                int parent_id = rs.getInt("parent_id");
                String grade = rs.getString("grade");
                String subject = rs.getString("subject");
                String teachCounty = rs.getString("teachCounty");
                String teachRegion = rs.getString("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                String teachExperience = rs.getString("teachExperience");
                int state = rs.getInt("state");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, parent_id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience, state, modified, created);
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
    
    public JSONObject getByParentId(String p_id) {
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
            String sql = "SELECT * FROM `sa16`.`cases` WHERE `cases`.`parent_id` = ?";
            
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
            	int id = rs.getInt("id"); 
                int parent_id = rs.getInt("parent_id");
                String grade = rs.getString("grade");
                String subject = rs.getString("subject");
                String teachCounty = rs.getString("teachCounty");
                String teachRegion = rs.getString("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                String teachExperience = rs.getString("teachExperience");
                int state = rs.getInt("state");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, parent_id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience, state, modified, created);
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
    
    public JSONObject getBySubject(String sub) {
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
            String sql = "SELECT * FROM `sa16`.`cases` WHERE `cases`.`subject` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, sub);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                
                /** 將 ResultSet 之資料取出 */ 
            	int id = rs.getInt("id"); 
                int parent_id = rs.getInt("parent_id");
                String grade = rs.getString("grade");
                String subject = rs.getString("subject");
                String teachCounty = rs.getString("teachCounty");
                String teachRegion = rs.getString("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                String teachExperience = rs.getString("teachExperience");
                int state = rs.getInt("state");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, parent_id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience, state, modified, created);
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
    
    public JSONObject getByExperience(String exp) {
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
            String sql = "SELECT * FROM `sa16`.`cases` WHERE `cases`.`teachExperience` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, exp);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                
                /** 將 ResultSet 之資料取出 */ 
            	int id = rs.getInt("id"); 
                int parent_id = rs.getInt("parent_id");
                String grade = rs.getString("grade");
                String subject = rs.getString("subject");
                String teachCounty = rs.getString("teachCounty");
                String teachRegion = rs.getString("teachRegion");
                int wage = rs.getInt("wage");
                String teachTime = rs.getString("teachTime");
                String teachExperience = rs.getString("teachExperience");
                int state = rs.getInt("state");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                c = new Case(id, parent_id, grade, subject, teachCounty, teachRegion, wage, teachTime, teachExperience, state, modified, created);
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
    
    public JSONObject deleteById(int id) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";

        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            
            /** SQL指令 */
            String sql = "DELETE FROM `sa16`.`cases` WHERE `id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);

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

        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);

        return response;
    }

    
}
