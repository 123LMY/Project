package com.org.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.org.pojo.DeviceApply;
import com.org.pojo.LabApply;
import com.org.service.DeviceApplyService;
import com.org.service.DeviceDetailService;
import com.org.service.LabApplyService;
import com.org.service.LabService;
import com.org.service.TempHumiRecordService;
import com.org.service.UserGrantService;
import com.org.vo.LabUseInfo;


@Component
public class ScheduledTask {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private LabApplyService labApplyService;
	@Resource
	private LabService labService;
	@Resource
	private UserGrantService userGrantService;
	@Resource
	private DeviceApplyService deviceApplyService;
	@Resource
	private DeviceDetailService deviceDetailService;
	@Resource
	private TempHumiRecordService tempHumiRecordService;

	//定时检测实验室状态 修改实验室状态 nullendtime
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void changeLabApplyStatus() {
		List idList = new ArrayList();
		List cancelGrantList = new ArrayList();
		List addGrantList = new ArrayList();
		Timestamp nowtime = new Timestamp(System.currentTimeMillis());
		Timestamp starttime = null;
		Timestamp endtime = null;
		List<LabApply> labapplyList = labApplyService.selectLendLabApply();
		for(int i=0;i<labapplyList.size();i++) {
			starttime = labapplyList.get(i).getStartTime();
			endtime = labapplyList.get(i).getEndTime();
			if(endtime==null) {
				continue;
			}
			if(endtime.getYear()==nowtime.getYear()) {
				if(endtime.getMonth()==nowtime.getMonth()) {
					if(endtime.getDay()==nowtime.getDay()) {
						if(endtime.getHours()==nowtime.getHours()) {
							idList.add(labapplyList.get(i).getId());
							cancelGrantList.add(labapplyList.get(i).gettUser_id_a());
							
						}
					}
				}
			}
			if(starttime.getYear()==nowtime.getYear()) {
				if(starttime.getMonth()==nowtime.getMonth()) {
					if(starttime.getDay()==nowtime.getDay()) {
						if(starttime.getHours()==nowtime.getHours()) {
							addGrantList.add(labapplyList.get(i).gettUser_id_a());
						}
					}
				}
			}
			
		}
		if(idList.size()>0) {
			labApplyService.backLabApplyStatus(idList,-1);
			int[] cancelid = new int[cancelGrantList.size()];
			for(int i=0;i<cancelid.length;i++) {
				cancelid[i] = Integer.parseInt(String.valueOf(cancelGrantList.get(i)));
			}
			userGrantService.cancelAuthorization(cancelid,-1);
			
		}
		if(addGrantList.size()>0) {
			int[] addid = new int[addGrantList.size()];
			for(int i=0;i<addid.length;i++) {
				addid[i] = Integer.parseInt(String.valueOf(addGrantList.get(i)));
			}
			userGrantService.addAuthorization(addid,-1);
		}
		
		List<LabUseInfo> labUseList = labApplyService.selectLabApplyStatus();
		for(int i=0;i<labUseList.size();i++) {
			if(String.valueOf(labUseList.get(i).getLabid()).equals("")||String.valueOf(labUseList.get(i).getLabid())!=null) {
				continue;
			}
			if(labUseList.get(i).getLendNum()==0) {
				labService.updateLabStatus(new int[(int) labUseList.get(i).getLabid()],'0');
			}else if(labUseList.get(i).getLendNum()>0) {
				labService.updateLabStatus(new int[(int) labUseList.get(i).getLabid()],'1');
			}
		}
		
		
		List<LabApply> bookApplyList = labApplyService.selectBookLabApply();
		List refuseIdList = new ArrayList();
		for(int i=0;i<bookApplyList.size();i++) {
			if(bookApplyList.get(i).getEndTime()==null) {
				continue;
			}
			if(bookApplyList.get(i).getEndTime().getYear()==nowtime.getYear()) {
				if(bookApplyList.get(i).getEndTime().getMonth()==nowtime.getMonth()) {
					if(bookApplyList.get(i).getEndTime().getDay()+1==nowtime.getDay()) {
						if(bookApplyList.get(i).getEndTime().getHours()==nowtime.getHours()) {
							refuseIdList.add(bookApplyList.get(i).getId());
						}
					}
				}
			}
		}
		if(refuseIdList.size()>0) {
			int[] ids = new int[refuseIdList.size()];
			for(int i=0;i<ids.length;i++) {
				ids[i] = Integer.parseInt(String.valueOf(refuseIdList.get(i)));
			}
			labApplyService.updateRefuseLabApplyStatus(ids, -1);
		}
	}
	
	//检测设备预约状态 修改设备预约状态
	@Scheduled(cron = "0 0 0 * * ?")
	public void changeDevDetailStatus() {
		List<DeviceApply> passdevdetailList = deviceApplyService.selectPassDevDetail();
		List<DeviceApply> bookeddevdetailList = deviceApplyService.selectBookedDevDetail();
		List bookedIdList1 = new ArrayList();
		List bookedIdList2 = new ArrayList();
		List devapplyId = new ArrayList();
		Date nowDate = new Date(System.currentTimeMillis());
		
		for(int i=0;i<bookeddevdetailList.size();i++) {
			if(nowDate.getYear() == bookeddevdetailList.get(i).getStartDate().getYear()) {
				if(nowDate.getMonth() == bookeddevdetailList.get(i).getStartDate().getMonth()) {
					if(nowDate.getDate() == bookeddevdetailList.get(i).getStartDate().getDate()) {
						bookedIdList2.add(bookeddevdetailList.get(i).gettDeviceDetail_id());
						devapplyId.add(bookeddevdetailList.get(i).getId());
					}
				}
			}
		}
		if(bookedIdList2.size()>0) {
			int[] ids = new int[bookedIdList2.size()];
			for(int i=0;i<bookedIdList2.size();i++) {
				ids[i] = Integer.parseInt(String.valueOf(bookedIdList2.get(i)));
			}
			deviceDetailService.updateDevDetailStatus(ids, '0', -1);
		}
		if(devapplyId.size()>0) {
			int[] ids = new int[devapplyId.size()];
			for(int i=0;i<devapplyId.size();i++) {
				ids[i] = Integer.parseInt(String.valueOf(devapplyId.get(i)));
			}
			deviceApplyService.updateDevApplyStatus(ids, '4', -1);
		}
		
		for(int i=0;i<passdevdetailList.size();i++) {
			if(nowDate.getYear() == passdevdetailList.get(i).getStartDate().getYear()) {
				if(nowDate.getMonth() == passdevdetailList.get(i).getStartDate().getMonth()) {
					if(nowDate.getDate() == passdevdetailList.get(i).getStartDate().getDate()) {
						bookedIdList1.add(passdevdetailList.get(i).gettDeviceDetail_id());
					}
				}
			}
		}
		if(bookedIdList1.size()>0) {
			int[] ids = new int[bookedIdList1.size()];
			for(int i=0;i<bookedIdList1.size();i++) {
				ids[i] = Integer.parseInt(String.valueOf(bookedIdList1.get(i)));
			}
			deviceDetailService.updateDevDetailStatus(ids, '5', -1);
		}
		
		List<DeviceApply> bookApplyList = deviceApplyService.selectOrderDevDetail();
		List applyId = new ArrayList();
		for(int i=0;i<bookApplyList.size();i++) {
			if(nowDate.getYear() == bookApplyList.get(i).getEndDate().getYear()) {
				if(nowDate.getMonth() == bookApplyList.get(i).getEndDate().getMonth()) {
					if(nowDate.getDate() == bookApplyList.get(i).getEndDate().getDate()+1) {
						applyId.add(bookApplyList.get(i).getId());
					}
				}
			}
		}
		if(applyId.size()>0) {
			int[] ids = new int[applyId.size()];
			for(int i=0;i<applyId.size();i++) {
				ids[i] = Integer.parseInt(String.valueOf(applyId.get(i)));
			}
			deviceApplyService.updateDevApplyStatus(ids, '1', -1);
		}
		tempHumiRecordService.deleteTemphumirecord();
	}	
}
