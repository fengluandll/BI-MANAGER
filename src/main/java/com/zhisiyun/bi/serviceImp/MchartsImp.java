package com.zhisiyun.bi.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhisiyun.bi.service.Mcharts;
@Transactional(propagation = Propagation.REQUIRED)
@Service("mCharts")
public class MchartsImp implements Mcharts{

}
