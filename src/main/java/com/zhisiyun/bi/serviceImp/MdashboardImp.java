package com.zhisiyun.bi.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhisiyun.bi.service.Mdashboard;

@Transactional(propagation = Propagation.REQUIRED)
@Service("mDashboard")
public class MdashboardImp implements Mdashboard {

}
