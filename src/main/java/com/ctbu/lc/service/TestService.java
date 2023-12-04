package com.ctbu.lc.service;

import com.ctbu.lc.dao.TestDao;
import com.ctbu.lc.entity.TestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TestService {

    @Resource
    TestDao testDao;

    @Transactional
    public void insertAndUpdate() {
        TestEntity data = new TestEntity("t22");
        testDao.insertData(data);
        System.out.println("插入数据id：" + data.getId());
        int a = 1 / 0;
        testDao.updateData(data.getId(), "fuck");
        System.out.println("end============");
    }
}
