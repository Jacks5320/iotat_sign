package com.iotat;

import com.iotat.sign.SignApplication;
import com.iotat.sign.controller.UserController;
import com.iotat.sign.dao.SignDao;
import com.iotat.sign.dao.UserDao;
import com.iotat.sign.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SignApplication.class)
class SignApplicationTests {

    @Autowired
    SignDao signDao;
    @Autowired
    UserDao userDao;
    @Autowired
    UserService service;
    @Autowired
    UserController userController;


    @Test
    void testFindByToken(){
        // System.out.println(userDao.findUserByToken("1C09754C61855297A6CADE0123A90153"));
    }
    @Test
    void testFindRecordByUserId(){
        //service.sign("1C09754C61855297A6CADE0123A90153");
    }

    @Test
    void testSaveSign(){
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date date = new Date();
        // s.setUserId(1l);
        // s.setNum(2);
        // s.setDateTime(dateFormat.format(date));
        // signDao.save(s);
        // //signDao.saveSign(2l,1,dateFormat.format(date));
    }
    @Test
    void testLogin(){
        // service.login("jk5320","123456");
    }

    @Test
    void testFindSign(){
        //System.out.println(signDao.findByDate(13,"2020-01-10" + "%"));
        // System.out.println(service.yesterdaySign("5EA4720A1663687017378273A983DC02"));
    }
    @Test
    void testSign(){
        // service.sign("5EA4720A1663687017378273A983DC02","2");
    }

    @Test
    void testLoginOut(){
        // service.loginOut("7B54A2DDB2E896FEAEDE015A1F13E3BB");
    }

    @Test
    void testController(){
        // JSONObject obj = new JSONObject();
        // obj.put("token","5EA4720A1663687017378273A983DC02");
        // userController.totalSign(obj);
    }

    @Test
    void testFindScoreByid(){
        // System.out.println(userDao.findScoreById());
        // userDao.saveScore(31,0);
    }

    @Test
    void testUpdateScore(){
        // userDao.updateScore(2,53);
    }

    @Test
    void testFindScoreById(){
        // System.out.println(userDao.findScoreById(1));
    }

    @Test
    void testSaveScore(){
        // userDao.saveScore(1,0);
    }

    @Test
    void testSave(){
        System.out.println(userDao.saveUser("jk123","123","123","321sadasd"));

    }

}
