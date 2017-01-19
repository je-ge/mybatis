package com.jege.mybatis;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jege.mybatis.mapper.User;

/**
 * @author JE哥
 * @email 1272434821@qq.com
 * @description:单表CRUD Test
 */
public class SingleTableTest {

  private static final String NAME_SPACE = "com.jege.mybatis.mapper.UserMapper";
  private static SqlSessionFactory sqlSessionFactory = null;
  private SqlSession sqlSession = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
  }

  @Before
  public void setUp() throws Exception {
    sqlSession = sqlSessionFactory.openSession();
    sqlSession.insert(NAME_SPACE + ".dropTable");
    sqlSession.insert(NAME_SPACE + ".createTable");
    sqlSession.commit();
  }

  // mybatis会通过mapper文件配置把保存后的主键放到当前对象的id里面
  @Test
  public void insert() throws Exception {
    sqlSession = sqlSessionFactory.openSession();
    User user = new User("je-ge", 22);
    System.out.println("保存之前：" + user);

    sqlSession.insert(NAME_SPACE + ".insert", user);
    sqlSession.commit();

    System.out.println("保存之后：" + user);
    System.out.println("+++++++++++++++");
  }

  @Test
  public void update() throws Exception {
    insert();

    User user = sqlSession.selectOne(NAME_SPACE + ".findByKey", 1L);
    System.out.println("修改前：" + user);

    user.setName("JE-GE");
    user.setAge(18);
    sqlSession.update(NAME_SPACE + ".update", user);
    sqlSession.commit();

    user = sqlSession.selectOne(NAME_SPACE + ".findByKey", 1L);
    System.out.println("修改后：" + user);
  }

  @Test
  public void findByKey() throws Exception {
    User user = sqlSession.selectOne(NAME_SPACE + ".findByKey", 1L);
    System.out.println(user);
  }

  @Test
  public void findAll() throws Exception {
    List<User> users = sqlSession.selectList(NAME_SPACE + ".findAll");
    for (User user : users) {
      System.out.println(user);
    }
  }

  @Test
  public void delete() throws Exception {
    User user = sqlSession.selectOne(NAME_SPACE + ".findByKey", 1L);
    System.out.println("删除前：" + user);

    sqlSession.delete(NAME_SPACE + ".delete", 1L);
    sqlSession.commit();

    user = sqlSession.selectOne(NAME_SPACE + ".findByKey", 1L);
    System.out.println("删除后：" + user);
  }

  @After
  public void tearDown() throws Exception {
    if (sqlSession != null)
      sqlSession.close();
  }

}
