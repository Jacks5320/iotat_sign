package com.iotat.sign.entity;

import javax.persistence.*;

@Entity
@Table(name = "tb_sign")
public class SignRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;
    //签到次数
    private int num;
    //本次签到的具体时间
    @Column(name = "c_time")
    private String time;

    @Override
    public String toString() {
        return "SignRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", num=" + num +
                ", dateTime='" + time + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String dateTime) {
        this.time = dateTime;
    }
}
