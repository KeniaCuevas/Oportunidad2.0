package mx.edu.utez.model.games;

import mx.edu.utez.model.category.BeanCategory;
import mx.edu.utez.service.ConnectionMySQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DaoGames {
    private Connection con;
    private CallableStatement cstm;
    private ResultSet rs;
    final private Logger CONSOLE = LoggerFactory.getLogger(DaoGames.class);

    public List<BeanGames> findAll(){
        List<BeanGames> listGames = new ArrayList<>();
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("SELECT * FROM game;");
            rs = cstm.executeQuery();

            while(rs.next()){
                BeanCategory beanCategory = new BeanCategory();
                BeanGames beanGames = new BeanGames();

                beanCategory.setIdCategory(rs.getInt("idCategory"));
                beanCategory.setName(rs.getString("name"));
                beanCategory.setStatus(rs.getInt("status"));

                beanGames.setIdGames(rs.getInt("idGames"));
                beanGames.setName(rs.getString("name"));
                beanGames.setImgGame(Base64.getEncoder().encodeToString(rs.getBytes("imgGames")));
                beanGames.setDatePremiere(rs.getString("date_premiere"));
                beanGames.setStatus(rs.getInt("status"));
                beanGames.setCategory_idCategory(beanCategory);

                listGames.add(beanGames);
            }
        }catch(SQLException e){
            CONSOLE.error("Ocurrio un error: " + e.getMessage());
        }finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return listGames;
    }

    public BeanGames findById(int id){
        BeanGames game = null;
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("SELECT * FROM game WHERE idGames = ?;");
            cstm.setInt(1, id);
            rs = cstm.executeQuery();

            if(rs.next()){
                BeanCategory category = new BeanCategory();
                game = new BeanGames();

                category.setIdCategory(rs.getInt("idCategory"));
                category.setName(rs.getString("name"));
                category.setStatus(rs.getInt("status"));

                game.setIdGames(rs.getInt("idGames"));
                game.setName(rs.getString("name"));
                game.setImgGame(Base64.getEncoder().encodeToString(rs.getBytes("imgGames")));
                game.setDatePremiere(rs.getString("date_premiere"));
                game.setCategory_idCategory(category);
                game.setStatus(rs.getInt("status"));

                game.setCategory_idCategory(category);
            }
        }catch (SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return game;
    }
    
    public boolean create(BeanGames game, InputStream image){
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_create(?,?,?)}");
            cstm.setString(1, game.getName());
            cstm.setBlob(2, image);
            cstm.setString(3, game.getDatePremiere());
            cstm.execute();
            flag = true;
        }catch(SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm);
        }
        return flag;
    }

    public boolean update(BeanGames game){
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_create(?,?,?,?,?)}");
            cstm.setInt(1, game.getIdGames());
            cstm.setString(2, game.getCategory_idCategory().getName());
            cstm.setString(3, game.getName());
            cstm.setString(4, game.getImgGame());
            cstm.setString(5, game.getDatePremiere());

            flag = cstm.execute();
        }catch(SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        }finally{
            ConnectionMySQL.closeConnections(con, cstm);
        }
        return flag;
    }

    public boolean delete(int idGames){
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call sp_delete2(?)}");
            cstm.setLong(1, idGames);

            flag = cstm.execute();
        }catch(SQLException e){
            System.out.println("Ocurrio un error: " + e.getMessage());
        }finally{
            ConnectionMySQL.closeConnections(con, cstm);
        }
        return flag;
    }
}
