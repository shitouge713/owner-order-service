package com.owner.order.service;


import com.owner.order.dao.pojo.UserInfo;

public interface IEsOrderGlobalService {

    Boolean syncToEsByLinkData(UserInfo userInfo);

    void deleteById(String id);

    void findOne();

    void findAll();

    void findAllSort();

    void findPage();

    void findByName();

    void searchBySql();
}
