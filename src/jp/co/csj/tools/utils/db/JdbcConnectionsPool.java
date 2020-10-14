
package jp.co.csj.tools.utils.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class JdbcConnectionsPool implements DataSource {

    /*
     * 使用静态块代码，初始化连接池，创建连接池的中最小链接数量连接，
     * 创建linkedlist集合，将这些连接放入集合中
     */
    //创建linkedlist集合
    private  LinkedList<Connection> linkedlist1=new LinkedList<Connection>();
    private  String driver;//
    private  String url;//
    private  String username;//数据库登陆名
    private  String password;//数据库的登陆密码
    private  int jdbcConnectionInitSize;//最小连接数量
    private  int max=1; //当前最大连接数量=max*jdbcConnectionInitSize
    
    public JdbcConnectionsPool(String propertiesPath) {
        //通过反射机制获取访问db.properties文件
        InputStream is=JdbcConnectionsPool.class.getResourceAsStream(propertiesPath);
        Properties prop=new Properties();
        try {
            //加载db.properties文件
            prop.load(is);
            //获取db.properties文件中的数据库连接信息
            driver=prop.getProperty("driver");
            url=prop.getProperty("url");
            username=prop.getProperty("username");
            password=prop.getProperty("password");
            jdbcConnectionInitSize=Integer.parseInt(prop.getProperty("jdbcConnectionInitSize"));
            
            Class.forName(driver);
            
            //创建最小连接数个数据库连接对象以备使用
            for(int i=0;i<jdbcConnectionInitSize;i++){
                Connection conn=DriverManager.getConnection(url, username, password);
                 System.out.println("获取到了链接" + conn);
                //将创建好的数据库连接对象添加到Linkedlist集合中
                linkedlist1.add(conn);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * 实现数据库连接的获取和新创建
     */
    @Override
    public Connection getConnection() throws SQLException {
         //如果集合中没有数据库连接对象了，且创建的数据库连接对象没有达到最大连接数量，可以再创建一组数据库连接对象以备使用
         if(linkedlist1.size()==0&&max<=5){
             try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             for(int i=0;i<jdbcConnectionInitSize;i++){
               Connection conn=DriverManager.getConnection(url, username, password);
               System.out.println("获取到了链接" + conn);
               //将创建好的数据库连接对象添加到Linkedlist集合中
               linkedlist1.add(conn);
                }
             max++;
            }
        if(linkedlist1.size()>0){
            //从linkedlist集合中取出一个数据库链接对象Connection使用
            Connection conn1=linkedlist1.removeFirst();
            System.out.println("linkedlist1数据库连接池大小是" + linkedlist1.size());
            /*返回一个Connection对象，并且设置Connection对象方法调用的限制，
            *当调用connection类对象的close()方法时会将Connection对象重新收集放入linkedlist集合中。
            */
            return (Connection) Proxy.newProxyInstance(conn1.getClass().getClassLoader(),//这里换成JdbcConnectionsPool.class.getClassLoader();也行
                 conn1.getClass().getInterfaces(), new InvocationHandler() {
                                    
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if(!method.getName().equalsIgnoreCase("close")){
                            return method.invoke(conn1, args);
                        }else{
                            linkedlist1.add(conn1);
                            System.out.println("今は対象"+linkedlist1.size()+"個はプールに存在している。");
                            return null;
                            }                    
                        }
                   });
        }else{
            System.out.println("接続失敗！");
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        return null;
    }

}