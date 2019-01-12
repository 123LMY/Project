package com.org.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.DeviceApply;
import com.org.repository.DeviceApplyRepository;
import com.org.repository.ProjectRepository;
import com.org.vo.DeviceApplyInfo;

@Service
public class DeviceApplyService {
	@Resource
	private DeviceApplyRepository deviceApplyRepository;
	@Resource
	private ProjectRepository projectRepository;

	// 新增deviceApply
	public int insertDeviceApplys(DeviceApply deviceApply) {
		return deviceApplyRepository.insertDeviceApplys(deviceApply);
	}

	// 根据预约人的id查询该人的所有预约单
	public List<DeviceApply> selectDeviceApplyByUserId(long tUser_id_b) {
		return deviceApplyRepository.selectDeviceApplyByUserId(tUser_id_b);
	}

	// 根据id删除设备使用情况
	public int deleteDeviceApplyById(long id) {
		return deviceApplyRepository.deleteDeviceApplyById(id);
	}

	// 查找设备申请信息
	public List<DeviceApplyInfo> selectDevApplyInfo(char auditStatus, String name) {
		List<DeviceApplyInfo> list = deviceApplyRepository.selectDevApplyInfo(auditStatus, name);
		List proNameList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDevStatus() == '0') {
				list.get(i).setDevStatusName("空闲");
			} else if (list.get(i).getDevStatus() == '1') {
				list.get(i).setDevStatusName("在用");
			} else if (list.get(i).getDevStatus() == '2') {
				list.get(i).setDevStatusName("维修");
			} else if (list.get(i).getDevStatus() == '3') {
				list.get(i).setDevStatusName("损坏");
			} else if (list.get(i).getDevStatus() == '4') {
				list.get(i).setDevStatusName("报废");
			} else if (list.get(i).getDevStatus() == '5') {
				list.get(i).setDevStatusName("已预约");
			}
			if (list.get(i).getAuditStatus() == '0') {
				list.get(i).setAuditStatusName("预约");
			} else if (list.get(i).getAuditStatus() == '1') {
				list.get(i).setAuditStatusName("拒绝");
			} else if (list.get(i).getAuditStatus() == '2') {
				list.get(i).setAuditStatusName("通过");
			} else if (list.get(i).getAuditStatus() == '3') {
				list.get(i).setAuditStatusName("借出");
			} else if (list.get(i).getAuditStatus() == '4') {
				list.get(i).setAuditStatusName("还回");
			}

			if (list.get(i).gettUser_id_a() != 0) {
				proNameList = projectRepository.selectProNameByUserId(list.get(i).gettUser_id_a());
				StringBuffer sb = new StringBuffer();
				if (proNameList.size() == 0) {
					sb.append("暂无");
					list.get(i).setProName(sb.toString());
					continue;
				}
				for (int j = 0; j < proNameList.size(); j++) {
					sb.append(proNameList.get(j));
					if (j < proNameList.size() - 1) {
						sb.append(",");
					}
				}
				list.get(i).setProName(sb.toString());
			}
		}
		return list;
	}

	// 修改设备申请状态
	@Transactional
	public void updateDevApplyStatus(int[] ids, char devapplystatus, long tUser_id_o) {
		deviceApplyRepository.updateDevApplyStatus(ids, devapplystatus, tUser_id_o);
	}

	// 系统消息 设备审核提示
	public boolean checkDevApply() {
		if (deviceApplyRepository.checkDevApply() != 0) {
			return true;
		}
		return false;
	}

	// 查找通过的设备审核
	public List<DeviceApply> selectPassDevDetail() {
		return deviceApplyRepository.selectPassDevDetail();
	}

	public int checkDeviceApplyByUserId(long userId) {
		return deviceApplyRepository.checkDeviceApplyByUserId(userId);
	}

	// 查找已预约的设备申请记录
	public List<DeviceApply> selectBookedDevDetail() {
		return deviceApplyRepository.selectBookedDevDetail();
	}

	public List<DeviceApply> selectOrderDevDetail() {
		return deviceApplyRepository.selectOrderDevDetail();
	}

	public int deleteByUserIdAndDeviceDetailId(long deviceDetailId, long userId) {
		return deviceApplyRepository.deleteByUserIdAndDeviceDetailId(deviceDetailId, userId);
	}
}
